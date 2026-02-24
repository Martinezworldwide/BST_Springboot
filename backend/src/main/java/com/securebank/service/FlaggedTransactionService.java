package com.securebank.service;

import com.securebank.bst.DeletionType;
import com.securebank.bst.FlaggedTransactionBst;
import com.securebank.bst.UnorderedListStore;
import com.securebank.dto.DeleteResultDto;
import com.securebank.dto.SearchResultDto;
import com.securebank.dto.TransactionRequest;
import com.securebank.model.FlaggedTransaction;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Service layer: coordinates BST and unordered list, builds API responses including
 * search efficiency comparison and deletion impact explanations.
 */
@Service
public class FlaggedTransactionService {

    private final FlaggedTransactionBst bst = new FlaggedTransactionBst();
    private final UnorderedListStore listStore = new UnorderedListStore();

    public boolean insert(TransactionRequest req) {
        FlaggedTransaction t = new FlaggedTransaction(
                req.getTransactionId(),
                req.getAmount() != null ? req.getAmount() : 0.0,
                Instant.now(),
                req.getReason()
        );
        boolean bstOk = bst.insert(t);
        if (bstOk) listStore.add(t);
        return bstOk;
    }

    public SearchResultDto search(String transactionId) {
        SearchResultDto dto = new SearchResultDto();
        dto.setListSize(bst.size());

        FlaggedTransactionBst.SearchOutcome bstOut = bst.searchWithCount(transactionId);
        UnorderedListStore.SearchOutcome listOut = listStore.search(transactionId);

        dto.setBstComparisons(bstOut.comparisons);
        dto.setListComparisons(listOut.comparisons);
        dto.setTransaction(bstOut.node != null ? bstOut.node.getTransaction() : null);
        dto.setFound(bstOut.node != null);

        // Efficiency note: BST is O(log n) vs list O(n)
        if (dto.getListSize() > 0) {
            String note = String.format(
                    "BST used %d comparisons (O(log n) average). Unordered list used %d comparisons (O(n)). " +
                            "BST is more efficient for large transaction volumes.",
                    dto.getBstComparisons(), dto.getListComparisons()
            );
            dto.setEfficiencyNote(note);
        } else {
            dto.setEfficiencyNote("No transactions in store. Add flagged transactions to compare search efficiency.");
        }
        return dto;
    }

    public DeleteResultDto delete(String transactionId) {
        FlaggedTransactionBst.DeleteResult result = bst.deleteWithType(transactionId);
        if (result.success) listStore.remove(transactionId);

        DeleteResultDto dto = new DeleteResultDto();
        dto.setDeleted(result.success);
        dto.setNodeType(result.type != null ? result.type.name() : null);
        dto.setImpactExplanation(explainDeletionImpact(result.type));
        return dto;
    }

    private static String explainDeletionImpact(DeletionType type) {
        if (type == null) return null;
        switch (type) {
            case LEAF:
                return "Leaf node: no children. Removed by clearing the parent's pointer. " +
                        "BST height/structure unchanged elsewhere. Fraud list shrinks by one; no rebalancing.";
            case ONE_CHILD:
                return "Node with one child: parent now points to that child, bypassing the deleted node. " +
                        "Subtree structure preserved; search paths for remaining IDs unchanged. " +
                        "Fraud detection continues with one fewer flagged transaction.";
            case TWO_CHILDREN:
                return "Node with two children: replaced by its inorder successor (leftmost in right subtree). " +
                        "BST ordering preserved; one node physically removed (the successor). " +
                        "Reviewed transaction is removed from the fraud set while keeping the tree valid.";
            default:
                return "";
        }
    }

    public java.util.List<FlaggedTransaction> listAll() {
        return bst.inOrder();
    }

    public int size() {
        return bst.size();
    }
}
