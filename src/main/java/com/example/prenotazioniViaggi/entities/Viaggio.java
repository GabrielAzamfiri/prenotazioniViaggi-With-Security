package com.example.prenotazioniViaggi.entities;

import com.example.prenotazioniViaggi.enums.StatoViaggio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Viaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String destinazione;
    private LocalDate dataViaggio;
    @Enumerated(EnumType.STRING)
    private StatoViaggio stato;

    @JsonIgnore
    @OneToMany(mappedBy = "viaggio")
    private List<Prenotazione> prenotazioneList;


    public Viaggio(String destinazione, LocalDate dataViaggio, StatoViaggio stato) {
        this.destinazione = destinazione;
        this.dataViaggio = dataViaggio;
        this.stato = stato;
    }
}
