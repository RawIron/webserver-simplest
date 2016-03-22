/*
 * 
 */
 
package server;

import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.logging.*;


class LoggerBuildException extends Exception {
    private static final long serialVersionUID = 0L;
    public LoggerBuildException(String message) {
        super(message);
    }
}


class LoggerModels {
    public static final int DEFAULT = 0;
    public static final int FILE = 1;
    public static final int CONSOLE = 2;
}

class LoggerFactory {
    public Logger build(int model) throws LoggerBuildException {
        switch(model) {
        case LoggerModels.DEFAULT:
            return new SimpleLogger();
        case LoggerModels.CONSOLE:
            return new ConsoleLogger();
        default:
            String message = "";
            throw new LoggerBuildException(message);
        }
    }

    public Logger build(int model, String fileName) throws LoggerBuildException {
        switch(model) {
        case LoggerModels.FILE:
            return new StreamLogger(fileName);
        default:
            String message = "";
            throw new LoggerBuildException(message);
        }
    }
}


abstract class Logger {
    protected String lastMessageLogged = "";
    protected int totalMessagesLogged = 0;

    public abstract void log(String message);
}


class SimpleLogger extends Logger {
    public void log(String message) {
        System.out.println(message);
        lastMessageLogged = message;
        ++totalMessagesLogged;
    }
}

class StreamLogger extends Logger {
    protected PrintStream log = null;

    public StreamLogger(String logName) {
        try {
            this.log = new PrintStream(
                            new BufferedOutputStream(
                            new FileOutputStream(logName)));
        } catch (FileNotFoundException e) {
        }
    }

    public void log(String message) {
        synchronized (log) {
            log.println(message);
            log.flush();
        }
        lastMessageLogged = message;
        ++totalMessagesLogged;
    }
}


class ConsoleLogger extends Logger {
    protected ConsoleHandler consoleHandler = null;

    public ConsoleLogger() {
        this.consoleHandler = new ConsoleHandler();
    }

    public void log(String message) {
        LogRecord r = new LogRecord(Level.INFO, message);
        consoleHandler.publish(r);
    }
}

