package com.securebank.dto;

/**
 * Delete response: success flag and explanation of deletion type (leaf / one child / two children).
 */
public class DeleteResultDto {

    private boolean deleted;
    private String nodeType; // "LEAF", "ONE_CHILD", "TWO_CHILDREN"
    private String impactExplanation;

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public String getImpactExplanation() { return impactExplanation; }
    public void setImpactExplanation(String impactExplanation) { this.impactExplanation = impactExplanation; }
}
