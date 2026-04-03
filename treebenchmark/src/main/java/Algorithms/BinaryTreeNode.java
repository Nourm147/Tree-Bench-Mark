package Algorithms;

public class BinaryTreeNode implements TreeNode {

    private int val;
    private BinaryTreeNode left;
    private BinaryTreeNode right;
    private BinaryTreeNode parent;

    public BinaryTreeNode(int x) {
        val = x;
    }

    @Override
    public int getVal() {
        return this.val;
    }

    @Override
    public BinaryTreeNode getLeft() {
        return this.left;
    }

    @Override
    public BinaryTreeNode getRight() {
        return this.right;
    }

    @Override
    public BinaryTreeNode getParent() {
        return this.parent;
    }

    @Override
    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public void setLeft(TreeNode left) {
        this.left = (BinaryTreeNode) left;
    }

    @Override
    public void setRight(TreeNode right) {
        this.right = (BinaryTreeNode) right;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = (BinaryTreeNode) parent;
    }

}
