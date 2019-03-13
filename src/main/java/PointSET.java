import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points = new TreeSet<>(new PointComparator());

    public boolean isEmpty() {
        return points.size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();
        points.add(point);
    }

    public boolean contains(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();
        return points.contains(point);
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<>();

        Point2D lowEnd = points.ceiling(new Point2D(rect.xmin(), rect.ymin()));
        Point2D highEnd = points.floor(new Point2D(rect.xmax(), rect.ymax()));

        if (lowEnd == null || highEnd == null || lowEnd.compareTo(highEnd) > 0)
            return list;

        for (Point2D point : points.subSet(lowEnd, true,
                highEnd, true))
            if (rect.contains(point))
                list.add(point);
        return list;
    }

    public Point2D nearest(Point2D base) {
        if (base == null)
            throw new IllegalArgumentException();
        Point2D nearest = null;
        double shortestDistance = Double.MAX_VALUE;
        for (Point2D other : points) {
            double distance = base.distanceTo(other);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = other;
            }
        }
        return nearest;
    }

    private class PointComparator implements Comparator<Point2D> {

        @Override
        public int compare(Point2D o1, Point2D o2) {
            int comp = Double.compare(o1.y(), o2.y());
            if (comp == 0)
                comp = Double.compare(o1.x(), o2.x());
            return comp;
        }
    }
}
