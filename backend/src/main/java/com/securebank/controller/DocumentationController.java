package com.securebank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Explains BST insertion, search efficiency vs list, and deletion impact for fraud detection.
 */
@RestController
@RequestMapping("/api/docs")
public class DocumentationController {

    @GetMapping("/bst-explanation")
    public ResponseEntity<Map<String, Object>> bstExplanation() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("title", "SecureBank BST Fraud Detection - Implementation Notes");

        body.put("insertion", Map.of(
                "description", "BST insertion adds new flagged transactions while maintaining a structured hierarchy.",
                "behavior", "Transactions are ordered by transaction ID: smaller IDs go left, larger IDs go right. " +
                        "Duplicate IDs update the existing node. Insertion is O(log n) average."
        ));

        body.put("search", Map.of(
                "description", "Search retrieves flagged transactions by transaction ID.",
                "bstEfficiency", "BST search: O(log n) average when balanced; O(n) worst case if tree is skewed.",
                "listEfficiency", "Unordered list search: O(n) always—every element may be checked.",
                "comparison", "For large volumes, BST reduces comparisons significantly (e.g. 1000 items: ~10 vs 500 average)."
        ));

        body.put("deletion", Map.of(
                "description", "Deletion removes reviewed transactions from the fraud set.",
                "leafNode", "Leaf: node has no children. Removed by setting parent's pointer to null. No structural change elsewhere.",
                "oneChild", "One child: parent's pointer is updated to the single child. Subtree stays valid; one fewer node.",
                "twoChildren", "Two children: node is replaced by its inorder successor (leftmost in right subtree). " +
                        "Ordering preserved; successor node is removed. Fraud list and BST remain consistent."
        ));

        body.put("fraudSystemImpact", "Deletion keeps the BST valid so future searches and inserts remain efficient. " +
                "Reviewed (cleared) transactions are removed from the active fraud set while preserving structure for remaining flagged items.");

        return ResponseEntity.ok(body);
    }
}
