import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;

/***
 * Data structure for quickly locating points on a plane.
 *
 *
 */
public class KdTree {

    private Node root = null;
    private HorizontalDistanceCalculator horizontalDistanceCalculator;

    public KdTree() {
        horizontalDistanceCalculator = new HorizontalDistanceCalculator();
        VerticalDistanceCalculator verticalDistanceCalculator = new VerticalDistanceCalculator();
        horizontalDistanceCalculator.other = verticalDistanceCalculator;
        verticalDistanceCalculator.other = horizontalDistanceCalculator;
    }

    /****
     *
     * Finds the the point P nearest to a given point X, which is stored on the accumulator object.
     *
     * To understand the algorithm here, it's important to note the alternating OneDimensionalCalculator objects,
     * one of which manipulates distances along the X axis, and the other along the Y axis.
     *
     * The boundingBox is split along the X or Y axis through node.point, according to the comparator, which alternates
     * at each depth of recursion.
     *
     * Always recurse to the node that's on the same side of the axis through this node as the query point.
     * If the current shortest-known distance (squared) is farther than the distance (squared) from the
     * query point to our current axis:
     *
     *  * Determine the bounding box for the other node, and recurse to that node if the new bounding box has a corner
     *      or side that's closer to the query point than the current nearest.
     *      This makes use of the (perhaps poorly named) method axialDistanceSquared(Point2D, RectHV), which returns
     *      zero if the query point is between the two horizontal or vertical lines of the bounding box (according
     *      to which axis is being measured.
     *
     * @param node Node containing points to check
     * @param accumulator Object for accumulating the nearest point.
     * @param comparator Vertical comparator or horizontal comparator
     * @param boundingBox RectHV representing the possible area that points under @node might fall in.
     */
    private static void nearest(Node node, NearestPointAccumulator accumulator, OneDimensionalCalculator comparator, RectHV boundingBox) {
        if (node == null)
            return;

        double axialDistance = comparator.axialDistance(accumulator.basePoint, node.point);
        double axialDistanceSquared = Math.pow(axialDistance, 2.0);

        if (accumulator.distanceSquared >= axialDistanceSquared)
            accumulator.tryAndPut(node.point,
                    axialDistanceSquared + comparator.other().axialDistanceSquared(node.point, accumulator.basePoint));

        if (axialDistance <= 0) {
            nearest(node.leftBottom, accumulator, comparator.other(), comparator.leftBottomBoundingBox(node.point, boundingBox));

            if (accumulator.distanceSquared > axialDistanceSquared) {
                RectHV newBox = comparator.rightTopBoundingBox(node.point, boundingBox);
                double distanceSquaredFromBasePointToBoundingBox =
                        comparator.other().axialDistanceSquared(accumulator.basePoint, newBox) + axialDistanceSquared;

                if (accumulator.distanceSquared > distanceSquaredFromBasePointToBoundingBox)
                    nearest(node.rightTop, accumulator, comparator.other(), newBox);
            }

        } else {
            nearest(node.rightTop, accumulator, comparator.other(), comparator.rightTopBoundingBox(node.point, boundingBox));

            if (accumulator.distanceSquared > axialDistanceSquared) {
                RectHV newBox = comparator.leftBottomBoundingBox(node.point, boundingBox);
                double distanceSquaredFromBasePointToBoundingBox =
                        comparator.other().axialDistanceSquared(accumulator.basePoint, newBox) + axialDistanceSquared;

                if (accumulator.distanceSquared > distanceSquaredFromBasePointToBoundingBox)
                    nearest(node.leftBottom, accumulator, comparator.other(), newBox);
            }
        }

    }

    private static boolean contains(Node node, Point2D point, OneDimensionalCalculator comparator) {
        if (node == null)
            return false;

        double comp = comparator.compare(point, node.point);

        if (comp <= 0) {
            if (comp == 0 && comparator.other().compare(point, node.point) == 0)
                return true;
            else
                return contains(node.leftBottom, point, comparator.other());
        } else {
            return contains(node.rightTop, point, comparator.other());
        }
    }

