package com.gmail.merikbest2015.controller.api;

import com.gmail.merikbest2015.dto.request.NotificationRequest;
import com.gmail.merikbest2015.dto.response.notification.NotificationResponse;
import com.gmail.merikbest2015.feign.WebSocketClient;
import com.gmail.merikbest2015.mapper.NotificationClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gmail.merikbest2015.constants.PathConstants.API_V1_NOTIFICATION;
import static com.gmail.merikbest2015.constants.WebsocketConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_NOTIFICATION)
public class NotificationApiController {

    private final NotificationClientMapper notificationClientMapper;
    private final WebSocketClient webSocketClient;

    @PostMapping("/list")
    public void sendListNotification(@RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationClientMapper.sendListNotification(request);

        if (notification.getId() != null) {
            webSocketClient.send(TOPIC_NOTIFICATIONS + notification.getUser().getId(), notification);
        }
    }

    @PostMapping("/user")
    public void sendUserNotification(@RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationClientMapper.sendUserNotification(request);

        if (notification.getId() != null) {
            webSocketClient.send(TOPIC_NOTIFICATIONS + notification.getUser().getId(), notification);
        }
    }

    @PostMapping("/tweet")
    public NotificationResponse sendTweetNotification(@RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationClientMapper.sendTweetNotification(request);

        if (notification.getId() != null) {
            webSocketClient.send(TOPIC_NOTIFICATIONS + notification.getTweet().getAuthorId(), notification);
        }
        webSocketClient.send(TOPIC_FEED, notification);
        webSocketClient.send(TOPIC_TWEET + notification.getTweet().getId(), notification);
        return notification;
    }

    @GetMapping("/tweet/{tweetId}")
    public void sendTweetNotificationToSubscribers(@PathVariable("tweetId") Long tweetId) {
        notificationClientMapper.sendTweetNotificationToSubscribers(tweetId);
    }
}