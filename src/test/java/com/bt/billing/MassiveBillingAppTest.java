package test.java.com.bt.billing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import billing.MassiveBillingApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class MassiveBillingAppTest {

    private static final String LOG_DIR = "src/test/java/com/bt/billing/logs";
    private static final String EXPECTED_OUTPUT_DIR = "src/test/java/com/bt/billing/expected_output";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @ParameterizedTest(name = "Testing file: {0}")
    @MethodSource("logFilesProvider")
    void testExistingLogFile(String fileName) {
        Path filePath = Paths.get(LOG_DIR, fileName);
        Path expectedOutputPath = Paths.get(EXPECTED_OUTPUT_DIR, fileName);

        if (!Files.exists(filePath)) {
            fail("Test file not found: " + filePath.toAbsolutePath() +
                    "\nPlease ensure you have created this file in the 'logs' folder.");
        }

        try {
            MassiveBillingApp.main(new String[] { filePath.toString() });
        } catch (Exception e) {
            fail("App crashed on file " + fileName + ": " + e.getMessage());
        }

        String output = outContent.toString().trim();

        if (fileName.equals("empty.txt")) {
            assertTrue(output.isEmpty(), "empty.txt should produce no output, but got:\n" + output);
            return;
        }

        String expectedContent = "";
        try {
            expectedContent = Files.readString(expectedOutputPath);
        } catch (IOException e) {
            fail("Failed to read expected output file: " + expectedOutputPath.toAbsolutePath());
        }

        assertTrue(output.contains(expectedContent),
                String.format("File '%s' output did not contain expected string '%s'.\nActual Output:\n%s",
                        fileName, expectedContent, output));
    }

    static Stream<Arguments> logFilesProvider() {
        return Stream.of(
                Arguments.of("deep_nesting.txt"),
                Arguments.of("interleaved.txt"),
                Arguments.of("only_ends.txt"),
                Arguments.of("only_starts.txt"),
                Arguments.of("single_line.txt"),
                Arguments.of("garbage.txt"),
                Arguments.of("empty.txt"),
                Arguments.of("many_users.txt"),
                Arguments.of("huge.txt"));
    }
}