import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.Deque;

public class Solver {
    private static final int MAX_ITERATIONS = 100000;

    private boolean isSolvable = false;
    private Deque<Board> solution = new java.util.ArrayDeque<>();
    private int iterations = 0;

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
        int maxQueueSize = 0;
        while (true) {
            ++iterations;

            GameNode node = playTheGame(myPQ, true);
            if (myPQ.size() > maxQueueSize)
                maxQueueSize = myPQ.size();
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

    private static int minHamming(Iterable<Board> boards) {
        int theVal = Integer.MAX_VALUE;
        int currentVal;
        for (Board board : boards)
            if ((currentVal = board.manhattan()) < theVal)
                theVal = currentVal;
        return theVal;
    }

    private void buildSolution(GameNode node) {
        solution.addLast(node.theBoard);
        while ((node = node.previous) != null)
            solution.addFirst(node.theBoard);
    }

//   1 2 0 3  -->  0 2 1 3 , 1 2 3 0

    private GameNode playTheGame(MinPQ<GameNode> queue, boolean keepHistory) {
        GameNode node = queue.delMin();

        for (Board board : node.theBoard.neighbors()) {
            int manhattan = board.manhattan();
            if (node.previous != null
                    && manhattan == node.previous.manhattan()
                    && board.hamming() == node.previous.theBoard.hamming()
                    && board.equals(node.previous.theBoard)) {
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
        private static final int HAMMING_MASK = 0xFF00;
        private static final int STEP_MASK = 0xFF;
        private int data;
        private Board theBoard;
        private GameNode previous;

        private GameNode(GameNode previous, int stepNumber, int manhattan, Board board) {
            this.previous = previous;
            data = ((manhattan + stepNumber << BITS) + manhattan << (BITS * 2)) + stepNumber;

            theBoard = board;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GameNode) {
                GameNode other = (GameNode) obj;
                return other.stepNumber() == stepNumber() && other.manhattan() == manhattan()
                        && other.theBoard.equals(theBoard);
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
