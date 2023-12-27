package com.livraison.commercants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private CommercantsDto commercantsDto;
    private List<ColisDto> colisList; // Change to a list to store multiple Colis items
}
