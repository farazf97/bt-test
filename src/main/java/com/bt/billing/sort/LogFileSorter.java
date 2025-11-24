package billing.sort;

import java.io.*;
import java.util.*;

public final class LogFileSorter {

    private final int maxChunkLines;
    private final ChunkWriter chunkWriter;
    private final ChunkMerger merger;

    public LogFileSorter() {
        this(SorterConfiguration.builder().build());
    }

    public LogFileSorter(SorterConfiguration config) {
        if (config.getMaxChunkLines() <= 0) {
            throw new IllegalArgumentException("maxChunkLines must be > 0");
        }
        this.maxChunkLines = config.getMaxChunkLines();
        this.chunkWriter = new ChunkWriter(this.maxChunkLines, config.getComparator());
        this.merger = new ChunkMerger(config.getComparator());
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
