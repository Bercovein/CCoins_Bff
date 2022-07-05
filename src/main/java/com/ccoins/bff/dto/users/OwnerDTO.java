package com.ccoins.bff.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static com.ccoins.bff.utils.DateUtils.DDMMYYYY_HHMM;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDTO {

    private Long id;

    @NotEmpty
    @Email
    private String email;

    @JsonFormat(pattern = DDMMYYYY_HHMM)
    private LocalDateTime startDate;
}
