package com.baddel.baddelBackend.user.services;

import com.baddel.baddelBackend.user.dtos.CommunityDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.models.Community;
import com.baddel.baddelBackend.user.repositories.CommunityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class CommunityService {
    private CommunityRepository communityRepository;

    public List<CommunityDTO> getAllCommunities(){
        return communityRepository.findAll().stream().map(Community::toCommunityDTO).toList();
    }
    public CommunityDTO getCommunityById(Long id){
        return communityRepository.findById(id).get().toCommunityDTO();
    }
    public CommunityDTO createCommunity(CommunityDTO communityDTO){
        return communityRepository.save(communityDTO.toCommunity()).toCommunityDTO();
    }
    public CommunityDTO updateCommunity(CommunityDTO communityDTO){
        List<ApplicationUser> members = communityRepository.findById(communityDTO.getId())
                .get().getMembers();
        return communityRepository.save(communityDTO.toCommunity(members)).toCommunityDTO();
    }
    public void deactivateCommunity(Long id){
        Community community = communityRepository.findById(id).get();
        community.setIsActive(false);
        communityRepository.save(community);
    }
}
