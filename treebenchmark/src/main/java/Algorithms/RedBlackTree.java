package Algorithms;

public class RedBlackTree extends BinarySearchTree {

    public RedBlackTree() {
        nil = new RBNode(0);
        nil.setLeft(nil);
        nil.setRight(nil);
        nil.setParent(nil);
        rb(nil).setRed(false);
        root = nil;
    }

    @Override
    protected void afterInsert(BinaryTreeNode node) {
        fixInsert((RBNode) node);
    }

    @Override
    protected void afterDelete(BinaryTreeNode node, BinaryTreeNode parent) {
        fixDelete((RBNode) node, (RBNode) parent);
    }

    private void fixInsert(RBNode node) {
        while (rb(node.getParent()).isRed()) {
            if (node.getParent() == node.getParent().getParent().getLeft()) {
                RBNode uncle = rb(node.getParent().getParent().getRight());
                if (uncle.isRed()) {
                    rb(node.getParent()).setRed(false);
                    uncle.setRed(false);
                    rb(node.getParent().getParent()).setRed(true);
                    node = rb(node.getParent().getParent());
                } else {
                    if (node == node.getParent().getRight()) {
                        node = rb(node.getParent());
                        rotateLeft(node);
                    }
                    rb(node.getParent()).setRed(false);
                    rb(node.getParent().getParent()).setRed(true);
                    rotateRight(rb(node.getParent().getParent()));
                }
            } else {
                RBNode uncle = rb(node.getParent().getParent().getLeft());
                if (uncle.isRed()) {
                    rb(node.getParent()).setRed(false);
                    uncle.setRed(false);
                    rb(node.getParent().getParent()).setRed(true);
                    node = rb(node.getParent().getParent());
                } else {
                    if (node == node.getParent().getLeft()) {
                        node = rb(node.getParent());
                        rotateRight(node);
                    }
                    rb(node.getParent()).setRed(false);
                    rb(node.getParent().getParent()).setRed(true);
                    rotateLeft(rb(node.getParent().getParent()));
                }
            }
            rb(root).setRed(false);
        }

    }

    private void fixDelete(RBNode node, RBNode parent) {
        while (node != root && !rb(node).isRed()) {
            if (node == parent.getLeft()) {
                RBNode sibling = rb(parent.getRight());
                if (sibling.isRed()) {
                    sibling.setRed(false);
                    rb(parent).setRed(true);
                    rotateLeft(parent);
                    sibling = rb(parent.getRight());
                }
                if (!rb(sibling.getLeft()).isRed() && !rb(sibling.getRight()).isRed()) {
                    sibling.setRed(true);
                    node = parent;
                    parent = rb(node.getParent());
                } else {
                    if (!rb(sibling.getRight()).isRed()) {
                        rb(sibling.getLeft()).setRed(false);
                        sibling.setRed(true);
                        rotateRight(sibling);
                        sibling = rb(parent.getRight());
                    }
                    sibling.setRed(rb(parent).isRed());
                    rb(parent).setRed(false);
                    rb(sibling.getRight()).setRed(false);
                    rotateLeft(parent);
                    node = rb(root);
                }
            } else {
                RBNode sibling = rb(parent.getLeft());
                if (sibling.isRed()) {
                    sibling.setRed(false);
                    rb(parent).setRed(true);
                    rotateRight(parent);
                    sibling = rb(parent.getLeft());
                }
                if (!rb(sibling.getLeft()).isRed() && !rb(sibling.getRight()).isRed()) {
                    sibling.setRed(true);
                    node = parent;
                    parent = rb(node.getParent());
                } else {
                    if (!rb(sibling.getLeft()).isRed()) {
                        rb(sibling.getRight()).setRed(false);
                        sibling.setRed(true);
                        rotateLeft(sibling);
                        sibling = rb(parent.getLeft());
                    }
                    sibling.setRed(rb(parent).isRed());
                    rb(parent).setRed(false);
                    rb(sibling.getLeft()).setRed(false);
                    rotateRight(parent);
                    node = rb(root);
                }
            }
        }
        rb(node).setRed(false);
    }

    private void rotateLeft(RBNode node) {
        RBNode right = rb(node.getRight());
        node.setRight(right.getLeft());
        if (right.getLeft() != nil) {
            right.getLeft().setParent(node);
        }
        right.setParent(node.getParent());
        if (node.getParent() == nil) {
            root = right;
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(right);
        } else {
            node.getParent().setRight(right);
        }
        right.setLeft(node);
        node.setParent(right);
    }

    private void rotateRight(RBNode node) {
        RBNode left = rb(node.getLeft());
        node.setLeft(left.getRight());
        if (left.getRight() != nil) {
            left.getRight().setParent(node);
        }
        left.setParent(node.getParent());
        if (node.getParent() == nil) {
            root = left;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(left);
        } else {
            node.getParent().setLeft(left);
        }
        left.setRight(node);
        node.setParent(left);
    }

    @Override
    protected BinaryTreeNode createNode(int v) {
        return new RBNode(v);
    }

    private RBNode rb(BinaryTreeNode node) {
        return (RBNode) node;
    }

}
