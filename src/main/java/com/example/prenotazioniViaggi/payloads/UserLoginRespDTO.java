package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginRespDTO(
        @NotEmpty(message = "Il accessToken è obbligatorio")
        String accessToken
) {
}
