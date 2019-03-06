import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {
    @Test
    void testPointSlopeEquality() {
        Point p = new Point(2, 9);
        Point q = new Point(11, -5);
        Assertions.assertEquals(p.slopeTo(q), q.slopeTo(p));
    }

    @Test
    void testCorrectnessOfPointSlope() {
        Point p = new Point(19000, 10000);
        Point q = new Point(18000, 10000);
        Point r = new Point(1234, 5678);
//        Point s = new Point(14000, 10000);

        double slope = p.slopeTo(q);
        Assertions.assertNotEquals(slope, p.slopeTo(r));

    }
}
