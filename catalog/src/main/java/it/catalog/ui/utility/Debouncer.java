package it.catalog.ui.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Questa classe di supporto serve a non lanciare la query subito ad ogni evento, aspetti un po’ (es. 300–500 ms).
  Se nel frattempo arrivano altri eventi, li ignori.
 * */
public class Debouncer {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pending;

    public void debounce(Runnable task, long delayMs) {
        if (pending != null && !pending.isDone()) {
            pending.cancel(false);
        }

        pending = scheduler.schedule(task, delayMs, TimeUnit.MILLISECONDS);
    }
}
