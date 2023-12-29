package com.livraison.livreurs.Service;

import com.livraison.livreurs.Entity.Livreur;
import com.livraison.livreurs.Repository.LivreurRepository;
import com.livraison.livreurs.dto.LivraisonDto;
import com.livraison.livreurs.dto.LivreurDto;
import com.livraison.livreurs.dto.ResponseDto;
import com.livraison.livreurs.dto.ResponseDtoLivraison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivreurService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LivreurRepository livreurRepository;

    public Livreur creerLivreur(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    public ResponseDto getLivreurById(Long livreurId) {
        ResponseDto responseDto = new ResponseDto();
        Livreur livreur = livreurRepository.findById(livreurId).orElse(null);
        if (livreur != null) {
            LivreurDto livreurDto = mapToLivreur(livreur);
            // Fetch the list of Colis items for the given LivraisonId
            ResponseEntity<ResponseDtoLivraison[]> responseEntity = restTemplate
                    .getForEntity("http://localhost:8089/api/livraisons/livreurs/" + livreur.getId(),
                            ResponseDtoLivraison[].class);
            ResponseDtoLivraison[] livraisonArray = responseEntity.getBody();
            List<ResponseDtoLivraison> livraisonList = Arrays.asList(livraisonArray);
            responseDto.setLivreurDto(livreurDto);
            responseDto.setLivraisonList(livraisonList);
        }
        return responseDto;
    }
    public void deleteLivreur(Long id) {
        livreurRepository.deleteById(id);
    }

    public Livreur updateLivreur(Long id, Livreur livreur) {
        Livreur existingLivreur = livreurRepository.findById(id).orElse(null);
        if (existingLivreur != null) {
            existingLivreur.setNom(livreur.getNom());
            existingLivreur.setPrenom(livreur.getPrenom());
            existingLivreur.setVehicule(livreur.getVehicule());
            existingLivreur.setDisponible(livreur.isDisponible());
            existingLivreur.setCode(livreur.getCode());
            existingLivreur.setAddrese(livreur.getAddrese());
            existingLivreur.setPhone(livreur.getPhone());
            existingLivreur.setStatusColis(livreur.getStatusColis());
            existingLivreur.setEmail(livreur.getEmail());
            existingLivreur.setImage(livreur.getImage());

            return livreurRepository.save(existingLivreur);
        }
        return null;  // Peut-être vous voulez gérer autrement si le livreur n'existe pas
    }
    public List<ResponseDto> getAllLivreurWithLivraison() {
        List<Livreur> livreurs = livreurRepository.findAll();
        return livreurs.stream()
                .map(this::mapToResponseDtoWithLivraison)
                .collect(Collectors.toList());
    }
    private ResponseDto mapToResponseDtoWithLivraison(Livreur livreur) {
        ResponseDto responseDto = new ResponseDto();
        LivreurDto livreurDto = mapToLivreur(livreur);
        responseDto.setLivreurDto(livreurDto);

        // Fetch the list of Livraison items for the given livreurId
        ResponseEntity<ResponseDtoLivraison[]> responseEntity = restTemplate
                .getForEntity("http://localhost:8089/api/livraisons/livreurs/" + livreur.getId(),
                        ResponseDtoLivraison[].class);

        ResponseDtoLivraison[] livraisonArray = responseEntity.getBody();
        List<ResponseDtoLivraison> livraisonList = Arrays.asList(livraisonArray);

        responseDto.setLivraisonList(livraisonList);

        return responseDto;
    }
    private LivreurDto mapToLivreur(Livreur livreur) {
        if (livreur != null) {
            LivreurDto livreurDto = new LivreurDto();
            livreurDto.setId(livreur.getId());
            livreurDto.setNom(livreur.getNom());
            livreurDto.setPrenom(livreur.getPrenom());
            livreurDto.setVehicule(livreur.getVehicule());
            livreurDto.setDisponible(livreur.isDisponible());
            livreurDto.setCode(livreur.getCode());
            livreurDto.setAddrese(livreur.getAddrese());
            livreurDto.setPhone(livreur.getPhone());
            livreurDto.setStatusColis(livreur.getStatusColis());
            livreurDto.setEmail(livreur.getEmail());
            livreurDto.setImage(livreur.getImage());




            return livreurDto;
        }
        return new LivreurDto(); // Return LivreurDto with default or null values
    }

    public List<LivreurDto> getLivreursDisponibles() {
        List<Livreur> livreurs = livreurRepository.findAllByDisponible(true);
        return livreurs.stream()
                .map(this::mapToLivreurDto)
                .collect(Collectors.toList());
    }

    private LivreurDto mapToLivreurDto(Livreur livreur) {
        LivreurDto livreurDto = new LivreurDto();
        livreurDto.setId(livreur.getId());
        livreurDto.setNom(livreur.getNom());
        livreurDto.setPrenom(livreur.getPrenom());
        livreurDto.setVehicule(livreur.getVehicule());
        livreurDto.setDisponible(livreur.isDisponible());
        livreurDto.setStatusColis(livreur.getStatusColis());
        livreurDto.setCode(livreur.getCode());
        livreurDto.setAddrese(livreur.getAddrese());
        livreurDto.setPhone(livreur.getPhone());
        livreurDto.setEmail(livreur.getEmail());
        livreurDto.setImage(livreur.getImage());
        // Ajoutez d'autres propriétés si nécessaire
        return livreurDto;
    }

    public Long getLivreurIdByEmail(String email) {
        Optional<Livreur> livreurOptional = livreurRepository.findByEmail(email);

        // Utilisez map pour extraire l'ID s'il existe, sinon, retournez null
        return livreurOptional.map(Livreur::getId).orElse(null);
    }
    public  List<Livreur> getAllLivreur(){
        return  livreurRepository.findAll();
    }
}