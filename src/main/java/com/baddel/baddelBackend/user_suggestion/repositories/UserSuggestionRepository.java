package com.baddel.baddelBackend.user_suggestion.repositories;

import com.baddel.baddelBackend.user_suggestion.models.UserSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuggestionRepository extends JpaRepository<UserSuggestion , Long> {
}

//DONT FORGET TO ADD THOSE TYPES
// CREATE TYPE suggestion_type AS ENUM ('CATEGORY', 'COMMUNITY', 'ENHANCEMENT', 'OTHER');
//CREATE TYPE suggestion_STATUS AS ENUM ('RECEIVED', 'TO_DO', 'REJECTED', 'DONE');