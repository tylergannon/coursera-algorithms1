import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class PercolationStatsTest {

    static Stream percolationStatsMeanArgs() {
        return Stream.of(
                Arguments.of(2, 10000, 0.66, 0.67)
        );
    }

    static Stream getParams() {
        return Stream.of(
                Arguments.of(1, 10),
                Arguments.of(10, 100),
                Arguments.of(100, 100),
                Arguments.of(100, 100),
                Arguments.of(200, 100),
                Arguments.of(400, 100),
                Arguments.of(800, 100),
                Arguments.of(1000, 50),
                Arguments.of(1000, 100),
                Arguments.of(2000, 50),
                Arguments.of(2000, 100)
        );
    }


    @Disabled("Takes too long")
    @ParameterizedTest(name = "{index}: using Percolation({arguments})")
    @MethodSource("getParams")
    void testPercolationTimes(Integer n, Integer trials) {
        PercolationStats state = new PercolationStats(n, trials);
        System.out.println("--------------------------------");
        System.out.println("Size: " + n.toString() + ", trials: " + trials.toString());
        System.out.print("Mean: ");
        System.out.println(state.mean());
        System.out.print("Stddev: ");
        System.out.println(state.stddev());
        System.out.print("Confidence : [");
        System.out.print(state.confidenceLo());
        System.out.print(", ");
        System.out.print(state.confidenceHi());
        System.out.println("]");
        System.out.println("--------------------------------");
    }

    @ParameterizedTest
    @Disabled
    @MethodSource("percolationStatsMeanArgs")
    void testPercolationStatsMean(int n, int trials, double expectedMeanLow, double expectedMeanHigh) {
        PercolationStats percolationStats = new PercolationStats(n, trials);
        Assertions.assertTrue(percolationStats.mean() > expectedMeanLow, String.format("Mean %5f is lower than %5f.", percolationStats.mean(), expectedMeanLow));
        Assertions.assertTrue(percolationStats.mean() < expectedMeanHigh, String.format("Mean %5f is higher than %5f.", percolationStats.mean(), expectedMeanHigh));
    }


}
