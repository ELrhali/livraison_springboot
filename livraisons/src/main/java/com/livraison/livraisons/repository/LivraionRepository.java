package com.livraison.livraisons.repository;

import com.livraison.livraisons.entity.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

@Repository
public interface LivraionRepository extends JpaRepository<Livraison,Long> {
     List<Livraison> findAllByLivreurId(Long id);
     @Query("SELECT l FROM Livraison l WHERE l.date_livraison BETWEEN :startDate AND :endDate")
     List<Livraison> findLivraisonsWithinAMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
     List<Livraison> findByDestination(String destination);


}

