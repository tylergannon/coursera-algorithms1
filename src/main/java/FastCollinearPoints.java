import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

/**
 * Algorithm:
 * <p>
 * Iterate through a reference copy of the Points array, sorted using Point.compareTo().
 * <p>
 * For each item P in the reference copy,
 * * Copy the reference array and sort the working copy according to slope order from P.
 * * If any item Q in the slope-ordered working copy has a Point.compareTo() order that is
 * lower than P's, then skip any line segments containing P and Q,
 * because we can know that we already found the same segment in a previous iteration
 * when Q was the base.
 */
public class FastCollinearPoints {
    private final Stack<LineSegment> lineSegments = new Stack<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        newSearch(copyOf(points));
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return copyOfLineSegments();
    }

    private void newSearch(Point[] pointsReference) {

        Arrays.sort(pointsReference);
        Point[] pointsWorkingCopy = new Point[pointsReference.length];


        if (pointsReference.length <= 1) {
            if (pointsReference.length == 1 && pointsReference[0] == null)
                throw new IllegalArgumentException();

            return;
        }

        for (Point basePoint : pointsReference) {
            copyInto(pointsReference, pointsWorkingCopy);
            Arrays.sort(pointsWorkingCopy, basePoint.slopeOrder());

//            The first item in pointsWorkingCopy will be basePoint.
//            If the second item slope is NEGATIVE_INFINITY, that means
//            there is a duplicate point in the array.

            int segmentStartId = 1;

            boolean segmentContainsPointsBelowBasepoint = basePoint.compareTo(pointsWorkingCopy[segmentStartId]) > 0;
            double currentSlope = basePoint.slopeTo(pointsWorkingCopy[segmentStartId]);
            if (currentSlope == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException();

            for (int cursor = segmentStartId + 1; cursor < pointsWorkingCopy.length; cursor++) {
                double nextSlope;

                boolean cursorRefersToPointBelowBasePoint = basePoint.compareTo(pointsWorkingCopy[cursor]) > 0;

                if ((nextSlope = basePoint.slopeTo(pointsWorkingCopy[cursor])) != currentSlope) {

                    if (cursor - segmentStartId > 2 && !segmentContainsPointsBelowBasepoint) {
                        Arrays.sort(pointsWorkingCopy, segmentStartId, cursor);
                        lineSegments.push(new LineSegment(basePoint, pointsWorkingCopy[cursor - 1]));
                    }

                    segmentStartId = cursor;
                    currentSlope = nextSlope;
                    segmentContainsPointsBelowBasepoint = cursorRefersToPointBelowBasePoint;
                } else {
                    if (cursorRefersToPointBelowBasePoint)
                        segmentContainsPointsBelowBasepoint = true;

                    if (cursor == pointsWorkingCopy.length - 1
                            && cursor - segmentStartId >= 2
                            && !segmentContainsPointsBelowBasepoint) {
                        lineSegments.push(new LineSegment(basePoint, pointsWorkingCopy[cursor]));
                    }
                }
            }
        }
    }

    private void copyInto(Point[] from, Point[] into) {
        for (int i = 0; i < from.length; i++)
            into[i] = from[i];
    }

    private Point[] copyOf(Point[] points) {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            newPoints[i] = points[i];
        }
        return newPoints;
    }

    private LineSegment[] copyOfLineSegments() {
        LineSegment[] theCopy = new LineSegment[lineSegments.size()];
        int i = 0;
        for (LineSegment segment : lineSegments)
            theCopy[i++] = segment;
        return theCopy;
    }
}
