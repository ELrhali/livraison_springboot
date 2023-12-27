package com.livraison.colis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercantsDto {
    private Long id;
    private String CIN;
    private String nom;
    private String prenom;
    private String code;
    private String addrese;
    private String phone;
    private String email;


}
