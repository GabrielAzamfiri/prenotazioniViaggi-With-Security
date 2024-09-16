package com.example.prenotazioniViaggi.repositories;

import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, UUID> {
}
