package billing.sort;

import java.io.*;
import java.util.*;

public final class LogFileSorter {

    private final int maxChunkLines;
    private final ChunkWriter chunkWriter;
    private final ChunkMerger merger;

    public LogFileSorter() {
        this(100_000);
    }

    public LogFileSorter(int maxChunkLines) {
        if (maxChunkLines <= 0) {
            throw new IllegalArgumentException("maxChunkLines must be > 0");
        }
        this.maxChunkLines = maxChunkLines;
        this.chunkWriter = new ChunkWriter(this.maxChunkLines, new LogLineComparator());
        this.merger = new ChunkMerger(new LogLineComparator());
    }

    public File sort(File input) throws IOException {
        List<File> chunks = chunkWriter.splitIntoChunks(input);
        try {
            return merger.merge(chunks);
        } finally {
            TempFileManager.deleteFiles(chunks);
        }
    }
}
