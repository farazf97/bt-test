package billing.sort;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public final class TempFileManager {

    public static File createTempChunk() throws IOException {
        File f = File.createTempFile("billing_chunk_", ".tmp");
        f.deleteOnExit();
        return f;
    }

    public static File createOutput() throws IOException {
        File f = File.createTempFile("sorted_logs_", ".txt");
        f.deleteOnExit();
        return f;
    }

    public static File createEmptyOutput() throws IOException {
        return createOutput();
    }

    public static void deleteFiles(List<File> files) {
        for (File f : files) {
            try {
                Files.deleteIfExists(f.toPath());
            } catch (Exception ignored) {
            }
        }
    }
}
