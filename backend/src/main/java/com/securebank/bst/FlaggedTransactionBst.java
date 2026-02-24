package com.securebank.bst;

import com.securebank.model.FlaggedTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree for flagged transactions, keyed by transaction ID.
 * Provides insert, search, and delete while maintaining BST invariant:
 * left subtree keys < node key < right subtree keys.
 */
public class FlaggedTransactionBst {

    private BstNode root;
    private int size;

    public FlaggedTransactionBst() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Inserts a new flagged transaction into the BST.
     * Maintains structured hierarchy: smaller IDs go left, larger IDs go right.
     * Duplicate transaction IDs are rejected (idempotent by ID).
     */
    public boolean insert(FlaggedTransaction transaction) {
        if (transaction == null || transaction.getTransactionId() == null || transaction.getTransactionId().isBlank()) {
            return false;
        }
        BstNode prev = null;
        BstNode curr = root;
        int cmp;
        while (curr != null) {
            cmp = transaction.getTransactionId().compareTo(curr.getKey());
            if (cmp == 0) {
                // Duplicate ID: replace payload, structure unchanged
                curr.setTransaction(transaction);
                return true;
            }
            prev = curr;
            curr = cmp < 0 ? curr.getLeft() : curr.getRight();
        }
        BstNode newNode = new BstNode(transaction);
        if (prev == null) {
            root = newNode;
        } else {
            cmp = transaction.getTransactionId().compareTo(prev.getKey());
            if (cmp < 0) prev.setLeft(newNode);
            else prev.setRight(newNode);
        }
        size++;
        return true;
    }

    /**
     * Searches for a flagged transaction by transaction ID.
     * BST search: O(log n) average when tree is balanced, O(n) worst case (skewed).
     */
    public BstNode search(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) return null;
        BstNode curr = root;
        int cmp;
        while (curr != null) {
            cmp = transactionId.compareTo(curr.getKey());
            if (cmp == 0) return curr;
            curr = cmp < 0 ? curr.getLeft() : curr.getRight();
        }
        return null;
    }

    /**
     * Search with comparison count for efficiency comparison vs unordered list.
     */
    public SearchOutcome searchWithCount(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) {
            return new SearchOutcome(null, 0);
        }
        int comparisons = 0;
        BstNode curr = root;
        int cmp;
        while (curr != null) {
            comparisons++;
            cmp = transactionId.compareTo(curr.getKey());
            if (cmp == 0) return new SearchOutcome(curr, comparisons);
            curr = cmp < 0 ? curr.getLeft() : curr.getRight();
        }
        return new SearchOutcome(null, comparisons);
    }

    public static class SearchOutcome {
        public final BstNode node;
        public final int comparisons;

        public SearchOutcome(BstNode node, int comparisons) {
            this.node = node;
            this.comparisons = comparisons;
        }
    }

    /**
     * Deletes the node with the given transaction ID.
     * Handles three cases: leaf, one child, two children.
     * Returns deletion result with type for API explanation.
     */
    public DeleteResult deleteWithType(String transactionId) {
        if (transactionId == null || transactionId.isBlank() || root == null) {
            return new DeleteResult(false, null);
        }
        BstNode parent = null;
        BstNode curr = root;
        int cmp;
        while (curr != null) {
            cmp = transactionId.compareTo(curr.getKey());
            if (cmp == 0) break;
            parent = curr;
            curr = cmp < 0 ? curr.getLeft() : curr.getRight();
        }
        if (curr == null) return new DeleteResult(false, null);

        DeletionType type;
        if (curr.isLeaf()) {
            type = DeletionType.LEAF;
            if (parent == null) root = null;
            else if (parent.getLeft() == curr) parent.setLeft(null);
            else parent.setRight(null);
        } else if (curr.hasOneChild()) {
            type = DeletionType.ONE_CHILD;
            BstNode child = curr.getLeft() != null ? curr.getLeft() : curr.getRight();
            if (parent == null) root = child;
            else if (parent.getLeft() == curr) parent.setLeft(child);
            else parent.setRight(child);
        } else {
            type = DeletionType.TWO_CHILDREN;
            // Replace with inorder successor (leftmost of right subtree)
            BstNode succParent = curr;
            BstNode successor = curr.getRight();
            while (successor.getLeft() != null) {
                succParent = successor;
                successor = successor.getLeft();
            }
            curr.setTransaction(successor.getTransaction());
            if (succParent == curr) succParent.setRight(successor.getRight());
            else succParent.setLeft(successor.getRight());
        }
        size--;
        return new DeleteResult(true, type);
    }

    public boolean delete(String transactionId) {
        return deleteWithType(transactionId).success;
    }

    public static class DeleteResult {
        public final boolean success;
        public final DeletionType type;

        public DeleteResult(boolean success, DeletionType type) {
            this.success = success;
            this.type = type;
        }
    }

    /** Returns the number of flagged transactions in the tree. */
    public int size() { return size; }

    /** Returns the root (for traversal/display). */
    public BstNode getRoot() { return root; }

    /**
     * In-order traversal: left, node, right. Produces sorted order by transaction ID.
     */
    public List<FlaggedTransaction> inOrder() {
        List<FlaggedTransaction> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    private void inOrder(BstNode node, List<FlaggedTransaction> out) {
        if (node == null) return;
        inOrder(node.getLeft(), out);
        out.add(node.getTransaction());
        inOrder(node.getRight(), out);
    }
}
