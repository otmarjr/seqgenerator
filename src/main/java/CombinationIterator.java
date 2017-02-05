import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by 07257618658 on 02/02/17.
 */

public class CombinationIterator<T>
        implements
        Iterator<List<T>> {
    private List<T> items;
    private int choose;
    private boolean started;
    private boolean finished;
    private int[] current;

    public CombinationIterator(Collection<T> items, int choose) {
        if (items == null) {
            throw new IllegalArgumentException("items");
        }
        if (choose <= 0 || choose > items.size()) {
            throw new IllegalArgumentException("choose");
        }
        this.items = Lists.newArrayList(items);
        this.choose = choose;
        this.finished = false;
    }

    public boolean hasNext() {
        return !finished;
    }

    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (current == null) {
            current = new int[choose];
            for (int i = 0; i < choose; i++) {
                current[i] = i;
            }
        }

        List<T> result = new ArrayList<T>(choose);
        for (int i = 0; i < choose; i++) {
            result.add(items.get(current[i]));
        }

        int n = items.size();
        finished = true;
        for (int i = choose - 1; i >= 0; i--) {
            if (current[i] < n - choose + i) {
                current[i]++;
                for (int j = i + 1; j < choose; j++) {
                    current[j] = current[i] - i + j;
                }
                finished = false;
                break;
            }
        }

        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}