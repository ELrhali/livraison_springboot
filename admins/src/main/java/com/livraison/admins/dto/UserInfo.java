package com.livraison.admins.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String name;
    private String prenom;
    private String email;
    private String password;
    private String cin;
    private String roles;
    private Long livreurId;
    private Long  commercantId;
    private String phone;
    private String addrese;


}
