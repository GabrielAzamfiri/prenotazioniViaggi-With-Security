package com.example.prenotazioniViaggi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String info;

    private LocalDate dataRichiesta;

    @ManyToOne
    @JoinColumn(name = "dipendente")
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio")
    private Viaggio viaggio;

    public Prenotazione(String info, Dipendente dipendente, Viaggio viaggio) {
        this.info = info;
        this.dataRichiesta = LocalDate.now();
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }
}
