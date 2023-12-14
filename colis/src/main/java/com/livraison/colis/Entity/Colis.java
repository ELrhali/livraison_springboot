package com.livraison.colis.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ville;
    private Double poids;
    private String nom ;
    private String prenom;
    private String adresse;
    private String code; // Numéro de suivi du colis
    private String typeContenu; // Type de contenu du colis (électroniques, vêtements, etc.)
    private String status;
    private String nouveauStatut;

    @Column(name = "livraison_id")

    private Long livraisonId;
}
