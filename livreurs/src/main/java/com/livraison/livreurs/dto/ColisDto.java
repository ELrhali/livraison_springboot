package com.livraison.livreurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String livraisonId;
    private String nouveauStatut;



}