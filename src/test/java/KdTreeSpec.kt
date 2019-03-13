import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.RectHV
import edu.princeton.cs.algs4.StdDraw
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class KdTreeSpec : DescribeSpec({
//    describe("range") {
//        context("five points") {
//            it("Should have one thingy") {
//
//                val kdTree = TestUtils.loadPoints("input5.txt")
//                val rect = RectHV(0.399, 0.496, 0.425, 0.904)
//                val range = kdTree.range(rect).toList()
//                StdDraw.clear()
//                kdTree.draw()
//                rect.draw()
//                StdDraw.save("build/input5.txt.png")
//                range.size shouldBe 1
//            }
//        }
//    }

    describe("nearest") {
//        context("ten points") {
//            it("Should not touch points in the wrong bounding box.") {
//                StdDraw.clear()
//                val kdTree = TestUtils.loadPoints("input10-test.txt")
//                kdTree.draw()
//                val nearest = kdTree.nearest(Point2D(0.6875, 0.625))
//                StdDraw.setPenColor(StdDraw.BOOK_RED)
//                StdDraw.setPenRadius(0.02)
//                Point2D(0.6875, 0.625).draw()
//                StdDraw.setPenColor(StdDraw.GREEN)
//                nearest.draw()
//
//                StdDraw.save("build/input10-test.txt.png")
//            }
//        }
        context("twenty points") {
            it("Should not touch points in the wrong bounding box.") {
                StdDraw.clear()
                val kdTree = TestUtils.loadPoints("input20-test.txt")
                kdTree.draw()
                val point = Point2D(0.46875, 0.34375)
                val nearest = kdTree.nearest(point)
                StdDraw.setPenColor(StdDraw.BOOK_RED)
                StdDraw.setPenRadius(0.02)
                point.draw()
                StdDraw.setPenColor(StdDraw.GREEN)
                nearest.draw()

                StdDraw.save("build/input10-test.txt.png")
                nearest.equals(Point2D(0.34375, 0.375)) shouldBe true
            }
        }
        context("five points points") {
            it("Should get the correct nearest point.") {
                StdDraw.clear()
                val filename = "input5-test1.txt"
                val kdTree = TestUtils.loadPoints(filename)
                kdTree.draw()
                val point = Point2D(0.217, 0.51)
                val nearest = kdTree.nearest(point)
                StdDraw.setPenColor(StdDraw.BOOK_RED)
                StdDraw.setPenRadius(0.02)
                point.draw()
                StdDraw.setPenColor(StdDraw.GREEN)
                nearest.draw()

                StdDraw.save("build/$filename.png")
                nearest.equals(Point2D(0.2, 0.3)) shouldBe true
            }
        }
    }
})