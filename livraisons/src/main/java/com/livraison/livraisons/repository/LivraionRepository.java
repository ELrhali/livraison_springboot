package com.livraison.livraisons.repository;

import com.livraison.livraisons.entity.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivraionRepository extends JpaRepository<Livraison,Long> {
}
