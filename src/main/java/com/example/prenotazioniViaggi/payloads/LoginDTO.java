package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
        @NotEmpty(message = "L'email è obbligatoria")
        String email,
        @NotEmpty(message = "Lo username è obbligatorio")
        String username) {
}
