import io.kotlintest.data.forall
import io.kotlintest.fail
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class SolverTest : StringSpec({

    "Three by three boards" {
        val toUse = 1..25

        forall(*toUse.map { row(it) }.toTypedArray()) { moves ->
            val blocks = BoardBuilder.loadBlocks(
                    "puzzle3x3-${moves.toString().padStart(2, '0')}.txt")
            try {
                val board = Board(blocks)


                val solver = Solver(board);
                solver.moves() shouldBe moves
                println("Solved $board\n iterations and $moves moves.")

            } catch (ex: Exception) {
                println(ex)
                ex.stackTrace.forEach {
                    println(it)
                }
                fail("GOT ERROR")
            }

        }
    }


    "Two by two boards"  {
        forall(
                row(0, Board(arrayOf(intArrayOf(1, 2),
                        intArrayOf(3, 0)))),
                row(1, Board(arrayOf(intArrayOf(1, 2),
                        intArrayOf(0, 3)))),
                row(2, Board(arrayOf(intArrayOf(0, 2),
                        intArrayOf(1, 3))))
        ) { moves, board ->
            val solver = Solver(board)
//            for (board in solver.solution())
//                StdOut.println(board);

            solver.isSolvable() shouldBe true
            solver.moves() shouldBe moves
        }
    }
})