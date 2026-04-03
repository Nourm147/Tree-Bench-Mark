package Algorithms;

public interface TreeNode {

    int getVal();

    TreeNode getLeft();

    TreeNode getParent();

    TreeNode getRight();

    void setVal(int val);

    void setLeft(TreeNode left);

    void setRight(TreeNode right);

    void setParent(TreeNode parent);

}
