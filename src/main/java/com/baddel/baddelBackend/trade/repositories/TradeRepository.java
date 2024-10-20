package com.baddel.baddelBackend.trade.repositories;

import com.baddel.baddelBackend.trade.models.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query("SELECT DISTINCT t FROM Trade t " +
            "JOIN t.initiatorItems initiatorItem " +
            "JOIN initiatorItem.lister initiatorLister " +
            "WHERE initiatorLister.id = :listerId order by t.createdAt desc ")
    Page<Trade> findAllTradesByInitiatorListerId(@Param("listerId") Long listerId, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Trade t " +
            "JOIN t.receiverItems receiverItem " +
            "JOIN receiverItem.lister receiverLister " +
            "WHERE receiverLister.id = :listerId order by t.createdAt desc ")
    Page<Trade> findAllTradesByReceiverListerId(@Param("listerId") Long listerId, Pageable pageable);

}
