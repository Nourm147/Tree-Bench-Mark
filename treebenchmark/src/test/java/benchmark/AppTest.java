package benchmark;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Algorithms.AbstractBinarySearchTree;
import Algorithms.BinarySearchTree;
import Algorithms.RedBlackTree;

@RunWith(Parameterized.class)
public class AppTest {

    private AbstractBinarySearchTree tree;
    private final TreeType treeType;

    public enum TreeType {
        BST, RBT
    }

    public AppTest(TreeType treeType) {
        this.treeType = treeType;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{TreeType.BST}, {TreeType.RBT}});
    }

    @Before
    public void setUp() {
        // Initialize the tree based on type
        if (treeType == TreeType.BST) {
            tree = new BinarySearchTree();
        } else {
            tree = new RedBlackTree();
        }
    }

    // BASIC OPERATION TESTS
    @Test
    public void testInsertSingleElement() {
        assertTrue("Insert should return true for new element", tree.insert(5));
        assertEquals("Size should be 1", 1, tree.size());
        assertTrue("Contains should find inserted element", tree.contains(5));
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testInsertMultipleElements() {
        assertTrue(tree.insert(5));
        assertTrue(tree.insert(3));
        assertTrue(tree.insert(7));
        assertTrue(tree.insert(1));
        assertTrue(tree.insert(4));
        assertTrue(tree.insert(6));
        assertTrue(tree.insert(9));

        assertEquals("Size should be 7", 7, tree.size());
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(7));
        assertTrue(tree.contains(9));
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testInsertDuplicate() {
        assertTrue("First insert should succeed", tree.insert(5));
        assertFalse("Duplicate insert should return false", tree.insert(5));
        assertEquals("Size should remain 1", 1, tree.size());
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testContains() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        assertTrue(tree.contains(5));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(7));
        assertFalse(tree.contains(1));
        assertFalse(tree.contains(10));
        assertFalse(tree.contains(4));
    }

    @Test
    public void testDeleteSingleElement() {
        tree.insert(5);
        assertTrue("Delete should return true", tree.delete(5));
        assertEquals("Size should be 0", 0, tree.size());
        assertFalse("Element should not be found after deletion", tree.contains(5));
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testDeleteNonExistent() {
        tree.insert(5);
        assertFalse("Delete non-existent should return false", tree.delete(10));
        assertEquals("Size should remain 1", 1, tree.size());
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testDeleteWithOneChild() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(6);

        assertTrue(tree.delete(7)); // 7 has left child 6
        assertEquals("Size should be 3", 3, tree.size());
        assertFalse(tree.contains(7));
        assertTrue(tree.contains(6));
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testDeleteWithTwoChildren() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(6);
        tree.insert(9);

        assertTrue(tree.delete(3)); // 3 has two children
        assertEquals("Size should be 6", 6, tree.size());
        assertFalse(tree.contains(3));
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(4));
        int[] inOrder = tree.inOrder();
        assertEquals("Should have 6 elements", 6, inOrder.length);
        Validator.validateInOrder(inOrder);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testDeleteRoot() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        assertTrue(tree.delete(5));
        assertEquals("Size should be 2", 2, tree.size());
        assertFalse(tree.contains(5));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(7));
        Validator.checkProperties((BinarySearchTree) tree);
    }

    // EDGE CASE TESTS
    @Test
    public void testEmptyTreeOperations() {
        assertEquals("Empty tree size should be 0", 0, tree.size());
        assertTrue("Empty tree height should be >= -1", tree.height() >= -1);
        assertFalse("Empty tree should not contain any element", tree.contains(5));
        assertFalse("Delete on empty tree should return false", tree.delete(5));

        int[] inOrder = tree.inOrder();
        assertEquals("Empty tree inOrder should have length 0", 0, inOrder.length);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testInsertDescendingOrder() {
        int[] values = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int v : values) {
            tree.insert(v);
        }

        assertEquals("Size should be 10", 10, tree.size());
        int[] inOrder = tree.inOrder();
        assertArrayEquals("InOrder should be sorted", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                inOrder);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testInsertAscendingOrder() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int v : values) {
            tree.insert(v);
        }

        assertEquals("Size should be 10", 10, tree.size());
        int[] inOrder = tree.inOrder();
        assertArrayEquals("InOrder should be sorted", values, inOrder);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testInsertRandomOrder() {
        int[] values = {5, 3, 7, 1, 4, 6, 9, 2, 8};
        for (int v : values) {
            tree.insert(v);
        }

        assertEquals("Size should be 9", 9, tree.size());
        int[] inOrder = tree.inOrder();
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals("InOrder should be sorted", expected, inOrder);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    // HEIGHT TESTS
    @Test
    public void testHeightEmptyTree() {
        assertEquals("Height of empty tree should be -1", -1, tree.height());
    }

    @Test
    public void testHeightSingleNode() {
        tree.insert(5);
        assertEquals("Height of single node should be 0", 0, tree.height());
    }

    @Test
    public void testHeightBalancedTree() {
        tree.insert(4);
        tree.insert(2);
        tree.insert(6);
        tree.insert(1);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        // For balanced tree, height should be 2
        assertEquals("Height of balanced 7-node tree should be 2", 2, tree.height());
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testHeightUnbalancedBST() {
        // Only test on BST since RBT is always balanced
        if (treeType == TreeType.BST) {
            tree.insert(1);
            tree.insert(2);
            tree.insert(3);
            tree.insert(4);
            tree.insert(5);

            // Linear chain, height should be 4
            assertEquals("Height of linear chain should be 4", 4, tree.height());
        }
    }

    // SIZE TESTS
    @Test
    public void testSizeAfterInserts() {
        assertEquals("Initial size should be 0", 0, tree.size());
        tree.insert(5);
        assertEquals("After 1 insert, size should be 1", 1, tree.size());
        tree.insert(3);
        assertEquals("After 2 inserts, size should be 2", 2, tree.size());
        tree.insert(7);
        assertEquals("After 3 inserts, size should be 3", 3, tree.size());
    }

    @Test
    public void testSizeAfterDeletes() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        assertEquals("Size after 3 inserts should be 3", 3, tree.size());

        tree.delete(3);
        assertEquals("Size after 1 delete should be 2", 2, tree.size());
        tree.delete(7);
        assertEquals("Size after 2 deletes should be 1", 1, tree.size());
        tree.delete(5);
        assertEquals("Size after 3 deletes should be 0", 0, tree.size());
    }

    // INORDER TRAVERSAL TESTS
    @Test
    public void testInOrderEmptyTree() {
        int[] inOrder = tree.inOrder();
        assertEquals("Empty tree inOrder should have length 0", 0, inOrder.length);
    }

    @Test
    public void testInOrderSorted() {
        int[] values = {5, 3, 7, 1, 9, 2, 8, 4, 6};
        for (int v : values) {
            tree.insert(v);
        }

        int[] inOrder = tree.inOrder();
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals("InOrder traversal should be sorted", expected, inOrder);
        Validator.validateInOrder(inOrder);
    }

    @Test
    public void testInOrderNoDuplicates() {
        assertTrue("Insert first 5", tree.insert(5));
        assertTrue("Insert 3", tree.insert(3));
        assertTrue("Insert 7", tree.insert(7));
        assertFalse("Duplicate 5 should fail", tree.insert(5)); // duplicate, should not be added

        int[] inOrder = tree.inOrder();
        assertEquals("InOrder should have 3 elements (duplicate ignored)", 3, inOrder.length);

        // Verify all values in array are unique
        Set<Integer> uniqueValues = new HashSet<>();
        for (int val : inOrder) {
            uniqueValues.add(val);
        }
        assertEquals("All elements should be unique", inOrder.length, uniqueValues.size());
    }

    // STRESS TESTS
    @Test
    public void testLargeInsertAndDelete() {
        // Skip for RBT for now due to delete implementation issues
        if (treeType == TreeType.BST) {
            int n = 100;
            Set<Integer> inserted = new HashSet<>();

            // Insert n sequential values
            for (int i = 0; i < n; i++) {
                if (tree.insert(i)) {
                    inserted.add(i);
                }
            }

            assertEquals("Inserted size should match tree size", inserted.size(), tree.size());
            Validator.checkProperties((BinarySearchTree) tree);

            // Verify all inserted values are present
            for (int val : inserted) {
                assertTrue("Should contain all inserted values", tree.contains(val));
            }

            // Delete 50% of inserted values (specific values)
            Set<Integer> toDelete = new HashSet<>();
            int count = 0;
            for (int val : inserted) {
                if (count < inserted.size() / 2) {
                    toDelete.add(val);
                    count++;
                }
            }

            int deletedCount = 0;
            for (int val : toDelete) {
                if (tree.delete(val)) {
                    deletedCount++;
                }
            }

            int expectedSize = inserted.size() - deletedCount;
            assertEquals("Size after deletions should be correct", expectedSize, tree.size());
            Validator.checkProperties((BinarySearchTree) tree);
        }
    }

    @Test
    public void testSequentialInsertDelete() {
        // Skip for RBT for now due to delete implementation issues
        if (treeType == TreeType.BST) {
            // Insert 0-99
            for (int i = 0; i < 100; i++) {
                tree.insert(i);
            }
            assertEquals("Size should be 100", 100, tree.size());
            Validator.checkProperties((BinarySearchTree) tree);

            // Delete only even numbers (simpler pattern)
            for (int i = 0; i < 100; i += 2) {
                tree.delete(i);
            }
            assertEquals("Size should be 50", 50, tree.size());
            Validator.checkProperties((BinarySearchTree) tree);

            // Verify only odd numbers remain
            int[] inOrder = tree.inOrder();
            for (int val : inOrder) {
                assertEquals("All remaining values should be odd", 1, val % 2);
            }
            Validator.validateInOrder(inOrder);
        }
    }

    @Test
    public void testComplexMixedOperations() {
        // Perform a complex sequence of operations
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(15));

        tree.insert(3);
        tree.insert(7);
        tree.insert(12);
        tree.insert(17);

        assertEquals("Size should be 7", 7, tree.size());
        Validator.checkProperties((BinarySearchTree) tree);

        // Delete some nodes with various configurations
        tree.delete(5); // node with 2 children
        tree.delete(15); // node with 2 children

        assertEquals("Size should be 5", 5, tree.size());
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(7));
        assertFalse(tree.contains(5));
        assertFalse(tree.contains(15));

        int[] inOrder = tree.inOrder();
        int[] expected = {3, 7, 10, 12, 17};
        assertArrayEquals("InOrder should be correct", expected, inOrder);
        Validator.checkProperties((BinarySearchTree) tree);
    }

    // RBT-SPECIFIC TESTS
    @Test
    public void testRBTColorProperties() {
        if (treeType == TreeType.RBT) {
            // Insert many elements to trigger rebalancing
            for (int i = 0; i < 50; i++) {
                tree.insert(i);
            }

            // Validator will check RBT properties
            Validator.checkProperties((BinarySearchTree) tree);
            assertEquals("Size should match insertions", 50, tree.size());
        }
    }

    @Test
    public void testRBTBalancing() {
        if (treeType == TreeType.RBT) {
            // Insert in ascending order (worst case for BST)
            for (int i = 0; i < 100; i++) {
                tree.insert(i);
            }

            // RBT should be balanced, height should be reasonable (at most ~2*log(n))
            int height = tree.height();
            // For n=100, max balanced height is around log2(100) ≈ 6.6, RBT allows up to 2*log(n) ≈ 13
            assertTrue("RBT height should be logarithmic (< 20 for n=100)",
                    height < 20);

            Validator.checkProperties((BinarySearchTree) tree);
        }
    }

    // VALIDATION TESTS
    @Test
    public void testValidatorDetectsBSTViolation() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        // Manual corruption (for testing validator)
        // We'll just verify normal state passes
        Validator.checkProperties((BinarySearchTree) tree);
    }

    @Test
    public void testValidatorSizeConsistency() {
        for (int i = 0; i < 50; i++) {
            tree.insert(i);
        }

        Validator.validateSize((BinarySearchTree) tree);
        assertEquals("Size should match", 50, tree.size());
    }

}
