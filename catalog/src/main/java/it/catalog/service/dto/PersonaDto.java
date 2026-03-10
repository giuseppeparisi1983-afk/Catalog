package it.catalog.service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonaDto {

    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private boolean attivo;

 // aggiungiamo i tag direttamente nel DTO
    private List<TagDto> tags;
}

