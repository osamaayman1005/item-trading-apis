package com.baddel.baddelBackend.trade.controllers;

import com.baddel.baddelBackend.trade.dtos.TradeDTO;
import com.baddel.baddelBackend.trade.enums.TradeStatus;
import com.baddel.baddelBackend.trade.services.TradeService;
import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TradeController {
    private final TradeService tradeService;

        @GetMapping("/api/v1/user/trades/incoming")
    public ResponseDTO getIncomingTrades(Authentication authentication,
                                         @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                         @RequestParam(required = false, defaultValue = "20") int pageSize) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Incoming trades retrieved successfully");
        responseDTO.setData(tradeService.getIncomingTrades(authentication.getName(), pageable));
        return responseDTO;
    }
    @GetMapping("/api/v1/user/trades/outgoing")
    public ResponseDTO getOutgoing(Authentication authentication,
                                   @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                   @RequestParam(required = false, defaultValue = "20") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Outgoing trades retrieved successfully");
        responseDTO.setData(tradeService.getOutgoingTrades(authentication.getName(), pageable));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/trades")
    public ResponseDTO saveItem(@RequestBody TradeDTO trade, Authentication authentication) {
        System.out.println("trade: " + trade);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade created successfully");
        responseDTO.setData(tradeService.saveTrade(trade, authentication.getName()));

        return responseDTO;
    }
    @GetMapping("/api/v1/user/trades/{tradeId}")
    public ResponseDTO getTradeById(@PathVariable Long tradeId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade retrieved successfully");
        responseDTO.setData(tradeService.getTradeById(tradeId));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/trades/{tradeId}/start")
    public ResponseDTO startTrade(@PathVariable Long tradeId, Authentication authentication){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade started successfully");
        responseDTO.setData(tradeService.changeTradeStatus(tradeId,
                TradeStatus.STARTED, authentication.getName()));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/trades/{tradeId}/finish")
    public ResponseDTO finishTrade(@PathVariable Long tradeId, Authentication authentication){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade finished successfully");
        responseDTO.setData(tradeService.finishTrade(tradeId, authentication.getName()));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/trades/{tradeId}/reject")
    public ResponseDTO rejectTrade(@PathVariable Long tradeId, Authentication authentication){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade rejected successfully");
        responseDTO.setData(tradeService.changeTradeStatus(tradeId,
                TradeStatus.REJECTED, authentication.getName()));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/trades/{tradeId}/counter-offer")
    public ResponseDTO counterOfferTrade(@PathVariable Long tradeId,
                                         @RequestBody TradeDTO trade,
                                         Authentication authentication){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Trade counter offered successfully");
        responseDTO.setData(tradeService.counterOfferTrade(tradeId,
                 trade, authentication.getName()));
        return responseDTO;
    }
    @GetMapping("/api/v1/user/trades/creation-context")
    public ResponseDTO getCreationContext(Authentication authentication,
                                         @RequestParam Long itemId) {
            tradeService.getCreationContext(itemId, authentication.getName());
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setMessage("You can start the trade");
            return responseDTO;
    }
}
