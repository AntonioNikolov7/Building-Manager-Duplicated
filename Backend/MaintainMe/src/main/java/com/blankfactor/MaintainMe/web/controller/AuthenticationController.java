package com.blankfactor.MaintainMe.web.controller;

import com.blankfactor.MaintainMe.repository.LocalUserRepository;
import com.blankfactor.MaintainMe.web.exception.UserAlreadyExistsException;
import com.blankfactor.MaintainMe.web.resource.Login.LoginRequest;
import com.blankfactor.MaintainMe.web.resource.Login.LoginResponse;
import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.service.UserService;
import com.blankfactor.MaintainMe.web.resource.ManagerCreateUser;
import com.blankfactor.MaintainMe.web.resource.ManagerRegistrationRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

//Controller for user registration and log in authentication
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final LocalUserRepository repository;

    @PostMapping("/register/manager")
    public ResponseEntity registerManager(@Valid @RequestBody ManagerRegistrationRequest managerRegistrationRequest){
        try {
            userService.registerManager(managerRegistrationRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/manager/register/user")
    public ResponseEntity<String> createManagerUser(@Valid @RequestBody ManagerCreateUser managerCreateUser)
            throws UserAlreadyExistsException {
        try {
            userService.ManagerCreateUser(managerCreateUser);
            return ResponseEntity.ok("User created successfully by the manager");
        } catch (UserAlreadyExistsException e) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.OK).body("User already exists");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginBody){

        String jwt=userService.loginUser(loginBody);
        if(jwt==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {

            User user  = repository.getUserByEmail(loginBody.getEmail());

            LoginResponse response=new LoginResponse();
            response.setJwt(jwt);
            response.setUser(user);
            //response.setRoleInBuilding(userService.getRoleInBuilding());
            return ResponseEntity.ok(response);

        }
    }

}
