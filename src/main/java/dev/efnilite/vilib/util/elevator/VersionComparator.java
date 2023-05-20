package dev.efnilite.vilib.util.elevator;

/**
 * Class containing comparators for versions which may be used in plugins.
 */
public enum VersionComparator {

    /**
     * Tests whether version strings are equal.
     */
    EQUAL {
        @Override
        public boolean isLatest(String latest, String current) {
            return latest.equalsIgnoreCase(current);
        }
    },

    /**
     * Compares versions with a semantic syntax. Does not support letters, just numbers.
     */
    FROM_SEMANTIC {
        @Override
        public boolean isLatest(String latest, String current) {
            try {
                return toVersionNumber(latest) <= toVersionNumber(current);
            } catch (NumberFormatException ex) {
                return false; // if numbers contain letters, assume the worst by not being updated
            }
        }
    };

    public abstract boolean isLatest(String latest, String current);

    protected double toVersionNumber(String version) {
        String stripped = strip(version);

        return Double.parseDouble(stripped) / stripped.length();
    }

    // strips a string
    protected String strip(String string) {
        return string.toLowerCase().replace("v", "").replace(".", "");
    }
}