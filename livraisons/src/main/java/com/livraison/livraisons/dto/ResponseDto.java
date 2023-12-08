package com.livraison.livraisons.dto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private LivraisonDto livraisonDto;
    private List<ColisDto> colisList; // Change to a list to store multiple Colis items
}
