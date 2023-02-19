package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.feign.TweetClient;
import com.gmail.merikbest2015.model.User;
import com.gmail.merikbest2015.repository.UserRepository;
import com.gmail.merikbest2015.repository.projection.*;
import com.gmail.merikbest2015.service.AuthenticationService;
import com.gmail.merikbest2015.service.UserService;
import com.gmail.merikbest2015.util.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserServiceHelper userServiceHelper;
    private final TweetClient tweetClient;

    @Override
    public UserProfileProjection getUserById(Long userId) {
        return userRepository.getUserById(userId, UserProfileProjection.class)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<UserProjection> getUsers(Pageable pageable) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.findByActiveTrueAndIdNot(authUserId, pageable);
    }

    @Override
    public List<UserProjection> getRelevantUsers() {
        return userRepository.findTop5ByActiveTrue();
    }

    @Override
    public <T> Page<T> searchUsersByUsername(String text, Pageable pageable, Class<T> type) {
        return userRepository.searchUsersByUsername(text, pageable, type);
    }

    @Override
    @Transactional
    public Boolean startUseTwitter() {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        userRepository.updateProfileStarted(authUserId);
        return true;
    }

    @Override
    @Transactional
    public AuthUserProjection updateUserProfile(User userInfo) {
        if (userInfo.getFullName().length() == 0 || userInfo.getFullName().length() > 50) {
            throw new ApiRequestException("Incorrect username length", HttpStatus.BAD_REQUEST);
        }
        User user = authenticationService.getAuthenticatedUser();

        if (userInfo.getAvatar() != null) {
            user.setAvatar(userInfo.getAvatar());
        }
        if (userInfo.getWallpaper() != null) {
            user.setWallpaper(userInfo.getWallpaper());
        }
        user.setFullName(userInfo.getFullName());
        user.setAbout(userInfo.getAbout());
        user.setLocation(userInfo.getLocation());
        user.setWebsite(userInfo.getWebsite());
        user.setProfileCustomized(true);
        return userRepository.getUserById(user.getId(), AuthUserProjection.class).get();
    }

    @Override
    @Transactional
    public Boolean processSubscribeToNotifications(Long userId) {
        userServiceHelper.checkIsUserExistOrMyProfileBlocked(userId);
        Long authUserId = authenticationService.getAuthenticatedUserId();
        boolean isUserSubscribed = userRepository.isUserSubscribed(userId, authUserId);

        if (isUserSubscribed) {
            userRepository.unsubscribe(authUserId, userId);
            return false;
        } else {
            userRepository.subscribe(authUserId, userId);
            return true;
        }
    }

    @Override
    @Transactional
    public Long processPinTweet(Long tweetId) {
        if (!tweetClient.isTweetExists(tweetId)) {
            throw new ApiRequestException("Tweet not found", HttpStatus.NOT_FOUND);
        }
        Long authUserId = authenticationService.getAuthenticatedUserId();
        Long pinnedTweetId = userRepository.getPinnedTweetId(authUserId);

        if (pinnedTweetId == null || !pinnedTweetId.equals(tweetId)) {
            userRepository.updatePinnedTweetId(tweetId, authUserId);
            return tweetId;
        } else {
            userRepository.updatePinnedTweetId(null, authUserId);
            return 0L;
        }
    }

    @Override
    public UserDetailProjection getUserDetails(Long userId) {
        return userRepository.getUserById(userId, UserDetailProjection.class)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
    }
}