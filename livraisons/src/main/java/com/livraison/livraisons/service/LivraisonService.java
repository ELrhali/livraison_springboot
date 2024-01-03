package com.livraison.livraisons.service;

import com.livraison.livraisons.dto.ColisDto;
import com.livraison.livraisons.dto.LivraisonDto;
import com.livraison.livraisons.dto.ResponseDto;
import com.livraison.livraisons.entity.Livraison;
import com.livraison.livraisons.exception.LivraisonNotFoundException;
import com.livraison.livraisons.repository.LivraionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class LivraisonService {
    @Autowired
    private LivraionRepository livraisonRepository;
    @Autowired
    private RestTemplate restTemplate;

    public List<Livraison> getAllLivraison() {
        return livraisonRepository.findAll();
    }
    public Livraison saveLivraison(Livraison livraison) {
        // Vérifier que la destination est spécifiée
        if (livraison.getDestination() == null || livraison.getDestination().isEmpty()) {
            throw new IllegalArgumentException("La destination de la livraison doit être spécifiée.");
        }
        // Vérifier que la date de livraison est dans le futur

        if (livraison.getDate_livraison() != null && livraison.getDate_livraison().before(new Date())) {
            throw new IllegalArgumentException("La date de livraison doit être dans le futur.");
        }
        // Autres vérifications ou logiques si nécessaire

        // Enregistrer la livraison
        return livraisonRepository.save(livraison);
    }
    public Long trouverLivraisonProche(Date dateLivraison, String destination) {
        // Récupérer toutes les livraisons de la base de données pour la destination donnée
        Iterable<Livraison> livraisons = livraisonRepository.findByDestination(destination);

        // Initialiser la variable pour stocker la livraison la plus proche
        Livraison livraisonProche = null;

        // Initialiser la variable pour stocker la différence minimale entre les dates
        long differenceMinimale = Long.MAX_VALUE;

        // Parcourir toutes les livraisons pour trouver la plus proche dans le futur
        for (Livraison livraison : livraisons) {
            Date dateCourante = livraison.getDate_livraison();

            // Ne considérer que les livraisons futures ou le même jour
            if (dateCourante.after(dateLivraison) || dateCourante.equals(dateLivraison)) {
                long difference = Math.abs(dateCourante.getTime() - dateLivraison.getTime());

                // Mettre à jour si la différence est plus petite que la différence minimale actuelle
                if (difference < differenceMinimale) {
                    differenceMinimale = difference;
                    livraisonProche = livraison;
                }
            }
        }
        if (livraisonProche == null) {
            Livraison nouvelleLivraison = new Livraison();
            nouvelleLivraison.setDestination(destination);
            nouvelleLivraison.setDate_livraison(dateLivraison);

            // Vous pouvez ajouter d'autres propriétés ou logique ici

            // Enregistrez la nouvelle livraison
            livraisonProche = livraisonRepository.save(nouvelleLivraison);
        }

        return livraisonProche.getId();
    }
    public ResponseDto getLivraisonById(Long livraisonId) {
        ResponseDto responseDto = new ResponseDto();
        Livraison livraison = livraisonRepository.findById(livraisonId).orElse(null);
        if (livraison != null) {
            LivraisonDto livraisonDto = mapToLivraison(livraison);
            // Fetch the list of Colis items for the given LivraisonId
            ResponseEntity<ColisDto[]> responseEntity = restTemplate
                    .getForEntity("http://localhost:8090/api/colis/livraison/" + livraison.getId(),
                            ColisDto[].class);
            ColisDto[] colisArray = responseEntity.getBody();
            List<ColisDto> colisList = Arrays.asList(colisArray);
            responseDto.setLivraisonDto(livraisonDto);
            responseDto.setColisList(colisList);
        }
        return responseDto;
    }
    private LivraisonDto mapToLivraison(Livraison livraison){
        LivraisonDto livraisonDto = new LivraisonDto();
        livraisonDto.setId(livraison.getId());
        livraisonDto.setDestination(livraison.getDestination());
        livraisonDto.setDate_livraison(livraison.getDate_livraison());
        livraisonDto.setLivreurId(livraison.getLivreurId());
        return livraisonDto;
    }
    public void deleteLivraison(Long livraisonId) {
        // Check if the LivraisonId exists
        Livraison livraison = livraisonRepository.findById(livraisonId).orElse(null);
        if (livraison != null) {
            // Delete all Colis items related to the LivraisonId
            //deleteColisByLivraisonId(livraisonId);

            // Now, delete the Livraison
            livraisonRepository.delete(livraison);
        } else {
            // Handle the case where LivraisonId does not exist
            throw new LivraisonNotFoundException("Livraison with ID " + livraisonId + " not found");
        }
    }
    private void deleteColisByLivraisonId(Long livraisonId) {
        // Make an HTTP call to the Colis service to delete all related Colis items
        restTemplate.delete("http://localhost:8090/api/colis/livraison/" + livraisonId);
    }
    public List<ResponseDto> getAllLivraisonsWithColis() {
        List<Livraison> livraisons = livraisonRepository.findAll();
        return livraisons.stream()
                .map(this::mapToResponseDtoWithColis)
                .collect(Collectors.toList());
    }

    private ResponseDto mapToResponseDtoWithColis(Livraison livraison) {
        ResponseDto responseDto = new ResponseDto();
        LivraisonDto livraisonDto = mapToLivraison(livraison);
        responseDto.setLivraisonDto(livraisonDto);

        // Fetch the list of Colis items for the given LivraisonId
        ResponseEntity<ColisDto[]> responseEntity = restTemplate
                .getForEntity("http://localhost:8090/api/colis/livraison/" + livraison.getId(),
                        ColisDto[].class);

        ColisDto[] colisArray = responseEntity.getBody();
        List<ColisDto> colisList = Arrays.asList(colisArray);

        responseDto.setColisList(colisList);

        return responseDto;
    }

    public Livraison updateLivraison(Long livraisonId, Livraison updatedLivraison) {
        // Vérifier si la livraison existe
        Livraison existingLivraison = livraisonRepository.findById(livraisonId).orElse(null);

        if (existingLivraison != null) {
            // Mettre à jour les détails de la livraison
            existingLivraison.setDate_livraison(updatedLivraison.getDate_livraison());
            existingLivraison.setDestination(updatedLivraison.getDestination());
            existingLivraison.setLivreurId(updatedLivraison.getLivreurId());

            // Enregistrer la livraison mise à jour
            Livraison updatedLivraisonEntity = livraisonRepository.save(existingLivraison);

            // Mettre à jour les colis associés
        //   updateColisLivraisonAssociation(livraisonId, updatedLivraisonEntity);

            return updatedLivraisonEntity;
        } else {
            // Gérer le cas où la livraison n'existe pas
            throw new LivraisonNotFoundException("Livraison with ID " + livraisonId + " not found");
        }
    }


    public ResponseDto getLivraisonWithColis(Long livraisonId) {
        ResponseDto responseDto = new ResponseDto();
        LivraisonDto livraisonDto = mapToLivraisonDto(livraisonRepository.findById(livraisonId).orElse(null));
        responseDto.setLivraisonDto(livraisonDto);

        // Fetch the list of Colis items for the given LivraisonId
        ResponseEntity<ColisDto[]> responseEntity = restTemplate
                .getForEntity("http://localhost:8090/api/colis/livraison/" + livraisonDto.getId(),
                        ColisDto[].class);
        ColisDto[] colisArray = responseEntity.getBody();
        List<ColisDto> colisList = Arrays.asList(colisArray);

        responseDto.setColisList(colisList);
        return responseDto;
    }

   public List<LivraisonDto> getLivraisonByLivreurId(Long livreurId) {
        List<Livraison> livraisonList = livraisonRepository.findAllByLivreurId(livreurId);
        return livraisonList.stream()
                .map(this::mapToLivraisonDto)
                .collect(Collectors.toList());
    }
    private LivraisonDto mapToLivraisonDto(Livraison livraison) {
        if (livraison != null) {
            LivraisonDto livraisonDto = new LivraisonDto();
            livraisonDto.setId(livraison.getId());
            livraisonDto.setDate_livraison(livraison.getDate_livraison());
            livraisonDto.setDestination(livraison.getDestination());
            livraisonDto.setLivreurId(livraison.getLivreurId()); // Set livreurId property

            // Make sure to set other properties as needed

            return livraisonDto;
        }
        return null; // Return null if the input Livraison is null
    }
    public List<Livraison> getLivraisonsWithinAMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusMonths(1);

        Date startDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return livraisonRepository.findLivraisonsWithinAMonth(startDateAsDate, endDateAsDate);
    }
    public List<Livraison> getLivraisonWithoutLivreur() {
        return livraisonRepository.findByLivreurIdIsNull();
    }



}

