package com.gmail.merikbest2015.controller.api;

import com.gmail.merikbest2015.dto.request.TweetTextRequest;
import com.gmail.merikbest2015.service.TagClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gmail.merikbest2015.constants.PathConstants.API_V1_TAGS;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_TAGS)
public class TagApiController {

    private final TagClientService tagClientService;

    @PostMapping("/parse/{tweetId}")
    public void parseHashtagsInText(@PathVariable("tweetId") Long tweetId, @RequestBody TweetTextRequest request) {
        tagClientService.parseHashtagsInText(tweetId, request.getText());
    }

    @DeleteMapping("/delete/{tweetId}")
    public void deleteTagsByTweetId(@PathVariable("tweetId") Long tweetId) {
        tagClientService.deleteTagsByTweetId(tweetId);
    }
}