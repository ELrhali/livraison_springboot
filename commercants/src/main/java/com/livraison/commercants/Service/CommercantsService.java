package com.livraison.commercants.Service;
import com.livraison.commercants.Entity.Commercants;
import com.livraison.commercants.Repository.CommercantsRepository;
import com.livraison.commercants.dto.ColisDto;
import com.livraison.commercants.dto.CommercantsDto;
import com.livraison.commercants.dto.ResponseDto;
import com.livraison.commercants.exception.CommercantsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommercantsService  {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommercantsRepository commercantsRepository;
    public Commercants creerCommercants(Commercants commercants) {
        return commercantsRepository.save(commercants);
    }
    public ResponseDto getColisByCommercantId(Long commercantId) {
        ResponseDto responseDto = new ResponseDto();
        Commercants commercant = commercantsRepository.findById(commercantId).orElse(null);

        if (commercant != null) {
            CommercantsDto commercantDto = mapToCommercants(commercant);

            // Fetch the list of Colis items for the given CommercantsId
            ResponseEntity<ColisDto[]> responseEntity = restTemplate
                    .getForEntity("http://localhost:8090/api/colis/commercant/" + commercant.getId(),
                            ColisDto[].class);
            ColisDto[] colisArray = responseEntity.getBody();
            List<ColisDto> colisList = Arrays.asList(colisArray);

            responseDto.setCommercantsDto(commercantDto);
            responseDto.setColisList(colisList);
        }

        return responseDto;
    }
    private CommercantsDto mapToCommercants(Commercants livraison){
        CommercantsDto livraisonDto = new CommercantsDto();
        livraisonDto.setId(livraison.getId());
        livraisonDto.setNom(livraison.getNom());
        livraisonDto.setEmail(livraison.getEmail());
        livraisonDto.setPhone(livraison.getPhone());
        return livraisonDto;
    }
    public void deleteCommercants(Long commercantId) {
        // Check if the LivraisonId exists
        Commercants commercant = commercantsRepository.findById(commercantId).orElse(null);
        if (commercant != null) {
            // Delete all Colis items related to the CommercantsId
            deleteColisByCommercantId(commercantId);

            // Now, delete the Commercants
            commercantsRepository.delete(commercant);
        } else {
            // Handle the case where CommercantsId does not exist
            throw new CommercantsNotFoundException("Commercants with ID " + commercantId + " not found");
        }
    }

    private void deleteColisByCommercantId(Long commercantId) {
        // Make an HTTP call to the Colis service to delete all related Colis items
        restTemplate.delete("http://localhost:8090/api/colis/commercant/" + commercantId);
    }
}
