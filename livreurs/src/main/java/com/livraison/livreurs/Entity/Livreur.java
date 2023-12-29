package com.livraison.livreurs.Entity;

import jakarta.persistence.*;
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
    @Column(unique = true)

    private String email;
    private String image;


    //@OneToMany(mappedBy = "id")
    //private List<Colis> colisLivres;
}
