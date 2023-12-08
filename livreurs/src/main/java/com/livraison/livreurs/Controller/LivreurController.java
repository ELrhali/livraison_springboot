package com.livraison.livreurs.Controller;

import com.livraison.livreurs.Entity.Livreur;
import com.livraison.livreurs.Service.LivreurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
public class LivreurController {

    @Autowired
    private LivreurService livreurService;

    @PostMapping
    public ResponseEntity<Livreur> creerLivreur(@RequestBody Livreur livreur) {
        try {
            Livreur nouveauLivreur = livreurService.creerLivreur(livreur);
            return new ResponseEntity<>(nouveauLivreur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        try {
            List<Livreur> livreurs = livreurService.getAllLivreurs();
            return new ResponseEntity<>(livreurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable Long id) {
        try {
            Livreur livreur = livreurService.getLivreurById(id);
            if (livreur != null) {
                return new ResponseEntity<>(livreur, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLivreur(@PathVariable Long id) {
        try {
            livreurService.deleteLivreur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
