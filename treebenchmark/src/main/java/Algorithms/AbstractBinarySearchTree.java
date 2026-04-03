package Algorithms;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBinarySearchTree<T extends Comparable<T>> {

    private TreeNode<T> root;
    private final TreeNode<T> nil;

    public AbstractBinarySearchTree() {
        nil = new TreeNode<>(null);
        root = nil;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    // public calls to private implementations
    public int height() {
        return height(root);
    }

    public int size() {
        return size(root);
    }

    public boolean contains(T v) {
        return contains(root, v);
    }

    public T[] inOrder() {
        List<T> list = new ArrayList<>();
        inOrder(root, list);
        return (T[]) list.toArray();
    }

    // abstract methods to be implemented by subclasses
    public abstract void insert(T v);

    public abstract void delete(T v);

    // actual private implementations
    private void inOrder(TreeNode<T> node, List<T> list) {
        if (node == nil) {
            return;
        }
        inOrder(node.getLeft(), list);
        list.add(node.getVal());
        inOrder(node.getRight(), list);
    }

    private boolean contains(TreeNode<T> node, T v) {
        if (node == nil) {
            return false;
        }
        int cmp = v.compareTo(node.getVal());
        if (cmp < 0) {
            return contains(node.getLeft(), v);
        } else if (cmp > 0) {
            return contains(node.getRight(), v);
        } else {
            return true;
        }
    }

    private int size(TreeNode node) {
        if (node == nil) {
            return 0;
        }
        return 1 + size(node.getLeft()) + size(node.getRight());
    }

    private int height(TreeNode node) {
        if (node == nil) {
            return -1;  // zero based height
        }
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }

}
