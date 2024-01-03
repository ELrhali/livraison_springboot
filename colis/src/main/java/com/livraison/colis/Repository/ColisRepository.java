package com.livraison.colis.Repository;
import com.livraison.colis.Entity.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {
   List<Colis> findAllByLivraisonId(Long id);
   List<Colis> findAllByCommercantId(Long id);
   int countByCommercantId(Long commercantId);
   Long countByStatus(String status);
   Long countByCommercantIdAndStatus(Long commercantId, String status);
   List<Colis> findByLivraisonIdIsNull();
   @Query("SELECT c.dateCreation, COUNT(c) FROM Colis c GROUP BY c.dateCreation HAVING COUNT(c) > 1")
   List<Object[]> countColisCreatedSameDate();
   @Query("SELECT c.dateCreation, COUNT(c) FROM Colis c GROUP BY c.dateCreation")
   List<Object[]> countTotalColisByDate();
}
