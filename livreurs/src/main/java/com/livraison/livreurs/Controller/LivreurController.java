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
@RequestMapping("/livreurs")
@RequiredArgsConstructor
public class LivreurController {
    @Autowired
    private LivreurService livreurService;


    // Endpoint pour créer un livreur
    @PostMapping
    public ResponseEntity<Livreur> creerLivreur(@RequestBody Livreur livreur) {
        Livreur nouveauLivreur = livreurService.creerLivreur(livreur);
        return new ResponseEntity<>(nouveauLivreur, HttpStatus.CREATED);
    }
    @GetMapping
    public List<Livreur> getAllLivreurs() {
        return livreurService.getAllLivreurs();
    }
    @GetMapping("/{id}")
    public Livreur getLivreurById(@PathVariable Long id) {
        return livreurService.getLivreurById(id);
    }


}
