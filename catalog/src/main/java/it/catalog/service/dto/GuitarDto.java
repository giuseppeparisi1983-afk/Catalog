package it.catalog.service.dto;

import it.catalog.common.enums.Difficolta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuitarDto extends VideoDto{   

    private Integer idGuitar;
//    private Integer videoId;     // id della tabella video (null se nuovo)
    private Boolean visto;
    private Boolean todo;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
 // Anche qui usiamo l'Enum! come sull'entity
    private Difficolta difficolta;
    private String autore;
}

