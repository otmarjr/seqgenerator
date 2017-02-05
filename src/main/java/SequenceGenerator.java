import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by otmar on 04/02/2017.
 */
public class SequenceGenerator<T> implements Iterator<List<T>> {
    long totalSequences;
    int sequenceSize;
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
        this.sequenceSize = 1;
        this.maximumLength = maximumSequenceLength;
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        if (sequenceSize < maximumLength){
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
            combinationsGenerator = new CombinationIterator<>(elements, sequenceSize);
            elementsToPermute = combinationsGenerator.next();
        }

        if (permutationGenerator == null){
            permutationGenerator = new PermutationIterator<>(elementsToPermute);
        }


        if (!permutationGenerator.hasNext()){
            if (!combinationsGenerator.hasNext()) {
                sequenceSize++;
                combinationsGenerator = new CombinationIterator<T>(elements, sequenceSize);
            }
            elementsToPermute = combinationsGenerator.next();
            permutationGenerator = new PermutationIterator<>(elementsToPermute);
        }

        totalSequences++;

        List<T> next = permutationGenerator.next();

        return next;
    }
}
