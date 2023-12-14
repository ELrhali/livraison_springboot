package com.livraison.livraisons.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Utiliser GenerationType.IDENTITY pour auto-incrémentation
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date_livraison;
    private String destination;
    private Long livreurId;


    /*@OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colis> colis = new ArrayList<>();*/
}
