package com.petsuite.Services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client_Dto {

    private String user;
    private String password;
    private String client_name;
    private String client_phone;
    private String client_e_mail;
    private String client_address;
    private String token;
    private String role;

    public Client_Dto(String client_name, String client_phone, String client_e_mail, String client_address, String client_token) {
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.client_e_mail = client_e_mail;
        this.client_address = client_address;
        this.token=client_token;
        
    }

}

