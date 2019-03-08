import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class FastCollinearPointsTest {

    static Stream makeGrids() {
        return Stream.of(
                Arguments.of(128, 1024),
                Arguments.of(256, 2048)
        );
    }

    @Test
    @Disabled
    void testOne() {
        Point[] points = new Point[]{
                new Point(3, 9),
                new Point(8, 4),
                new Point(6, 6),
                new Point(3, 10),
                new Point(3, 22),
                new Point(3, 4),
                new Point(123, 10),
                new Point(5, 7),
                new Point(4, 8),
                new Point(9, 3)
        };

//        StdRandom.shuffle(points, 1, points.length);
//        Arrays.sort(points, 1, points.length, points[0].slopeOrder());
        StdOut.println("Dauce");
        for (Point p : points)
            StdOut.println(p);

        FastCollinearPoints p = new FastCollinearPoints(points);
        for (LineSegment l : p.segments())
            StdOut.println(l);

    }

    @ParameterizedTest
    @MethodSource("makeGrids")
    @Disabled
    void testBuildGiantGridWithFast(int gridSize, int numPoints) {
        Point[] points = new Point[gridSize * gridSize];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(i % gridSize, i / gridSize);
        }
        StdRandom.shuffle(points);
        Point[] pointsToUse = new Point[numPoints];
        for (int i = 0; i < numPoints; i++)
            pointsToUse[i] = points[i];

        FastCollinearPoints fast = new FastCollinearPoints(pointsToUse);

        for (LineSegment l : fast.segments())
            StdOut.println("Fast " + l.toString());

        Assertions.assertTrue(fast.numberOfSegments() > 0);

    }

    @ParameterizedTest
    @MethodSource("makeGrids")
    @Disabled
    void testCompareLargeGridToBrute(int gridSize, int numPoints) {
        Point[] points = new Point[gridSize * gridSize];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(i % gridSize, i / gridSize);
        }
        StdRandom.shuffle(points);
        Point[] pointsToUse = new Point[numPoints];
        for (int i = 0; i < numPoints; i++)
            pointsToUse[i] = points[i];

        BruteCollinearPoints brute = new BruteCollinearPoints(pointsToUse);
        FastCollinearPoints fast = new FastCollinearPoints(pointsToUse);
        for (LineSegment l : brute.segments())
            StdOut.println("Brute " + l.toString());

        for (LineSegment l : fast.segments())
            StdOut.println("Fast " + l.toString());

        Assertions.assertEquals(brute.numberOfSegments(), fast.numberOfSegments());

    }
}
