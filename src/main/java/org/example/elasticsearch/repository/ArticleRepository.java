package org.example.elasticsearch.repository;

import org.example.elasticsearch.vo.ArticleVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleVO,String> {
    List<ArticleVO> findByTitleAndContent(String title, String author);
}
