package com.baddel.baddelBackend.trade.services;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.item.repositories.ItemRepository;
import com.baddel.baddelBackend.item.service.ItemService;
import com.baddel.baddelBackend.trade.dtos.TradeDTO;
import com.baddel.baddelBackend.trade.dtos.TradeUserDTO;
import com.baddel.baddelBackend.trade.enums.TradeStatus;
import com.baddel.baddelBackend.trade.models.Trade;
import com.baddel.baddelBackend.trade.repositories.TradeRepository;
import com.baddel.baddelBackend.trade.validations.TradeValidation;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TradeService {
    private TradeRepository tradeRepository;
    private ItemRepository itemRepository;
    private ItemService itemService;
    private TradeValidation tradeValidation;
    private UserService userService;
    public TradeDTO saveTrade(TradeDTO tradeDTO, String username) {
        tradeValidation.validateTradeHasItems(tradeDTO);
        setTradeParties(tradeDTO, username);
        tradeValidation.validateTradeDTO(tradeDTO);
        Trade trade = tradeDTO.toTrade();
        trade.setStatus(TradeStatus.PENDING);
        trade.setInitiatorItems(tradeDTO.getFromUser()
                .getItems().stream().map(itemDTO ->
                        itemRepository.findByIdAndIsDeletedFalse(itemDTO.getId()).get()).toList());
        trade.setReceiverItems(tradeDTO.getToUser()
                .getItems().stream().map(itemDTO ->
                        itemRepository.findByIdAndIsDeletedFalse(itemDTO.getId()).get()).toList());
        Trade savedTrade = tradeRepository.save(trade);
        return new TradeDTO(savedTrade);
    }

    public Page<TradeDTO> getOutgoingTrades(String username, Pageable pageable){
        ApplicationUser user = userService.getUserByUsername(username);
        return tradeRepository.findAllTradesByInitiatorListerId(user.getId(), pageable)
                .map(TradeDTO::new);
    }
    public Page<TradeDTO> getIncomingTrades(String username, Pageable pageable){
        ApplicationUser user = userService.getUserByUsername(username);
        return tradeRepository.findAllTradesByReceiverListerId(user.getId(), pageable)
                .map(TradeDTO::new);
    }
    public TradeDTO getTradeById(Long id) {
        return new TradeDTO(tradeRepository.findById(id)
                .orElseThrow(() -> new ApiException("Trade not found",HttpStatus.NOT_FOUND)));
    }
    public TradeDTO changeTradeStatus(Long tradeId, TradeStatus tradeStatus, String username){
        ApplicationUser user = userService.getUserByUsername(username);
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new ApiException("Trade not found", HttpStatus.NOT_FOUND));
        tradeValidation.validateTradeAction(trade.getStatus(), tradeStatus, trade, user.getId());
        trade.setStatus(tradeStatus);
        return new TradeDTO(tradeRepository.save(trade));
    }
    @Transactional
    public TradeDTO finishTrade(Long tradeId, String username){
        TradeDTO trade = changeTradeStatus(tradeId, TradeStatus.FINISHED, username);
        markTradePartyItemsAsTraded(trade.getFromUser().getItems());
        markTradePartyItemsAsTraded(trade.getToUser().getItems());
        return trade;
    }
    @Transactional
    public TradeDTO counterOfferTrade(Long tradeId,TradeDTO tradeDTO,
                                       String username){
        ApplicationUser user = userService.getUserByUsername(username);
        Trade oldTrade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new ApiException("Trade not found", HttpStatus.NOT_FOUND));
        tradeValidation.validateTradeAction(oldTrade.getStatus(), TradeStatus.COUNTER_OFFERED, oldTrade, user.getId());
        oldTrade.setStatus(TradeStatus.COUNTER_OFFERED);
        tradeDTO.setCounterOfferedTradeId(tradeId);
        tradeRepository.save(oldTrade);
        return saveTrade(tradeDTO, username);
    }
    public void getCreationContext(Long itemId,String initiatorUsername){
        ApplicationUser initiator = userService.getUserByUsername(initiatorUsername);
        tradeValidation.validateCreationContext(itemId, initiator);
    }
    private void setTradeParties(TradeDTO tradeDTO, String initiatorUsername){
        ApplicationUser user = userService.getUserByUsername(initiatorUsername);
        TradeUserDTO initiator = tradeDTO.getFromUser();
        TradeUserDTO receiver = tradeDTO.getToUser();

        initiator.setId(user.getId());
        receiver.setId(itemRepository
                .findById(tradeDTO.getToUser().getItems().get(0).getId())
                .get().getLister().getId());

        tradeDTO.setFromUser(initiator);
        tradeDTO.setToUser(receiver);

    }

    private void markTradePartyItemsAsTraded(List<ItemDTO> items){
        for (ItemDTO item : items) {
            Long itemId = item.getId();
            itemService.updateItemStatus(itemId, ItemStatus.TRADED);
        }
    }
}
