package com.example.act2.act2.demo.logging;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomLogging {

    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOG_ENTRY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void writeLog(String level, String className, String method, String description) {
        // 1. Verificar directorio
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 2. Nombre del archivo diario
        String dateStr = LocalDateTime.now().format(FILE_DATE_FORMAT);
        String fileName = LOG_DIR + File.separator + "aplicacio-" + dateStr + ".log";

        // 3. Formato del mensaje
        String timestamp = LocalDateTime.now().format(LOG_ENTRY_FORMAT);
        String logMessage = String.format("[%s] %s - %s - %s - %s",
                timestamp, level, className, method, description);

        // 4. Escribir
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error LOG: " + e.getMessage());
        }
    }
}