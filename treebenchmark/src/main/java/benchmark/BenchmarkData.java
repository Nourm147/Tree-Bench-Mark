package benchmark;

import static java.util.Arrays.sort;

public class BenchmarkData {

    private final String label;
    private final long[] rawTimes; // time in nanoseconds for each run
    private final int treeHeight;

    private final long median;
    private final double mean;
    private final double stdDev;

    public BenchmarkData(String label, long[] rawTimes, int treeHeight) {
        this.label = label;
        this.rawTimes = rawTimes;
        this.treeHeight = treeHeight;
        this.median = computeMedian(rawTimes);
        this.mean = computeMean(rawTimes);
        this.stdDev = computeStdDev(rawTimes, this.mean);
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public long[] getRawTimes() {
        return rawTimes;
    }

    public int getTreeHeight() {
        return treeHeight;
    }

    public long getMedian() {
        return median;
    }

    public double getMean() {
        return mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    // Compute median, mean, and standard deviation for given times
    private long computeMedian(long[] t) {
        long[] sorted = t.clone();
        sort(sorted);
        int n = sorted.length;
        if (n % 2 == 1) {
            return sorted[n / 2];
        } else {
            return (sorted[n / 2 - 1] + sorted[n / 2]) / 2;
        }
    }

    private double computeMean(long[] t) {
        double sum = 0;
        for (long time : t) {
            sum += time;
        }
        return sum / t.length;
    }

    private double computeStdDev(long[] t, double mean) {
        double sum = 0;
        for (long time : t) {
            sum += Math.pow(time - mean, 2);
        }
        return Math.sqrt(sum / t.length);
    }
}
