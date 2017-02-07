import java.util.*;


public class PermutationIterator<T>
        implements Iterator<List<T>> {

    private List<T> nextPermutation;
    private final List<T> allElements = new ArrayList<>();
    private int[] indices;
    private int totalSteps = 0;

    PermutationIterator(Collection<T> allElements, int[] currentPermutationIndices) {

        if (allElements.isEmpty()) {
            return;
        }

        this.allElements.addAll(allElements);

        if (currentPermutationIndices == null) {
            this.indices = new int[allElements.size()];

            for (int i = 0; i < this.indices.length; ++i) {
                this.indices[i] = i;
            }
        } else {
            this.indices = currentPermutationIndices;
        }

    }

    PermutationIterator(List<T> allElements) {
        this(allElements, null);
    }

    @Override
    public boolean hasNext() {
        return getNextIndexToPermute() >= 0;
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No permutations left.");
        }

        totalSteps++;
        List<T> ret = nextPermutation;
        generateNextPermutation();
        return ret;
    }

    public int[] getCurrentIndices() {
        return Arrays.copyOf(indices, indices.length);
    }

    public List<T> getNextPermutation() {
        return nextPermutation;
    }

    private void generateNextPermutation() {
        int i = getNextIndexToPermute();

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

    private int getNextIndexToPermute() {
        int i = indices.length - 2;

        while (i >= 0 && indices[i] > indices[i + 1]) {
            --i;
        }
        return i;
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

    public int getTotalSteps() {
        return totalSteps;
    }
}