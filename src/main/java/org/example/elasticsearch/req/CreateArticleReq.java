package org.example.elasticsearch.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateArticleReq {
    @NotBlank(message = "title 不可為空")
    private  String title;
    @NotBlank(message = "content 不可為空")
    private  String content;
}
