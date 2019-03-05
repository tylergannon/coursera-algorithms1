import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_SIZE = 10;
    private int sizeField = 0;
    private Object[] items = new Object[INITIAL_SIZE];

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (sizeField >= items.length)
            expand();
        items[sizeField++] = item;
    }

    public Item dequeue() {
        if (sizeField == 0)
            throw new NoSuchElementException();
        int itemToRemove = StdRandom.uniform(sizeField);
        sizeField--;
        Item item = (Item) items[itemToRemove];
        items[itemToRemove] = items[sizeField];
        items[sizeField] = null;
        if (sizeField <= items.length / 2 && sizeField > INITIAL_SIZE)
            decrease();
        return item;
    }

    public boolean isEmpty() {
        return sizeField == 0;
    }

    public int size() {
        return sizeField;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        return (Item) items[StdRandom.uniform(sizeField)];
    }


    private void decrease() {
        Object[] newItems = new Object[items.length / 2];
        for (int i = 0; i < sizeField; i++)
            newItems[i] = items[i];

        items = newItems;
    }

    private void expand() {
        Object[] newItems = new Object[2 * items.length];
        for (int i = 0; i < sizeField; i++)
            newItems[i] = items[i];

        items = newItems;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedIterator(items, sizeField);
    }

    private class RandomizedIterator implements Iterator<Item> {
        private Object[] items;
        private int nextItem = 0;

        RandomizedIterator(Object[] items, int size) {
            this.items = new Object[size];
            for (int i = 0; i < size; i++)
                this.items[i] = items[i];
            StdRandom.shuffle(this.items);
        }

        @Override
        public boolean hasNext() {
            return nextItem < items.length;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = (Item) items[nextItem];
            items[nextItem] = null;
            nextItem++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
