package Input;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BenchmarkInput {

    private final int[] insertData;
    private final int[] presentLookups;
    private final int[] absentLookups;
    private final int[] deleteTargets;

    public BenchmarkInput(int n, InputDistribution dist) {
        insertData = generateInsertData(n, dist);
        presentLookups = generateSample(insertData, n / 2);
        absentLookups = generateAbsent(insertData, n / 2);
        deleteTargets = generateSample(insertData, n / 5);
    }

    // Getters
    public int[] getInsertData() {
        return insertData;
    }

    public int[] getPresentLookups() {
        return presentLookups;
    }

    public int[] getAbsentLookups() {
        return absentLookups;
    }

    public int[] getDeleteTargets() {
        return deleteTargets;
    }

    private int[] generateInsertData(int n, InputDistribution dist) {
        switch (dist) {
            case RANDOM:
                return InputGenerator.generateRandomArray(n);
            case NEARLY_SORTED1:
                return InputGenerator.generateSemiSortedArray(n, 1);
            case NEARLY_SORTED5:
                return InputGenerator.generateSemiSortedArray(n, 5);
            case NEARLY_SORTED10:
                return InputGenerator.generateSemiSortedArray(n, 10);
        }
        throw new IllegalArgumentException("Unknown distribution: " + dist);
    }

    private int[] generateAbsent(int[] inserted, int count) {
        Set<Integer> present = new HashSet<>();
        for (int v : inserted) {
            present.add(v);
        }

        List<Integer> absent = new ArrayList<>();
        while (absent.size() < count) {
            int candidate = (int) (Math.random() * 10 * inserted.length);
            if (!present.contains(candidate)) {
                absent.add(candidate);
            }
        }
        return absent.stream().mapToInt(Integer::intValue).toArray();
    }

    private int[] generateSample(int[] source, int count) {
        int[] sample = new int[count];
        for (int i = 0; i < count; i++) {
            int idx = (int) (Math.random() * source.length);
            sample[i] = source[idx];
        }
        return sample;
    }
}
