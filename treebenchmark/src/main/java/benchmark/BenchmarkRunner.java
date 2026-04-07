package benchmark;

import java.nio.file.Path;

import org.slf4j.LoggerFactory;

import Algorithms.BinarySearchTree;
import Algorithms.RedBlackTree;
import Input.BenchmarkInput;
import Input.InputDistribution;
import ch.qos.logback.classic.Logger;

public class BenchmarkRunner {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(BenchmarkRunner.class);

    public void runBenchmarks() {
        int N = 100000;
        Benchmark benchmark = new Benchmark();

        Path csvPath = Path.of("results", "benchmark_results.csv");
        BenchmarkExporter exporter;
        try {
            exporter = new BenchmarkExporter(csvPath);
        } catch (Exception e) {
            logger.error("Failed to create BenchmarkExporter: {}", e.getMessage());
            return;
        }
        // Run benchmarks for each distribution
        for (InputDistribution dist : InputDistribution.values()) {
            logger.debug("--------- Distribution: {} ---------", dist);

            BenchmarkInput benchmarkInput = new BenchmarkInput(N, dist);
            // Measure insert performance
            BenchmarkData bstInsert = benchmark.measureInsert(BinarySearchTree::new, benchmarkInput, "BST Insert ");
            BenchmarkData rbtInsert = benchmark.measureInsert(RedBlackTree::new, benchmarkInput, "RBT Insert ");
            logger.debug("Height after insert | BST: {} | RBT: {}",
                    bstInsert.getTreeHeight(), rbtInsert.getTreeHeight());
            printStats(bstInsert);
            printStats(rbtInsert);
            exporter.appendRow(dist, bstInsert);
            exporter.appendRow(dist, rbtInsert);
            printSpeedup(bstInsert, rbtInsert);

            // Measure contains performance
            BenchmarkData bstContains = benchmark.measureContains(BinarySearchTree::new, benchmarkInput, "BST Contains ", bstInsert.getTreeHeight());
            BenchmarkData rbtContains = benchmark.measureContains(RedBlackTree::new, benchmarkInput, "RBT Contains ", rbtInsert.getTreeHeight());
            printStats(bstContains);
            printStats(rbtContains);
            exporter.appendRow(dist, bstContains);
            exporter.appendRow(dist, rbtContains);
            printSpeedup(bstContains, rbtContains);

            // Measure delete performance
            BenchmarkData bstDelete = benchmark.measureDelete(BinarySearchTree::new, benchmarkInput, "BST Delete ", bstInsert.getTreeHeight());
            BenchmarkData rbtDelete = benchmark.measureDelete(RedBlackTree::new, benchmarkInput, "RBT Delete ", rbtInsert.getTreeHeight());
            printStats(bstDelete);
            printStats(rbtDelete);
            exporter.appendRow(dist, bstDelete);
            exporter.appendRow(dist, rbtDelete);
            printSpeedup(bstDelete, rbtDelete);

            // Measure sorting performance
            BenchmarkData bstSorting = benchmark.measureSorting(BinarySearchTree::new, benchmarkInput, "BST Sorting ", bstInsert.getTreeHeight());
            BenchmarkData rbtSorting = benchmark.measureSorting(RedBlackTree::new, benchmarkInput, "RBT Sorting ", rbtInsert.getTreeHeight());
            BenchmarkData quickSort = benchmark.measureQuickSort(benchmarkInput.getInsertData(), "Quick Sort ");
            printStats(bstSorting);
            printStats(rbtSorting);
            printStats(quickSort);
            exporter.appendRow(dist, bstSorting);
            exporter.appendRow(dist, rbtSorting);
            exporter.appendRow(dist, quickSort);
            printSpeedup(bstSorting, rbtSorting);
        }
        exporter.close();
    }

    private void printStats(BenchmarkData data) {
        logger.debug("{} | mean: {} ms | median: {} ms | stdDev: {} ms",
                data.getLabel(),
                String.format("%.2f", data.getMean() / 1e6),
                String.format("%.2f", data.getMedian() / 1e6),
                String.format("%.2f", data.getStdDev() / 1e6));
    }

    private void printSpeedup(BenchmarkData bstData, BenchmarkData rbtData) {
        double bstMean = bstData.getMean() / 1e6;
        double rbtMean = rbtData.getMean() / 1e6;
        double speedup = bstMean / rbtMean;
        // use logging to avoid I/O overhead
        logger.debug("{} vs {}: BST mean = {} ms, RBT mean = {} ms, Speedup = {}x",
                bstData.getLabel(),
                rbtData.getLabel(),
                String.format("%.2f", bstMean),
                String.format("%.2f", rbtMean),
                String.format("%.2f", speedup));
    }

}
