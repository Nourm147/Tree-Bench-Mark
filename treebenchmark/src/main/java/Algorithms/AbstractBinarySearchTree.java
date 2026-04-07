package Algorithms;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBinarySearchTree {

    protected BinaryTreeNode root;
    protected BinaryTreeNode nil;
    protected static final boolean VALIDATE = false;

    public BinaryTreeNode getRoot() {
        return root;
    }

    public BinaryTreeNode getNil() {
        return nil;
    }

    public void setRoot(BinaryTreeNode root) {
        this.root = root;
    }

    // public calls to private implementations
    public int height() {
        return height(root);
    }

    public int size() {
        return size(root);
    }

    public boolean contains(int v) {
        return contains(root, v);
    }

    public int[] inOrder() {
        List<Integer> list = new ArrayList<>();
        inOrder(root, list);
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    // abstract methods to be implemented by subclasses
    public abstract boolean insert(int v);

    public abstract boolean delete(int v);

    protected abstract BinaryTreeNode createNode(int v);

    // actual private implementations
    private void inOrder(BinaryTreeNode node, List<Integer> list) {
        if (node == nil) {
            return;
        }
        inOrder(node.getLeft(), list);
        list.add(node.getVal());
        inOrder(node.getRight(), list);
    }

    private boolean contains(BinaryTreeNode node, int v) {
        if (node == nil) {
            return false;
        }
        int cmp = Integer.compare(v, node.getVal());
        if (cmp < 0) {
            return contains(node.getLeft(), v);
        } else if (cmp > 0) {
            return contains(node.getRight(), v);
        } else {
            return true;
        }
    }

    private int size(BinaryTreeNode node) {
        if (node == nil) {
            return 0;
        }
        return 1 + size(node.getLeft()) + size(node.getRight());
    }

    private int height(BinaryTreeNode node) {
        if (node == nil) {
            return -1;  // zero based height
        }
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }

    // helper methods
    protected BinaryTreeNode findMin(BinaryTreeNode node) {
        if (node == nil) {
            return nil;
        }
        while (node.getLeft() != nil) {
            node = node.getLeft();
        }
        return node;
    }

    protected void afterInsert(BinaryTreeNode node) {
    }

    protected void afterDelete(BinaryTreeNode node, BinaryTreeNode parent, BinaryTreeNode deletedNode) {
    }

}
