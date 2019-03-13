import edu.princeton.cs.algs4.Point2D
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern
import java.util.stream.Collectors

class TestUtils {
    companion object {
        val WHITESPACE = Pattern.compile("\\s+")

        fun loadPoints(filename: String) = KdTree().apply {
            openResource("kdtree", filename).forEachLine {
                val trimmed = it.trim()
                if (trimmed != "")
                    trimmed.split(WHITESPACE).apply {
                        Point2D(get(0).toDouble(), get(1).toDouble()).apply {
                            insert(this)
                        }
                    }
            }
        }

        fun openResource(dir: String, name: String): BufferedReader =
                TestUtils::class.java.getResourceAsStream("$dir/$name").let { resource ->
                    BufferedReader(InputStreamReader(resource))
                }

        fun resourceAsString(dir: String, name: String): String =
                openResource(dir, name).lines().collect(Collectors.joining("\n"))
    }
}