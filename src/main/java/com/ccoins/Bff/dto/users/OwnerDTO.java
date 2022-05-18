package com.ccoins.Bff.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDTO {

    private Long id;

    @NotEmpty
    @Email
    private String email;

    private LocalDateTime startDate;
}
