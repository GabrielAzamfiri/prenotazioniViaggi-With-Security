package com.example.prenotazioniViaggi.controllers;

import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.payloads.StatoViaggioDTO;
import com.example.prenotazioniViaggi.payloads.ViaggioDTO;
import com.example.prenotazioniViaggi.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {
    @Autowired
    private ViaggioService viaggioService;


    @GetMapping() // Qua sto definendo il metodo HTTP da utilizzare per questo endpoint e l'ultima parte dell'URL
    // Per contattare questo endpoint dovrò mandare una richiesta GET a http://localhost:3001/authors
    public Page<Viaggio> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy){
        return this.viaggioService.findAll(page, size, sortBy);
    }
    @GetMapping("/{viaggioId}")
    private Viaggio getById(@PathVariable UUID viaggioId){
        return viaggioService.findById(viaggioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Serve per customizzare lo status code (CREATED --> 201)
    private Viaggio createAuthor(@RequestBody @Validated ViaggioDTO viaggioDTO, BindingResult validationResult){
        // @Validated serve per 'attivare' le regole di validazione descritte nel DTO
        // BindingResult mi permette di capire se ci sono stati errori e quali errori ci sono stati
        if(validationResult.hasErrors())  {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente
            return viaggioService.save(viaggioDTO);
        }

    }
    @PutMapping("/{viaggioId}")
    private Viaggio findByIdAndUpdate(@PathVariable UUID  viaggioId, @RequestBody @Validated ViaggioDTO updatedViaggioDTO){
        return viaggioService.findByIdAndUpdate(viaggioId,updatedViaggioDTO);
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    private void findByIdAndDelete(@PathVariable UUID  viaggioId){
        viaggioService.findByIdAndDelete(viaggioId);
    }
    @PatchMapping("/{viaggioId}/stato")
    private Viaggio findByStato(@PathVariable UUID viaggioId ,@RequestBody @Validated StatoViaggioDTO statoViaggioDTO){
        return viaggioService.findByIdAndUpdateStato(viaggioId, statoViaggioDTO);
    }
}
