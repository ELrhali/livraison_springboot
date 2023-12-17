package com.livraison.colis.Service;
import com.livraison.colis.Entity.Colis;
import com.livraison.colis.Repository.ColisRepository;
import com.livraison.colis.dto.ColisDto;
import com.livraison.colis.dto.LivraisonDto;
import com.livraison.colis.exception.ColisNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColisService {
    @Autowired
    private ColisRepository colisRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String LIVRAISON_SERVICE_URL = "http://localhost:8089/api/livraisons/";
    public void deleteColisByLivraisonId(Long livraisonId) {
        // Find all Colis items with the specified LivraisonId
        List<Colis> colisList = colisRepository.findAllByLivraisonId(livraisonId);

        // Delete each Colis item
        for (Colis colis : colisList) {
            colisRepository.deleteById(colis.getId());
        }
    }
    public ColisDto addColis(ColisDto colisDto) {
        // Vérifier si livraisonId est null ou inférieur ou égal à zéro
        if (colisDto.getLivraisonId() == null || colisDto.getLivraisonId() <= 0) {
            throw new IllegalArgumentException("LivraisonId doit être spécifié et doit être supérieur à zéro.");
        }

        // Vérifier si Livraison avec livraisonId existe
        ResponseEntity<LivraisonDto> livraisonResponse = restTemplate.getForEntity(
                LIVRAISON_SERVICE_URL + colisDto.getLivraisonId(),
                LivraisonDto.class);

        if (livraisonResponse.getStatusCode() != HttpStatus.OK) {
            throw new ColisNotFoundException("Livraison avec l'ID " + colisDto.getLivraisonId() + " introuvable.");
        }

        // LivraisonId existe, procéder à l'enregistrement de Colis
        Colis colis = mapToColisEntity(colisDto);
        colisRepository.save(colis);

        // Mapper l'entité Colis en DTO
        return mapToColisDto(colis);
    }



    public List<ColisDto> getColisByLivraisonId(Long livraisonId) {

        List<Colis> colisList = colisRepository.findAllByLivraisonId(livraisonId);
        return colisList.stream()
                .map(this::mapToColisDto)
                .collect(Collectors.toList());
    }
    public Colis mapToColisEntity(ColisDto colisDto) {
        Colis colis = new Colis();
        colis.setId(colisDto.getId());
        colis.setVille(colisDto.getVille());
        colis.setNom(colisDto.getNom());
        colis.setPrenom(colisDto.getPrenom());
        colis.setAdresse(colisDto.getAdresse());
        colis.setCode(colisDto.getCode());
        colis.setTypeContenu(colisDto.getTypeContenu());
        colis.setStatus(colisDto.getStatus());
        colis.setLivraisonId(colisDto.getLivraisonId());
        return colis;
    }

    private ColisDto mapToColisDto(Colis colis) {
        ColisDto colisDto = new ColisDto();
        colisDto.setId(colis.getId());
        colisDto.setVille(colis.getVille());
        colisDto.setNom(colis.getNom());
        colisDto.setPrenom(colis.getPrenom());
        colisDto.setAdresse(colis.getAdresse());
        colisDto.setCode(colis.getCode());
        colisDto.setTypeContenu(colis.getTypeContenu());
        colisDto.setStatus(colis.getStatus());
        colisDto.setLivraisonId(colis.getLivraisonId());
        return colisDto;
    }


    // Méthode pour créer un colis avec association à une livraison


    /* public ResponseEntity<?> fetchStudentById(Long id){
        Optional<Colis> colis =  ColisRepository.findById(id);
        if(colis.isPresent()){
            Livraison livraison = restTemplate.getForObject("http://localhost:8082/livraison/" + colis.get().getSchoolId(), School.class);
            ColisResponse colisResponse = new ColisResponse(
                    colis.get().getId(),
                    colis.get().getNom(),
                    colis.get().getPrenom(),
                    livraison
            );
            return new ResponseEntity<>(colisResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No Student Found",HttpStatus.NOT_FOUND);
        }
    }*/
    public Colis createColis(Colis colis) {
        return colisRepository.save(colis);
    }

    // Obtenir un colis par son ID
    public Colis getColisById(Long id) {
        return colisRepository.findById(id).orElse(null);
    }

    /*public List<Colis> getAllbyLiv(Long id){
        return  colisRepository.findAllByLivraisonId(id);
    }*/

    // Mettre à jour un colis existant
   /* public Colis updateColis(Long id, Colis nouveauColis) {
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
    }*/
    public Colis updateColis(Long colisId, Colis updatedColis) {
        // Vérifier si le colis existe
        Colis existingColis = colisRepository.findById(colisId).orElse(null);

        if (existingColis != null) {
            // Mettre à jour les détails du colis
            existingColis.setVille(updatedColis.getVille());
            existingColis.setPoids(updatedColis.getPoids());
            existingColis.setNom(updatedColis.getNom());
            existingColis.setPrenom(updatedColis.getPrenom());
            existingColis.setAdresse(updatedColis.getAdresse());
            existingColis.setCode(updatedColis.getCode());
            existingColis.setTypeContenu(updatedColis.getTypeContenu());
            existingColis.setStatus(updatedColis.getStatus());

            // Enregistrer le colis mis à jour
            return colisRepository.save(existingColis);
        } else {
            // Gérer le cas où le colis n'existe pas
            throw new ColisNotFoundException("Colis with ID " + colisId + " not found");
        }
    }
    public Colis updateColisStatus(Long colisId, Colis updatedColis) {
        Colis existingColis = colisRepository.findById(colisId).orElse(null);

        if (existingColis != null) {
            existingColis.setStatus(updatedColis.getNouveauStatut());
            return colisRepository.save(existingColis);
        } else {
            throw new ColisNotFoundException("Colis with ID " + colisId + " not found");
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

    //statistic


}


