package com.blankfactor.MaintainMe.repository;

import com.blankfactor.MaintainMe.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

}
