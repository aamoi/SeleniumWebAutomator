/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;
/**
 *
 * @author amoi
 */
import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Initializes the log files.
 *
 * @author <a href="kim.kiogora@cellulant.com">Kim Kiogora</a>
 */
@SuppressWarnings("FinalClass")
public final class Logging {
    private static Props properties=null;
    static {
        properties=new Props();
    }
    
    /**
     * Info log.
     */
    private static Logger infoLog;
    /**
     * Error log.
     */
    private static Logger errorLog;
    /**
     * Loaded system properties.
     */
    private transient Props props;

    /**
     * Constructor.
     *
     * @param properties passed in loaded system properties
     */
    public Logging(final Props properties) {
        this.props = properties;
        initializeLoggers();
    }
    public Logging() {
        props = properties;
        initializeLoggers();
    }

    /**
     * Initialize the log managers.
     */
    @SuppressWarnings({ "CallToThreadDumpStack", "UseOfSystemOutOrSystemErr" })
    private void initializeLoggers() {
        infoLog = Logger.getLogger("infoLog");
        errorLog = Logger.getLogger("errorLog");

        PatternLayout layout = new PatternLayout();
        layout.setConversionPattern("%d{yyyy MMM dd HH:mm:ss,SSS}: %p : %m%n");

        try {
            RollingFileAppender rfaInfoLog = new RollingFileAppender(layout,
                    props.getInfoLogFile(), true);
            String maxfilesize=props.getLogfilesize();
            
            rfaInfoLog.setMaxFileSize(maxfilesize);
            rfaInfoLog.setMaxBackupIndex(props.getLogfiles());

            RollingFileAppender rfaErrorLog = new RollingFileAppender(layout,
                    props.getErrorLogFile(), true);
            rfaErrorLog.setMaxFileSize(maxfilesize);
            rfaErrorLog
                    .setMaxBackupIndex(props.getLogfiles());

            infoLog.addAppender(rfaInfoLog);
            errorLog.addAppender(rfaErrorLog);
        } catch (IOException ex) {
            System.err.println("Failed to initialize loggers... EXITING: "
                    + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        infoLog.setLevel(Level.toLevel(props.getInfoLogLevel()));
        errorLog.setLevel(Level.toLevel(props.getErrorLogLevel()));

        info("Just finished initializing Loggers...");
    }

    /**
     * Log info messages.
     *
     * @param message the message content
     */
    public void info(final String message) {
        infoLog.info(message);
    }

    /**
     * Log debug messages.
     *
     * @param message the message content
     */
    public void debug(final String message) {
        infoLog.debug(message);
    }

    /**
     * Log error messages.
     *
     * @param message the message content
     */
    public void error(final String message) {
        errorLog.error(message);
    }

    /**
     * Log fatal error messages.
     *
     * @param message the message content
     */
    public void fatal(final String message) {
        errorLog.fatal(message);
    }

    public static Logger getInfoLog() {
        return infoLog;
    }

    public static Logger getErrorLog() {
        return errorLog;
    }
}
