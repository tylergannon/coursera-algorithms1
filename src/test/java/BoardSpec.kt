import io.kotlintest.data.forall
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.boolean.shouldNotBeTrue
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class BoardSpec : StringSpec({
    "manhattan foo" {
        val blocks = BoardBuilder.loadBlocks("puzzle27.txt")
        val newBoard = Board(blocks)
        newBoard.manhattan() shouldBe 17

    }

    "It gets twins correctly." {
        var board = Board(BoardBuilder.loadBlocks("puzzle00.txt"))
        board.manhattan() shouldBe 0
        (1..13).forEach {
            board = board.twin();
            board.manhattan() shouldBe (it and 1) * 2
        }
    }

//    "It prints correctly" {
//        println(TestUtils.resourceAsString("8puzzle", "puzzle00.txt"))
//        val board = BoardBuilder.load("puzzle00.txt")
//        board.toString() shouldBe "borth" +
//                ""
//    }


    "It constructs a new board." {
        val blocks = BoardBuilder.loadBlocks("puzzle3x3-${0.toString().padStart(2, '0')}.txt")
        val board = Board(blocks)
        board.dimension() shouldBe 3
        board.hamming() shouldBe 0
        board.manhattan() shouldBe 0
    }

    val board4 = Board(arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(0, 5, 6),
            intArrayOf(4, 7, 8)))

    val board5 = Board(arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(5, 0, 6),
            intArrayOf(4, 7, 8)))

    val board55 = Board(arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(5, 7, 6),
            intArrayOf(4, 0, 8)))

    val board6 = Board(arrayOf(
            intArrayOf(1, 0, 3),
            intArrayOf(5, 2, 6),
            intArrayOf(4, 7, 8)))

    val board7 = Board(arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(5, 6, 0),
            intArrayOf(4, 7, 8)))





    "isGoal should work." {
        val blocks: Array<IntArray> = arrayOf(
                intArrayOf(1, 2, 3),
                intArrayOf(4, 5, 6),
                intArrayOf(7, 8, 0)
        )

        val board = Board(blocks)

        board.isGoal.shouldBeTrue()
        board.twin().isGoal.shouldNotBeTrue()
    }

    "neighbors when 8th square" {
        forall(row(board6), row(board7), row(board55), row(board4)) { board ->
            board5.neighbors().toList().forAtLeastOne { other ->
                other shouldBe (board)
            }
        }
    }

    fun randomBlocks(dim: Int) = (0..(dim * dim - 1)).toList().shuffled().chunked(dim).map { it.toIntArray() }.toTypedArray()

    "Hamming" {
        Board(randomBlocks(5)).hamming() shouldBeGreaterThan 12
    }

    "Make a giant board" {
        val board = Board(randomBlocks(127)).apply {
            dimension() shouldBe 127
            manhattan() shouldBeGreaterThan 1000000
            neighbors()
        }
    }

    "Another neighbor situation" {
        Board(arrayOf(intArrayOf(1, 2),
                intArrayOf(0, 3))).let { board ->
            println(board)
            board.neighbors().toList().let {

                for (board in it) {
                    println(board);
                }
                it.shouldHaveSize(2)

                it.shouldContain(Board(arrayOf(intArrayOf(1, 2),
                        intArrayOf(3, 0))))

                it.shouldContain(Board(arrayOf(intArrayOf(0, 2),
                        intArrayOf(1, 3))))



                it.shouldContainAll(
                        Board(arrayOf(intArrayOf(1, 2),
                                intArrayOf(3, 0))),

                        Board(arrayOf(intArrayOf(0, 2),
                                intArrayOf(1, 3)))
                )
            }
        }
    }

    "neighbors when corner item" {
        forall(
                row(arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 6),
                        intArrayOf(7, 0, 8))),
                row(arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 0),
                        intArrayOf(7, 8, 6)))
        ) { blocks ->

            Board(arrayOf(
                    intArrayOf(1, 2, 3),
                    intArrayOf(4, 5, 6),
                    intArrayOf(7, 8, 0))).neighbors().toList().forAtLeastOne {

                it.equals(Board(blocks)) shouldBe true
            }
        }
    }

    "manhattan" {
        forall(
                row(0, 0, arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 6),
                        intArrayOf(7, 8, 0))),
                row(3, 2, arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 8),
                        intArrayOf(7, 0, 6))),
                row(6, 3, arrayOf(
                        intArrayOf(0, 2, 3),
                        intArrayOf(4, 5, 8),
                        intArrayOf(7, 1, 6)))
        ) { manhattan, hamming, squares: Array<IntArray> ->
            val board = Board(squares)
            board.manhattan() shouldBe manhattan
            board.hamming() shouldBe hamming
        }
    }

})