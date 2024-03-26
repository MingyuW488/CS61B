package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;
import org.eclipse.jetty.websocket.server.NativeWebSocketConfiguration;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE

        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for(int i = 0;i < 8; i++){
           Ns.addLast((int) (1000* Math.pow(2,i)));
           opCounts.addLast(10000);
        }

        for(int i = 0;i < 8;i++){

            SLList<Integer> list = new SLList<>();
            for(int j = 0;j < Ns.get(i); j++){
                list.addFirst(j);
            }
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < opCounts.get(i);j++){
                list.getLast();
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(Ns,times,opCounts);

        }


    }


