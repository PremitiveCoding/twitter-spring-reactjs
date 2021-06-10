package com.gmail.merikbest2015.twitterspringreactjs.service;

import com.gmail.merikbest2015.twitterspringreactjs.dto.response.AuthenticationResponse;
import com.gmail.merikbest2015.twitterspringreactjs.model.User;

import java.util.Map;

public interface AuthenticationService {

    AuthenticationResponse login(String email);

    boolean registration(User user);

    boolean activateUser(String code);

    User findByPasswordResetCode(String code);

    boolean sendPasswordResetCode(String email);

    String passwordReset(String email, String password);
}