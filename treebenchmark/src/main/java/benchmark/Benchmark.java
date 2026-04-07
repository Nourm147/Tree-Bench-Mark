package benchmark;

import java.util.function.Supplier;

import Algorithms.BinarySearchTree;
import Input.BenchmarkInput;

public class Benchmark {

    private static final int RUNS = 8;
    private static final int WARMUP_RUNS = 3;

    // use a Supplier to create a new tree instance for each run with the same type
    public BenchmarkData measureInsert(Supplier<BinarySearchTree> treeFactory,
            BenchmarkInput benchmarkInput, String label) {
        long[] times = new long[RUNS - WARMUP_RUNS];
        int lastHeight = -1;

        for (int i = 0; i < RUNS; i++) {
            BinarySearchTree tree = treeFactory.get(); // fresh empty tree each run
            long start = System.nanoTime();
            for (int j : benchmarkInput.getInsertData()) {
                tree.insert(j);
            }
            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = System.nanoTime() - start;
                lastHeight = tree.height();
            }
        }

        return new BenchmarkData(label, times, lastHeight);
    }

    public BenchmarkData measureContains(Supplier<BinarySearchTree> treeFactory,
            BenchmarkInput input, String label, int treeHeight) {
        long[] times = new long[RUNS - WARMUP_RUNS];

        for (int i = 0; i < RUNS; i++) {
            BinarySearchTree tree = buildTree(treeFactory, input.getInsertData());
            long start = System.nanoTime();
            for (int j : input.getPresentLookups()) {
                tree.contains(j);
            }
            for (int j : input.getAbsentLookups()) {
                tree.contains(j);
            }
            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = System.nanoTime() - start;
            }
        }

        return new BenchmarkData(label, times, treeHeight);
    }

    public BenchmarkData measureDelete(Supplier<BinarySearchTree> treeFactory,
            BenchmarkInput input, String label, int treeHeight) {

        long[] times = new long[RUNS - WARMUP_RUNS];

        for (int i = 0; i < RUNS; i++) {
            BinarySearchTree tree = buildTree(treeFactory, input.getInsertData());
            long start = System.nanoTime();
            for (int j : input.getDeleteTargets()) {
                tree.delete(j);
            }
            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = System.nanoTime() - start;
            }
        }

        return new BenchmarkData(label, times, treeHeight);
    }

    public BenchmarkData measureSorting(Supplier<BinarySearchTree> treeFactory,
            BenchmarkInput input, String label, int treeHeight) {

        long[] times = new long[RUNS - WARMUP_RUNS];

        // messure the total time (build + in-order traversal) to get the sorted output
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            BinarySearchTree tree = buildTree(treeFactory, input.getInsertData());
            tree.inOrder();
            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = System.nanoTime() - start;
            }
        }

        return new BenchmarkData(label, times, treeHeight);
    }

    public BenchmarkData measureQuickSort(int[] data, String label) {
        long[] times = new long[RUNS - WARMUP_RUNS];

        for (int i = 0; i < RUNS; i++) {
            int[] copy = data.clone(); // sort a fresh copy each run
            long start = System.nanoTime();
            new Algorithms.QuickSort().performSort(copy);
            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = System.nanoTime() - start;
            }
        }

        return new BenchmarkData(label, times, -1);
    }

    private BinarySearchTree buildTree(Supplier<BinarySearchTree> treeFactory, int[] data) {
        BinarySearchTree tree = treeFactory.get();
        for (int v : data) {
            tree.insert(v);
        }
        return tree;
    }

}
