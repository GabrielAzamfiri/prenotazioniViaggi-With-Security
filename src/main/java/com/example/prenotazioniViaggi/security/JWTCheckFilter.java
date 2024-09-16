package com.example.prenotazioniViaggi.security;

import com.example.prenotazioniViaggi.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo è il metodo che verrà richiamato ad ogni richiesta (a parte quelle che escludono il filtro)
        // e dovrà controllare che il token allegato sia valido. Il token lo troveremo nell'Authorization Header (se c'è)
        // Cose da fare:

        // 1. Verifichiamo se nella richiesta c'è effettivamente l'Authorization Header, se non c'è --> 401
        String authHeader = request.getHeader("Authorization");
        // "Authorization": "Bearer eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MjY0ODE1MDMsImV4cCI6MTcyNzA4NjMwMywic3ViIjoiOTFkMTg2MGItZjE2Yy00MTYwLWIyYTYtODU2NWY0MzY5MTBiIn0.l58gBS6yJnRom0gYNRECl3W_e1B0TmdNkOivPncYP0fO3LIC2QXwvgft71jNYhfJ"
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Per favore inserisci correttamente il token nell'Authorization Header");

        // 2. Se c'è estraiamo il token dall'header
        String accessToken = authHeader.substring(7); // "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MjY0ODE1MDMsImV4cCI6MTcyNzA4NjMwMywic3ViIjoiOTFkMTg2MGItZjE2Yy00MTYwLWIyYTYtODU2NWY0MzY5MTBiIn0.l58gBS6yJnRom0gYNRECl3W_e1B0TmdNkOivPncYP0fO3LIC2QXwvgft71jNYhfJ"
        System.out.println("ACCESS TOKEN " + accessToken);
        // 3. Verifichiamo se il token è stato manipolato (verifichiamo la signature) e se è scaduto (verifica dell'Expiration Date)
        jwtTools.verifyToken(accessToken);

        // 4. Se è tutto ok andiamo avanti (il che potrebbe voler dire o andare al prossimo filtro oppure al controller)
        filterChain.doFilter(request, response);

        // 5. Se il token non è ok --> 401
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Fare l'override di questo metodo ci serve per 'disabilitare' questo filtro per alcune richieste,
        // ad esempio richieste su determinati endpoint oppure direttamente su determinati controller
        // Nel nostro caso ad esempio ci interessa che il filtro, che dovrà verificare i token, non venga chiamato in causa
        // per tutte le richieste di Login o di Register perché sono richieste che non devono richiedere un token per poter essere eseguite
        // Se gli endpoint di Login e Register si trovano nello stesso controller avranno lo stesso URL di base "http://localhost:3001/auth/**"

        // Posso quindi escludere dal controllo del filtro tutte le richieste verso gli endpoint che contengono /auth nell'URL
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }

}
