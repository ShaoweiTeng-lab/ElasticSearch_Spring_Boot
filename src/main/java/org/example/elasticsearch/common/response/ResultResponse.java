package org.example.elasticsearch.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> {
    private Integer code;
    private Boolean success;
    private T data;

    public static <T> ResultResponse<T> success(T data) {
        return ResultResponse.<T>builder()
                .code(200)
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ResultResponse<T> fail(Integer code, T data) {
        return ResultResponse.<T>builder()
                .code(code)
                .success(false)
                .data(data)
                .build();
    }
}
