package it.catalog.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.catalog.persistence.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long>,JpaSpecificationExecutor<Persona> {

    List<Persona> findByAttivoTrue();
    
    // Ricerca per campo
	Page<Persona> findByDataNascitaContainingIgnoreCase(LocalDate dataNascita, Pageable pageable);
	Page<Persona> findByCognomeContainingIgnoreCaseAndNomeContainingIgnoreCase(String cognome,String nome, Pageable pageable);
}

