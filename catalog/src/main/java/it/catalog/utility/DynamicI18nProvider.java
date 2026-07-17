package it.catalog.utility;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;

/**
 * Utility per generare dinamicamente la configurazione di localizzazione (I18n)
 * per il componente DatePicker e DateTimePicker basandosi sul Locale corrente
 * del browser dell'utente.
 */

public class DynamicI18nProvider {

	/**
	 * Genera dinamicamente la localizzazione (I18n) compatibile con Vaadin 24
	 * basandosi sul Locale del browser dell'utente.
	 */
	public static DatePickerI18n getI18nForCurrentUser() {
		// 1. Recupera il Locale dell'utente
		Locale locale = null;
		if (UI.getCurrent() != null) {
			locale = UI.getCurrent().getLocale();
		}

		// Fallback di sicurezza
		if (locale == null) {
			locale = Locale.ITALIAN;
		}

		DatePickerI18n i18n = new DatePickerI18n();
		DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);

		// 2. Mesi (12 mesi standard)
		i18n.setMonthNames(Arrays.asList(symbols.getMonths()).subList(0, 12));

		// 3. Giorni della settimana (Domenica deve essere all'indice 0)
		String[] weekdays = symbols.getWeekdays();
		String[] shortWeekdays = symbols.getShortWeekdays();

		i18n.setWeekdays(List.of(weekdays[Calendar.SUNDAY], weekdays[Calendar.MONDAY], weekdays[Calendar.TUESDAY],
				weekdays[Calendar.WEDNESDAY], weekdays[Calendar.THURSDAY], weekdays[Calendar.FRIDAY],
				weekdays[Calendar.SATURDAY]));

		i18n.setWeekdaysShort(List.of(shortWeekdays[Calendar.SUNDAY], shortWeekdays[Calendar.MONDAY],
				shortWeekdays[Calendar.TUESDAY], shortWeekdays[Calendar.WEDNESDAY], shortWeekdays[Calendar.THURSDAY],
				shortWeekdays[Calendar.FRIDAY], shortWeekdays[Calendar.SATURDAY]));

		// 4. Primo giorno della settimana (In Italia è Lunedì = 1, in USA è Domenica =
		// 0)
		Calendar cal = Calendar.getInstance(locale);
		int firstDay = cal.getFirstDayOfWeek() - 1; // Adatta all'indice 0-based di Vaadin
		i18n.setFirstDayOfWeek(firstDay);

		// 5. Traduzione dei bottoni (Today e Cancel) e formati data (passati come
		// varargs)
		if (Locale.ITALIAN.getLanguage().equals(locale.getLanguage())) {
			i18n.setToday("Oggi");
			i18n.setCancel("Annulla");

			// Il primo formato è quello di visualizzazione principale, i successivi servono
			// per il parsing di fallback
			i18n.setDateFormats("dd/MM/yyyy", "d/M/yyyy", "dd-MM-yyyy");
		} else {
			// Default in Inglese per qualsiasi altra lingua del browser
			i18n.setToday("Today");
			i18n.setCancel("Cancel");

			i18n.setDateFormats("MM/dd/yyyy", "M/d/yyyy", "yyyy-MM-dd");
		}

		return i18n;
	}
}