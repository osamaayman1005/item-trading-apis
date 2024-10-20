package com.baddel.baddelBackend.item.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class PriceVariation {
    private Double maximumAmountToPay;
    private Double maximumAmountToReceive;
}
