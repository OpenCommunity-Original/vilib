package dev.efnilite.vilib.util;

import org.bukkit.Bukkit;

/**
 * Version class, useful for checking whether certain features on legacy can be executed.
 *
 * @author Efnilite
 */
public enum Version {

    V1_8(8), V1_9(9), V1_10(10), V1_11(11), V1_12(12), V1_13(13), V1_14(14),
    V1_15(15), V1_16(16), V1_17(17), V1_18(18), V1_19(19), V1_20(20), V1_21(21),
    V1_22(22), V1_23(23), V1_24(24), V1_25(25), V1_26(26), V1_27(27), V1_28(28),
    V1_29(29), V1_30(30), V1_31(31), V1_32(32), V1_33(33), V1_34(34);

    public static Version VERSION;
    public final int major;

    Version(int major) {
        this.major = major;
    }

    /**
     * Returns whether the version is higher or equal to a given version.
     *
     * @param compareTo The version to compare to
     * @return true if the current version is higher or equal to the given version, false if not
     */
    public static boolean isHigherOrEqual(Version compareTo) {
        return VERSION.major >= compareTo.major; // 17 >= 16 -> true
    }

    /**
     * Get the current version as a String which can be displayed to users.
     *
     * @return the pretty version as a String
     */
    public static String getPrettyVersion() {
        String string = getInternalVersion().substring(0, 5).replace("_", ".");
        string = string.endsWith(".") ? string.substring(0, string.length() - 1) : string;
        return string; // returns a pretty looking version, ex: 1.12 instead of 1_12
    }

    /**
     * Returns the current version as an instance of this enum.
     *
     * @return the version.
     */
    public static Version getVersion() {
        String string = getInternalVersion().substring(0, 5).toUpperCase();
        string = string.endsWith("_") ? string.substring(0, string.length() - 1) : string;
        VERSION = valueOf(string);
        return VERSION;
    }

    /**
     * Gets the internal version from the Bukkit package.
     * Format: "v1_20_R1"
     *
     * @return the internal version with format "v1_20_R1"
     */
    public static String getInternalVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }
}
