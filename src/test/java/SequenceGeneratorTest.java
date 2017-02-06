import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Set;


public class SequenceGeneratorTest {
    @Rule
    public ExpectedException expectedForEmptySets = ExpectedException.none();
    private Set<String> emptySet = Sets.newHashSet();
    private Set<String> singletonSet = Sets.newHashSet("A");
    private Set<String> largerSet = Sets.newHashSet("A", "B", "C", "D");


    @Test
    public void hasNextForEmptySet() throws Exception {

        // Checks if an empty set produces just one sequence (an empty sequence).
        expectedForEmptySets.expect(IllegalArgumentException.class);
        SequenceGenerator<String> sgen = new SequenceGenerator<>(emptySet);
    }


    @Test
    public void hasNextForSingletonSet() throws Exception {

        // Checks if a singleton set produces just two sequences (empty and singleton).
        SequenceGenerator<String> sgen = new SequenceGenerator<>(singletonSet);

        Assert.assertTrue(sgen.hasNext());
        sgen.next();
        Assert.assertFalse(sgen.hasNext());
    }

    private int getNumberOfArrangements(int n, int choose){
        int nFactorial = BigIntegerMath.factorial(n).intValue();
        int nMinusMFactorial = BigIntegerMath.factorial(n - choose).intValue();

        int numberOfArrangements = nFactorial/nMinusMFactorial;
        return numberOfArrangements;
    }
    @Test
    public void hasNextForLargerSet() throws Exception {

        SequenceGenerator<String> sgen = new SequenceGenerator<>(largerSet);
        int n = largerSet.size();
        int expectedNumberOfSequences = 0;


        // The expected number of sequences is equal to the sum of A(n,1) + A(n,2) + ... + A(n,n)
        for (int i =1; i<= n;i++){
            expectedNumberOfSequences += getNumberOfArrangements(n, i);
        }

        Set<List<String>> generatedSequences = Sets.newHashSet();

        while (sgen.hasNext()){
            generatedSequences.add(sgen.next());
        }

        Assert.assertEquals(generatedSequences.size(), expectedNumberOfSequences);
    }

    @Test
    public void getTotalSequencesIterated() throws Exception {
        SequenceGenerator<String> sgenLarger = new SequenceGenerator<>(largerSet);

        long beforeIterateLarger = sgenLarger.getTotalSequencesIterated();
        int numberOfSequencesToGenerate=13;

        for (int i =0;i<numberOfSequencesToGenerate;i++){
            sgenLarger.next();
        }

        Assert.assertEquals(0, beforeIterateLarger);
        Assert.assertEquals(numberOfSequencesToGenerate, sgenLarger.getTotalSequencesIterated());
    }

}