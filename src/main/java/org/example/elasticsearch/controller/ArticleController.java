package org.example.elasticsearch.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.elasticsearch.common.response.PageResponse;
import org.example.elasticsearch.common.response.ResultResponse;
import org.example.elasticsearch.req.CreateArticleReq;
import org.example.elasticsearch.req.EditArticleReq;
import org.example.elasticsearch.service.ArticleService;
import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping()
    public ResponseEntity<ResultResponse<PageResponse<List<ArticleVO>>>> findArticle(
            @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size) {

        return ResponseEntity.ok(articleService.searchArticle(keyword, page, size));
    }

    @PostMapping()
    public ResponseEntity<ResultResponse<String>> addArticle(@ModelAttribute @Validated CreateArticleReq createArticleReq) {
        return ResponseEntity.ok(articleService.createArticle(createArticleReq));
    }

    @PutMapping()
    public ResponseEntity<ResultResponse<String>> editArticle(@ModelAttribute @Validated EditArticleReq editArticleReq) {
        return ResponseEntity.ok(articleService.editArticle(editArticleReq));
    }

    @DeleteMapping()
    public ResponseEntity<ResultResponse<String>> deleterticle(@RequestParam @NotBlank String id) {
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }
}
