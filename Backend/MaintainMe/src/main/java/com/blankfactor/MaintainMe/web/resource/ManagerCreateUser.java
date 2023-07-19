package com.blankfactor.MaintainMe.web.resource;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCreateUser {
    @NotBlank
    @Size(min = 8, max = 50)
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 2, max = 32)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 32)
    private String lastName;
    Long buildingId;
    Long unitId;

}
