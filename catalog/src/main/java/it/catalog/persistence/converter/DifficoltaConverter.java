package it.catalog.persistence.converter;

import it.catalog.common.enums.Livello;
import it.catalog.persistence.converter.base.AbstractLabelConverter;
import jakarta.persistence.Converter;

//3. Implementazione specifica (Una per ogni Enum)
@Converter(autoApply = true)
public class DifficoltaConverter extends AbstractLabelConverter<Livello> {
  
	public DifficoltaConverter() {
        super(Livello.class);
    }

}
