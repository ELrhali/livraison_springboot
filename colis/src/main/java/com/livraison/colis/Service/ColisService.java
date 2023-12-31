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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ColisService {
    @Autowired
    private ColisRepository colisRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String LIVRAISON_SERVICE_URL = "http://localhost:8089/api/livraisons/";
    public List<Colis> getColisWithoutLivraison() {
        return colisRepository.findByLivraisonIdIsNull();
    }
    public ColisDto addColis(ColisDto colisDto) {
        // If LivraisonId is not provided or is invalid, find the nearest Livraison
        if (colisDto.getLivraisonId() == null || colisDto.getLivraisonId() == 1) {
            Long livraisonId = findNearestLivraisonId(colisDto.getDateLivraisonPrevue(), colisDto.getVille());
            colisDto.setLivraisonId(livraisonId);
        }

        // Save Colis to the database
        Colis colis = mapToColisEntity(colisDto);
        colisRepository.save(colis);

        // Mapper l'entité Colis en DTO
        return mapToColisDto(colis);
    }

    private Long findNearestLivraisonId(LocalDate dateLivraison, String destination) {
        ResponseEntity<Long> livraisonIdResponse = restTemplate.getForEntity(
                LIVRAISON_SERVICE_URL + "livraisonProche?dateLivraison={dateLivraison}&destination={destination}",
                Long.class, dateLivraison, destination);

        if (livraisonIdResponse.getStatusCode() == HttpStatus.OK) {
            return livraisonIdResponse.getBody();
        } else {
            throw new RuntimeException("Failed to find the nearest Livraison.");
        }
    }




    public List<ColisDto> getColisByLivraisonId(Long livraisonId) {

        List<Colis> colisList = colisRepository.findAllByLivraisonId(livraisonId);
        return colisList.stream()
                .map(this::mapToColisDto)
                .collect(Collectors.toList());
    }

    public List<ColisDto> getColisByCommercantId(Long commercantId) {
        List<Colis> colisList = colisRepository.findAllByCommercantId(commercantId);
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
        colis.setStatus("encour"); // Assurez-vous que le statut est "en cours"
        colis.setLivraisonId(colisDto.getLivraisonId());
        colis.setDateLivraisonPrevue(colisDto.getDateLivraisonPrevue());
        colis.setCommercantId(colisDto.getCommercantId());// Fix here
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
        colisDto.setDateLivraisonPrevue(colis.getDateLivraisonPrevue());
        colisDto.setCommercantId(colis.getCommercantId());
        return colisDto;
    }



    public Colis createColis(Colis colis) {
        return colisRepository.save(colis);
    }

    // Obtenir un colis par son ID
    public Colis getColisById(Long id) {
        return colisRepository.findById(id).orElse(null);
    }
    public void deleteColisByByCommercantId(Long commercantId) {
        // Find all Colis items with the specified LivraisonId
        List<Colis> colisList = colisRepository.findAllByCommercantId(commercantId);

        // Delete each Colis item
        for (Colis colis : colisList) {
            colisRepository.deleteById(colis.getId());
        }
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
    public Long getTotalColis() {
        return colisRepository.count();
    }
    public Long getCountByStatus(String status) {
        return colisRepository.countByStatus(status);
    }
    public int getNombreColisByCommercantId(Long commercantId) {
        return colisRepository.countByCommercantId(commercantId);
    }
    public Long getCountByStatusAndCommercantId(String status, Long commercantId) {
        return colisRepository.countByCommercantIdAndStatus(commercantId, status);
    }


    public List<Object[]> countColisCreatedSameDate() {
        return colisRepository.countColisCreatedSameDate();
    }
    public List<Map<String, Object>> countTotalColisByDate() {
        List<Object[]> data = colisRepository.countTotalColisByDate();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] entry : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", entry[0]);
            map.put("totalColis", entry[1]);
            result.add(map);
        }

        return result;
    }
}


