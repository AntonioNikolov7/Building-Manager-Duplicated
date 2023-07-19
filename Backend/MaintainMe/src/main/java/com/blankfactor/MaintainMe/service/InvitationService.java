package com.blankfactor.MaintainMe.service;

import com.blankfactor.MaintainMe.entity.Invitation;
import com.blankfactor.MaintainMe.repository.BuildingRepository;
import com.blankfactor.MaintainMe.repository.InvitationRepository;
import com.blankfactor.MaintainMe.repository.LocalUserRepository;
import com.blankfactor.MaintainMe.repository.UnitRepository;

import com.blankfactor.MaintainMe.web.resource.ManagerCreateUser;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

   private final InvitationRepository invitationRepository;
   private final UnitRepository unitRepository;
   private final LocalUserRepository userRepository;
   private final BuildingRepository buildingRepository;
   private final LocalUserRepository localUserRepository;



    public InvitationService(InvitationRepository invitationRepository, UnitRepository unitRepository, LocalUserRepository userRepository, BuildingRepository buildingRepository, LocalUserRepository localUserRepository) {
        this.invitationRepository = invitationRepository;
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
        this.buildingRepository = buildingRepository;
        this.localUserRepository = localUserRepository;
    }

    public void sendInvitation(ManagerCreateUser managerCreateUser) throws Exception {
       Invitation invitation = new Invitation();



            invitation.setUnit(unitRepository.findById(managerCreateUser.getUnitId())
                    .orElseThrow(null));
            invitation.setBuilding(buildingRepository.findById(managerCreateUser.getBuildingID())
                    .orElseThrow(null));
            invitation.setUser(userRepository.getUserByEmail(managerCreateUser.getEmail()));
            invitation.setJointStatus(false);
            invitationRepository.save(invitation);



    }



}
