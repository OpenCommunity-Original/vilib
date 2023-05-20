package dev.efnilite.vilib.command;

import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to wrap commands, which makes it a lot easier to produce them.
 *
 * @author Efnilite
 */
public abstract class ViCommand implements CommandExecutor, TabCompleter {

    private static Method SYNC_COMMANDS;

    static {
        try {
            SYNC_COMMANDS = getCBClass().getMethod("syncCommands");
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the current command map instance
     *
     * @return the command map instance
     */
    public static @Nullable SimpleCommandMap retrieveMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (SimpleCommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            ViMain.logging().error("Error while trying to access the command map.");
            ViMain.logging().error("Commands will not show up on completion.");
            return null;
        }
    }

    /**
     * Adds a command to the Command Map
     *
     * @param alias   The alias
     * @param command The command instance
     * @return the command that was added
     */
    public static Command add(@NotNull String alias, @NotNull Command command) {
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);

            CommandMap map = retrieveMap();

            Map<String, Command> knownCommands = (Map<String, Command>) field.get(map);

            field.set(map, knownCommands);

            return knownCommands.put(alias, command);
        } catch (NoSuchFieldException ex) {
            ViMain.logging().stack("knownCommands field not found for registry", "update your server or switch to a supported server platform", ex);
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            ViMain.logging().error("There was an error while trying to register your command to the Command Map");
            ViMain.logging().error("It might not show up in-game in the auto-complete, but it does work.");
            return null;
        }
    }

    /**
     * Unregister a command from the map.
     *
     * @param command The command
     */
    public static void unregister(@NotNull Command command) {
        CommandMap map = retrieveMap();

        if (map != null) {
            command.unregister(map);
        }
    }

    /**
     * Syncs all commands to all players
     */
    public static void sync() {
        if (SYNC_COMMANDS == null) {
            return;
        }

        try {
            SYNC_COMMANDS.invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Registers a command under plugin.yml
     *
     * @param name    The name of this command in plugin.yml
     * @param wrapper The command that's going to be registered
     */
    public static void register(String name, ViCommand wrapper) {
        PluginCommand command = Bukkit.getPluginCommand(name);

        if (command == null) {
            return;
        }

        command.setExecutor(wrapper);
        command.setTabCompleter(wrapper);

        // add command to internal register
        add(name, command);
    }

    private static Class<?> getCBClass() {
        try {
            return Class.forName("org.bukkit.craftbukkit.%s.CraftServer".formatted(Version.getInternalVersion()));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Couldn't find CraftBukkit class " + "CraftServer");
        }
    }

    /**
     * UUID-based cooldown system
     */
    private final Map<UUID, List<CommandCooldown>> cooldowns = new HashMap<>();

    /**
     * Execute a command
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Get what should be suggested
     */
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    /**
     * Checks the cooldown
     *
     * @param sender     The CommandSender which may have a cooldown
     * @param arg        The argument to which this cooldown applies
     * @param cooldownMs The cooldown in ms
     * @return false if the cooldown is not over yet, true if it has been.
     */
    protected boolean cooldown(CommandSender sender, String arg, long cooldownMs) {
        if (sender instanceof ConsoleCommandSender) { // ignore console (has no UUID)
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        CommandCooldown cooldown; // the current cooldown
        List<CommandCooldown> playerCooldowns = cooldowns.get(uuid);

        if (playerCooldowns == null) {
            playerCooldowns = new ArrayList<>();
            playerCooldowns.add(new CommandCooldown(System.currentTimeMillis(), arg));
            cooldowns.put(uuid, playerCooldowns);
            return true;
        }

        // get the appropriate commandcooldown class
        cooldown = playerCooldowns.stream()
                .filter(plCooldown -> plCooldown.arg().equals(arg))
                .findFirst().orElse(null);

        if (cooldown == null) { // cooldown doesnt exist yet
            playerCooldowns.add(new CommandCooldown(System.currentTimeMillis(), arg));
            cooldowns.put(uuid, playerCooldowns);
            return true;
        }

        if (System.currentTimeMillis() - cooldown.lastExecuted() <= cooldownMs) {
            return false;
        }

        playerCooldowns.remove(cooldown); // update cooldown
        playerCooldowns.add(new CommandCooldown(System.currentTimeMillis(), arg));
        cooldowns.put(uuid, playerCooldowns);

        return true;
    }

    /**
     * Gets completions in relation to what the user has already typed
     *
     * @param typed    What the player has typed so far
     * @param possible The possible completions
     * @return the updated possible completions
     */
    protected List<String> completions(String typed, List<String> possible) {
        return possible.stream().filter(option -> option.toLowerCase().contains(typed)).toList();
    }

    /**
     * Gets completions in relation to what the user has already typed
     *
     * @param typed    What the player has typed so far
     * @param possible The possible completions
     * @return the updated possible completions
     */
    protected List<String> completions(String typed, String... possible) {
        return Arrays.stream(possible).filter(option -> option.toLowerCase().contains(typed)).toList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return tabComplete(sender, args);
    }

    /**
     * @param arg          The argument
     * @param lastExecuted When the command with arg was last executed
     */
    private record CommandCooldown(long lastExecuted, String arg) {

    }
}