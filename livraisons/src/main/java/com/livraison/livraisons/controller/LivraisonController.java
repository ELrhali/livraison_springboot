package com.livraison.livraisons.controller;

import com.livraison.livraisons.entity.Livraison;
import com.livraison.livraisons.service.LivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livraison")
@RequiredArgsConstructor
public class LivraisonController {
    @Autowired
    private LivraisonService livraisonService;

    // Créer une nouvelle livraison
    @PostMapping
    public ResponseEntity<Livraison> createLivraison(@RequestBody Livraison livraison) {
        Livraison nouvelleLivraison = livraisonService.createLivraison(livraison);
        return new ResponseEntity<>(nouvelleLivraison, HttpStatus.CREATED);
    }

    // Obtenir toutes les livraisons
    @GetMapping
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        List<Livraison> livraisons = livraisonService.getAllLivraisons();
        return new ResponseEntity<>(livraisons, HttpStatus.OK);
    }

    // Obtenir une livraison par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Livraison> getLivraisonById(@PathVariable Long id) {
        Livraison livraison = livraisonService.getLivraisonById(id);
        if (livraison != null) {
            return new ResponseEntity<>(livraison, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    // Supprimer une livraison par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        livraisonService.deleteLivraison(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
