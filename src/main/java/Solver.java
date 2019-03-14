import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.Deque;

public class Solver {

    private boolean isSolvable = false;
    private Deque<Board> solution = new java.util.ArrayDeque<>();

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        if (initial.isGoal()) {
            isSolvable = true;
            solution.push(initial);
            return;
        }

        MinPQ<GameNode> myPQ = new MinPQ<>(new ManhattanComparator());
        MinPQ<GameNode> twinPQ = new MinPQ<>(new ManhattanComparator());

        myPQ.insert(new GameNode(null, 0, initial.manhattan(), initial));
        Board twin = initial.twin();
        twinPQ.insert(new GameNode(null, 0, twin.manhattan(), twin));
        while (true) {

            GameNode node = playTheGame(myPQ, true);

            if (node != null) {
                buildSolution(node);
                isSolvable = true;
                break;
            }
            node = playTheGame(twinPQ, false);
            if (node != null)
                break;

        }
    }

    public int moves() {
        return isSolvable ? solution.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return isSolvable ? solution : null;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    private void buildSolution(GameNode node) {
        solution.addLast(node.board);
        while ((node = node.previous) != null)
            solution.addFirst(node.board);
    }

//   1 2 0 3  -->  0 2 1 3 , 1 2 3 0

    private GameNode playTheGame(MinPQ<GameNode> queue, boolean keepHistory) {
        GameNode node = queue.delMin();

        for (Board board : node.board.neighbors()) {
            int manhattan = board.manhattan();
            if (node.previous != null
                    && manhattan == node.previous.manhattan()
                    && board.hamming() == node.previous.board.hamming()
                    && board.equals(node.previous.board)) {
                continue;
            }

            if (!keepHistory)
                node.previous = null;

            GameNode newNode = new GameNode(node, node.stepNumber() + 1, manhattan, board);
            if (board.isGoal())
                return newNode;
            else
                queue.insert(newNode);
        }
        return null;
    }

    private class ManhattanComparator implements Comparator<GameNode> {
        @Override
        public int compare(GameNode o1, GameNode o2) {
            int comp = Integer.compare(o1.manhattanPriority(), o2.manhattanPriority());
            if (comp == 0)
                comp = Integer.compare(o1.manhattan(), o2.manhattan());

            return comp;
        }
    }

    /****
     * Step number:
     * 1111111000000000
     */
//    public static int count = 0;
    private class GameNode {
        private static final int BITS = 8;
        private static final int MANHATTAN_BITS = BITS * 2;
        private static final int MANHATTAN_PRIORITY_BITS = BITS + MANHATTAN_BITS;
        private static final int MANHATTAN_MASK = 0xFF0000;
        private static final int STEP_MASK = 0xFF;
        private int data;
        private Board board;
        private GameNode previous;

        private GameNode(GameNode previous, int stepNumber, int manhattan, Board board) {
            this.previous = previous;
            data = ((manhattan + stepNumber << BITS) + manhattan << (BITS * 2)) + stepNumber;

            this.board = board;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GameNode) {
                GameNode other = (GameNode) obj;
                return other.stepNumber() == stepNumber() && other.manhattan() == manhattan()
                        && other.board.equals(board);
            } else return false;
        }

        public int stepNumber() {
            return data & STEP_MASK;
        }

        public int manhattan() {
            return (data & MANHATTAN_MASK) >> MANHATTAN_BITS;
        }

        public int manhattanPriority() {
            return data >> MANHATTAN_PRIORITY_BITS;
        }
    }
}
