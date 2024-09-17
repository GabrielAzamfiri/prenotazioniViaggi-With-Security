package com.example.prenotazioniViaggi.controllers;


import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Prenotazione;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.payloads.PrenotazioneDTO;
import com.example.prenotazioniViaggi.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping() // Qua sto definendo il metodo HTTP da utilizzare per questo endpoint e l'ultima parte dell'URL
    // Per contattare questo endpoint dovrò mandare una richiesta GET a http://localhost:3001/authors
    public Page<Prenotazione> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioneService.findAll(page, size, sortBy);
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione getById(@PathVariable UUID prenotazioneId) {
        return prenotazioneService.findById(prenotazioneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Serve per customizzare lo status code (CREATED --> 201)
    public Prenotazione createAuthor(@RequestBody @Validated PrenotazioneDTO prenotazioneDTO, BindingResult validationResult) {
        // @Validated serve per 'attivare' le regole di validazione descritte nel DTO
        // BindingResult mi permette di capire se ci sono stati errori e quali errori ci sono stati
        if (validationResult.hasErrors()) {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente
            return prenotazioneService.save(prenotazioneDTO);
        }
    }

    @PutMapping("/me")
    public Prenotazione findMyPrenotazioneAndUpdate(@AuthenticationPrincipal Dipendente currentAuthenticatedUser, @RequestBody @Validated PrenotazioneDTO updatedPrenotazioneDTO) {
        return prenotazioneService.findByIdAndUpdate(currentAuthenticatedUser.getId(), updatedPrenotazioneDTO);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    public void findMyPrenotazioneAndDelete(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        prenotazioneService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @PutMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    public Prenotazione findByIdAndUpdate(@PathVariable UUID prenotazioneId, @RequestBody @Validated PrenotazioneDTO updatedPrenotazioneDTO) {
        return prenotazioneService.findByIdAndUpdate(prenotazioneId, updatedPrenotazioneDTO);
    }

    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        prenotazioneService.findByIdAndDelete(prenotazioneId);
    }


}
