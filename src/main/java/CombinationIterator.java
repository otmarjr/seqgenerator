import com.google.common.collect.Lists;

import java.util.*;


public class CombinationIterator<T>
        implements
        Iterator<List<T>> {
    private List<T> items;
    private int choose;
    private boolean finished;
    private int[] current;

    public CombinationIterator(Collection<T> items, int choose) {
       this(items,choose,null);
    }

    public CombinationIterator(Collection<T> items, int choose, int[] currentCombinationIndices) {
        if (items == null) {
            throw new IllegalArgumentException("items");
        }
        if (choose <= 0 || choose > items.size()) {
            throw new IllegalArgumentException("choose");
        }
        this.items = Lists.newArrayList(items);
        this.choose = choose;
        this.finished = false;

        if (currentCombinationIndices == null) {
            current = new int[choose];
            for (int i = 0; i < choose; i++) {
                current[i] = i;
            }
        }
        else {
            if (currentCombinationIndices.length != choose){
                throw new IllegalArgumentException("Current combination must have the same length of choose variable.");
            }

            for (int i=0;i<choose;i++){
                if (currentCombinationIndices[i] < 0 || currentCombinationIndices[i] >= choose){
                    throw new IllegalArgumentException("Current combination have an illegal value at position " + i + ".");
                }
            }
            this.current = currentCombinationIndices;
        }
    }


    public int[] getCurrentIndices(){
        return Arrays.copyOf(current, current.length);
    }

    public boolean hasNext() {
        return !finished;
    }

    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
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