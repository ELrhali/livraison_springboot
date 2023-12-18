package com.livraison.livreurs.Controller;

import com.livraison.livreurs.Entity.Livreur;
import com.livraison.livreurs.Service.LivreurService;
import com.livraison.livreurs.dto.ResponseDto;
import com.livraison.livreurs.exception.LivreurNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
public class LivreurController {

    @Autowired
    private LivreurService livreurService;
    @GetMapping("/all-livreur")
    public ResponseEntity<List<Livreur>>getAllLivreur() {

        List<Livreur> livreur = livreurService.getAllLivreur();
        return new ResponseEntity<>(livreur, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Livreur> creerLivreur(@RequestBody Livreur livreur) {
        try {
            Livreur nouveauLivreur = livreurService.creerLivreur(livreur);
            return new ResponseEntity<>(nouveauLivreur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        try {
            List<Livreur> livreurs = livreurService.getAllLivreurs();
            return new ResponseEntity<>(livreurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllLivraisonsWithColis() {
        List<ResponseDto> livraisonsWithColis = livreurService.getAllLivreurWithLivraison();

        if (!livraisonsWithColis.isEmpty()) {
            return ResponseEntity.ok(livraisonsWithColis);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // Ou créez un message d'erreur personnalisé si nécessaire
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable("id") Long livraisonId) {
        ResponseDto responseDto = livreurService.getLivreurById(livraisonId);

        if (responseDto.getLivreurDto() != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return null; // Or create a custom error message if needed
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

    @PutMapping("/{id}")
    public ResponseEntity<Livreur> updateLivreur(@PathVariable("id") Long livraisonId, @RequestBody Livreur updatedLivreur) {
        try {
            Livreur updatedLivreurEntity = livreurService.updateLivreur(livraisonId, updatedLivreur);
            return ResponseEntity.ok(updatedLivreurEntity);
        } catch (LivreurNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou créez un message d'erreur personnalisé si nécessaire
        }
    }
}
