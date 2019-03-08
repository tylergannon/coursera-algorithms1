import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

class PercolationTest {
    private static Stream backwashTestArgs() {
        return Stream.of(
                Arguments.of("input20.txt", 20, 231, 18, 1),
                Arguments.of("jerry47.txt", 47, 1076, 11, 47)
        );
    }

    private static Stream getFileDetails() {
        return Stream.of(
                Arguments.of("input1.txt", true, 1, 1),
                Arguments.of("input2.txt", true, 3, 2),
                Arguments.of("input2-no.txt", false, 2, 2),
                Arguments.of("input4.txt", true, 8, 4),
                Arguments.of("input5.txt", true, 25, 5),
                Arguments.of("input6.txt", true, 18, 6),
                Arguments.of("input7.txt", true, 16, 7),
                Arguments.of("input8-dups.txt", true, 34, 8),
                Arguments.of("input8-no.txt", false, 33, 8),
                Arguments.of("input10.txt", true, 56, 10),
                Arguments.of("input20.txt", true, 250, 20),
                Arguments.of("input50.txt", true, 1412, 50),
                Arguments.of("snake101.txt", true, 5101, 101)
        );
    }

    @ParameterizedTest()
    @MethodSource("getFileDetails")
    @Disabled
    void testInputFiles(String fname,
                        boolean shouldPercolate,
                        int numOpenSites,
                        int size) {
        Percolation percolation = null;
        try {
            percolation = buildPercolation(fname, 0);
        } catch (IOException e) {
            Assertions.fail("Got an IOException.");
        }
        if (shouldPercolate != percolation.percolates()) {
            PercolationVisualizer.draw(percolation, size);
            StdDraw.save("./build/" + fname + ".png");
        }
        Assertions.assertEquals(shouldPercolate, percolation.percolates());
        Assertions.assertEquals(numOpenSites, percolation.numberOfOpenSites());
    }

    @ParameterizedTest
    @MethodSource("backwashTestArgs")
    @Disabled
    void testBackwash(String filename, int size, int sites, int row, int col) {
        Percolation percolation = null;
        try {
            percolation = buildPercolation(filename, sites);
        } catch (IOException ex) {
            Assertions.fail("Got an IOException.");
        }
        if (percolation.isFull(row, col)) {
            PercolationVisualizer.draw(percolation, size);
            StdDraw.save("./build/" + filename + ".png");
        }
        Assertions.assertFalse(percolation.isFull(row, col));
    }

    private Percolation buildPercolation(String fileName, int lines) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        PercolationTest.class.getResourceAsStream("percolation/" + fileName)
                )
        )) {
            String line;
            int size = Integer.parseInt(reader.readLine());
            Percolation perc = new Percolation(size);
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals(""))
                    continue;


                String[] nums = line.split("\\s+");
                perc.open(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
                if (lines > 0 && lineNumber >= lines)
                    break;
                lineNumber++;
            }
            return perc;
        }
    }
}
