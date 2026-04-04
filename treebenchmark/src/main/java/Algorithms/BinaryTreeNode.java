package Algorithms;

public class BinaryTreeNode {

    private int val;
    private BinaryTreeNode left;
    private BinaryTreeNode right;
    private BinaryTreeNode parent;

    public BinaryTreeNode(int x) {
        val = x;
    }

    public int getVal() {
        return this.val;
    }

    public BinaryTreeNode getLeft() {
        return this.left;
    }

    public BinaryTreeNode getRight() {
        return this.right;
    }

    public BinaryTreeNode getParent() {
        return this.parent;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setLeft(BinaryTreeNode left) {
        this.left = left;
    }

    public void setRight(BinaryTreeNode right) {
        this.right = right;
    }

    public void setParent(BinaryTreeNode parent) {
        this.parent = parent;
    }
}
