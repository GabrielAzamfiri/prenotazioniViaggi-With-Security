package com.example.prenotazioniViaggi.services;

import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.exceptions.UnauthorizedException;
import com.example.prenotazioniViaggi.payloads.LoginDTO;
import com.example.prenotazioniViaggi.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1. Controllo le credenziali
        // 1.1 Cerco nel db tramite email se esiste l'utente
        Dipendente found = this.dipendenteService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 1.2 Se lo trovo verifico se la pw trovata combacia con quella passataci tramite body
            // 2. Se è tutto ok --> genero un access token e lo restituisco
            return jwtTools.createToken(found);
        } else {
            // 3. Se le credenziali sono errate --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }

    }
}
