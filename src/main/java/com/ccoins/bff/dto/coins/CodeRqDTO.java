package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeRqDTO {

    private Long game;

    @Nullable
    private String code;

    @Positive
    private Long quantity = 1L;

    private boolean oneUse = true;

    private boolean perPerson = false;

    private Long prizeId;

    private Long points;

    @Nullable
    private LocalDateTime expirationDate;

    private boolean expires = true;

}
