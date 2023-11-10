package com.livraison.livraisons.entity;

//import com.livraison.colis.Entity.Colis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date_livraison ;

    //@OneToMany(mappedBy = "livraison",cascade = CascadeType.ALL,orphanRemoval = true)
    //private List<Colis> colis = new ArrayList<Colis>();

}
