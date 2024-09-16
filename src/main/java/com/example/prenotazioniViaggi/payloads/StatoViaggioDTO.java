package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.NotEmpty;

public record StatoViaggioDTO(
        @NotEmpty(message = "Lo stato è obbligatorio")
        String statoViaggio
) {
}
