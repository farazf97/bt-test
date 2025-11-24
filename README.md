# Fair Billing Solution

An enterprise-grade, memory-efficient Java application for calculating user session durations from massive log files.

## Overview

This application processes log files containing `Start` and `End` session events. It calculates the total session duration and number of sessions for each user.

### Key Features
- **Massive File Support**: Designed to handle files larger than available RAM using an external merge sort algorithm.
- **Memory Efficient**: Streaming processing ensures constant memory usage regardless of input size.
- **Robust**: Gracefully handles malformed lines, out-of-order records, and missing start/end events.
- **Clean Architecture**: Implements **Builder Pattern** for configuration, **TDD** approach for testing.

## Architecture

The solution uses a three-stage pipeline:

1.  **Scanner (`LogFileScanner`)**:
    -   First pass to determine the global earliest and latest timestamps.
    -   Crucial for handling "open" sessions (missing Start or End) by bounding them to the file's time range.

2.  **Sorter (`LogFileSorter`)**:
    -   Splits the massive input file into manageable chunks.
    -   Sorts each chunk in memory and saves to temporary files.
    -   Merges sorted chunks into a single sorted stream.
    -   **Pattern**: Uses the **Builder Pattern** (`SorterConfiguration`) for flexible configuration of chunk sizes and comparators.

3.  **Calculator (`BillingCalculator`)**:
    -   Processes the sorted stream sequentially.
    -   Aggregates session durations for each user.
    -   Produces a `BillingReport`.

## Prerequisites

-   Java 21 or higher
-   JUnit 5 (provided in `lib/junit-standalone.jar`)

## Build & Run

### 1. Compile
Compile the source code and tests:
```bash
javac -d bin -cp lib/junit-standalone.jar src/**/*.java
```

### 2. Run Application
Run the application with a log file:
```bash
java -cp bin billing.MassiveBillingApp src/test/java/com/bt/billing/logs/huge.txt
```

**Output Format:**
```text
<username> <session_count> <total_seconds>
```

### 3. Run Tests
Execute the test suite using the standalone JUnit runner:
```bash
java -jar lib/junit-standalone.jar -cp bin -c test.java.com.bt.billing.MassiveBillingAppTest
```

## Design Decisions

-   **File based merge sort**: Chosen over in-memory sorting, allows processing files larger than available RAM (even Terabytes in size) and 1 billion+ users. Using HashMap will only be good for smaller work loads and will throw OutOfMemoryError when processing large files or when there are too many users (~200 Bytes per user, for 1 billion users it's 200 GB). This solution achieves O(n log n) Time complexity, O(n) Space complexity, scales to Terabytes and over Billion users.

-   **Builder Pattern**: Applied to `LogFileSorter` to allow easy extension of configuration parameters (e.g., buffer size, temp directory) without polluting constructors.
