package com.baddel.baddelBackend.trade.validations;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.item.service.ItemService;
import com.baddel.baddelBackend.trade.dtos.TradeDTO;
import com.baddel.baddelBackend.trade.dtos.TradeUserDTO;
import com.baddel.baddelBackend.trade.enums.TradeStatus;
import com.baddel.baddelBackend.trade.models.Trade;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @AllArgsConstructor
public class TradeValidation {
    private final ItemService itemService;
    public void validateTradeDTO(TradeDTO tradeDTO){
        validateTradeItems(tradeDTO);
        validateTradeParties(tradeDTO);
        validateOnlyOneAmountToPayIsAdded(tradeDTO);
        validateAmountsToPay(tradeDTO);
    }
    public void validateTradeAction(TradeStatus oldStatus, TradeStatus newStatus,
                                    Trade trade, Long userId){
        validateTradeActionUser(trade, userId);
        validateStatusChange(oldStatus, newStatus);
    }
    public void validateTradeHasItems(TradeDTO tradeDTO){
        if (tradeDTO.getFromUser().getItems().size()==0 ||
        tradeDTO.getToUser().getItems().size()==0){
            throw new ApiException("Each trade party need to have at least one item", HttpStatus.BAD_REQUEST);
        }
    }
    public void validateCreationContext(Long itemId, ApplicationUser initiator){
        int initiatorItemCount = itemService.getListerItems(initiator.getId(), PageRequest.of(0,10)).stream().toList().size();
        if(initiatorItemCount==0){
            throw new ApiException("You don't have any items",HttpStatus.NOT_ACCEPTABLE);
        }
    }

        private void validateTradeActionUser(Trade trade, Long userId){
        if (trade.getStatus().equals(TradeStatus.FINISHED) &&
                !(trade.getInitiatorItems().get(0).getLister().getId().equals(userId)
                 || trade.getReceiverItems().get(0).getLister().getId().equals(userId))) {
            throw new ApiException("You are not a trade party", HttpStatus.BAD_REQUEST);
        }
        if(!trade.getReceiverItems().get(0).getLister().getId().equals(userId)){
            throw new ApiException("Only trade recipient can change this trade to this status", HttpStatus.BAD_REQUEST);
        }
    }
    private void validateStatusChange(TradeStatus oldStatus, TradeStatus newStatus){
        if(oldStatus.equals(newStatus)){
            throw new ApiException("Trade status did not change", HttpStatus.BAD_REQUEST);
        }
        if (newStatus.equals(TradeStatus.PENDING)){
            throw new ApiException("Trade status cannot be changed to pending", HttpStatus.BAD_REQUEST);

        }
        if (oldStatus.equals(TradeStatus.REJECTED)
                || oldStatus.equals(TradeStatus.COUNTER_OFFERED)
                || oldStatus.equals(TradeStatus.FINISHED)){
            throw new ApiException("Trade already ended", HttpStatus.BAD_REQUEST);
        }
        if (!oldStatus.equals(TradeStatus.STARTED) && newStatus.equals(TradeStatus.FINISHED)){
            throw new ApiException("You can't finish this trade", HttpStatus.BAD_REQUEST);
        }
    }
    private void validateTradeItems(TradeDTO tradeDTO){
        validateItemsConsistency(tradeDTO.getFromUser().getItems(), tradeDTO.getFromUser().getId());
        validateItemsConsistency(tradeDTO.getToUser().getItems(), tradeDTO.getToUser().getId());
    }
    private void validateItemsConsistency(List<ItemDTO> items, Long userId){
        for (ItemDTO item: items) {
            ItemDTO databaseItem = itemService.getItemById(item.getId());
            if (databaseItem == null){
                throw new ApiException("item '"+ item.getId()
                        +"' does not exist or deleted", HttpStatus.BAD_REQUEST);
            }
            if(!databaseItem.getListerId().equals(userId)){
                throw new ApiException("item '"+ item.getId()
                        + "' does not belong to user: '" + userId
                        + "'", HttpStatus.BAD_REQUEST);
            }
            if (!databaseItem.getStatus().equals(ItemStatus.LISTED)){
                throw new ApiException("item '"+ item.getId()
                        +"' is not listed anymore", HttpStatus.BAD_REQUEST);
            }
        }
    }
    private void validateTradeParties(TradeDTO tradeDTO){
        if (tradeDTO.getToUser().getId().equals(tradeDTO.getFromUser().getId())){
            throw new ApiException("you can't trade yourself", HttpStatus.BAD_REQUEST);
        }
    }
    private void validateAmountsToPay(TradeDTO tradeDTO){
        validateUserAmountToPay(tradeDTO.getFromUser());
        validateUserAmountToPay(tradeDTO.getToUser());
    }
    private void validateUserAmountToPay(TradeUserDTO user){
        if (user.getAmountToPay() != null && user.getAmountToPay() < 0){
            throw new ApiException("wrong amount to pay for this trade should be greater than 0",
                    HttpStatus.BAD_REQUEST);
        }
    }
    private void validateOnlyOneAmountToPayIsAdded(TradeDTO tradeDTO){
        if(tradeDTO.getFromUser().getAmountToPay() != null &&
                tradeDTO.getToUser().getAmountToPay() != null){
            throw new ApiException("both trading parties cannot pay at the same time",
                    HttpStatus.BAD_REQUEST);
        }
    }


}
