package com.securebank.bst;

import com.securebank.model.FlaggedTransaction;

/**
 * Single node in the BST. Holds one flagged transaction and left/right children.
 */
public class BstNode {

    private FlaggedTransaction transaction;
    private BstNode left;
    private BstNode right;

    public BstNode(FlaggedTransaction transaction) {
        this.transaction = transaction;
        this.left = null;
        this.right = null;
    }

    public FlaggedTransaction getTransaction() { return transaction; }
    public void setTransaction(FlaggedTransaction transaction) { this.transaction = transaction; }

    public BstNode getLeft() { return left; }
    public void setLeft(BstNode left) { this.left = left; }

    public BstNode getRight() { return right; }
    public void setRight(BstNode right) { this.right = right; }

    /** Returns the transaction ID used for BST ordering. */
    public String getKey() {
        return transaction != null ? transaction.getTransactionId() : null;
    }

    /** True if this node has no children (leaf). */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /** True if this node has exactly one child. */
    public boolean hasOneChild() {
        return (left != null && right == null) || (left == null && right != null);
    }

    /** True if this node has two children. */
    public boolean hasTwoChildren() {
        return left != null && right != null;
    }
}
