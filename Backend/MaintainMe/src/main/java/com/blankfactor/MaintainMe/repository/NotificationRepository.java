package com.blankfactor.MaintainMe.repository;


import com.blankfactor.MaintainMe.entity.Comment;
import com.blankfactor.MaintainMe.entity.Invoice;
import com.blankfactor.MaintainMe.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> getNotificationByBuildingId(Long buildingId);

    @Query(value = "SELECT * FROM notification u WHERE u.id = :id", nativeQuery = true)
    Notification getNotificationById(@Param("id") Long id);



}
