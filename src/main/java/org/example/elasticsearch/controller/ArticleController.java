package org.example.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.elasticsearch.common.response.PageResponse;
import org.example.elasticsearch.service.ArticleService;
import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public ResponseEntity<PageResponse<List<ArticleVO>>> findArticle(
            @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size) {

        return ResponseEntity.ok(articleService.searchArticle(keyword , page , size));
    }
}
