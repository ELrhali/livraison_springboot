package com.livraison.livraisons.controller;

import com.livraison.livraisons.dto.LivraisonDto;
import com.livraison.livraisons.dto.ResponseDto;
import com.livraison.livraisons.entity.Livraison;
import com.livraison.livraisons.exception.LivraisonNotFoundException;
import com.livraison.livraisons.service.LivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {
    @Autowired
    private LivraisonService livraisonService;
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<LivraisonDto>> getLivraisonByLivreurId(@PathVariable Long livreurId) {
        List<LivraisonDto> livraisonList = livraisonService.getLivraisonByLivreurId(livreurId);
        return ResponseEntity.ok(livraisonList);
    }
    @GetMapping("/livreurs/{livreurId}")
    public ResponseEntity<List<ResponseDto>> getLivraisonByLivreurIds(@PathVariable Long livreurId) {
        List<ResponseDto> responseList = livraisonService.getLivraisonByLivreurId(livreurId)
                .stream()
                .map(livraisonDto -> livraisonService.getLivraisonWithColis(livraisonDto.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @GetMapping("/all-livraison")
    public ResponseEntity<List<Livraison>>getAllLivraison() {

        List<Livraison> livraison = livraisonService.getAllLivraison();
        return new ResponseEntity<>(livraison, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Livraison> saveLivraison(@RequestBody Livraison livraison) {
        try {
            Livraison savedLivraison = livraisonService.saveLivraison(livraison);
            return new ResponseEntity<>(savedLivraison, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
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
    @GetMapping("/within-a-month")
    public List<Livraison> getLivraisonsWithinAMonth() {
        return livraisonService.getLivraisonsWithinAMonth();
    }
    @GetMapping("/livraisonProche")
    public ResponseEntity<Long> trouverLivraisonProche(
            @RequestParam("dateLivraison") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateLivraison,@RequestParam("destination") String destination) {

        Long livraisonProche = livraisonService.trouverLivraisonProche(dateLivraison,destination);

        if (livraisonProche != null) {
            return ResponseEntity.ok(livraisonProche);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
