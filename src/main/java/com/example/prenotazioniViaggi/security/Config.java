package com.example.prenotazioniViaggi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Per poter configurare le questioni relative alla sicurezza derivanti da Spring Security
        // devo crearmi un Bean apposito che mi consenta di:
        // - disabilitare dei comportamenti di default non graditi
        httpSecurity.formLogin(http -> http.disable()); // Non voglio il form di login (avremo React per quello)
        httpSecurity.csrf(http -> http.disable()); // Non voglio la protezione da attacchi CSRF (per le nostre app non è necessaria, anzi
        // andrebbe a complicare anche il codice lato FE)
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Non voglio le sessioni (perché con
        // JWT non si utilizzano le sessioni)
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll()); // Questo evita di ricevere 401 su OGNI richiesta
        // - customizzare il comportamento di alcuni filtri di sicurezza
        // - aggiungere ulteriori filtri personalizzati
        return httpSecurity.build();
    }
}
