package com.livraison.commercants.Repository;
import com.livraison.commercants.Entity.Commercants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository

public interface CommercantsRepository extends JpaRepository<Commercants, Long> {


}