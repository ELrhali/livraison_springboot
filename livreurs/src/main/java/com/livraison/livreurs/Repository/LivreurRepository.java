package com.livraison.livreurs.Repository;

import com.livraison.livreurs.Entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    List<Livreur> findAllByDisponible(boolean disponible);
    Optional<Livreur> findByEmail(String email);

}
