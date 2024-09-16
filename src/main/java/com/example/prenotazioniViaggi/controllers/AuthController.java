package com.example.prenotazioniViaggi.controllers;


import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.payloads.DipendenteDTO;
import com.example.prenotazioniViaggi.payloads.LoginDTO;
import com.example.prenotazioniViaggi.payloads.UserLoginRespDTO;
import com.example.prenotazioniViaggi.services.AuthService;
import com.example.prenotazioniViaggi.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendenteService dipendenteService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody LoginDTO payload) {
        return new UserLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }

    // 2. POST http://localhost:3001/users (+req.body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Serve per customizzare lo status code (CREATED --> 201)
    private Dipendente createAuthor(@RequestBody @Validated DipendenteDTO dipendenteDTO, BindingResult validationResult) {
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
            return dipendenteService.save(dipendenteDTO);
        }

    }
}
