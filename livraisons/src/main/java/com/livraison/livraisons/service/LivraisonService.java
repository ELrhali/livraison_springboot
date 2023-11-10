package com.livraison.livraisons.service;

import com.livraison.livraisons.entity.Livraison;
import com.livraison.livraisons.repository.LivraionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivraisonService {
    @Autowired
    private LivraionRepository livraionRepository;

    // Créer une nouvelle livraison
    public Livraison createLivraison(Livraison livraison) {
        return livraionRepository.save(livraison);
    }

    // Obtenir toutes les livraisons
    public List<Livraison> getAllLivraisons() {
        return livraionRepository.findAll();
    }

    // Obtenir une livraison par son ID
    public Livraison getLivraisonById(Long id) {
        return livraionRepository.findById(id).orElse(null);
    }





    // Supprimer une livraison par son ID
    public void deleteLivraison(Long id) {
        livraionRepository.deleteById(id);
    }

}
