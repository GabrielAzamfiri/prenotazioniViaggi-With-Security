package com.example.prenotazioniViaggi.services;


import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.enums.StatoViaggio;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.exceptions.NotFoundException;
import com.example.prenotazioniViaggi.payloads.StatoViaggioDTO;
import com.example.prenotazioniViaggi.payloads.ViaggioDTO;
import com.example.prenotazioniViaggi.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ViaggioService {
    @Autowired
    private ViaggioRepository viaggioRepository;

    public Page<Viaggio> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio save(ViaggioDTO viaggioDTO){
        StatoViaggio statoViaggio;
        try {
           statoViaggio = StatoViaggio.valueOf(viaggioDTO.stato().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Errore: lo stato specificato non esiste.");
        }

        LocalDate dataViaggio = null;
        try {
          dataViaggio= LocalDate.parse(viaggioDTO.dataViaggio());
        }catch (Exception e){
            throw new BadRequestException("Errore: il formato data inserito non è corretto. inserire un formato data (yyyy-mm-dd)");
        }
        Viaggio viaggio = new Viaggio(viaggioDTO.destinazione(),dataViaggio, statoViaggio);

        return this.viaggioRepository.save(viaggio);

    }

    public Viaggio findById(UUID viaggioId){
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));

    }
    public Viaggio findByIdAndUpdate(UUID  viaggioId, ViaggioDTO updatedViaggioDTO){
        Viaggio found = findById(viaggioId);
        StatoViaggio statoViaggio;
        LocalDate dataViaggio = null;
        try {
            statoViaggio = StatoViaggio.valueOf(updatedViaggioDTO.stato().toUpperCase());
        }catch (Exception e) {
            throw new BadRequestException("Errore: lo stato specificato non esiste.");
        }

        try {
            dataViaggio= LocalDate.parse(updatedViaggioDTO.dataViaggio());
        }catch (Exception e){
            throw new BadRequestException("Errore: il formato data inserito non è corretto. inserire un formato data (yyyy-mm-dd)");
        }

        found.setDestinazione(updatedViaggioDTO.destinazione());
        found.setDataViaggio(dataViaggio);
        found.setStato(statoViaggio);

        return this.viaggioRepository.save(found);
    }
    public void findByIdAndDelete(UUID  viaggioId){
        Viaggio found = findById(viaggioId);
        this.viaggioRepository.delete(found);
    }

    public Viaggio findByIdAndUpdateStato(UUID viaggioId, StatoViaggioDTO statoViaggioDTO){
        StatoViaggio statoViaggio;
        Viaggio viaggio = findById(viaggioId);
        try {
            statoViaggio = StatoViaggio.valueOf(statoViaggioDTO.statoViaggio().toUpperCase());
        }catch (Exception e) {
            // Gestisci l'errore, ad esempio lanciando un'eccezione personalizzata o restituendo un messaggio d'errore
            throw new BadRequestException("Errore: lo stato specificato non esiste.");
            // Puoi anche lanciare un'eccezione personalizzata, oppure restituire un valore di default.
        }
        viaggio.setStato(statoViaggio);
        return this.viaggioRepository.save(viaggio);

    }

}
