package com.livraison.livraisons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreurDto {
    private Long id;
    private String nom;
    private String prenom;
    private String vehicule;
    private boolean disponible;
    private String statusColis;
    private String code;
    private String addrese;
    private String phone;

}
