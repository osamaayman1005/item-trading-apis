package com.baddel.baddelBackend.item.repositories;

import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.item.models.Item;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByIsDeletedFalse();
    Optional<Item> findByIdAndIsDeletedFalse(Long id);
    Optional<Item> findItemByIdAndListerAndIsDeletedFalse(Long id, ApplicationUser Lister);
    List<Item> findByListerIdAndIsDeletedFalse(Long listerId);

    Page<Item> findAllByIsDeletedFalseAndStatus(ItemStatus status, Pageable pageable);
    @Query(value = "SELECT * " +
            "FROM baddel.item " +
            "WHERE " +
            "item_status = 'LISTED' AND is_deleted = false AND (category_id in :categoryIds) "
            , nativeQuery = true)
    Page<Item> findAllByIsDeletedFalseAndStatusListedAndCategory(@Param("categoryIds")List<Long> categoryIds, Pageable pageable);

    Optional<Item> findByIdAndIsDeletedFalseAndStatus(Long id, ItemStatus status);

    Optional<Item> findItemByIdAndListerAndIsDeletedFalseAndStatus(Long id, ApplicationUser lister, ItemStatus status);

    Page<Item> findByListerIdAndIsDeletedFalseAndStatus(Long listerId, ItemStatus status, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM search_v1(:searchTerm,:categoryIds)", nativeQuery = true)
    Page<Item> searchItemsByNameAndDescriptionAndCategory(@Param("searchTerm") String searchTerm,
                                                          @Param("categoryIds")Long[] categoryIds,
                                                          Pageable pageable);

// remember to run those if search query failed
//CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;
//CREATE EXTENSION IF NOT EXISTS pg_trgm;
}
