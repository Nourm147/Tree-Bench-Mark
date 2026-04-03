package Algorithms;

public class RBNode extends BinaryTreeNode {

    private boolean isRed;

    public RBNode(int x) {
        super(x);
        this.isRed = true;
    }

    public RBNode(int val, boolean isRed) {
        super(val);
        this.isRed = isRed;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }
}
