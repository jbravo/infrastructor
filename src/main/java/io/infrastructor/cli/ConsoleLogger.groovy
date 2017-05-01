package io.infrastructor.cli

import java.text.SimpleDateFormat
import java.util.Date
import org.fusesource.jansi.Ansi.Color

import static org.fusesource.jansi.Ansi.ansi
import static io.infrastructor.cli.ApplicationProperties.logLevel


public class ConsoleLogger {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS")

    public static final int ERROR = 1
    public static final int INFO = 2
    public static final int DEBUG = 3

    private static def LEVEL_MAPPINGS = [1: 'E', 2: 'I', 3: 'D']
    
    public static void debug(String message) {
        printIfLevel(DEBUG, Color.YELLOW, message)
    }

    public static void info(String message) {
        printIfLevel(INFO, Color.DEFAULT, message)
    }

    public static void error(String message) {
        printIfLevel(ERROR, Color.RED, message)
    }

    private static void printIfLevel(int level, Color color, String message) {
        if (level <= logLevel()) {
            printAlways(level, color, message)
        }
    }

    private static void printAlways(int level, Color color, String message) {
        println(ansi().
                fg(Color.DEFAULT).a(DATE_FORMAT.format(new Date())).
                fg(color).a(" [${LEVEL_MAPPINGS[level]}] ").
                fg(color).a(message).
                reset())
    }
}
