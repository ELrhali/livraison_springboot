package com.livraison.commercants.Controller;

import com.livraison.commercants.Entity.Commercants;
import com.livraison.commercants.Service.CommercantsService;
import com.livraison.commercants.dto.ResponseDto;
import com.livraison.commercants.exception.CommercantsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/commercants")
@CrossOrigin(origins = {"http://localhost:4200"})
public class CommercantsController {
    @Autowired
    private CommercantsService commercantsService;

    @PostMapping()
    public ResponseEntity<Commercants> creerCommercants(@RequestBody Commercants commercants) {
        Commercants createdCommercant = commercantsService.creerCommercants(commercants);
        return ResponseEntity.ok(createdCommercant);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable("id") Long commercantId) {
        ResponseDto responseDto = commercantsService.getColisByCommercantId(commercantId);

        if (responseDto.getCommercantsDto() != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(null, null)); // Or create a custom error message if needed
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommercants(@PathVariable("id") Long commercantsId) {
        try {
            commercantsService.deleteCommercants(commercantsId);
            return new ResponseEntity<>("Commercants with ID " + commercantsId + " deleted successfully", HttpStatus.OK);
        } catch (CommercantsNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
