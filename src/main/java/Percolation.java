import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int NO_ELEMENT = -1;
    private final int size;
    private final int topVirtualElementId;
    private final boolean[] openedSites;
    private final boolean[] bottomConnectedSites;
    private final WeightedQuickUnionUF wqu;
    private int numOpenSites = 0;
    private boolean percolatesField = false;


    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        size = n;
        topVirtualElementId = n * n;
        openedSites = new boolean[n * n];
        bottomConnectedSites = new boolean[n * n];
        int arraySize = size * size;

        for (int x = 0; x < arraySize; x++) {
            openedSites[x] = false;
            bottomConnectedSites[x] = false;
        }

        wqu = new WeightedQuickUnionUF(n * n + 1);
    }

    //    Implemented only to adhere to interface requirements.
    public static void main(String[] args) {
//    Implemented only to adhere to interface requirements.

    }

    /*******************************************
     * If one of the connected openedSites is connected to
     * the top, then this one is too.
     */
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();

        if (isOpen(row, col))
            return;

        int elementId = getElementId(row, col);
        openedSites[elementId] = true;
        numOpenSites++;

        boolean connectedToTop = false;
        boolean connectedToBottom = false;


//        Element Above:
        if (row == 1) {
            wqu.union(elementId, topVirtualElementId);
            connectedToTop = true;
        } else {
            connectedToBottom = connect(elementId, elementId - size);
        }

        boolean tempConnected;

//        Element to left:
        if (col > 1) {
            tempConnected = connect(elementId, elementId - 1);
            connectedToBottom = connectedToBottom || tempConnected;
        }

//        Element to right:
        if (col < size) {
            tempConnected = connect(elementId, elementId + 1);
            connectedToBottom = connectedToBottom || tempConnected;
        }

//        Element below:
        if (row == size)
            connectedToBottom = true;
        else {
            tempConnected = connect(elementId, elementId + size);
            connectedToBottom = connectedToBottom || tempConnected;
        }

        if (connectedToBottom) {
            bottomConnectedSites[wqu.find(elementId)] = true;
            percolatesField = percolatesField || connectedToTop || wqu.connected(elementId, topVirtualElementId);
        }
    }    // open site (row, col) if it is not open already

    private boolean connect(int elementId, int other) {
        if (openedSites[other]) {
            boolean isBottomConnected = bottomConnectedSites[wqu.find(other)];
            wqu.union(elementId, other);
            return isBottomConnected;
        }
        return false;
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();

        return openedSites[getElementId(row, col)];
    }  // is site (row, col) open?

    /*******************************************
     * Something to do with noting with a given item is connected
     * specifically to the top.
     * This means keeping my own array of markers that specifically
     * knows whether it's connected to the top.
     */
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();

        return wqu.connected(getElementId(row, col), topVirtualElementId);
    }  // is site (row, col) full?

    public int numberOfOpenSites() {
        return numOpenSites;
    }       // number of open openedSites

    public boolean percolates() {
        return percolatesField;
    }              // does the system percolate?

    private int getElementId(int row, int col) {
        return size * (row - 1) + col - 1;
    }
}
