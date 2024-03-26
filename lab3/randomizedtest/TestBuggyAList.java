package randomizedtest;

import com.sun.media.sound.SoftTuning;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> listA = new AListNoResizing<>();
        BuggyAList<Integer> listB = new BuggyAList<>();

        listA.addLast(1);
        listA.addLast(2);
        listA.addLast(3);
        listB.addLast(1);
        listB.addLast(2);
        listB.addLast(3);
        assertEquals(listA.getLast(),listB.getLast());
        listA.removeLast();
        listB.removeLast();
        assertEquals(listA.getLast(),listB.getLast());
        listA.removeLast();
        listB.removeLast();
        assertEquals(listA.getLast(),listB.getLast());
        listA.removeLast();
        listB.removeLast();
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(L.size(), L2.size());
            }
            else if ( operationNumber == 2 ) {
                if(L.size() == 0 || L2.size() == 0)
                    continue;
                int num = L.getLast();
                int num2 = L2.getLast();
                assertEquals(num,num2);
            }
            else if ( operationNumber == 3) {
                if(L.size() == 0 || L2.size() == 0)
                    continue;
                int num = L.removeLast();
                int num2 = L2.removeLast();
                assertEquals(num,num2);
            }
        }
    }
}
