package com.securebank.controller;

import com.securebank.dto.DeleteResultDto;
import com.securebank.dto.SearchResultDto;
import com.securebank.dto.TransactionRequest;
import com.securebank.model.FlaggedTransaction;
import com.securebank.service.FlaggedTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API for BST-based flagged transaction management.
 * Base path: /api/flagged-transactions
 */
@RestController
@RequestMapping("/api/flagged-transactions")
public class FlaggedTransactionController {

    private final FlaggedTransactionService service;

    public FlaggedTransactionController(FlaggedTransactionService service) {
        this.service = service;
    }

    /** Insert a new flagged transaction (BST insert). */
    @PostMapping
    public ResponseEntity<Map<String, Object>> insert(@Valid @RequestBody TransactionRequest request) {
        boolean inserted = service.insert(request);
        return ResponseEntity
                .status(inserted ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "success", inserted,
                        "message", inserted ? "Flagged transaction added to BST." : "Invalid or duplicate transactionId.",
                        "size", service.size()
                ));
    }

    /** Search by transaction ID; response includes BST vs list comparison. */
    @GetMapping("/search")
    public SearchResultDto search(@RequestParam String transactionId) {
        return service.search(transactionId);
    }

    /** Delete a reviewed transaction; response includes node type and impact explanation. */
    @DeleteMapping("/{transactionId}")
    public DeleteResultDto delete(@PathVariable String transactionId) {
        return service.delete(transactionId);
    }

    /** List all flagged transactions (in-order = sorted by transaction ID). */
    @GetMapping
    public List<FlaggedTransaction> list() {
        return service.listAll();
    }

    /** Tree size. */
    @GetMapping("/size")
    public Map<String, Integer> size() {
        return Map.of("size", service.size());
    }
}
