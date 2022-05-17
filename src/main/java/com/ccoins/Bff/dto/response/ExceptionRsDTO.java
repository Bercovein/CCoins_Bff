package com.ccoins.Bff.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionRsDTO {

    private String code;
    private Object message;

}
