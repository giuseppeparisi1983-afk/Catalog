package it.catalog.persistence.converter;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.persistence.converter.base.AbstractLabelConverter;
import jakarta.persistence.Converter;

//3. Implementazione specifica (Una per ogni Enum)
@Converter(autoApply = true)
public class StatiDocumentoConverter extends AbstractLabelConverter<StatiDocumento> {
  
	public StatiDocumentoConverter() {
        super(StatiDocumento.class);
    }

}
