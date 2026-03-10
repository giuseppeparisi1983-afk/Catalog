package it.catalog.persistence.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "image_file")
public class ImageFile {
    public enum Formato { JPEG, RAW, TIFF, PNG }
    public enum TipoFile { Fotografia, Sfondo, Illustrazione }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String filename;
    private String mimeType;
    private long sizeBytes;

    @Enumerated(EnumType.STRING)
    private Formato formato;

    @Enumerated(EnumType.STRING)
    private TipoFile tipoFile;

    private boolean cancelled;
    private boolean preferito;
    private Integer rating;
    private long visualizzazioni;
    private Instant dataArchiviazione;
    private Instant dataUltimaVisualizzazione;
    private boolean backup;
    private String note;

    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
    // getters & setters
}

