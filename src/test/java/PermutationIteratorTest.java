import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.List;
import java.util.Set;


public class PermutationIteratorTest {
    public ExpectedException expectedForInvalidLists = ExpectedException.none();


    @Test
    public void constructorEmptyList() throws Exception {
        List<String> emptyList = Lists.newArrayList();
        expectedForInvalidLists.expect(IllegalArgumentException.class);
        PermutationIterator<String> sgen = new PermutationIterator<>(emptyList);
    }

    @Test
    public void hasNextForSingletonList() throws Exception {
        List<String> singletonList = Lists.newArrayList("A");
        PermutationIterator<String> sgen = new PermutationIterator<>(singletonList);
        Assert.assertTrue(sgen.hasNext());
        sgen.next();
        Assert.assertFalse(sgen.hasNext());
    }

    @Test
    public void hasNextForLargerList() throws Exception {
        List<String> largerList = Lists.newArrayList("A", "B", "C", "D");
        PermutationIterator<String> sgen = new PermutationIterator<>(largerList);

        int numberOfPermutations = BigIntegerMath.factorial(largerList.size()).intValue();

        for (int i =0;i<numberOfPermutations;i++){
            Assert.assertTrue("Should return true after " + (i+1) + " permutations.", sgen.hasNext());
        }
    }

    @Test
    public void nextForSingletonList() throws Exception {
        List<String> singleElement = Lists.newArrayList("A");
        Set<List<String>> expectedPermutations = Sets.newHashSet();
        expectedPermutations.add(singleElement);

        Set<List<String>> actualPermutations = Sets.newHashSet();

        PermutationIterator<String> sgen = new PermutationIterator<>(Lists.newArrayList("A"));

        while(sgen.hasNext()){
            actualPermutations.add(sgen.next());
        }

        Assert.assertEquals(expectedPermutations, actualPermutations);
    }

    @Test
    public void nextForTwoElements() throws Exception {
        Set<List<String>> expectedPermutations = Sets.newHashSet(Lists.newArrayList("A", "B"), Lists.newArrayList("B", "A"));
        Set<List<String>> actualPermutations = Sets.newHashSet();

        PermutationIterator<String> sgen = new PermutationIterator<>(Lists.newArrayList("A", "B"));

        while(sgen.hasNext()){
            actualPermutations.add(sgen.next());
        }

        Assert.assertEquals(expectedPermutations, actualPermutations);
    }

    @Test
    public void nextForThreeElements() throws Exception {
        Set<List<String>> expectedPermutations = Sets.newHashSet();
        expectedPermutations.add(Lists.newArrayList("A","B","C"));
        expectedPermutations.add(Lists.newArrayList("A","C","B"));
        expectedPermutations.add(Lists.newArrayList("B","A","C"));
        expectedPermutations.add(Lists.newArrayList("B","C","A"));
        expectedPermutations.add(Lists.newArrayList("C","B","A"));
        expectedPermutations.add(Lists.newArrayList("C","A","B"));

        Set<List<String>> actualPermutations = Sets.newHashSet();

        PermutationIterator<String> sgen = new PermutationIterator<>(Lists.newArrayList("A", "B", "C"));

        while(sgen.hasNext()){
            actualPermutations.add(sgen.next());
        }

        Assert.assertEquals(expectedPermutations, actualPermutations);
    }

    @Test
    public void getIndices() throws Exception {
        PermutationIterator<String> sgen = new PermutationIterator<>(Lists.newArrayList("A", "B", "C"));

        int[] indicesBeforeAnyPermutation = sgen.getCurrentIndices();

        sgen.next();
        sgen.next();
        sgen.next();

        int[] indicesAfter3Permutations = sgen.getCurrentIndices();

        Assert.assertArrayEquals(new int[]{0,1,2}, indicesBeforeAnyPermutation);
        Assert.assertArrayEquals(new int[]{1,0,2}, indicesAfter3Permutations);

    }
}