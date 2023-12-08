package com.livraison.livraisons.controller;

import com.livraison.livraisons.dto.ResponseDto;
import com.livraison.livraisons.entity.Livraison;
import com.livraison.livraisons.exception.LivraisonNotFoundException;
import com.livraison.livraisons.service.LivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {
    @Autowired
    private LivraisonService livraisonService;
    @Autowired
    RestTemplate restTemplate;


    @PostMapping
    public ResponseEntity<Livraison> saveLivraison(@RequestBody Livraison livraison) {
        try {
            Livraison savedLivraison = livraisonService.saveLivraison(livraison);
            return new ResponseEntity<>(savedLivraison, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable("id") Long livraisonId) {
        ResponseDto responseDto = livraisonService.getLivraisonById(livraisonId);

        if (responseDto.getLivraisonDto() != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(null, null)); // Or create a custom error message if needed
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivraison(@PathVariable("id") Long livraisonId) {
        try {
            livraisonService.deleteLivraison(livraisonId);
            return new ResponseEntity<>("Livraison with ID " + livraisonId + " deleted successfully", HttpStatus.OK);
        } catch (LivraisonNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllLivraisonsWithColis() {
        List<ResponseDto> livraisonsWithColis = livraisonService.getAllLivraisonsWithColis();

        if (!livraisonsWithColis.isEmpty()) {
            return ResponseEntity.ok(livraisonsWithColis);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // Ou créez un message d'erreur personnalisé si nécessaire
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Livraison> updateLivraison(@PathVariable("id") Long livraisonId, @RequestBody Livraison updatedLivraison) {
        try {
            Livraison updatedLivraisonEntity = livraisonService.updateLivraison(livraisonId, updatedLivraison);
            return ResponseEntity.ok(updatedLivraisonEntity);
        } catch (LivraisonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou créez un message d'erreur personnalisé si nécessaire
        }
    }
    // Créer une nouvelle livraison
    /*@PostMapping
    public ResponseEntity<Livraison> createLivraison(@RequestBody Livraison livraison) {
        Livraison nouvelleLivraison = livraisonService.createLivraison(livraison);
        return new ResponseEntity<>(nouvelleLivraison, HttpStatus.CREATED);
    }
*/
    // Obtenir toutes les livraisons
   /*@GetMapping
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        List<Livraison> livraisons = livraisonService.getAllLivraisons();
        return new ResponseEntity<>(livraisons, HttpStatus.OK);
    }*/

    // Obtenir une livraison par son ID
   /* @GetMapping("/{id}")
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
    }*/
}
