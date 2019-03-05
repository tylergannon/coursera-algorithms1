import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node firstNode = null;
    private Node lastNode = null;
    private int itemCount = 0;

    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator(firstNode);
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        itemCount++;
        Node oldFirst = firstNode;
        firstNode = new Node(item);
        firstNode.nextNode = oldFirst;

        if (lastNode == null)
            lastNode = firstNode;
        else
            firstNode.nextNode.previousNode = firstNode;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        itemCount++;
        Node oldLast = lastNode;
        lastNode = new Node(item);
        lastNode.previousNode = oldLast;

        if (firstNode == null)
            firstNode = lastNode;
        else
            lastNode.previousNode.nextNode = lastNode;
    }

    public boolean isEmpty() {
        return firstNode == null;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node returnVal = lastNode;

        if (firstNode == lastNode) {
            firstNode = null;
            lastNode = null;
        } else {
            returnVal.previousNode.nextNode = null;
            lastNode = returnVal.previousNode;
        }
        itemCount--;
        return returnVal.item;
    }

    public int size() {
        return itemCount;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node returnVal = firstNode;
        if (firstNode == lastNode) {
            firstNode = null;
            lastNode = null;
        } else {
            returnVal.nextNode.previousNode = null;
            firstNode = returnVal.nextNode;
        }
        itemCount--;
        return returnVal.item;
    }

    private class Node {
        final Item item;
        Node previousNode = null;
        Node nextNode = null;

        private Node(Item item) {
            this.item = item;
        }
    }

    private class ItemIterator implements Iterator<Item> {
        Node nextNode;

        private ItemIterator(Node node) {
            nextNode = node;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Node itemToReturn = nextNode;
            nextNode = itemToReturn.nextNode;
            return itemToReturn.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }


    }
}
