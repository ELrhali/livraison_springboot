package com.livraison.colis.Controller;

import com.livraison.colis.Entity.Colis;
import com.livraison.colis.Service.ColisService;
import com.livraison.colis.dto.ColisDto;
import com.livraison.colis.exception.ColisNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api/colis")
public class ColisController {
    @Autowired
    private ColisService colisService;

    // Lister tous les colis
    @GetMapping
    public ResponseEntity<List<Colis>> getAllColis() {

        List<Colis> colis = colisService.getAllColis();
        return new ResponseEntity<>(colis, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> addColis(@RequestBody ColisDto colisDto) {
        try {
            ColisDto addedColis = colisService.addColis(colisDto);
            return new ResponseEntity<>(addedColis, HttpStatus.CREATED);
        } catch (ColisNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/livraison/{livraisonId}")
    public ResponseEntity<List<ColisDto>> getColisByLivraisonId(@PathVariable Long livraisonId) {
        List<ColisDto> colisList = colisService.getColisByLivraisonId(livraisonId);
        return ResponseEntity.ok(colisList);
    }
    @DeleteMapping("/livraison/{livraisonId}")
    public ResponseEntity<String> deleteColisByLivraisonId(@PathVariable Long livraisonId) {
        colisService.deleteColisByLivraisonId(livraisonId);
        return new ResponseEntity<>("Colis items with LivraisonId " + livraisonId + " deleted successfully", HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteColis(@PathVariable Long id) {

        colisService.deleteColis(id);
    }
    /*
      HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        responseHeaders.set("Access-Control-Allow-Credentials", "true");



    @GetMapping("/list-cols")
    public ResponseEntity<List<Colis>> getAllbyliv(@PathVariable Long id) {
        List<Colis> colis = colisService.getAllbyLiv(id);
        return new ResponseEntity<>(colis, HttpStatus.OK);
    }*/
    @GetMapping("/{id}")
    public ResponseEntity<Colis> getColisById(@PathVariable Long id) {
        Colis colis = colisService.getColisById(id);
        if (colis != null) {
            return new ResponseEntity<>(colis, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



   /* @PutMapping("/{id}")
    public Colis updateColis(@PathVariable Long id, @RequestBody Colis colis) {
        return colisService.updateColis(id, colis);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Colis> updateColis(@PathVariable("id") Long colisId, @RequestBody Colis updatedColis) {
        try {
            Colis updatedColisEntity = colisService.updateColis(colisId, updatedColis);
            return ResponseEntity.ok(updatedColisEntity);
        } catch (ColisNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou créez un message d'erreur personnalisé si nécessaire
        }
    }
    @PostMapping("/{id}/valider")
    public ResponseEntity<String> validerColis(@PathVariable Long id) {
        colisService.validerColis(id);
        return new ResponseEntity<>("Colis validé", HttpStatus.OK);
    }
    // Refuser un colis
    @PostMapping("/{id}/refuser")
    public ResponseEntity<String> refuserColis(@PathVariable Long id) {
        colisService.refuserColis(id);
        return new ResponseEntity<>("Colis refusé", HttpStatus.OK);
    }
    // Affecter la livraison d'un colis
    /*@PostMapping("/{id}/affecter-livraison")
    public ResponseEntity<String> affecterLivraison(@PathVariable Long id, @RequestBody Livraison livraison) {
        colisService.affecterLivraison(id, livraison);
        return new ResponseEntity<>("Livraison affectée au colis", HttpStatus.OK);
    }*/
}
