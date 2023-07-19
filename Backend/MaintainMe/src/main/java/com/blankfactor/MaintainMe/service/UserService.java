package com.blankfactor.MaintainMe.service;

import com.blankfactor.MaintainMe.entity.*;
import com.blankfactor.MaintainMe.repository.*;
import com.blankfactor.MaintainMe.web.assembler.BuildingAssembler;
import com.blankfactor.MaintainMe.web.exception.UserAlreadyExistsException;
import com.blankfactor.MaintainMe.web.resource.*;
import com.blankfactor.MaintainMe.web.resource.Login.LoginRequest;
import com.blankfactor.MaintainMe.web.utilities.GmailChecker;
import com.blankfactor.MaintainMe.web.utilities.RandomPassword;
import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UserService {

    private final LocalUserRepository localUserRepository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final AddressRepository addressRepository;
    private final BuildingRepository buildingRepository;
    private final EmailService emailService;
    private final UserRoleBuildingRepository userRoleBuildingRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final UnitRepository unitRepository;
    private final InvitationService invitationService;


    public User register(RegistrationRequestManager registrationRequestManager) throws UserAlreadyExistsException {
        if (localUserRepository.findByEmailIgnoreCase(registrationRequestManager.getEmail()).isPresent()){
        throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(registrationRequestManager.getEmail());
        user.setFirstName(registrationRequestManager.getFirstName());
        user.setLastName(registrationRequestManager.getLastName());
        user.setPassword(encryptionService.encryptPassword(registrationRequestManager.getPassword()));
        return localUserRepository.save(user);


    }

    @Transactional(rollbackFor = Exception.class)
    public void registerManager(ManagerRegistrationRequest request) throws UserAlreadyExistsException {


        BuildingResource buildingResource = request.getBuildingResource();
        BuildingAssembler buildingAssembler = new BuildingAssembler();
        Building building = buildingAssembler.fromResource(buildingResource);
        Building savedBuilding = buildingRepository.save(building);
        User user = register(request.getRegistrationRequestManager());

        Role role = new Role();
        role.setId(2L);

        UserRoleBuilding userRoleBuilding = new UserRoleBuilding(user, role, savedBuilding);
        userRoleBuildingRepository.save(userRoleBuilding);
        authenticate(user);
    }

    @Transactional
    public void ManagerCreateUser(ManagerCreateUser managerCreateUser) throws Exception {

        UserRoleBuilding userRoleBuilding = new UserRoleBuilding();

        Building building = buildingRepository.findById(managerCreateUser.getBuildingId()).orElse(null);
        Role role = new Role();
        role.setId(1L);
        user.setUnit(unitRepository.findById(managerCreateUser.getUnitId()).orElse(null));
        userRoleBuilding.setUser(user);
        userRoleBuilding.setBuilding(building);
        userRoleBuilding.setRole(role);

        String email = user.getEmail();
        boolean isGoogleMail = GmailChecker.isGoogleEmail(email);

        String subject;
        String body;

        String password = RandomPassword.generateRandomPassword();
        user.setPassword(encryptionService.encryptPassword(password));

        if (isGoogleMail) {
            user.setPassword(RandomPassword.generateRandomPassword());
            subject = "Account Registration - Login Credentials";
            body = "Hello " + user.getFirstName() + ",\n\n"
                    + "Your account has been created. Here are your credentials:\n"
                    + "Password: " + password + "\n\n"
                    + "Your account has been created. You can log in to the app using your Google profile or Your Credentials.\n"
                    + "Click the following link to log in: " + "https://example.com/google-login";
        } else {


            // Send email with a link to login and the credentials (username and randomly generated password)
            subject = "Account Registration - Login Credentials";
            body = "Hello " + user.getFirstName() + ",\n\n"
                    + "Your account has been created. Here are your credentials:\n"
                    + "Password: " + password + "\n\n"
                    + "Please login and change your password for security reasons.";
        }

            localUserRepository.save(user);

            emailService.sendEmail(user.getEmail(), subject, body);

            userRoleBuildingRepository.save(userRoleBuilding);
        }

        invitationService.sendInvitation(managerCreateUser);

    }

    public String loginUser(LoginRequest loginBody) {
        Optional<User> optionalUser = localUserRepository.findByEmailIgnoreCase(loginBody.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
              authenticate(user);
              return jwtService.generateJWT(user);
            }

        }
        return null;
    }


    @Transactional
    public void updateResetPasswordToken(String token, String email) throws Exception {

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        User user = localUserRepository.getUserByEmail(email);
        resetTokenRepository.deleteAllByUser_Id(user.getId());

        System.out.println("print " + user);

        if (user != null) {

            Calendar date = Calendar.getInstance();
            System.out.println("Current Date and TIme : " + date.getTime());
            long timeInSecs = date.getTimeInMillis();
            Date expDate = new Date(timeInSecs + (30 * 60 * 1000));
            System.out.println("After adding 30 mins : " + expDate);

            passwordResetToken.setUser(user);
            passwordResetToken.setToken(token);
            passwordResetToken.setExpiryDate(expDate);
            resetTokenRepository.save(passwordResetToken);
            localUserRepository.save(user);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        Long userId = resetTokenRepository.getUserIdByToken(token);
        return localUserRepository.findById(userId).orElse(null);
    }

    public void updatePassword(User user, String newPassword) {

        user.setPassword(encryptionService.encryptPassword(newPassword));

        PasswordResetToken passwordResetToken = resetTokenRepository.getPasswordResetTokenByEmail(user.getEmail());
        resetTokenRepository.delete(passwordResetToken);
        localUserRepository.save(user);

    }

    private String getEmailFromToken(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        return (String) attributes.get("email");
    }



    public Collection<Map<String, Object>> getBuildingsManagedByLoggedManager() {

        User authUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Role role = new Role();
        role.setId(2L);
        Collection<Map<String, Object>> buildingId = userRoleBuildingRepository.getBuildingDataByUserIdAndRoleId(authUser.getId(), role.getId());
        return buildingId;

    }


    public Map<String, Object> getRoleInBuilding() {
        User authUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Map<String, Object> roleInBuilding = userRoleBuildingRepository.findRoleAndBuildingByUserId(authUser.getId());
        return roleInBuilding;
    }


    public User getCurrentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String email = getEmailFromToken(oAuth2AuthenticationToken);
        User user = localUserRepository.findByEmailIgnoreCase(email).orElse(null);
        authenticate(user);
        return user;
    }

    public void authenticate(User user){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }






}
