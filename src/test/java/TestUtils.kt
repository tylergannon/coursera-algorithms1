import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class TestUtils {
    companion object {
        fun openResource(dir: String, name: String): BufferedReader =
                TestUtils::class.java.getResourceAsStream("$dir/$name").let { resource ->
                    BufferedReader(InputStreamReader(resource))
                }

        fun resourceAsString(dir: String, name: String): String =
                openResource(dir, name).lines().collect(Collectors.joining("\n"))
    }
}