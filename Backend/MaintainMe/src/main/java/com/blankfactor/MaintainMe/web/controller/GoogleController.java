package com.blankfactor.MaintainMe.web.controller;

import com.blankfactor.MaintainMe.entity.Comment;
import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class GoogleController {
  private final UserService userService;

    @GetMapping()
    public ResponseEntity<User> getCurrentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return ResponseEntity.ok(userService.getCurrentUser(oAuth2AuthenticationToken));
    }

}
