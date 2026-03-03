package org.example.elasticsearch.repository;

import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleVO, String> {
    List<ArticleVO> findByTitleAndContent(String title, String author);

    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["title", "content"]
              }
            }
            """)
    Page<List<ArticleVO>> searchByKeyword(String keyword, Pageable pageable);
}
