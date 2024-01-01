package com.livraison.colis.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDto {
    private Long id;
    private LocalDate date_livraison ;
    private String destination;
}
