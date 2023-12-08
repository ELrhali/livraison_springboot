package com.livraison.colis.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDto {
    private Long id;
    private Date date_livraison ;
    private String destination;
}
