import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class UtilityClass {
    static void closeLogger(FileHandler logHandler) {
        if (logHandler != null) {
            logHandler.flush();
            logHandler.close();
        }
    }

    static Logger getLogger(String className) {
        return Logger.getLogger(className);

    }

    static FileHandler initLogger(java.util.logging.Logger logger, String filePath) throws IOException {
        logger.setUseParentHandlers(false); // disables console duplication of logs, by setting Parent logger field to false
        FileHandler logHandler = new FileHandler(filePath, true);
        logHandler.setFormatter(new SimpleFormatter());
        logger.setLevel(Level.ALL);
        logger.addHandler(logHandler);
        return logHandler;
    }

    static int getRandNumberFromTO(int from, int to) {
        return new Random().nextInt(to - from) + from;
    }

}
