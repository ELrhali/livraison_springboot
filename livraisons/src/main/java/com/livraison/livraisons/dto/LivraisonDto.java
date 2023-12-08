package com.livraison.livraisons.dto;

import java.util.Date;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDto {
    private Long id;
    private Date date_livraison ;
    private String destination;

}
