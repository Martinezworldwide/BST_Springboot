package com.securebank.bst;

/**
 * Type of BST node deleted: determines structural impact and fraud-system behaviour.
 */
public enum DeletionType {
    LEAF,
    ONE_CHILD,
    TWO_CHILDREN
}
