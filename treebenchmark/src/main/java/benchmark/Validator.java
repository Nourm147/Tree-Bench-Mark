package benchmark;

import Algorithms.BinarySearchTree;
import Algorithms.BinaryTreeNode;
import Algorithms.RBNode;
import Algorithms.RedBlackTree;

public class Validator {

    private static class ValidationException extends RuntimeException {

        ValidationException(String message) {
            super("Validation failed: " + message);
        }
    }

    public static void checkProperties(BinarySearchTree tree) {
        BinaryTreeNode nil = tree.getNil();
        BinaryTreeNode root = tree.getRoot();

        if (nil == null) {
            throw new ValidationException("nil node is null");
        }

        if (root == nil) {
            return;
        }

        checkBSTProperty(root, nil);

        checkParentPointers(root, nil, nil);

        if (tree instanceof RedBlackTree redBlackTree) {
            checkRBProperties(redBlackTree);
        }
    }

    private static void checkBSTProperty(BinaryTreeNode node, BinaryTreeNode nil) {
        if (node == nil) {
            return;
        }

        int nodeVal = node.getVal();

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

        checkBSTProperty(node.getLeft(), nil);
        checkBSTProperty(node.getRight(), nil);
    }

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

    private static void checkRBProperties(RedBlackTree tree) {
        RBNode root = (RBNode) tree.getRoot();
        RBNode nil = (RBNode) tree.getNil();

        if (root != nil && root.isRed()) {
            throw new ValidationException("RBT property violation: root is red");
        }

        checkBlackHeight(root, nil);
    }

    private static int checkBlackHeight(RBNode node, RBNode nil) {
        if (node == nil) {
            return 1; // nil is considered black
        }

        // Check for red-red violation
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

        int leftHeight = checkBlackHeight((RBNode) node.getLeft(), nil);
        int rightHeight = checkBlackHeight((RBNode) node.getRight(), nil);

        // Check that black heights are equal
        if (leftHeight != rightHeight) {
            throw new ValidationException(
                    "RBT property violation: black height mismatch at node " + node.getVal()
                    + " (left: " + leftHeight + ", right: " + rightHeight + ")");
        }

        return node.isRed() ? leftHeight : leftHeight + 1;
    }

    public static void validateInOrder(int[] inOrder) {
        for (int i = 1; i < inOrder.length; i++) {
            if (inOrder[i] <= inOrder[i - 1]) {
                throw new ValidationException(
                        "InOrder traversal not sorted: " + inOrder[i - 1]
                        + " followed by " + inOrder[i]);
            }
        }
    }

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
