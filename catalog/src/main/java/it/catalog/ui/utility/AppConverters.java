package it.catalog.ui.utility;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import java.time.*;
import java.util.*;

/**
 * Classe contenitore in cui sono definite le classi statiche o 
 * per le conversioni utilizzate nel binder di AbstractCommonFileForm.
 * Questa scelta è stata fatta perchè i converter sono molto piccoli (poche righe di codice), 
 * avere 5 file diversi caricherebbe inutilmente l'albero del progetto.
 * */

public class AppConverters {

	// Impedisce l'istanziazione della classe utility
	private AppConverters() {}

	public static class InstantToLocalDate implements Converter<LocalDate, Instant> {
		@Override
		public Result<Instant> convertToModel(LocalDate v, ValueContext c) {
			return Result.ok(v == null ? null : v.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		@Override
		public LocalDate convertToPresentation(Instant v, ValueContext c) {
			return v == null ? null : v.atZone(ZoneId.systemDefault()).toLocalDate();
		}
	}
	
	public static class InstantToLocalDateTime implements Converter<LocalDateTime, Instant> {
	    @Override
	    public Result<Instant> convertToModel(LocalDateTime v, ValueContext c) {
	        // Converte LocalDateTime (UI) -> Instant (DTO) usando il fuso orario di sistema
	        return Result.ok(v == null ? null : v.atZone(ZoneId.systemDefault()).toInstant());
	    }

	    @Override
	    public LocalDateTime convertToPresentation(Instant v, ValueContext c) {
	        // Converte Instant (DTO) -> LocalDateTime (UI) per la visualizzazione
	        return v == null ? null : v.atZone(ZoneId.systemDefault()).toLocalDateTime();
	    }
	}

	public static class DoubleToLong implements Converter<Double, Long> {
		@Override
		public Result<Long> convertToModel(Double v, ValueContext c) {
			return Result.ok(v == null ? null : v.longValue());
		}
		@Override
		public Double convertToPresentation(Long v, ValueContext c) {
			return v == null ? null : v.doubleValue();
		}
	}

	public static class TagListToSet implements Converter<Set<it.catalog.service.dto.TagDto>, List<it.catalog.service.dto.TagDto>> {
		@Override
		public Result<List<it.catalog.service.dto.TagDto>> convertToModel(Set<it.catalog.service.dto.TagDto> v, ValueContext c) {
			return Result.ok(v == null ? new ArrayList<>() : new ArrayList<>(v));
		}
		@Override
		public Set<it.catalog.service.dto.TagDto> convertToPresentation(List<it.catalog.service.dto.TagDto> v, ValueContext c) {
			return v == null ? new HashSet<>() : new HashSet<>(v);
		}
	}
}