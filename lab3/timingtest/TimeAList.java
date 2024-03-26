package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.apache.commons.math3.analysis.function.Pow;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE

        // store the addLast times

        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> optimes = new AList<>();
        for (int i = 0; i < 8; i++) {

            AList<Integer> list = new AList<>();
            int optime = 0;
            int N = (int) (1000 * Math.pow(2,i));

            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < N ; j++){
                list.addLast(j);
                 optime++;
            }
            double time = sw.elapsedTime();
            Ns.addLast(N);
            times.addLast(time);
            optimes.addLast(optime);
        }

        printTimingTable(Ns,times,optimes);




    }
}
