package com.livraison.livreurs.Repository;

import com.livraison.livreurs.Entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LivreurRepository extends JpaRepository<Livreur, Long> {
}
