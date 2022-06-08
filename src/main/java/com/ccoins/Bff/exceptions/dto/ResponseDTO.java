package com.ccoins.Bff.exceptions.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO<T> {

    private String code;
    private Object message;
    private T data;
}
