package com.livraison.livreurs.Service;

import com.livraison.livreurs.Entity.Livreur;
import com.livraison.livreurs.Repository.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreurService {
    @Autowired
    private LivreurRepository livreurRepository;

    public Livreur creerLivreur(Livreur livreur) {

        return livreurRepository.save(livreur);
    }
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }
    public Livreur getLivreurById(Long id) {
        return livreurRepository.findById(id).orElse(null);
    }

}
