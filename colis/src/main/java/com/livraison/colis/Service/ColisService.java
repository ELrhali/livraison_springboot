package com.livraison.colis.Service;

import com.livraison.colis.Entity.Colis;
import com.livraison.colis.Repository.ColisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColisService {
    @Autowired
    private ColisRepository colisRepository;

    // Créer un nouveau colis
    public Colis createColis(Colis colis) {
        return colisRepository.save(colis);
    }

    // Obtenir un colis par son ID
    public Colis getColisById(Long id) {
        return colisRepository.findById(id).orElse(null);
    }

    // Mettre à jour un colis existant
    public Colis updateColis(Long id, Colis nouveauColis) {
        Colis colis = getColisById(id);
        if (colis != null) {
            colis.setVille(nouveauColis.getVille());
            colis.setNom(nouveauColis.getNom());
            colis.setPrenom(nouveauColis.getPrenom());
            colis.setAdresse(nouveauColis.getAdresse());
            colis.setCode(nouveauColis.getCode());
            colis.setTypeContenu(nouveauColis.getTypeContenu());
            colis.setStatus(nouveauColis.getStatus());
            // Mettre à jour la livraison et le vendeur si nécessaire
            //colis.setLivraison(nouveauColis.getLivraison());
            //colis.setVendeur(nouveauColis.getVendeur());
            return colisRepository.save(colis);
        } else {
            return null;
        }
    }

    // Supprimer un colis par son ID
    public void deleteColis(Long id) {
        colisRepository.deleteById(id);
    }

    // Valider un colis
    public void validerColis(Long id) {
        Colis colis = getColisById(id);
        if (colis != null) {
            colis.setStatus("Validé");
            colisRepository.save(colis);
        }
    }

    // Refuser un colis
    public void refuserColis(Long id) {
        Colis colis = getColisById(id);
        if (colis != null) {
            colis.setStatus("Refusé");
            colisRepository.save(colis);
        }
    }

    // Affecter la livraison d'un colis
   /* public void affecterLivraison(Long colisId, Livraison livraison) {
        Colis colis = getColisById(colisId);
        if (colis != null) {
            colis.setLivraison(livraison);
            colisRepository.save(colis);
        }
    }*/

    // List all colis
    public List<Colis> getAllColis() {
        return colisRepository.findAll();
    }
}


