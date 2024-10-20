package com.baddel.baddelBackend.user.controllers;

import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import com.baddel.baddelBackend.user.dtos.CommunityDTO;
import com.baddel.baddelBackend.user.services.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @AllArgsConstructor
public class CommunityController {
    private CommunityService communityService;

    @GetMapping("/api/v1/admin/communities")
    public ResponseDTO getAllCommunities(){
        return new ResponseDTO("communities retrieved successfully",
                communityService.getAllCommunities());
    }
    @GetMapping("/api/v1/admin/communities/{communityId}")
    public ResponseDTO getCommunityById(@PathVariable Long communityId){
        return new ResponseDTO("community retrieved successfully",
                communityService.getCommunityById(communityId));
    }
    @PostMapping("/api/v1/admin/communities")
    public ResponseDTO createCommunity(@RequestBody CommunityDTO communityDTO){
        return new ResponseDTO("community created successfully",
                communityService.createCommunity(communityDTO));
    }
    @PutMapping ("/api/v1/admin/communities")
    public ResponseDTO updateCommunity(@RequestBody CommunityDTO communityDTO){
        return new ResponseDTO("community updated successfully",
                communityService.updateCommunity(communityDTO));
    }
    @DeleteMapping("/api/v1/admin/communities/{communityId}")
    public ResponseDTO deleteCommunity(@PathVariable Long communityId){
        communityService.deactivateCommunity(communityId);
        return new ResponseDTO("community deleted successfully",
                null);
    }
}
