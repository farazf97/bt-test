package billing.sort;

import java.io.*;

public final class ChunkReader {

    private final BufferedReader reader;

    public ChunkReader(File file) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(file));
    }

    public String read() throws IOException {
        return reader.readLine();
    }

    public void closeQuietly() {
        try {
            reader.close();
        } catch (IOException ignored) {
        }
    }
}
