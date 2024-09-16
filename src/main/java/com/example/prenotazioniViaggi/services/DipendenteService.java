package com.example.prenotazioniViaggi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.exceptions.NotFoundException;
import com.example.prenotazioniViaggi.payloads.DipendenteDTO;
import com.example.prenotazioniViaggi.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Dipendente> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente save(DipendenteDTO dipendenteDTO){
        // 1. Verifico che l'email non sia già stata utilizzata
        this.dipendenteRepository.findByEmail(dipendenteDTO.email()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                dipendente -> {
                    throw new BadRequestException("L'email " + dipendenteDTO.email() + " è già in uso!");
                }
        );

        // 2. Se tutto è ok procedo con l'aggiungere campi 'server-generated' (nel nostro caso avatarURL)
        Dipendente newDipendente = new Dipendente(dipendenteDTO.nome(), dipendenteDTO.cognome(),dipendenteDTO.username(), dipendenteDTO.email(),"https://ui-avatars.com/api/?name="+dipendenteDTO.nome()+"+"+dipendenteDTO.cognome());

        // 3. Salvo lo User
        return this.dipendenteRepository.save(newDipendente);
    }
    public Dipendente findById(UUID dipendenteId){
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    public Dipendente findByIdAndUpdate(UUID dipendenteId, DipendenteDTO updatedDipendenteDTO){
        // 1. Controllo se l'email nuova è già in uso
        this.dipendenteRepository.findByEmail(updatedDipendenteDTO.email()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                dipendente -> {
                    throw new BadRequestException("L'email " + updatedDipendenteDTO.email() + " è già in uso!");
                }
        );
        Dipendente found = this.findById(dipendenteId);
        found.setNome(updatedDipendenteDTO.nome());
        found.setCognome(updatedDipendenteDTO.cognome());
        found.setEmail(updatedDipendenteDTO.email());
        found.setUsername(updatedDipendenteDTO.username());
        found.setAvatar("https://ui-avatars.com/api/?name="+updatedDipendenteDTO.nome()+"+"+updatedDipendenteDTO.cognome());
        return this.dipendenteRepository.save(found);
    }

    public void findByIdAndDelete(UUID dipendenteId){
        Dipendente found = this.findById(dipendenteId);
        this.dipendenteRepository.delete(found);
    }

    public Dipendente uploadImage(MultipartFile file , UUID dipendenteId) throws IOException {
        Dipendente dipendente = findById(dipendenteId);
        String url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        System.out.println("URL: " + url);
        dipendente.setAvatar(url);
        // ... poi l'url lo salvo nel db per quello specifico utente
        dipendenteRepository.save(dipendente);
        return dipendente;
    }
}
