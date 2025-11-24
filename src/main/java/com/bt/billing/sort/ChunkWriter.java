package billing.sort;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class ChunkWriter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final int maxChunkLines;
    private final Comparator<String> comparator;

    public ChunkWriter(int maxChunkLines, Comparator<String> comparator) {
        this.maxChunkLines = maxChunkLines;
        this.comparator = comparator;
    }

    public List<File> splitIntoChunks(File input) throws IOException {
        List<File> chunks = new ArrayList<>(8);
        List<String> buffer = new ArrayList<>(Math.min(maxChunkLines, 16_384));

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (isValidLine(line)) {
                    buffer.add(line);
                }

                if (buffer.size() >= maxChunkLines) {
                    chunks.add(sortAndSave(buffer));
                    buffer.clear();
                }
            }

            if (!buffer.isEmpty()) {
                chunks.add(sortAndSave(buffer));
            }
        }

        return chunks;
    }

    private File sortAndSave(List<String> chunk) throws IOException {
        chunk.sort(comparator);

        File temp = TempFileManager.createTempChunk();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
            for (String line : chunk) {
                bw.write(line);
                bw.newLine();
            }
        }
        return temp;
    }

    private boolean isValidLine(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3)
            return false;

        try {
            LocalTime.parse(parts[0], TIME_FORMATTER);
        } catch (Exception ex) {
            return false;
        }

        return "Start".equalsIgnoreCase(parts[2]) || "End".equalsIgnoreCase(parts[2]);
    }
}
