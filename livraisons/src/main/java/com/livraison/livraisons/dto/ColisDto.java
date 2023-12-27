package com.livraison.livraisons.dto;
import lombok.*;

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
    private String livraisonId;
    private String nouveauStatut;
    private LocalDate dateLivraisonPrevue;
    private Long commercantsId;



}
