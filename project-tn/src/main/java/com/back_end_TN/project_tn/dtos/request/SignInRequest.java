package com.back_end_TN.project_tn.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInRequest implements Serializable {
    @NotBlank(message = "username must be not null")
    private String username;
    @NotBlank(message = "password must be not blank")
    private String password;
    @NotNull(message = "username must be not null")
    private String deviceToken;
    private String version;
}
