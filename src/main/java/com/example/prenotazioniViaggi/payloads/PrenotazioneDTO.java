package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;



public record PrenotazioneDTO(
        @NotEmpty(message = "Le info sono obbligatorie")
        @Size(min = 3, max = 100, message = "Le info  devono essere comprese tra 3 e 100 caratteri")
        String info,

        @NotEmpty(message = "Il dipendente è obbligatorio")
        String dipendente,

        @NotEmpty(message = "Il viaggio è obbligatorio")
        String viaggio
) {
}
