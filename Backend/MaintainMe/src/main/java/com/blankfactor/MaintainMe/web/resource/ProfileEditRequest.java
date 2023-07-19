package com.blankfactor.MaintainMe.web.resource;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEditRequest {

    private String token;

    @NotBlank
    @Size(min = 8,max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 2,max = 32)
    private String firstName;
    @NotBlank
    @Size(min = 2,max = 32)
    private String lastName;

}
