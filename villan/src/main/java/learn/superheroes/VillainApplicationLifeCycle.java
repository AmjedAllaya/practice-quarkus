package learn.superheroes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

@ApplicationScoped
public class VillainApplicationLifeCycle {

    private static final Logger LOGGER = Logger.getLogger(VillainApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("    _               _____   _   _            _____   _                _           ");
        LOGGER.info("   | |       __ _  |___  | (_) (_)  _ __    |  ___| (_)   ___   ___  | |_     __ _ ");
        LOGGER.info("   | |      / _` |    / /  | | | | | '__|   | |_    | |  / _ \\ / __| | __|  / _` | ");
        LOGGER.info("   | |___  | (_| |   / /   | | | | | |      |  _|   | | |  __/ \\__ \\ | |_  | (_| | ");
        LOGGER.info("   |_____| \\__,_|  /_/    |_| |_| |_|      |_|     |_|  \\___| |___/  \\__| \\__,_|");

        LOGGER.info("The application VILLAIN is starting with profile " + ConfigUtils.getProfiles());

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application VILLAIN is stopping...");
    }
}