import java.util.ArrayList;
import java.util.Arrays;

//import static java.lang.Math.*;

/****
 * Max Values:
 * Dimension:       127   (7)    4  (7 bits)
 * Address Size:    14    (4)    4  (4 bits)
 * Address:         16384 (14)   15 (4 bits)
 * Zero Location:   16384 (14)   15 (4 bits)
 * Hamming:         16384  (14)  15 (4 bits)
 * Manhattan:       2080768 (21) 64 (6 bits)
 *
 * 15 14 13 12
 * 11 10  9  8
 *  7  6  5  4
 *  3  2  1
 *
 * Size of Manhattan: 2*addressSize
 *
 */
public class Board {

    private static final int HEADER_INDEX = 0;
    private static final int SIZE_DIMENSION = 7;
    private static final int ADDR_DIMENSION = 0;
    private static final int SIZE_ADDRESS = 4;
    private static final int ADDR_ADDRESS = SIZE_DIMENSION;
    private static final int HEADER_VARIABLE_LENGTH_DATA_START = ADDR_ADDRESS + SIZE_ADDRESS;
    //    private static final int SIZE_NUM_ELEM = 0;
//    private static final int ADDR_NUM_ELEM = ADDR_ADDRESS + SIZE_ADDRESS;
    private static final int SIZE_ZERO_LOC = 7;
    private static final int ADDR_ZERO_LOC = 123;
    private static final int SIZE_HAMMING = 7;
    private static final int ADDR_HAMMING = ADDR_ZERO_LOC + SIZE_ZERO_LOC;
    private static final int SIZE_MANHATTAN = 10;
    private static final int ADDR_MANHATTAN = ADDR_HAMMING + SIZE_HAMMING;
    //    private static final String FINAL_FORMAT = "%3s";
    private static final int[] BITMASKS = new int[]{
            0,
            0b1,
            0b11,
            0b111,
            0b1111,
            0b11111,
            0b111111,
            0b1111111,
            0b11111111,
            0b111111111,
            0b1111111111,
            0b11111111111,
            0b111111111111,
            0b1111111111111,
            0b11111111111111,
            0b111111111111111,
            0b1111111111111111,
            0b11111111111111111,
            0b111111111111111111,
            0b1111111111111111111,
            0b11111111111111111111,
            0b111111111111111111111,
            0b1111111111111111111111,
            0b11111111111111111111111,
            0b111111111111111111111111,
            0b1111111111111111111111111,
            0b11111111111111111111111111,
            0b111111111111111111111111111,
            0b1111111111111111111111111111,
            0b11111111111111111111111111111,
            0b111111111111111111111111111111,
            0b1111111111111111111111111111111,
            0b11111111111111111111111111111111
    };
    private static final int[] ADDRESS_SIZES = new int[]{
            2,  // two bits for n=2
            4,  // 4 bits for n=3
            4,  // 4 bits for n=4
            5,  // 5 bits for n=5
            6,  // 6 bits for n=6
            6,  // 6 bits for n=7
            7,  // 7 bits for n=8
            7,
            7
    };
    private static final int SINGLE_INT_DIM_SIZE = 4;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final double RATIO_ADDRESS_SIZE_TO_MANHATTAN_SIZE = 1.5;
    private static final int USE_ADDRESS_SIZE = -1;
    private final int[] data;

