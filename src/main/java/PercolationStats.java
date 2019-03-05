import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int size;
    private final double _mean;
    private final double _stddev;
    private final double _confidenceLo;
    private final double _confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        size = n;
        int arraySize = n * n;
        int[] order = new int[arraySize];
        double[] trialData = new double[trials];
        int sitesOpen;

        for (int trial = 0; trial < trials; trial++) {
            sitesOpen = 0;
//            Build a randomized list of sites to open.
            for (int i = 0; i < arraySize; i++)
                order[i] = i;

            StdRandom.shuffle(order);

            Percolation percolation = new Percolation(n);
            for (; sitesOpen < arraySize; sitesOpen++) {
                percolation.open(row(order[sitesOpen]), col(order[sitesOpen]));

                if (percolation.percolates()) {
                    trialData[trial] = (double) percolation.numberOfOpenSites() / (double) arraySize;
                    break;
                }
            }
        }

        _mean = StdStats.mean(trialData);
        _stddev = StdStats.stddev(trialData);
        _confidenceHi = _mean + 1.96 * _stddev / Math.sqrt(trials);
        _confidenceLo = _mean - 1.96 * _stddev / Math.sqrt(trials);
    }

    // test client (described below)
    public static void main(String[] args) {
//    Implemented only to adhere to interface requirements.

    }

    private int col(int elementId) {
        return 1 + elementId % size;
    }

    private int row(int elementId) {
        return 1 + elementId / size;
    }

    // sample mean of percolation threshold
    public double mean() {
        return _mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return _stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return _confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return _confidenceHi;
    }
}
