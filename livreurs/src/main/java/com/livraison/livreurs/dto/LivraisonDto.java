package com.livraison.livreurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDto {
    private Long id;
    private Date date_livraison ;
    private String destination;
    private Long livreurId;


}
