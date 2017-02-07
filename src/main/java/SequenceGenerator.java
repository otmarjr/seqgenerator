import javafx.collections.transformation.SortedList;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class SequenceGenerator<T> implements Iterator<List<T>> {
    long totalSequences;
    int sequenceLength;
    int maximumLength;
    Collection<T> elements;
    List<T> elementsToPermute;
    PermutationIterator<T> permutationGenerator;
    CombinationIterator<T> combinationsGenerator;

    public SequenceGenerator(Collection<T> elements){
        this(elements, elements == null ? 0 : elements.size());
    }

    public SequenceGenerator(Collection<T> elements, int maximumSequenceLength){

        if (elements == null){
            throw new IllegalArgumentException("Elements set must be not null.");
        }

        if (elements.isEmpty()){
            throw new IllegalArgumentException("Cannot generate sequences for empty sets.");
        }

        this.totalSequences = 0;
        this.sequenceLength = 1;
        this.maximumLength = maximumSequenceLength;
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        if (sequenceLength < maximumLength){
            return true;
        }
        else {
            return permutationGenerator == null ||
                    permutationGenerator.hasNext()
                    ||
                    combinationsGenerator.hasNext()
                    ||
                    (!permutationGenerator.hasNext() && combinationsGenerator.hasNext())
                    ||
                    (!combinationsGenerator.hasNext() && permutationGenerator.hasNext());
        }

    }

    public  long getTotalSequencesIterated(){
        return totalSequences;
    }

    @Override
    public List<T> next() {
        if (!hasNext()){
            throw new NoSuchElementException();
        }

        if (combinationsGenerator == null){
            combinationsGenerator = new CombinationIterator<>(elements, sequenceLength);
            elementsToPermute = combinationsGenerator.next();
        }

        if (permutationGenerator == null){
            permutationGenerator = new PermutationIterator<>(elementsToPermute);
        }


        if (!permutationGenerator.hasNext()){
            if (!combinationsGenerator.hasNext()) {
                sequenceLength++;
                combinationsGenerator = new CombinationIterator<T>(elements, sequenceLength);
            }
            elementsToPermute = combinationsGenerator.next();
            permutationGenerator = new PermutationIterator<>(elementsToPermute);
        }

        totalSequences++;

        List<T> next = permutationGenerator.next();

        return next;
    }

    class SequenceIndex implements Serializable{
        private static final long serialVersionUID = 2496849987065726718L;

        private int[] currentCombinationIndices;
        private int[] currentPermutationIndices;
        private int sequenceLength;

        public SequenceIndex(int sequenceLength, int[] combinationIndices, int[] permutationIndices){
            this.currentCombinationIndices = combinationIndices;
            this.currentPermutationIndices = permutationIndices;
            this.sequenceLength = sequenceLength;
        }

        public int[] getCurrentCombinationIndices() {
            return currentCombinationIndices;
        }

        public void setCurrentCombinationIndices(int[] currentCombinationIndices) {
            this.currentCombinationIndices = currentCombinationIndices;
        }

        public int[] getCurrentPermutationIndices() {
            return currentPermutationIndices;
        }

        public void setCurrentPermutationIndices(int[] currentPermutationIndices) {
            this.currentPermutationIndices = currentPermutationIndices;
        }

        public int getSequenceLength() {
            return sequenceLength;
        }

        public void setSequenceLength(int sequenceLength) {
            this.sequenceLength = sequenceLength;
        }
    }

    public SequenceIndex getCurrentIndex(){
        return new SequenceIndex(this.sequenceLength, this.combinationsGenerator.getCurrentIndices(), this.permutationGenerator.getCurrentIndices());
    }

    public void setCurrentIndex(SequenceIndex index){
        if (index == null || index.getCurrentCombinationIndices() == null || index.getCurrentPermutationIndices() == null){
            throw new IllegalArgumentException("Index and its members cannot be null.");
        }

        combinationsGenerator = new CombinationIterator<>(elements, sequenceLength, index.getCurrentCombinationIndices());
        permutationGenerator = new PermutationIterator<> (elements, index.getCurrentPermutationIndices());
    }
}
