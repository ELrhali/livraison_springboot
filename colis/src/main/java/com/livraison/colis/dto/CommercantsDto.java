package com.livraison.colis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercantsDto {
    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String code;
    private String addrese;
    private String phone;
    private String email;

    private LocalDate dateCreation = LocalDate.now();


}
