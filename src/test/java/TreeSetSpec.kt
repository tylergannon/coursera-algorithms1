//import edu.princeton.cs.algs4.Point2D
//import io.kotlintest.matchers.doubles.shouldBeGreaterThanOrEqual
//import io.kotlintest.shouldBe
//import io.kotlintest.specs.StringSpec
//import java.util.regex.Pattern
//
//class PointSetSpec : StringSpec({
//    val WHITESPACE = Pattern.compile("\\s+")
//    "Input10.txt" {
//        val arrayList = ArrayList<Point2D>()
//        val kdTree = KdTree()
//        val pointSet = PointSET()
//        var trimmed: String
//
//        TestUtils.openResource("kdtree", "input10K.txt").forEachLine {
//            trimmed = it.trim()
//            if (trimmed != "")
//                trimmed.split(WHITESPACE).apply {
//                    Point2D(get(0).toDouble(), get(1).toDouble()).apply {
//                        pointSet.insert(this)
//                        kdTree.insert(this)
//                        arrayList.add(this)
//                    }
//                }
//        }
//
//        for (point2d in arrayList) {
//            kdTree.contains(point2d) shouldBe true
//
//        }
//
//        kdTree.size() shouldBe 10000
//
//        arrayList.forEachIndexed { index, point ->
//            kdTree.nearest(point).equals(Point2D(point.x(), point.y())) shouldBe true
//
//            for (anotherPoint in arrayOf(point,
//                    Point2D(point.x() - 0.001, point.y()),
//                    Point2D(point.x(), point.y() - 0.001),
//                    Point2D(point.x() - 0.001, point.y() - 0.001),
//                    Point2D(point.x() + 0.001, point.y()),
//                    Point2D(point.x(), point.y() + 0.001),
//                    Point2D(point.x() + 0.001, point.y() + 0.001))) {
//
//                val nearest = kdTree.nearest(anotherPoint)
//                if (!nearest.equals(point)) {
//                    point.distanceSquaredTo(anotherPoint) shouldBeGreaterThanOrEqual
//                            nearest.distanceSquaredTo(anotherPoint)
//                }
//            }
//        }
//    }
//})