    private static int size(Node node) {
        if (node == null)
            return 0;
        else
            return node.size;
    }

    public void draw() {
        draw(root, new RectHV(0.0, 0.0, 1.0, 1.0), horizontalDistanceCalculator);
    }

    public int size() {
        return size(root);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        ArrayList<Point2D> points = new ArrayList<>();
        buildRange(root, points, rect, horizontalDistanceCalculator);
        return points;
    }

    public Point2D nearest(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();

        if (root == null)
            return null;

        NearestPointAccumulator accumulator = new NearestPointAccumulator(point);

        nearest(root, accumulator, horizontalDistanceCalculator, new RectHV(0, 0, 1, 1));
        return accumulator.nearestPoint;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();
        return contains(root, point, horizontalDistanceCalculator);
    }

    public void insert(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();

        root = put(root, point, horizontalDistanceCalculator);
    }

    private void buildRange(Node node, ArrayList<Point2D> points, RectHV rect, OneDimensionalCalculator comparator) {
        if (node == null)
            return;

        int comp = comparator.rectCompare(node.point, rect);
        if (comp == 0) {
            if (comparator.other().rectCompare(node.point, rect) == 0)
                points.add(node.point);
            buildRange(node.leftBottom, points, rect, comparator.other());
            buildRange(node.rightTop, points, rect, comparator.other());
        } else if (comp < 0) {
            buildRange(node.rightTop, points, rect, comparator.other());
        } else {
            buildRange(node.leftBottom, points, rect, comparator.other());
        }
    }

    private Node put(Node node, Point2D point, OneDimensionalCalculator comparator) {
        if (node == null)
            return new Node(point);

        int comp = comparator.compare(point, node.point);

        if (comp <= 0) {
            if (comp != 0 || comparator.other().compare(point, node.point) != 0)
                node.leftBottom = put(node.leftBottom, point, comparator.other());
        } else {
            node.rightTop = put(node.rightTop, point, comparator.other());
        }
        node.size = 1 + size(node.leftBottom) + size(node.rightTop);
        return node;
    }

