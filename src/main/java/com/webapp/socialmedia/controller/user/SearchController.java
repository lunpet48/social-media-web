package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping("")
    public ResponseEntity<?> search(String keyword){

        return ResponseEntity.ok(searchService.search(keyword));
    }
}
