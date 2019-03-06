import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segmentsField;
    private LineSegmentDescriptor[] descriptors = new LineSegmentDescriptor[4];
    private Point[] points;
    private int size = 0;

    /**
     * Corner cases. Throw a java.lang.IllegalArgumentException if the argument
     * to the constructor is null, if any point in the array is null,
     * or if the argument to the constructor contains a repeated point.
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        this.points = copyOf(points);
        Arrays.sort(this.points);
        doSearch();
        this.points = null;
        segmentsField = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            segmentsField[i] = descriptors[i].lineSegment();
        }
    }

    public int numberOfSegments() {
        return size;
    }

    public LineSegment[] segments() {
        return copyOf(segmentsField);
    }

    private void doSearch() {
        Point basePoint;

        for (int basePointIndex = 0; basePointIndex < points.length; basePointIndex++) {
            basePoint = points[basePointIndex];
            if (basePoint == null)
                throw new IllegalArgumentException();

            for (int endPointIndex = points.length - 1; endPointIndex > basePointIndex; endPointIndex--) {
                LineSegmentDescriptor lineSegment = new LineSegmentDescriptor(basePoint, points[endPointIndex]);

                if (!alreadyHave(lineSegment) && haveFourPointsOnSegment(lineSegment, basePointIndex, endPointIndex))
                    add(lineSegment);

            }
        }
    }

    private LineSegment[] copyOf(LineSegment[] lineSegments) {
        LineSegment[] newSegments = new LineSegment[lineSegments.length];

        for (int i = 0; i < lineSegments.length; i++)
            newSegments[i] = lineSegments[i];
        return newSegments;
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

    private boolean haveFourPointsOnSegment(LineSegmentDescriptor lineSegment, int basePointIndex, int endPointIndex) {
        for (int k = basePointIndex + 1; k < endPointIndex; k++)
            if (lineSegment.containsPoint(points[k]))
                for (int l = k + 1; l < endPointIndex; l++)
                    if (lineSegment.containsPoint(points[l]))
                        return true;

        return false;
    }

    private boolean alreadyHave(LineSegmentDescriptor lineSegment) {
        for (int i = 0; i < size; i++) {
            if (descriptors[i].containsSegment(lineSegment))
                return true;
        }
        return false;
    }

    private void add(LineSegmentDescriptor lineSegmentDescriptor) {

        if (size == descriptors.length)
            resize(size * 2);
        descriptors[size++] = lineSegmentDescriptor;
    }

    private void resize(int newSize) {
        LineSegmentDescriptor[] newSegments = new LineSegmentDescriptor[newSize];
        for (int i = 0; i < size; i++)
            newSegments[i] = descriptors[i];
        descriptors = newSegments;
    }

    private class LineSegmentDescriptor {
        private final Point endPoint;
        private final double slope;
        private final Point basePoint;

        private LineSegmentDescriptor(Point basePoint, Point endPoint) {
            this.slope = basePoint.slopeTo(endPoint);
            if (slope == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException();

            this.basePoint = basePoint;
            this.endPoint = endPoint;
        }

        public boolean containsSegment(LineSegmentDescriptor other) {
            return containsPoint(other.basePoint) && containsPoint(other.endPoint);
        }

        public boolean containsPoint(Point p) {
            return basePoint.equals(p) || basePoint.slopeTo(p) == slope;
        }

        public LineSegment lineSegment() {
            return new LineSegment(basePoint, endPoint);
        }
    }
}
