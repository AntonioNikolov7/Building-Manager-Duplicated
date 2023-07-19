package com.blankfactor.MaintainMe.repository;

import com.blankfactor.MaintainMe.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentByNotificationId(Long buildingId);

    @Query(value = "SELECT * FROM comment u WHERE u.id = :id", nativeQuery = true)
    Comment getCommentById(@Param("id") Long id);
}

