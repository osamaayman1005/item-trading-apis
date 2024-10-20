package com.baddel.baddelBackend.trade.dtos;

import com.baddel.baddelBackend.item.models.Item;
import com.baddel.baddelBackend.trade.enums.TradeStatus;
import com.baddel.baddelBackend.trade.models.Trade;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class TradeDTO {
    private Long id;
    private TradeUserDTO fromUser;
    private TradeUserDTO toUser;
    private TradeStatus status;
    private Long counterOfferedTradeId;
    private Date createdAt;
    private Date updatedAt;

    public Trade toTrade() {
        Trade trade = new Trade();
        trade.setId(id);
        trade.setInitiatorItems(fromUser.getItems().stream()
                .map(itemDTO -> {
                    Item item = new Item();
                    itemDTO.setId(itemDTO.getId());
                    return item;
                }).toList());
        trade.setReceiverItems(toUser.getItems().stream()
                .map(itemDTO -> {
                    Item item = new Item();
                    itemDTO.setId(itemDTO.getId());
                    return item;
                }).toList());
        trade.setStatus(status);
        trade.setInitiatorAmountToPay(fromUser.getAmountToPay());
        trade.setReceiverAmountToPay(toUser.getAmountToPay());
        return trade;
    }
    public TradeDTO(Trade trade){
        ApplicationUser initiator = trade.getInitiatorItems().get(0).getLister();
        ApplicationUser receiver = trade.getReceiverItems().get(0).getLister();
        id = trade.getId();
        fromUser = new TradeUserDTO(initiator, trade.getInitiatorItems().stream()
                .map(item -> item.toItemDTO()).toList(), trade.getInitiatorAmountToPay());
        toUser = new TradeUserDTO(receiver, trade.getReceiverItems().stream()
                .map(item -> item.toItemDTO()).toList(), trade.getReceiverAmountToPay());
        status = trade.getStatus();
        createdAt = trade.getCreatedAt();
        updatedAt = trade.getUpdatedAt();
    }
}
