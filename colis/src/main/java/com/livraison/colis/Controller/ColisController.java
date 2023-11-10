package com.livraison.colis.Controller;

import com.livraison.colis.Entity.Colis;
import com.livraison.colis.Service.ColisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colis")
public class ColisController {
    @Autowired
    private ColisService colisService;
    @PostMapping
    public Colis createColis(@RequestBody Colis colis) {
        return colisService.createColis(colis);
    }
    // Lister tous les colis
    @GetMapping
    public ResponseEntity<List<Colis>> getAllColis() {
        List<Colis> colis = colisService.getAllColis();
        return new ResponseEntity<>(colis, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colis> getColisById(@PathVariable Long id) {
        Colis colis = colisService.getColisById(id);
        if (colis != null) {
            return new ResponseEntity<>(colis, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/{id}")
    public Colis updateColis(@PathVariable Long id, @RequestBody Colis colis) {
        return colisService.updateColis(id, colis);
    }

    @DeleteMapping("/{id}")
    public void deleteColis(@PathVariable Long id) {
        colisService.deleteColis(id);
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
