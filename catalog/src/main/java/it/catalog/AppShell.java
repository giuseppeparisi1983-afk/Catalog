package it.catalog;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@Push
@PWA(name = "Catalog App", shortName = "Catalog")
//@Theme(value = "my-theme")
public class AppShell implements AppShellConfigurator {
}
