package com.example.prenotazioniViaggi.entities;

import com.example.prenotazioniViaggi.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonIgnoreProperties({"password", "role", "authorities", "prenotazioneList", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "dipendente")
    private List<Prenotazione> prenotazioneList;

    public Dipendente(String nome, String cognome, String username, String email, String password, String avatar) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.role = Role.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Questo metodo deve restituire una lista di ruoli dell'utente (SimpleGrantedAuthority, classe che in Spring rappresenta i ruoli degli utenti)
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
