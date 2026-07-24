package org.interview.system_design.lld.cache;

/** Supported cache eviction policies. */
public enum EvictionPolicy {
    LRU,   // Least Recently Used
    LFU,   // Least Frequently Used
    FIFO,  // First In, First Out
    RANDOM // Random eviction
}
