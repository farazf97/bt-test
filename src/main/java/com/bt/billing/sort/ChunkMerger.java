package billing.sort;

import java.io.*;
import java.util.*;

public final class ChunkMerger {

    private final Comparator<String> comparator;

    public ChunkMerger(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    public File merge(List<File> chunks) throws IOException {
        if (chunks.isEmpty()) {
            return TempFileManager.createEmptyOutput();
        }

        PriorityQueue<MergeEntry> heap = new PriorityQueue<>(Comparator.comparing(e -> e.line, comparator));

        List<ChunkReader> readers = new ArrayList<>();

        try {
            for (File f : chunks) {
                ChunkReader reader = new ChunkReader(f);
                readers.add(reader);

                String line = reader.read();
                if (line != null) {
                    heap.add(new MergeEntry(line, reader));
                }
            }

            File output = TempFileManager.createOutput();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                while (!heap.isEmpty()) {
                    MergeEntry e = heap.poll();
                    writer.write(e.line);
                    writer.newLine();

                    String next = e.reader.read();
                    if (next != null) {
                        heap.add(new MergeEntry(next, e.reader));
                    }
                }
            }

            return output;
        } finally {
            readers.forEach(ChunkReader::closeQuietly);
        }
    }

    private static final class MergeEntry {
        final String line;
        final ChunkReader reader;

        MergeEntry(String line, ChunkReader reader) {
            this.line = line;
            this.reader = reader;
        }
    }
}
