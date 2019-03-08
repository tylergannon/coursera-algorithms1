import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class BitmaskTest : StringSpec({
    "Placing a five-bit number into the 4th-8th bit of an integer" {
        val lowValFourBits: Int = 14;
        val myValFiveBits: Int = 30;  // five bits
        val highValSixBits: Int = 40;

        var data: Int = highValSixBits
        data = data shl 5;
        data += myValFiveBits;
        data = data shl 4;
        data += lowValFourBits;

        ((data and 0b111110000) shr 4) shouldBe myValFiveBits
        (data shr 9) shouldBe highValSixBits
        data and 0b1111 shouldBe lowValFourBits
    }

//    "Game node member attributes" {
//        val board = Board(BoardBuilder.loadBlocks("puzzle3x3-${0.toString().padStart(2, '0')}.txt"))
//        val solver = Solver(board)
//        val node = solver.GameNode(null, 14, board.manhattan(), board)
//        node.hamming() shouldBe board.hamming()
//        node.manhattanPriority() shouldBe (14 + board.manhattan())
//        node.manhattan() shouldBe board.manhattan()
//        node.stepNumber() shouldBe 14
//    }
})