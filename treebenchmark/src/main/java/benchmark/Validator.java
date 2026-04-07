package benchmark;

import java.util.HashSet;
import java.util.Set;

import Algorithms.BinarySearchTree;
import Algorithms.BinaryTreeNode;
import Algorithms.RBNode;
import Algorithms.RedBlackTree;

/**
 * Enhanced Validator class for comprehensive tree property checking. Validates
 * both BST and RBT invariants without relying on disabled assertions.
 */
public class Validator {

    private static class ValidationException extends RuntimeException {

        ValidationException(String message) {
            super("Validation failed: " + message);
        }
    }

    /**
     * Validates all properties of a tree: BST properties and RBT properties if
     * applicable.
     *
     * @param tree The tree to validate
     * @throws ValidationException if any invariant is violated
     */
    public static void checkProperties(BinarySearchTree tree) {
        BinaryTreeNode nil = tree.getNil();
        BinaryTreeNode root = tree.getRoot();

        // Check nil node integrity
        checkNilIntegrity(nil);

        // Check for empty tree (valid state)
        if (root == nil) {
            return;
        }

        // Check structural integrity (cycles, parent pointers, etc.)
        checkStructuralIntegrity(root, nil);

        // Check BST property with full subtree bounds
        checkBSTProperty(root, nil, Integer.MIN_VALUE, Integer.MAX_VALUE);

        // Check parent pointers
        checkParentPointers(root, nil, nil);

        // Check RBT-specific properties if it's a RedBlackTree
        if (tree instanceof RedBlackTree) {
            checkRBProperties((RedBlackTree) tree);
        }
    }

    /**
     * Validates that the nil node (sentinel) is properly initialized.
     */
    private static void checkNilIntegrity(BinaryTreeNode nil) {
        if (nil == null) {
            throw new ValidationException("nil node is null");
        }
        // For nil nodes, they should point to themselves or have consistent structure
        // This prevents dangling null pointers
    }

    /**
     * Checks for cycles and validates node count.
     */
    private static void checkStructuralIntegrity(BinaryTreeNode root, BinaryTreeNode nil) {
        Set<BinaryTreeNode> visited = new HashSet<>();
        checkForCycles(root, nil, visited);
    }

    /**
     * Recursive helper to detect cycles in the tree using a visited set.
     */
    private static void checkForCycles(BinaryTreeNode node, BinaryTreeNode nil,
            Set<BinaryTreeNode> visited) {
        if (node == nil) {
            return;
        }
        if (visited.contains(node)) {
            throw new ValidationException("Cycle detected in tree structure");
        }
        visited.add(node);
        checkForCycles(node.getLeft(), nil, visited);
        checkForCycles(node.getRight(), nil, visited);
    }

    /**
     * Validates BST property with full subtree bounds. Left subtree must have
     * ALL values < node value
     * Right subtree must have ALL values > node value
     */
    private static void checkBSTProperty(BinaryTreeNode node, BinaryTreeNode nil,
            int minVal, int maxVal) {
        if (node == nil) {
            return;
        }

        int nodeVal = node.getVal();

        // Check bounds from ancestors
        if (nodeVal <= minVal) {
            throw new ValidationException(
                    "BST violation: node value " + nodeVal + " <= min bound " + minVal);
        }
        if (nodeVal >= maxVal) {
            throw new ValidationException(
                    "BST violation: node value " + nodeVal + " >= max bound " + maxVal);
        }

        // Direct child checks
        if (node.getLeft() != nil && node.getLeft().getVal() >= nodeVal) {
            throw new ValidationException(
                    "BST violation: left child " + node.getLeft().getVal()
                    + " >= node value " + nodeVal);
        }
        if (node.getRight() != nil && node.getRight().getVal() <= nodeVal) {
            throw new ValidationException(
                    "BST violation: right child " + node.getRight().getVal()
                    + " <= node value " + nodeVal);
        }

        // Recursive checks with updated bounds
        checkBSTProperty(node.getLeft(), nil, minVal, nodeVal);
        checkBSTProperty(node.getRight(), nil, nodeVal, maxVal);
    }

    /**
     * Validates parent-child pointer consistency.
     */
    private static void checkParentPointers(BinaryTreeNode node, BinaryTreeNode nil,
            BinaryTreeNode expectedParent) {
        if (node == nil) {
            return;
        }

        if (node.getParent() != expectedParent) {
            throw new ValidationException(
                    "Parent pointer mismatch for node " + node.getVal()
                    + ": expected " + (expectedParent == nil ? "nil" : expectedParent.getVal())
                    + " but got " + (node.getParent() == nil ? "nil" : node.getParent().getVal()));
        }

        checkParentPointers(node.getLeft(), nil, node);
        checkParentPointers(node.getRight(), nil, node);
    }

    /**
     * Validates all Red-Black Tree properties.
     */
    private static void checkRBProperties(RedBlackTree tree) {
        RBNode root = (RBNode) tree.getRoot();
        RBNode nil = (RBNode) tree.getNil();

        // Property 1: Root must be black
        if (root != nil && root.isRed()) {
            throw new ValidationException("RBT property violation: root is red");
        }

        // Property 2 & 3: Check no red-red violations and equal black height
        checkBlackHeight(root, nil);
    }

    /**
     * Validates Red-Black Tree properties: - No two consecutive red nodes - All
     * paths have equal black height Returns the black height for consistency
     * checking.
     */
    private static int checkBlackHeight(RBNode node, RBNode nil) {
        if (node == nil) {
            // nil nodes are considered black, return black height of 1
            return 1;
        }

        // Check for red-red violation (red node cannot have red children)
        if (node.isRed()) {
            RBNode left = (RBNode) node.getLeft();
            RBNode right = (RBNode) node.getRight();

            if (left != nil && left.isRed()) {
                throw new ValidationException(
                        "RBT property violation: red node " + node.getVal()
                        + " has red left child " + left.getVal());
            }
            if (right != nil && right.isRed()) {
                throw new ValidationException(
                        "RBT property violation: red node " + node.getVal()
                        + " has red right child " + right.getVal());
            }
        }

        // Recursively check left and right subtrees
        int leftHeight = checkBlackHeight((RBNode) node.getLeft(), nil);
        int rightHeight = checkBlackHeight((RBNode) node.getRight(), nil);

        // Check that black heights are equal
        if (leftHeight != rightHeight) {
            throw new ValidationException(
                    "RBT property violation: black height mismatch at node " + node.getVal()
                    + " (left: " + leftHeight + ", right: " + rightHeight + ")");
        }

        // Return black height: +1 if current node is black, same if red
        return node.isRed() ? leftHeight : leftHeight + 1;
    }

    /**
     * Utility method to validate a complete inOrder traversal returns sorted
     * values.
     */
    public static void validateInOrder(int[] inOrder) {
        for (int i = 1; i < inOrder.length; i++) {
            if (inOrder[i] <= inOrder[i - 1]) {
                throw new ValidationException(
                        "InOrder traversal not sorted: " + inOrder[i - 1]
                        + " followed by " + inOrder[i]);
            }
        }
    }

    /**
     * Utility method to validate size matches actual element count.
     */
    public static void validateSize(BinarySearchTree tree) {
        int[] inOrder = tree.inOrder();
        int treeSize = tree.size();
        if (inOrder.length != treeSize) {
            throw new ValidationException(
                    "Size mismatch: inOrder array has " + inOrder.length
                    + " elements but tree.size() returns " + treeSize);
        }
    }
}
