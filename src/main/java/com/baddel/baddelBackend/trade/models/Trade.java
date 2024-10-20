package com.baddel.baddelBackend.trade.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.baddel.baddelBackend.item.models.Item;
import com.baddel.baddelBackend.trade.enums.TradeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name= "trade")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Trade extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @NotNull
    private TradeStatus status;
    @ManyToMany
    @JoinTable(
            name = "trade_initiator_items_junction",
            joinColumns = @JoinColumn(name = "trade_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> initiatorItems;

    private Double initiatorAmountToPay;
    private Double receiverAmountToPay;
    @ManyToMany
    @JoinTable(
            name = "trade_receiver_items_junction",
            joinColumns = @JoinColumn(name = "trade_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> receiverItems;
}
