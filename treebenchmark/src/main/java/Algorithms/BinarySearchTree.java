package Algorithms;

import Utils.Pair;

public class BinarySearchTree extends AbstractBinarySearchTree {

    public BinarySearchTree() {
        nil = new BinaryTreeNode(0);
        nil.setLeft(nil);
        nil.setRight(nil);
        nil.setParent(nil);
        root = nil;
    }

    @Override
    public boolean insert(int v) {
        BinaryTreeNode newNode = createNode(v);
        if (root == nil) {
            root = newNode;
            return true;
        }
        BinaryTreeNode current = root;
        while (current != nil) {
            int cmp = Integer.compare(v, current.getVal());
            if (cmp < 0) {
                if (current.getLeft() == nil) {
                    current.setLeft(newNode);
                    newNode.setParent(current);
                    afterInsert(newNode); // called for RedBlackTree Insertion fix
                    return true;
                }
                current = current.getLeft();
            } else if (cmp > 0) {
                if (current.getRight() == nil) {
                    current.setRight(newNode);
                    newNode.setParent(current);
                    afterInsert(newNode);
                    return true;
                }
                current = current.getRight();
            } else {
                return false; // duplicate value
            }
        }
        return false; // shouldn't reach here
    }

    @Override
    public boolean delete(int v) {
        if (root == nil) {
            return false;
        }
        Pair<BinaryTreeNode, Boolean> result = delete(root, nil, v);
        root = result.first;
        return result.second;
    }

    private Pair<BinaryTreeNode, Boolean> delete(BinaryTreeNode node, BinaryTreeNode parent, int v) {
        if (node == nil) {
            return new Pair<>(nil, false);
        }

        int cmp = Integer.compare(v, node.getVal());
        if (cmp < 0) {
            Pair<BinaryTreeNode, Boolean> result = delete(node.getLeft(), node, v);
            node.setLeft(result.first);
            return new Pair<>(node, result.second);
        } else if (cmp > 0) {
            Pair<BinaryTreeNode, Boolean> result = delete(node.getRight(), node, v);
            node.setRight(result.first);
            return new Pair<>(node, result.second);
        } else {
            // Leaf
            if (node.getLeft() == nil && node.getRight() == nil) {
                afterDelete(node, parent); // called for RedBlackTree Deletion fix
                return new Pair<>(nil, true);
            }
            // One child
            if (node.getLeft() == nil) {
                node.getRight().setParent(parent);
                afterDelete(node, parent);
                return new Pair<>(node.getRight(), true);
            }
            if (node.getRight() == nil) {
                node.getLeft().setParent(parent);
                afterDelete(node, parent);
                return new Pair<>(node.getLeft(), true);
            }
            // Two children
            /// replace with the successor
            BinaryTreeNode successor = findMin(node.getRight());
            BinaryTreeNode successorParent = successor.getParent();

            node.setVal(successor.getVal());
            node.setRight(delete(node.getRight(), node, successor.getVal()).first);
            afterDelete(node, successorParent);
            return new Pair<>(node, true);
        }
    }

    @Override
    protected BinaryTreeNode createNode(int v) {
        return new BinaryTreeNode(v);
    }

}
