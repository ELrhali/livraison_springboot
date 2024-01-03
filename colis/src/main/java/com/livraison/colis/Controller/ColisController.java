package com.livraison.colis.Controller;

import com.livraison.colis.Entity.Colis;
import com.livraison.colis.Service.ColisService;
import com.livraison.colis.dto.ColisDto;
import com.livraison.colis.exception.ColisNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

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
    @PutMapping("/livraison/update-status/{colisId}")
    public ResponseEntity<Colis> updateColisStatus(
            @PathVariable Long colisId,
            @RequestBody Colis updatedColis
    ) {
        Colis result = colisService.updateColisStatus(colisId, updatedColis);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Définir le Content-Type de la réponse

        return ResponseEntity.ok()
                .headers(headers)
                .body(result);
    }

    @GetMapping("/livraison/{livraisonId}")
    public ResponseEntity<List<ColisDto>> getColisByLivraisonId(@PathVariable Long livraisonId) {
        List<ColisDto> colisList = colisService.getColisByLivraisonId(livraisonId);
        return ResponseEntity.ok(colisList);
    }
    @GetMapping("/commercant/{commercantId}")
    public ResponseEntity<List<ColisDto>> getColisByCommercantId(@PathVariable Long commercantId) {
        List<ColisDto> colisList = colisService.getColisByCommercantId(commercantId);
        return ResponseEntity.ok(colisList);
    }
    @DeleteMapping("/commercant/{commercant}")
    public void deleteColisByLivraisonId(@PathVariable Long commercant) {
        colisService.deleteColisByByCommercantId(commercant);
    }
    @DeleteMapping("/{id}")
    public void deleteColis(@PathVariable Long id) {

        colisService.deleteColis(id);
    }
    @GetMapping("/sanslivraison")
    public List<Colis> getColisWithoutLivraison() {
        return colisService.getColisWithoutLivraison();
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
    //statistic
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalColis() {
        Long totalColis = colisService.getTotalColis();
        return ResponseEntity.ok(totalColis);
    }
    @GetMapping("/total/enretard")
    public ResponseEntity<Long> getCountEnRetard() {
        Long countEnRetard = colisService.getCountByStatus("enretard");
        return ResponseEntity.ok(countEnRetard);
    }
    @GetMapping("/total/encour")
    public ResponseEntity<Long> getCountEncour() {
        Long countEncour = colisService.getCountByStatus("encour");
        return ResponseEntity.ok(countEncour);
    }
    @GetMapping("/total/livre")
    public ResponseEntity<Long> getCountLivre() {
        Long countEnLivre = colisService.getCountByStatus("livre");
        return ResponseEntity.ok(countEnLivre);
    }
    @GetMapping("/commercant/colis-total/{commercantId}")
    public ResponseEntity<Integer> getNombreColisByCommercantId(@PathVariable Long commercantId) {
        int nombreColis = colisService.getNombreColisByCommercantId(commercantId);
        return ResponseEntity.ok(nombreColis);
    }
    @GetMapping("/commercant/total-enretard/{commercantId}")
    public ResponseEntity<Long> getCountEnRetardByCommercantId(@PathVariable Long commercantId) {
        Long countEnRetard = colisService.getCountByStatusAndCommercantId("enretard", commercantId);
        return ResponseEntity.ok(countEnRetard);
    }

    @GetMapping("/commercant/total-encour/{commercantId}")
    public ResponseEntity<Long> getCountEncourByCommercantId(@PathVariable Long commercantId) {
        Long countEncour = colisService.getCountByStatusAndCommercantId("encour", commercantId);
        return ResponseEntity.ok(countEncour);
    }

    @GetMapping("/commercant/total-livre/{commercantId}")
    public ResponseEntity<Long> getCountLivreByCommercantId(@PathVariable Long commercantId) {
        Long countLivre = colisService.getCountByStatusAndCommercantId("livre", commercantId);
        return ResponseEntity.ok(countLivre);
    }
    @GetMapping("/stats/totalByDate")
    public List<Map<String, Object>> getTotalColisByDate() {
        return colisService.countTotalColisByDate();
    }
}
