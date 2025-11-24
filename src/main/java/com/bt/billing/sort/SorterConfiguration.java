package billing.sort;

import java.util.Comparator;

public class SorterConfiguration {

    private final int maxChunkLines;
    private final Comparator<String> comparator;

    private SorterConfiguration(Builder builder) {
        this.maxChunkLines = builder.maxChunkLines;
        this.comparator = builder.comparator;
    }

    public int getMaxChunkLines() {
        return maxChunkLines;
    }

    public Comparator<String> getComparator() {
        return comparator;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int maxChunkLines = 100_000;
        private Comparator<String> comparator = new LogLineComparator();

        public Builder maxChunkLines(int maxChunkLines) {
            this.maxChunkLines = maxChunkLines;
            return this;
        }

        public Builder comparator(Comparator<String> comparator) {
            this.comparator = comparator;
            return this;
        }

        public SorterConfiguration build() {
            return new SorterConfiguration(this);
        }
    }
}
