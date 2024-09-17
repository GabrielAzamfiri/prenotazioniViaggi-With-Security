package com.example.prenotazioniViaggi.controllers;

import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.payloads.DipendenteDTO;
import com.example.prenotazioniViaggi.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        // Tramite @AuthenticationPrincipal posso accedere ai dati dell'utente che sta effettuando la richiesta
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser, @RequestBody DipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        this.dipendenteService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

//    @DeleteMapping("/me/avatar")
//    public void uploadProfileAvatar(@AuthenticationPrincipal Dipendente currentAuthenticatedUser,
//                                    @RequestParam("avatar") MultipartFile image) throws IOException {
//        this.dipendenteService.uploadImage(image, currentAuthenticatedUser.getId());
//    }

    @GetMapping("/{dipendenteId}")// Tutti gli utenti possono leggere il profilo di un altro utente
    private Dipendente getById(@PathVariable UUID dipendenteId) {
        return dipendenteService.findById(dipendenteId);
    }


    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    private Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO updatedDipendenteDTO) {
        return dipendenteService.findByIdAndUpdate(dipendenteId, updatedDipendenteDTO);
    }

    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono modificare altri utenti
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    private void findByIdAndDelete(@PathVariable UUID dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }

    @PostMapping("/{dipendenteId}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono modificare altri utenti
    public Dipendente uploadAvatar(@RequestParam("avatar") MultipartFile image, @PathVariable UUID dipendenteId) throws IOException {
        // "avatar" deve corrispondere ESATTAMENTE come il campo del FormData che ci invia il Frontend
        // Se non corrisponde non troverò il file
        return this.dipendenteService.uploadImage(image, dipendenteId);
    }
}
