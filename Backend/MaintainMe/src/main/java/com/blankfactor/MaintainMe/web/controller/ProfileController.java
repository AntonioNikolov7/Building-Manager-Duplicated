package com.blankfactor.MaintainMe.web.controller;

import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.service.ProfileService;
import com.blankfactor.MaintainMe.web.resource.ProfileEditRequest;
import com.blankfactor.MaintainMe.web.resource.ProfileInfoRequest;
import com.blankfactor.MaintainMe.web.resource.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/info")
    public UserInfoResponse getInfo(@RequestBody ProfileInfoRequest request) {
        return profileService.getInfo(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<User> editInfo(@RequestBody ProfileEditRequest request){
        return ResponseEntity.ok(profileService.editInfo(request));
    }



}