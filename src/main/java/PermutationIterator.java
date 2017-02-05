import java.util.*;

/**
 * Created by 07257618658 on 03/02/17.
 */
public class PermutationIterator<T>
        implements Iterator<List<T>> {

    private List<T> nextPermutation;
    private final List<T> allElements = new ArrayList<>();
    private int[] indices;
    private int totalSteps = 0;

    PermutationIterator(Collection<T> allElements, int[] indices, List<T> nextPermutation) {

        if (indices == null && nextPermutation != null || indices != null && nextPermutation == null) {
            throw new IllegalArgumentException("Both indices and nextPermutation must be non-null.");
        }

        if (allElements.isEmpty()) {
            nextPermutation = null;
            return;
        }

        this.allElements.addAll(allElements);

        if (indices == null) {
            this.indices = new int[allElements.size()];

            for (int i = 0; i < this.indices.length; ++i) {
                this.indices[i] = i;
            }
        } else {
            this.indices = indices;
        }

        if (nextPermutation == null) {
            this.nextPermutation = new ArrayList<>(this.allElements);
        } else {
            this.nextPermutation = nextPermutation;
        }


    }

    PermutationIterator(List<T> allElements) {
        this(allElements, null, null);
    }

    @Override
    public boolean hasNext() {
        return nextPermutation != null;
    }

    @Override
    public List<T> next() {
        if (nextPermutation == null) {
            throw new NoSuchElementException("No permutations left.");
        }

        totalSteps++;
        List<T> ret = nextPermutation;
        generateNextPermutation();
        return ret;
    }

    public int[] getIndices() {
        return Arrays.copyOf(indices, indices.length);
    }

    public List<T> getNextPermutation() {
        return nextPermutation;
    }

    private void generateNextPermutation() {
        int i = indices.length - 2;

        while (i >= 0 && indices[i] > indices[i + 1]) {
            --i;
        }

        if (i == -1) {
            // No more new permutations.
            nextPermutation = null;
            return;
        }

        int j = i + 1;
        int min = indices[j];
        int minIndex = j;

        while (j < indices.length) {
            if (indices[i] < indices[j] && indices[j] < min) {
                min = indices[j];
                minIndex = j;
            }

            ++j;
        }

        swap(indices, i, minIndex);

        ++i;
        j = indices.length - 1;

        while (i < j) {
            swap(indices, i++, j--);
        }

        loadPermutation();
    }

    private void loadPermutation() {
        List<T> newPermutation = new ArrayList<>(indices.length);

        for (int i : indices) {
            newPermutation.add(allElements.get(i));
        }

        this.nextPermutation = newPermutation;
    }


    private static void swap(int[] array, int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

}