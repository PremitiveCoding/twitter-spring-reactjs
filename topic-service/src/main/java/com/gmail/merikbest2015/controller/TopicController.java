package com.gmail.merikbest2015.controller;

import com.gmail.merikbest2015.dto.request.SuggestedTopicsRequest;
import com.gmail.merikbest2015.dto.request.TopicsCategoriesRequest;
import com.gmail.merikbest2015.dto.response.TopicResponse;
import com.gmail.merikbest2015.dto.response.TopicsByCategoriesResponse;
import com.gmail.merikbest2015.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.UI_V1_TOPICS;

@RestController
@RequiredArgsConstructor
@RequestMapping(UI_V1_TOPICS)
public class TopicController {

    private final TopicMapper topicMapper;

    @PostMapping("/suggested")
    public ResponseEntity<List<TopicResponse>> getTopicsByIds(@RequestBody SuggestedTopicsRequest request) {
        return ResponseEntity.ok(topicMapper.getTopicsByIds(request.getTopicsIds()));
    }

    @PostMapping("/category")
    public ResponseEntity<List<TopicsByCategoriesResponse>> getTopicsByCategories(@RequestBody TopicsCategoriesRequest request) {
        return ResponseEntity.ok(topicMapper.getTopicsByCategories(request.getCategories()));
    }

    @GetMapping("/followed")
    public ResponseEntity<List<TopicResponse>> getFollowedTopics() {
        return ResponseEntity.ok(topicMapper.getFollowedTopics());
    }

    @GetMapping("/followed/{userId}")
    public ResponseEntity<List<TopicResponse>> getFollowedTopicsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(topicMapper.getFollowedTopicsByUserId(userId));
    }

    @GetMapping("/not_interested")
    public ResponseEntity<List<TopicResponse>> getNotInterestedTopics() {
        return ResponseEntity.ok(topicMapper.getNotInterestedTopics());
    }

    @GetMapping("/not_interested/{topicId}")
    public ResponseEntity<Boolean> processNotInterestedTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.ok(topicMapper.processNotInterestedTopic(topicId));
    }

    @GetMapping("/follow/{topicId}")
    public ResponseEntity<Boolean> processFollowTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.ok(topicMapper.processFollowTopic(topicId));
    }
}