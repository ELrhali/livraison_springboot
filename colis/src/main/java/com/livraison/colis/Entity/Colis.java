package com.livraison.colis.Entity;
//import com.livraison.livraisons.entity.Livraison;
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
    private String nom ;
    private String prenom;
    private String adresse;
    private String code; // Numéro de suivi du colis
    private String typeContenu; // Type de contenu du colis (électroniques, vêtements, etc.)
    private String status;
    @Column(name = "livraison_id") // Nom de la colonne de la clé étrangère
    private Long livraisonId;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "livraison_id")
    //private Livraison livraison;

}
