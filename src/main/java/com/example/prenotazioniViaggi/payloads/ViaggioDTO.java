package com.example.prenotazioniViaggi.payloads;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record ViaggioDTO(
        @NotEmpty(message = "La destinazione  è obbligatoria")
        @Size(min = 3, max = 40, message = "La destinazione deve essere compresa tra 3 e 40 caratteri")
        String destinazione,

        @NotEmpty(message = "La data Viaggio è obbligatoria")
        String dataViaggio,

        @NotEmpty(message = "Lo stato è obbligatorio")
        String stato
) {
}
