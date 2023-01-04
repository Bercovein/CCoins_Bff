package com.ccoins.bff.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class IdSPTF {
    private String id;
    private String name;
    private String uri;
}
