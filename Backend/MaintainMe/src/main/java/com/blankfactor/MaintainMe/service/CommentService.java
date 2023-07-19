package com.blankfactor.MaintainMe.service;

import com.blankfactor.MaintainMe.entity.Comment;
import com.blankfactor.MaintainMe.entity.Notification;
import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.repository.CommentRepository;
import com.blankfactor.MaintainMe.repository.LocalUserRepository;
import com.blankfactor.MaintainMe.repository.NotificationRepository;
import com.blankfactor.MaintainMe.web.exception.InvalidCommentException;
import com.blankfactor.MaintainMe.web.resource.Comment.CommentByNotificationRequest;
import com.blankfactor.MaintainMe.web.resource.Comment.CommentRequest;
import com.blankfactor.MaintainMe.web.resource.Comment.EditCommentRequest;
import com.blankfactor.MaintainMe.web.resource.Notification.DeleteCommentRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final JWTService jwtService;
    private final LocalUserRepository userRepository;

    public List<Comment> getCommentByNotificationId(Long id){
        return commentRepository.getCommentByNotificationId(id);
    }

    public Comment sendComment(CommentRequest commentRequest, Long id) throws Exception {      

        String email =  jwtService.getEmail(commentRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);


        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new Exception("Notification not found"));

        Date date = new Date();

        try {
            var comment = Comment.builder()
                    .text(commentRequest.getText())
                    .user(authUser)
                    .notification(notification)
                    .date(date)
                    .build();

            commentRepository.save(comment);

        }catch (Exception ex){
            throw new InvalidCommentException(ex.getMessage());
        }

        return null;
    }

    public Comment editComment(EditCommentRequest editCommentRequest, Long id) throws Exception{

        String email =  jwtService.getEmail(editCommentRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Notification not found"));

        if(comment.getUser().getId() == authUser.getId()) {
            comment.setText(editCommentRequest.getText());
            commentRepository.save(comment);
        }else{
            throw new Exception("No access");
        }

        return null;
    }

    public Comment deleteComment(DeleteCommentRequest deleteCommentRequest, Long id){

        Comment delete = commentRepository.getCommentById(id);

        String email =  jwtService.getEmail(deleteCommentRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        try {
            if(authUser.getId() == delete.getUser().getId()) {
                commentRepository.deleteById(id);
            }

        }catch (Exception ex){
            throw new InvalidCommentException(ex.getMessage());
        }

        return null;
    }
}