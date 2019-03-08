import java.util.regex.Pattern

class BoardBuilder {
    companion object {
        fun loadBlocks(filename: String) = TestUtils.resourceAsString("8puzzle", filename).let { parseBlocks(it) }

        fun load(filename: String) = loadBlocks(filename).let { Board(it) }

        fun parseBlocks(str: String) = str.split("\n").map { it.trim() }.filter { it.isNotBlank() }.let {
            it.subList(1, it.size)
        }.map { it.split(Pattern.compile("\\s+")).map { it.toInt() }.toIntArray() }.toTypedArray()
    }
}