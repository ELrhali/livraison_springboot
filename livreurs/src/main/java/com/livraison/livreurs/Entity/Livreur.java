package com.livraison.livreurs.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String vehicule;
    private boolean disponible;
    private String statusColis;
    private String code;
    private String addrese;
    private String phone;

    //@OneToMany(mappedBy = "id")
    //private List<Colis> colisLivres;
}
