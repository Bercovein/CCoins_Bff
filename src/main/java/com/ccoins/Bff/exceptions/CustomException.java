package com.ccoins.Bff.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class CustomException extends RuntimeException{
    private String code;

    public CustomException(String code){
        super();
        this.code = code;
    }

    public CustomException(String code, String message){
        super(message);
        this.code = code;
    }
}
