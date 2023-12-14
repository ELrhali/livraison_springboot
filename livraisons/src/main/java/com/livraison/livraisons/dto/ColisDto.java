package com.livraison.livraisons.dto;
import lombok.*;

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
