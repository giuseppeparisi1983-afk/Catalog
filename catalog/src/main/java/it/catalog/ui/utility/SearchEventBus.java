package it.catalog.ui.utility;


/**
 * aggregatore di eventi: tutti i listener notificano un unico oggetto, 
 * che decide quando lanciare la query
 * */

public class SearchEventBus {

    private Runnable listener;

    public void subscribe(Runnable listener) {
        this.listener = listener;
    }

    public void fire() {
        if (listener != null) {
            listener.run();
        }
    }
}
