# BST vs Red-Black Tree — Benchmarking Suite

A comparative benchmarking project for two tree data structures — a standard Binary Search Tree (BST) and a Red-Black Tree (RBT) — tested across multiple input distributions to evaluate real-world performance tradeoffs.

---

## Overview

This project implements and benchmarks two tree structures:

- **Binary Search Tree (BST):** A standard iterative BST with a sentinel `nil` node. Deletion is implemented iteratively to prevent stack overflow on degenerate (nearly-sorted) inputs.
- **Red-Black Tree (RBT):** Extends the BST with color-based rebalancing (`fixInsert`, `fixDelete`) and rotations, guaranteeing O(log n) height for all input distributions.

---

## Input Distributions

All benchmarks run at N = 100,000 elements across four distributions:

| Distribution      | Description                        |
|-------------------|------------------------------------|
| `RANDOM`          | Uniformly random integers          |
| `NEARLY_SORTED1`  | Sorted with 1% random swaps        |
| `NEARLY_SORTED5`  | Sorted with 5% random swaps        |
| `NEARLY_SORTED10` | Sorted with 10% random swaps       |

---

## Benchmarks

Four operations are measured:

- **Insert** — inserting all N elements into an empty tree
- **Contains** — 50,000 present + 50,000 absent lookups
- **Delete** — removal of 20% of inserted elements (chosen randomly)
- **Sort** — insert all N elements then perform one in-order traversal (compared against QuickSort)

Each benchmark reports mean, median, and standard deviation in milliseconds.

---

## Key Results

### Tree Height After Insertion

| Distribution      | BST Height | RBT Height |
|-------------------|------------|------------|
| RANDOM            | 41         | 20         |
| NEARLY_SORTED1    | 3613       | 29         |
| NEARLY_SORTED5    | 809        | 29         |
| NEARLY_SORTED10   | 1026       | 28         |

The RBT maintains a stable height of ~29 across all distributions. The BST degrades to heights exceeding 3600 on nearly-sorted input — effectively a linked list.

### Summary of Findings

- The RBT's O(log n) height guarantee is its defining advantage. On random data its constant rebalancing overhead makes it comparable or slightly slower than BST; on adversarial inputs the gap becomes orders of magnitude.
- BST insert is faster than RBT insert on all distributions due to the absence of rebalancing cost (BST: 13.84 ms vs RBT: 16.67 ms on random data).
- On `NEARLY_SORTED1`, RBT insert is **9.57×** faster and RBT contains is **14.10×** faster than BST.
- QuickSort is the fastest sorting method in all cases — tree-based sorting is penalised by heap allocation and pointer-chasing overhead.
- Standard deviation is low (< 1 ms) for most measurements, indicating stable and reproducible results after JVM warm-up.

---

## Project Structure

```
src
 ┣ main
 ┃ ┣ Python
 ┃ ┃ ┗ visualize.py              # Generates benchmark charts
 ┃ ┣ java
 ┃ ┃ ┣ Algorithms
 ┃ ┃ ┃ ┣ AbstractBinarySearchTree.java
 ┃ ┃ ┃ ┣ BinarySearchTree.java
 ┃ ┃ ┃ ┣ BinaryTreeNode.java
 ┃ ┃ ┃ ┣ QuickSort.java
 ┃ ┃ ┃ ┣ RBNode.java
 ┃ ┃ ┃ ┗ RedBlackTree.java
 ┃ ┃ ┣ Input
 ┃ ┃ ┃ ┣ BenchmarkInput.java
 ┃ ┃ ┃ ┣ InputDistribution.java  # Enum for RANDOM, NEARLY_SORTED*
 ┃ ┃ ┃ ┗ InputGenerator.java
 ┃ ┃ ┣ Utils
 ┃ ┃ ┃ ┗ Pair.java
 ┃ ┃ ┗ benchmark
 ┃ ┃ ┃ ┣ App.java               # Entry point
 ┃ ┃ ┃ ┣ Benchmark.java
 ┃ ┃ ┃ ┣ BenchmarkData.java
 ┃ ┃ ┃ ┣ BenchmarkExporter.java  # Exports results (CSV/JSON)
 ┃ ┃ ┃ ┣ BenchmarkRunner.java
 ┃ ┃ ┃ ┗ Validator.java
 ┃ ┗ resources
 ┃ ┃ ┗ logback.xml
 ┗ test
   ┗ java
     ┗ benchmark
       ┗ AppTest.java
```

---

## Running the Benchmarks

```bash
# Compile
javac src/*.java -d out/

# Run benchmarks
java -cp out/ benchmark.App

# Generate charts (requires Python + matplotlib)
python src/main/Python/visualize.py
```

> JVM warm-up is performed before each benchmark run to ensure stable timing results.

---

## Requirements

- Java 11+
- Python 3 with `matplotlib` (for chart generation via `visualize.py`)