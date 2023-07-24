package com.blankfactor.MaintainMe.web.resource.Notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEditRequest {

    private String token;

    @NotBlank(message = "message title cannot be empty.")
    @NotEmpty
    private String title;

    @NotBlank(message = "message information cannot be empty.")
    @NotEmpty
    private String description;

}