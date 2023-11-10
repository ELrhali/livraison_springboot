package com.livraison.colis.Repository;

import com.livraison.colis.Entity.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {
}
