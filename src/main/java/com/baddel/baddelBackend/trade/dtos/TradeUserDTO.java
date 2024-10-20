package com.baddel.baddelBackend.trade.dtos;

import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.user.dtos.UserDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import lombok.*;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeUserDTO extends UserDTO {

    private List<ItemDTO> items;
    private Double amountToPay;
    public TradeUserDTO(ApplicationUser user, List<ItemDTO> items, Double amountToPay){
        super(user);
        this.items = items;
        this.amountToPay = amountToPay;
    }
}
