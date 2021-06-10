package com.gmail.merikbest2015.twitterspringreactjs.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String username;
    private String location;
    private String about;
    private String website;
    private boolean confirmed;
    private List<TweetResponse> tweets;
}