    private void draw(Node node, RectHV boundingBox, OneDimensionalCalculator comparator) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();
        comparator.drawLineThroughPoint(node.point, boundingBox);
        if (node.leftBottom != null)
            draw(node.leftBottom, comparator.leftBottomBoundingBox(node.point, boundingBox), comparator.other());
        if (node.rightTop != null)
            draw(node.rightTop, comparator.rightTopBoundingBox(node.point, boundingBox), comparator.other());
    }

    private interface OneDimensionalCalculator extends Comparator<Point2D> {
        OneDimensionalCalculator other();

        int rectCompare(Point2D point, RectHV rect);

        double axialDistance(Point2D from, Point2D to);

        double axialDistanceSquared(Point2D p1, Point2D p2);

        double axialDistanceSquared(Point2D point, RectHV boundingBox);

        void drawLineThroughPoint(Point2D point, RectHV boundingBox);

        RectHV leftBottomBoundingBox(Point2D point, RectHV boundingBox);

        RectHV rightTopBoundingBox(Point2D point, RectHV boundingBox);
    }

    private static class HorizontalDistanceCalculator implements OneDimensionalCalculator {
        private OneDimensionalCalculator other = null;

        @Override
        public OneDimensionalCalculator other() {
            return other;
        }

        @Override
        public int rectCompare(Point2D point, RectHV rect) {
            if (point.x() < rect.xmin())
                return -1;
            else if (point.x() > rect.xmax())
                return 1;
            else
                return 0;
        }

        @Override
        public double axialDistance(Point2D from, Point2D to) {
            return from.x() - to.x();
        }

        @Override
        public double axialDistanceSquared(Point2D p1, Point2D p2) {
            return Math.pow(p1.x() - p2.x(), 2);
        }

        @Override
        public double axialDistanceSquared(Point2D point, RectHV boundingBox) {
            if (point.x() < boundingBox.xmin())
                return Math.pow(point.x() - boundingBox.xmin(), 2);
            else if (point.x() > boundingBox.xmax())
                return Math.pow(point.x() - boundingBox.xmax(), 2);
            else
                return 0;
        }

        @Override
        public void drawLineThroughPoint(Point2D point, RectHV boundingBox) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(point.x(), boundingBox.ymin()).drawTo(new Point2D(point.x(), boundingBox.ymax()));
        }

        @Override
        public RectHV leftBottomBoundingBox(Point2D point, RectHV boundingBox) {
            return new RectHV(boundingBox.xmin(), boundingBox.ymin(), point.x(), boundingBox.ymax());
        }

        @Override
        public RectHV rightTopBoundingBox(Point2D point, RectHV boundingBox) {
            return new RectHV(point.x(), boundingBox.ymin(), boundingBox.xmax(), boundingBox.ymax());
        }

        @Override
        public int compare(Point2D o1, Point2D o2) {
            return Double.compare(o1.x(), o2.x());
        }
    }

    private static class VerticalDistanceCalculator implements OneDimensionalCalculator {
        private OneDimensionalCalculator other = null;

        @Override
        public int compare(Point2D o1, Point2D o2) {
            return Double.compare(o1.y(), o2.y());
        }

        @Override
        public OneDimensionalCalculator other() {
            return other;
        }

        @Override
        public int rectCompare(Point2D point, RectHV rect) {
            if (point.y() < rect.ymin())
                return -1;
            else if (point.y() > rect.ymax())
                return 1;
            else
                return 0;
        }

        @Override
        public double axialDistance(Point2D from, Point2D to) {
            return from.y() - to.y();
        }

        @Override
        public double axialDistanceSquared(Point2D p1, Point2D p2) {
            return Math.pow(p1.y() - p2.y(), 2);
        }

        @Override
        public double axialDistanceSquared(Point2D point, RectHV boundingBox) {
            if (point.y() < boundingBox.ymin())
                return Math.pow(point.y() - boundingBox.ymin(), 2);
            else if (point.y() > boundingBox.ymax())
                return Math.pow(point.y() - boundingBox.ymax(), 2);
            else
                return 0;
        }

        @Override
        public void drawLineThroughPoint(Point2D point, RectHV boundingBox) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(boundingBox.xmin(), point.y()).drawTo(new Point2D(boundingBox.xmax(), point.y()));
        }

        @Override
        public RectHV leftBottomBoundingBox(Point2D point, RectHV boundingBox) {
            return new RectHV(boundingBox.xmin(), boundingBox.ymin(), boundingBox.xmax(), point.y());
        }

        @Override
        public RectHV rightTopBoundingBox(Point2D point, RectHV boundingBox) {
            return new RectHV(boundingBox.xmin(), point.y(), boundingBox.xmax(), boundingBox.ymax());
        }
    }

    private static class NearestPointAccumulator {
        Point2D basePoint;
        Point2D nearestPoint = null;
        double distanceSquared = Double.MAX_VALUE;

        NearestPointAccumulator(Point2D basePoint) {
            this.basePoint = basePoint;
        }

        void tryAndPut(Point2D point) {
            tryAndPut(point, point.distanceSquaredTo(basePoint));
        }

        void tryAndPut(Point2D point, double thisDistanceSquared) {
            if (thisDistanceSquared < distanceSquared) {
                nearestPoint = point;
                distanceSquared = thisDistanceSquared;
            }
        }
    }

    private static class Node {
        int size = 1;
        Point2D point;
        Node leftBottom = null;
        Node rightTop = null;

        Node(Point2D point) {
            this.point = point;
        }
    }

}
