package com.gmail.merikbest2015.twitterspringreactjs.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PollResponse {
    private Long id;
    private LocalDateTime dateTime;
//    private TweetResponse tweet;
    private List<PollChoiceResponse> pollChoices;
}