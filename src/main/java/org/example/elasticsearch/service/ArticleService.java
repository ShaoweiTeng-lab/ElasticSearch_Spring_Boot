package org.example.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.example.elasticsearch.common.response.PageResponse;
import org.example.elasticsearch.repository.ArticleRepository;
import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public PageResponse<List<ArticleVO>> searchArticle(String keyword, int page, int size) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        Query query;

        if (!hasKeyword) {
            query = QueryBuilders.matchAll().build()._toQuery();
        } else {
            // 命中其中一個就算成功
            query = QueryBuilders.multiMatch(m -> m
                    .query(keyword)
                    .fields("title", "content")
            );
        }

        int currentPage = (page < 1) ? 0 : page - 1;
        int pageSize = (size <= 0) ? 10 : size;

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(currentPage, pageSize))
                .build();

        var searchHits = elasticsearchOperations.search(nativeQuery, ArticleVO.class);

        List<ArticleVO> data = searchHits.stream()
                .map(hit -> hit.getContent())
                .toList();

        long total = searchHits.getTotalHits();

        return PageResponse.<List<ArticleVO>>builder()
                .page(currentPage + 1)
                .size(pageSize)
                .total((int) total)
                .data(data)
                .build();

    }
}