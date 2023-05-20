package dev.efnilite.vilib;

import dev.efnilite.vilib.util.elevator.GitElevator;
import dev.efnilite.vilib.util.elevator.VersionComparator;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.Nullable;

public class ViMain extends ViPlugin {

    private static ViMain instance;

    @Override
    public void enable() {
        instance = this;

        new Metrics(this, 15090);

        logging.info("Enabled vilib " + getDescription().getVersion());
    }

    @Override
    public void disable() {
    }

    @Override
    public @Nullable GitElevator getElevator() {
        return new GitElevator("Efnilite/vilib", this, VersionComparator.FROM_SEMANTIC, Config.CONFIG.getBoolean("auto-updater"));
    }

    /**
     * Returns this plugin instance.
     *
     * @return the plugin instance.
     */
    public static ViMain getPlugin() {
        return instance;
    }
}
