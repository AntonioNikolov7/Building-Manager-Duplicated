package com.blankfactor.MaintainMe.repository;

import com.blankfactor.MaintainMe.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//repository

//creates CRUD Repository for the user with all sorts of methods and since my user has an ID
// of type Long I pass the Identifier as Long
public interface LocalUserRepository extends ListCrudRepository<User,Long> {


    Optional<User> findByEmailIgnoreCase(String email);


    Long findUserIdByEmail(String email);

    @Transactional(readOnly = true)
    @Query(value = "select * from user where auto_pay =1", nativeQuery = true)
    Slice<User> findAllByAutoPay(Pageable page);



    @Query(value = "SELECT * FROM User u WHERE u.email = :email", nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM User u WHERE u.unit_id = :unitId", nativeQuery = true)
    List<User> getUsersByUnitId(@Param("unitId") Long unitId);
    @Query(value = "select * from user where id = :userId", nativeQuery = true)
    User getUserById(@Param("userId") Long userID);


    @Query(value = "select * from user where auto_pay =1", nativeQuery = true)
    List<User> getUserByAutoPay();



}