    public Board(int[][] initialBlocks) {
        int dimension = initialBlocks.length;
        int numElements = dimension * dimension;
        int addressSize = bitSizeOf(numElements);
        int manhattan = 0;
        int hamming = 0;
        int headerSize = dimension <= SINGLE_INT_DIM_SIZE ? ONE : TWO;
        int dataSize = headerSize + (int) Math.ceil((double) numElements * addressSize / Integer.SIZE);

        int currentBlock = 0;
        int zeroIndex = 0;
        data = new int[dataSize];

        data[HEADER_INDEX] = ((dimension - 1) << ADDR_DIMENSION) +
                (addressSize << ADDR_ADDRESS);


        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int currentVal = initialBlocks[row][col];
                if (currentVal == 0) {
                    zeroIndex = currentBlock;
                } else if (currentVal != currentBlock + 1) {
                    hamming++;
                    manhattan += Math.abs((currentBlock) / dimension - (currentVal - 1) / dimension);
                    manhattan += Math.abs((currentBlock) % dimension - (currentVal - 1) % dimension);
                }

                setBlockValue(currentBlock, currentVal, data, addressSize);
                currentBlock++;
            }
        }

        setNumElements(numElements, addressSize);
        setZeroIndex(zeroIndex, addressSize);
        setHamming(hamming, addressSize);
        setManhattan(manhattan, addressSize);
    }

    private Board(int[] data) {
        this.data = data;
    }

    private static int bitSizeOf(int num) {

        return (int) Math.ceil(Math.log10(num) / Math.log10(TWO));
    }

    private static final void setBlockValue(int address, int value, int[] data, int addressSize) {
        setValueAt(address * addressSize + getOffset(data), value, data, addressSize, addressSize);
    }

    private static int getBlockValue(int address, int[] data, int addressSize) {
        return getValueAt(address * addressSize + getOffset(data), data, addressSize, addressSize);
    }

    private static void setValueAt(int index, int value, int[] data, int addressSize) {
        setValueAt(index, value, data, addressSize, USE_ADDRESS_SIZE);
    }

    private static void setValueAt(int index, int value, int[] data, int addressSize, int dataLength) {
        if (dataLength == USE_ADDRESS_SIZE)
            dataLength = addressSize;

        int whichInt = index / Integer.SIZE;
        int shiftLength = index % Integer.SIZE;
        int bitsToSet = Math.min(dataLength, Integer.SIZE - shiftLength);
//        First reset the value to zero.
        data[whichInt] &= ~(BITMASKS[bitsToSet] << shiftLength);

        if (bitsToSet < dataLength)
            data[whichInt + 1] &= ~BITMASKS[dataLength - bitsToSet];
        if (value > 0) {
            data[whichInt] |= (value & BITMASKS[bitsToSet]) << shiftLength;
            if (bitsToSet < dataLength)
                data[whichInt + 1] |= value >> bitsToSet;
        }
    }

    private static String join(int[] nums, String separator, String FINAL_FORMAT) {

        String val = "";
        for (int i = 0; i < nums.length - 1; i++)
            val += String.format(FINAL_FORMAT + separator, nums[i]);
        val += String.format(FINAL_FORMAT, nums[nums.length - 1]);
        return val;
    }

    private static int manhattan(int currentBlock, int currentVal, int dimension) {
        return Math.abs((currentBlock) / dimension - (currentVal - 1) / dimension)
                + Math.abs((currentBlock) % dimension - (currentVal - 1) % dimension);
    }

    private static int getValueAt(int index, int[] data, int addressSize) {
        return getValueAt(index, data, addressSize, USE_ADDRESS_SIZE);
    }

    private static int getValueAt(int index, int[] data, int addressSize, int dataLength) {
        if (dataLength == USE_ADDRESS_SIZE)
            dataLength = addressSize;

        int whichInt = index / Integer.SIZE;
        int shiftLength = index % Integer.SIZE;
        int val = data[whichInt] >>> shiftLength & BITMASKS[dataLength];

        if (shiftLength + dataLength > Integer.SIZE) {
            int bitsRemaining = dataLength - (Integer.SIZE - shiftLength);
            val += (data[whichInt + 1] & BITMASKS[bitsRemaining]) << (dataLength - bitsRemaining);
        }
        return val;
    }

    private static int getAddressSize(int[] data) {
        return data[HEADER_INDEX] >> ADDR_ADDRESS & BITMASKS[SIZE_ADDRESS];
    }

    private static int getOffset(int[] data) {
        return (data[HEADER_INDEX] & BITMASKS[SIZE_DIMENSION]) < SINGLE_INT_DIM_SIZE ? Integer.SIZE : Long.SIZE;
    }

    private void setManhattan(int manhattan, int addressSize) {
        setValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize * THREE,
                manhattan,
                data,
                addressSize,
                (int) Math.ceil(addressSize * RATIO_ADDRESS_SIZE_TO_MANHATTAN_SIZE));
    }

    public int manhattan() {
        return getManhattan(getAddressSize(data));
    }

    private int getManhattan(int addressSize) {
        return getValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize * THREE,
                data,
                addressSize,
                (int) Math.ceil(addressSize * RATIO_ADDRESS_SIZE_TO_MANHATTAN_SIZE));
    }

    private void setHamming(int hamming, int addressSize) {
        setValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize * TWO, hamming, data, addressSize);
    }

    public int hamming() {
        return getHamming(getAddressSize(data));
    }

    private int getHamming(int addressSize) {
        return getValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize * TWO, data, addressSize);
    }

    private void setZeroIndex(int zeroIndex, int addressSize) {
        setValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize, zeroIndex, data, addressSize);
    }

    private int getZeroIndex(int addressSize) {
        return getValueAt(HEADER_VARIABLE_LENGTH_DATA_START + addressSize, data, addressSize);
    }

    private void setNumElements(int numElements, int addressSize) {
        setValueAt(HEADER_VARIABLE_LENGTH_DATA_START, numElements - 1, data, addressSize);
    }

    private int getNumElements() {
        return getValueAt(HEADER_VARIABLE_LENGTH_DATA_START, data, getAddressSize()) + 1;
    }

    public int dimension() {
        return (data[HEADER_INDEX] & BITMASKS[SIZE_DIMENSION]) + 1;
    }

    private int getAddressSize() {
        return getAddressSize(data);
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int addressSize = getAddressSize();
        int[] toMove = new int[2];
        int found = 0, current = 0;
        while (found != 2) {
            if (getBlockValue(current++, data, addressSize) != 0)
                toMove[found++] = current - 1;
        }
        int[] newData = Arrays.copyOf(data, data.length);
        setBlockValue(toMove[0], getBlockValue(toMove[1], data, addressSize), newData, addressSize);
        setBlockValue(toMove[1], getBlockValue(toMove[0], data, addressSize), newData, addressSize);

        int hamming = hamming();
        int manhattan = manhattan();

        for (int i = 0; i < 2; i++) {
            int other = i == 0 ? 1 : 0;
            if (getBlockValue(toMove[i], data, addressSize) != toMove[i] + 1)
                hamming--;
            if (getBlockValue(toMove[other], newData, addressSize) != toMove[other] + 1)
                hamming++;
            manhattan -= manhattan(toMove[i], getBlockValue(toMove[i], data, addressSize), dimension());
            manhattan += manhattan(toMove[other], getBlockValue(toMove[i], data, addressSize), dimension());
        }

        Board newBoard = new Board(newData);
        newBoard.setHamming(hamming, getAddressSize());
        newBoard.setManhattan(manhattan, getAddressSize());
        return newBoard;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Board) {
            Board other = (Board) obj;
            if (other.data[HEADER_INDEX] >> ADDR_MANHATTAN != data[HEADER_INDEX] >> ADDR_MANHATTAN)
                return false;

            return Arrays.equals(this.data, other.data);

        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        int dimension = dimension();
        int numElements = getNumElements();
        int[] row = new int[dimension];
        StringBuilder builder = new StringBuilder(String.format("%s\n", dimension));
        int fieldWidth = (1 + (int) Math.ceil(Math.log10(numElements)));
        String format = "%" + fieldWidth + "s";
        for (int i = 0; i < numElements; i++) {
            row[i % dimension] = getBlockValue(i, data, getAddressSize());
            if (i % dimension == dimension - 1) {
                builder.append(join(row, "", format) + "\n");
            }
        }

        return builder.toString();
    }

    private Board move(int blockToMove) {
        int addressSize = getAddressSize();
        int[] newData = Arrays.copyOf(data, data.length);
        int zero = getZeroIndex(addressSize);
        int valueToMove = getBlockValue(blockToMove, data, addressSize);
        setBlockValue(zero, valueToMove, newData, addressSize);
        setBlockValue(blockToMove, 0, newData, addressSize);

        int newHamming;
        if (blockToMove + 1 == valueToMove)
            newHamming = hamming() + 1;
        else {
            if (zero + 1 == valueToMove)
                newHamming = hamming() - 1;
            else
                newHamming = hamming();
        }

        int newManhattan = manhattan() - manhattan(blockToMove, valueToMove, dimension())
                + manhattan(zero, valueToMove, dimension());

        Board newBoard = new Board(newData);
        newBoard.setManhattan(newManhattan, addressSize);
        newBoard.setHamming(newHamming, addressSize);
        newBoard.setZeroIndex(blockToMove, addressSize);

        return newBoard;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int zero = getZeroIndex(getAddressSize());
        int dimension = dimension();
        int row = zero / dimension;
        int col = zero % dimension;
        if (col > 0)
            neighbors.add(move(zero - 1));
        if (col < dimension - 1)
            neighbors.add(move(zero + 1));
        if (row > 0)
            neighbors.add(move(zero - dimension));
        if (row < dimension - 1)
            neighbors.add(move(zero + dimension));

        return neighbors;
    }
}
