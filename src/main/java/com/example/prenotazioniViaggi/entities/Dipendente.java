package com.example.prenotazioniViaggi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Dipendente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String avatar;

    @JsonIgnore
    @OneToMany(mappedBy = "dipendente")
    private List<Prenotazione> prenotazioneList;

    public Dipendente(String nome, String cognome, String username, String email, String avatar) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
    }
}
