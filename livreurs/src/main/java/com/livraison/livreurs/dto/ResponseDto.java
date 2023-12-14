package com.livraison.livreurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private LivreurDto livreurDto;
    private List<ResponseDtoLivraison> livraisonList;
}
