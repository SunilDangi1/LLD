package LoggingSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

enum LogLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private final int level;

    LogLevel(int level) { this.level = level; }

    public boolean isGreaterOrEqual(LogLevel other) {
        return this.level >= other.level;
    }
}

interface Appender {
    void append(String formatedMsg);
}

class ConsoleAppender implements Appender{
    @Override
    public void append(String formatedMsg) {
        System.out.println(formatedMsg);
    }
}

class FileAppender implements Appender{
private final String filePath;
    private long currentSize = 0;
    private static final long MAX_SIZE = 10 * 1024 * 1024;  // 10MB

    public FileAppender(String filePath) { this.filePath = filePath; }

    @Override
    public synchronized void append(String formattedMessage) {  // Thread-safe lock
        // Write to file (use BufferedWriter for perf)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formattedMessage + "\n");
            currentSize += formattedMessage.length();
            if (currentSize > MAX_SIZE) {
                rotateFile();  // Rename/log.N to log.N.1
                currentSize = 0;
            }
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }

    private void rotateFile() {
        // Simple rotation: log.txt -> log.1.txt, new log.txt
        // Use java.nio.file.Files.move() in full impl
    }
    
}

class MessageFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String format(LogLevel level, String message, String threadName, String className) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("{\"timestamp\":\"%s\",\"level\":\"%s\",\"thread\":\"%s\",\"class\":\"%s\",\"message\":\"%s\"}",
                timestamp, level, threadName, className, message);
    }
}

interface Logger {
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    // Internal: log(LogLevel, String)
    void log(LogLevel level, String message);
    void addAppender(Appender appender);
}

class RootLogger implements Logger{
    public static final RootLogger INSTANCE = new RootLogger();
    public static LogLevel configLevel = LogLevel.INFO;
    private final List<Appender>appenders = new CopyOnWriteArrayList<>();

    private RootLogger(){
        appenders.add(new ConsoleAppender());
    }

    public static RootLogger getInstance(){
        return INSTANCE;
    }

    @Override
    public void addAppender(Appender appender) { appenders.add(appender); }

    public void setConfigLevel(LogLevel level) { this.configLevel = level; }
    
    @Override
    public void error(String message) {
       log(LogLevel.ERROR,message);
        
    }
    @Override public void debug(String msg) { log(LogLevel.DEBUG, msg); }
    @Override public void info(String msg) { log(LogLevel.INFO, msg); }
    @Override
    public void log(LogLevel level, String message) {
        if (!level.isGreaterOrEqual(configLevel)) return;
        String formatted = MessageFormatter.format(level, message, Thread.currentThread().getName(),
                Thread.currentThread().getStackTrace()[2].getClassName());  // Caller class
        appenders.parallelStream().forEach(appender -> appender.append(formatted)); 
        
    }
    @Override
    public void warn(String message) {
        log(LogLevel.WARN,message);   
    }
}

class LoggerFactory{
    private static final Map<String,Logger> loggers = new ConcurrentHashMap<>(); //using for cahse for multiple threads

    public static Logger getLogger(String name) {
    return RootLogger.getInstance();  // Always the same one
}
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Logging system apllication started");


        Logger logger = LoggerFactory.getLogger(Main.class.getName());
        RootLogger.getInstance().setConfigLevel(LogLevel.INFO);  // Global config via root

        logger.info("App started");
        logger.debug("Debug mode on");  // Logged if >= DEBUG
        logger.error("Error occurred");

        // Add file appender
        RootLogger.getInstance().addAppender(new FileAppender("app.log"));
    }
}

