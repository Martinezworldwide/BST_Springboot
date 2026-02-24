package com.securebank.bst;

import com.securebank.model.FlaggedTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Unordered list storing the same flagged transactions as the BST.
 * Used to compare search efficiency: list search is O(n), BST search is O(log n) average.
 */
public class UnorderedListStore {

    private final List<FlaggedTransaction> list = new CopyOnWriteArrayList<>();

    public void add(FlaggedTransaction t) {
        if (t == null || t.getTransactionId() == null) return;
        for (FlaggedTransaction existing : list) {
            if (t.getTransactionId().equals(existing.getTransactionId())) {
                list.set(list.indexOf(existing), t);
                return;
            }
        }
        list.add(t);
    }

    /**
     * Search by transaction ID. Returns the transaction and the number of comparisons performed.
     */
    public SearchOutcome search(String transactionId) {
        int comparisons = 0;
        for (FlaggedTransaction t : list) {
            comparisons++;
            if (t.getTransactionId().equals(transactionId)) {
                return new SearchOutcome(t, comparisons);
            }
        }
        return new SearchOutcome(null, comparisons);
    }

    public void remove(String transactionId) {
        list.removeIf(t -> t.getTransactionId().equals(transactionId));
    }

    public int size() { return list.size(); }

    public List<FlaggedTransaction> getAll() { return new ArrayList<>(list); }

    public static class SearchOutcome {
        public final FlaggedTransaction transaction;
        public final int comparisons;

        public SearchOutcome(FlaggedTransaction transaction, int comparisons) {
            this.transaction = transaction;
            this.comparisons = comparisons;
        }
    }
}
