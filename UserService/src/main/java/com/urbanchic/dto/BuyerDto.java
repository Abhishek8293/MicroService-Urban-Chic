package com.urbanchic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {

    private String buyerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

}
