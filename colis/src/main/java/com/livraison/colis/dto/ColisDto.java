package com.livraison.colis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ColisDto {
    private Long id;
    private String ville;
    private String nom;
    private String prenom;
    private String adresse;
    private String code;
    private String typeContenu;
    private String status;
    private Long livraisonId;
    private String nouveauStatut;
    private LocalDate dateLivraisonPrevue;
    private Long  commercantId;



}