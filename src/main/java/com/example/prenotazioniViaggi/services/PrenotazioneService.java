package com.example.prenotazioniViaggi.services;


import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Prenotazione;
import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.exceptions.NotFoundException;
import com.example.prenotazioniViaggi.payloads.PrenotazioneDTO;
import com.example.prenotazioniViaggi.repositories.DipendenteRepository;
import com.example.prenotazioniViaggi.repositories.PrenotazioneRepository;
import com.example.prenotazioniViaggi.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private  ViaggioRepository viaggioRepository;


    public Page<Prenotazione> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione save(PrenotazioneDTO prenotazioneDTO){
        UUID uuidDipendente;
        UUID uuidViaggio;
        try {
            uuidDipendente=  UUID.fromString(prenotazioneDTO.dipendente());
            uuidViaggio=  UUID.fromString(prenotazioneDTO.viaggio());
        }catch (Exception e){
            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un ID di Tipo UUID");
        }


        Dipendente dipendente = dipendenteRepository.findById(uuidDipendente).orElseThrow(() ->  new NotFoundException(uuidDipendente));
        Viaggio viaggio = viaggioRepository.findById(uuidViaggio).orElseThrow(() ->  new NotFoundException(uuidViaggio));

        prenotazioneRepository.findByDipendenteAndViaggioDataViaggio(dipendente,viaggio.getDataViaggio()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                prenotazione -> {
                    throw new BadRequestException("Hai già un viaggio prenotato per la data " + viaggio.getDataViaggio());
                }
        );



        Prenotazione prenotazione = new Prenotazione(prenotazioneDTO.info(),dipendente,viaggio);

        return this.prenotazioneRepository.save(prenotazione);

    }

    public Prenotazione findById(UUID prenotazioneId){
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID  prenotazioneId, PrenotazioneDTO updatedPrenotazioneDTO){
        Prenotazione found = findById(prenotazioneId);
        UUID uuidDipendente;
        UUID uuidViaggio;
        try {
            uuidDipendente=  UUID.fromString(updatedPrenotazioneDTO.dipendente());
            uuidViaggio=  UUID.fromString(updatedPrenotazioneDTO.viaggio());
        }catch (Exception e){
            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un id di Tipo UUID");
        }


        Dipendente dipendente = dipendenteRepository.findById(uuidDipendente ).orElseThrow(() ->  new NotFoundException(uuidDipendente));
        Viaggio viaggio = viaggioRepository.findById(uuidViaggio ).orElseThrow(() ->  new NotFoundException(uuidViaggio));


        found.setInfo(updatedPrenotazioneDTO.info());
        found.setDipendente(dipendente);
        found.setViaggio(viaggio);

        return this.prenotazioneRepository.save(found);
    }
    public void findByIdAndDelete(UUID  prenotazioneId){
        Prenotazione found = findById(prenotazioneId);
        this.prenotazioneRepository.delete(found);
    }
}
