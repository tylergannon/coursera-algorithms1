import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int countItemsToPrint = Integer.parseInt(args[0]);
        int itemIndex = 0;
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String nextString = StdIn.readString();
            itemIndex++;
            if (queue.size() < countItemsToPrint)
                queue.enqueue(nextString);
            else if (StdRandom.bernoulli((double) queue.size() / itemIndex)) {
                queue.dequeue();
                queue.enqueue(nextString);
            }
        }

        for (String str : queue)
            StdOut.println(str);

    }
}
