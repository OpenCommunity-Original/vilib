package dev.efnilite.vilib;

import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.elevator.GitElevator;
import dev.efnilite.vilib.util.elevator.VersionComparator;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ViMain extends ViPlugin {

    private static ViMain instance;
    private static Logging logging;

    /**
     * @param child The file name.
     * @return A file from within the plugin folder.
     */
    public static File getInFolder(String child) {
        return new File(instance.getDataFolder(), child);
    }

    /**
     * @return The logger.
     */
    public static Logging logging() {
        return logging;
    }

    @Override
    public void enable() {
        instance = this;
        logging = new Logging(this);

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
