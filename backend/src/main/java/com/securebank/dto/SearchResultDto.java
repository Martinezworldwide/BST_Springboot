package com.securebank.dto;

import com.securebank.model.FlaggedTransaction;

/**
 * Search response: transaction if found, plus BST vs list efficiency comparison.
 */
public class SearchResultDto {

    private FlaggedTransaction transaction;
    private boolean found;
    private int bstComparisons;
    private int listComparisons;
    private int listSize;
    private String efficiencyNote;

    public FlaggedTransaction getTransaction() { return transaction; }
    public void setTransaction(FlaggedTransaction transaction) { this.transaction = transaction; }

    public boolean isFound() { return found; }
    public void setFound(boolean found) { this.found = found; }

    public int getBstComparisons() { return bstComparisons; }
    public void setBstComparisons(int bstComparisons) { this.bstComparisons = bstComparisons; }

    public int getListComparisons() { return listComparisons; }
    public void setListComparisons(int listComparisons) { this.listComparisons = listComparisons; }

    public int getListSize() { return listSize; }
    public void setListSize(int listSize) { this.listSize = listSize; }

    public String getEfficiencyNote() { return efficiencyNote; }
    public void setEfficiencyNote(String efficiencyNote) { this.efficiencyNote = efficiencyNote; }
}
