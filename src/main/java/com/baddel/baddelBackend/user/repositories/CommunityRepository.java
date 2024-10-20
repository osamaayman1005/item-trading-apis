package com.baddel.baddelBackend.user.repositories;

import com.baddel.baddelBackend.user.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
