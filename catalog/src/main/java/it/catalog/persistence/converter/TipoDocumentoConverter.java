package it.catalog.persistence.converter;

import it.catalog.common.enums.TipoDocumento;
import it.catalog.persistence.converter.base.AbstractLabelConverter;
import jakarta.persistence.Converter;

//3. Implementazione specifica (Una per ogni Enum)
@Converter(autoApply = true)
public class TipoDocumentoConverter extends AbstractLabelConverter<TipoDocumento> {
  
	public TipoDocumentoConverter() {
        super(TipoDocumento.class);
    }

}
