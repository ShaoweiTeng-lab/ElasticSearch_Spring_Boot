package org.example.elasticsearch.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private Integer page;//當前頁數
    private Integer size;//顯示筆數
    private Integer total;//總筆數
    private T data;
}
