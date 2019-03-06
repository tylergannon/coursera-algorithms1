import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class CollinearPointsTest {
    private static Stream getPointsArguments() {
        return Stream.of(
                Arguments.of("input6.txt", 1),
                Arguments.of("input9.txt", 1),
                Arguments.of("input10.txt", 2),
                Arguments.of("input20.txt", 5),
                Arguments.of("input40.txt", 4),
                Arguments.of("input50.txt", 7),
                Arguments.of("input100.txt", 62),
                Arguments.of("input200.txt", 4),
                Arguments.of("input400.txt", 7),
                Arguments.of("input8.txt", 2),
                Arguments.of("equidistant.txt", 4)
        );
    }

    private static Stream getFastPointsArguments() {
        return Stream.of(
                Arguments.of("input6.txt", 1),
                Arguments.of("input9.txt", 1),
                Arguments.of("input10.txt", 2),
                Arguments.of("input20.txt", 5),
                Arguments.of("input40.txt", 4),
                Arguments.of("input50.txt", 7),
                Arguments.of("input100.txt", 62),
                Arguments.of("input200.txt", 4),
                Arguments.of("input400.txt", 7),
                Arguments.of("input1000.txt", 0),
                Arguments.of("input2000.txt", 0),
                Arguments.of("input4000.txt", 3),
                Arguments.of("input8000.txt", 15),
                Arguments.of("input10000.txt", 35),
                Arguments.of("input8.txt", 2)
        );
    }

    @ParameterizedTest
    @MethodSource("getPointsArguments")
    void testBruteCollinearPoints(String filename, int expectedSegments) {
        try {
            BruteCollinearPoints collinearPoints = new BruteCollinearPoints(
                    readPoints(filename)
            );

//            StdOut.println("Brute");
//            for (LineSegment segment : collinearPoints.segments())
//                StdOut.println(segment);
//            StdOut.println("Brute End");

            Assertions.assertEquals(expectedSegments,
                    collinearPoints.numberOfSegments(),
                    String.format("Expected %s to have %d segments but got %d",
                            filename,
                            expectedSegments,
                            collinearPoints.numberOfSegments()
                    )
            );
        } catch (IOException ex) {
            Assertions.fail("Got an IOException.");
        }
    }

    @ParameterizedTest
    @MethodSource("getPointsArguments")
    void testFastCollinearPoints(String filename, int expectedSegments) {
        try {
            FastCollinearPoints collinearPoints = new FastCollinearPoints(
                    readPoints(filename)
            );

            if (expectedSegments != collinearPoints.numberOfSegments()) {
                for (LineSegment segment : collinearPoints.segments()) {
                    StdOut.println(segment);
                }
            }

            Assertions.assertEquals(expectedSegments,
                    collinearPoints.numberOfSegments(),
                    String.format("Expected %s to have %d segments but got %d",
                            filename,
                            expectedSegments,
                            collinearPoints.numberOfSegments()
                    )
            );
        } catch (IOException ex) {
            Assertions.fail("Got an IOException.");
        }
    }

    @ParameterizedTest
    @Disabled("Takes too long")
    @MethodSource("getFastPointsArguments")
    void testFastCollinearPointsWithLotsOfData(String filename, int expectedSegments) {
        testFastCollinearPoints(filename, expectedSegments);
    }

    private Point[] readPoints(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        PercolationTest.class.getResourceAsStream("collinear/" + filename)
                )
        )) {

            String line;
            int size = Integer.parseInt(reader.readLine());

            Point[] points = new Point[size];
            for (int i = 0; i < size; i++) {
                line = reader.readLine().trim();
                while (line.equals(""))
                    line = reader.readLine().trim();
                String[] nums = line.split("\\s+");
                points[i] = new Point(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
            }

            return points;
        }

    }
}
