package org.example.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.example.elasticsearch.common.response.PageResponse;
import org.example.elasticsearch.common.response.ResultResponse;
import org.example.elasticsearch.repository.ArticleRepository;
import org.example.elasticsearch.req.CreateArticleReq;
import org.example.elasticsearch.req.EditArticleReq;
import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    public ResultResponse<String> createArticle(CreateArticleReq createArticleReq) {
        ArticleVO article = ArticleVO.builder()
                .title(createArticleReq.getTitle())
                .content(createArticleReq.getContent())
                .build();
        elasticsearchOperations.save(article);
       return ResultResponse.success("成功");
    }

    public ResultResponse<String> editArticle(EditArticleReq editArticleReq) {
        String id = editArticleReq.getId();

        // 先檢查是否存在
        ArticleVO exist = elasticsearchOperations.get(id, ArticleVO.class);

        if (ObjectUtils.isEmpty(exist)) {
            return ResultResponse.fail(4001, "資料異常無法辨識");
        }
        ArticleVO article = ArticleVO.builder()
                .id(editArticleReq.getId())
                .title(editArticleReq.getTitle())
                .content(editArticleReq.getContent())
                .build();
        elasticsearchOperations.save(article);
        return ResultResponse.success("成功");
    }

    public ResultResponse<String> deleteArticle(String id) {

        // 先檢查是否存在
        ArticleVO exist = elasticsearchOperations.get(id, ArticleVO.class);

        if (ObjectUtils.isEmpty(exist)) {
            return ResultResponse.fail(4001, "資料不存在");
        }
        elasticsearchOperations.delete(id, ArticleVO.class);
        return ResultResponse.success("成功");
    }
}