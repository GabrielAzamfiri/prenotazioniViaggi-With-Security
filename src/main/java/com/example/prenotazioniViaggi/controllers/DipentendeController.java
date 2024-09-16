package com.example.prenotazioniViaggi.controllers;

import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.payloads.DipendenteDTO;
import com.example.prenotazioniViaggi.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipentendeController {
    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping() // Qua sto definendo il metodo HTTP da utilizzare per questo endpoint e l'ultima parte dell'URL
    // Per contattare questo endpoint dovrò mandare una richiesta GET a http://localhost:3001/authors
    public Page<Dipendente> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{dipendenteId}")
    private Dipendente getById(@PathVariable UUID dipendenteId) {
        return dipendenteService.findById(dipendenteId);
    }


    @PutMapping("/{dipendenteId}")
    private Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO updatedDipendenteDTO) {
        return dipendenteService.findByIdAndUpdate(dipendenteId, updatedDipendenteDTO);
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    private void findByIdAndDelete(@PathVariable UUID dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }

    @PostMapping("/{dipendenteId}/avatar")
    public Dipendente uploadAvatar(@RequestParam("avatar") MultipartFile image, @PathVariable UUID dipendenteId) throws IOException {
        // "avatar" deve corrispondere ESATTAMENTE come il campo del FormData che ci invia il Frontend
        // Se non corrisponde non troverò il file
        return this.dipendenteService.uploadImage(image, dipendenteId);
    }
}
