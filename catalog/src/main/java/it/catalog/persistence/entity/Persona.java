package it.catalog.persistence.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;

    @Column(name = "data_nascita")
    private LocalDate dataNascita;

    private boolean attivo = true;

 // relazione con OggettoTag
    @OneToMany
    @JoinColumn(name = "id_oggetto", referencedColumnName = "id")
    private Set<OggettoTag> tags = new HashSet<>(); 
    
 }
