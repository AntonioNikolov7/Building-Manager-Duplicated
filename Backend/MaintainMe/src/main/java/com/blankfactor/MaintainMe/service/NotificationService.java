package com.blankfactor.MaintainMe.service;

import com.blankfactor.MaintainMe.entity.Building;
import com.blankfactor.MaintainMe.entity.Notification;
import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.repository.BuildingRepository;
import com.blankfactor.MaintainMe.repository.LocalUserRepository;
import com.blankfactor.MaintainMe.repository.NotificationRepository;
import com.blankfactor.MaintainMe.repository.UserRoleBuildingRepository;

import com.blankfactor.MaintainMe.web.exception.InvalidNotificationException;
import com.blankfactor.MaintainMe.web.resource.Notification.NotificationDeleteRequest;
import com.blankfactor.MaintainMe.web.resource.Notification.NotificationEditRequest;
import com.blankfactor.MaintainMe.web.resource.Notification.NotificationRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BuildingRepository buildingRepository;
    private final UserRoleBuildingRepository userRoleBuildingRepository;
    private final EmailService emailService;
    private final JWTService jwtService;
    private final LocalUserRepository userRepository;

    public List<Notification> getAllNotificationsByBuilding(Long id) {
        return notificationRepository.getNotificationByBuildingId(id);
    }

    public Notification sendNotification(NotificationRequest notificationRequest) throws Exception {

        Building building =  buildingRepository.findById(notificationRequest.getBuildingId())
                .orElseThrow(() -> new Exception("Building not found"));

        String email =  jwtService.getEmail(notificationRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        Date date = new Date();
        List<String> emailsToSend = userRoleBuildingRepository.getUsersByBuildingId(building.getId());

        try {

            var notification = Notification.builder()
                    .title(notificationRequest.getTitle())
                    .description(notificationRequest.getDescription())
                    .date(date)
                    .building(building)
                    .user(authUser)
                    .build();

            for (int i =0; i <emailsToSend.size();i++){
                emailService.sendEmail(emailsToSend.get(i), notificationRequest.getTitle(), notificationRequest.getDescription());
            }

            notificationRepository.save(notification);
        }catch (Exception ex){
            throw new InvalidNotificationException(ex.getMessage());
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    public Notification editNotification(NotificationEditRequest notificationEditRequest, Long id) throws Exception {

        String email =   jwtService.getEmail(notificationEditRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new Exception("Notification not found"));

        if(notification.getUser().getId() == authUser.getId()){
            notification.setDescription(notificationEditRequest.getDescription());
            notification.setTitle(notificationEditRequest.getTitle());
            notificationRepository.save(notification);
        }else {
            throw new Exception("unable to edit foreign notifications");
        }
        return null;
    }


    public Notification deleteNotification(NotificationDeleteRequest notificationDeleteRequest, Long id) {

        String email =   jwtService.getEmail(notificationDeleteRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        Notification deleteNotification = notificationRepository.getNotificationById(id);



        try {
            if(authUser.getId() == deleteNotification.getUser().getId() ) {
                notificationRepository.deleteById(id);
                System.out.println("deleted");
            }
        }catch (Exception ex){
            throw new InvalidNotificationException(ex.getMessage());
        }
        return null;
    }
}