package it.catalog;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;

@Push
//@Theme(value = "my-theme")
@CssImport("./styles/styles.css") // DA VERIFICARE
public class AppShell implements AppShellConfigurator {
	
	
	@Override
    public void configurePage(AppShellSettings settings) {
        // Se l'icona è in src/main/resources/static/favicon.ico
        // Vaadin la vede come "/favicon.ico"
        settings.addFavIcon("icon", "favicon.ico", "32x32");
    }
	
}
