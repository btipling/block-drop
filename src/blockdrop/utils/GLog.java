package blockdrop.utils;

import java.util.logging.Logger;

public class GLog {
    private static final Logger logger = Logger.getLogger("BlockDrop");

    private GLog() {}

    public static void info(String message, Object... arguments) {
       logger.info(String.format(message, arguments));
    }
}

