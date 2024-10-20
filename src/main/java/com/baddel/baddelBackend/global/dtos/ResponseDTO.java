package com.baddel.baddelBackend.global.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDTO {
    private String message;
    private Object data;
}
