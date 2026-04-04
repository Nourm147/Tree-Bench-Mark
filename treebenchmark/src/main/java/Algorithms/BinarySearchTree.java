package Algorithms;

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
            afterInsert(newNode);
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

        // Find the node
        BinaryTreeNode node = root;
        while (node != nil) {
            int cmp = Integer.compare(v, node.getVal());
            if (cmp < 0) {
                node = node.getLeft();
            } else if (cmp > 0) {
                node = node.getRight();
            } else {
                break;
            }
        }
        if (node == nil) {
            return false;
        }

        // Two children reduce to one/zero child case
        if (node.getLeft() != nil && node.getRight() != nil) {
            BinaryTreeNode successor = findMin(node.getRight());
            node.setVal(successor.getVal());
            node = successor;  // successor has at most one child (no left child)
        }

        // Step 3: node now has at most one child
        BinaryTreeNode parent = node.getParent();
        BinaryTreeNode child = (node.getLeft() != nil) ? node.getLeft() : node.getRight();

        if (child != nil) {
            child.setParent(parent);
        }

        if (parent == nil) {
            root = child;
        } else if (parent.getLeft() == node) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }

        afterDelete(node, parent);
        return true;
    }

    @Override
    protected BinaryTreeNode createNode(int v) {
        BinaryTreeNode newNode = new BinaryTreeNode(v);
        newNode.setLeft(nil);
        newNode.setRight(nil);
        newNode.setParent(nil);
        return newNode;
    }

}
