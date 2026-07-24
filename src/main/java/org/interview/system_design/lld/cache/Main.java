package org.interview.system_design.lld.cache;

/**
 * Demo runner for the Cache Library LLD.
 * Patterns: Strategy (eviction), Template Method, Builder
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // -- Scenario 1: LRU Cache -- verify LRU eviction order --
        System.out.println("\n=== Scenario 1: LRU Cache (capacity=3) ===");
        Cache<String, String> lruCache = new CacheBuilder<String, String>()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.LRU)
                .build();

        lruCache.put("A", "Apple");
        lruCache.put("B", "Banana");
        lruCache.put("C", "Cherry");
        System.out.println("  Put A, B, C -- size: " + lruCache.size());

        lruCache.get("A"); // Access A -> refreshes A's recency (B is now LRU)
        System.out.println("  get(A) -- A refreshed");

        lruCache.put("D", "Date"); // evicts B (least recently used)
        System.out.println("  put(D) -> evicts B (LRU). Contains B: " + lruCache.get("B").isPresent());
        System.out.println("  Contains A: " + lruCache.get("A").isPresent());
        System.out.println("  Contains D: " + lruCache.get("D").isPresent());

        // -- Scenario 2: LFU Cache -- verify frequency-based eviction --
        System.out.println("\n=== Scenario 2: LFU Cache (capacity=3) ===");
        Cache<String, Integer> lfuCache = new CacheBuilder<String, Integer>()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.LFU)
                .build();

        lfuCache.put("X", 1); lfuCache.put("Y", 2); lfuCache.put("Z", 3);
        lfuCache.get("X"); lfuCache.get("X"); // freq(X)=2
        lfuCache.get("Y");                    // freq(Y)=1
        // Z has freq=0 (never accessed after put) -> should be evicted
        lfuCache.put("W", 4);
        System.out.println("  After put(W): Contains Z (freq=0): " + lfuCache.get("Z").isPresent());
        System.out.println("  Contains X (freq=2): "  + lfuCache.get("X").isPresent());
        System.out.println("  Contains Y (freq=1): "  + lfuCache.get("Y").isPresent());
        System.out.println("  Contains W (new): "     + lfuCache.get("W").isPresent());

        // -- Scenario 3: FIFO Cache --
        System.out.println("\n=== Scenario 3: FIFO Cache (capacity=3) ===");
        Cache<String, String> fifoCache = new CacheBuilder<String, String>()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.FIFO)
                .build();

        fifoCache.put("P1", "v1");
        fifoCache.put("P2", "v2");
        fifoCache.put("P3", "v3");
        fifoCache.get("P1"); // access P1 -- FIFO doesn't change order
        fifoCache.put("P4", "v4"); // evicts P1 (first in)
        System.out.println("  Contains P1 (first in): " + fifoCache.get("P1").isPresent());
        System.out.println("  Contains P2: "            + fifoCache.get("P2").isPresent());
        System.out.println("  Contains P4 (new): "      + fifoCache.get("P4").isPresent());

        // -- Scenario 4: TTL expiry --
        System.out.println("\n=== Scenario 4: TTL expiry (1 second) ===");
        Cache<String, String> ttlCache = new CacheBuilder<String, String>()
                .maxCapacity(10)
                .evictionPolicy(EvictionPolicy.LRU)
                .ttlSeconds(1)
                .build();

        ttlCache.put("session-1", "user-abc");
        System.out.println("  Immediately after put: " + ttlCache.get("session-1").orElse("MISSING"));
        Thread.sleep(1200); // wait for TTL to expire
        System.out.println("  After 1.2s (TTL=1s):   " + ttlCache.get("session-1").orElse("EXPIRED"));

        // -- Scenario 5: CacheService hit/miss stats --
        System.out.println("\n=== Scenario 5: CacheService -- hit rate and stats ===");
        Cache<String, String> statsCache = new CacheBuilder<String, String>()
                .maxCapacity(5)
                .evictionPolicy(EvictionPolicy.LRU)
                .build();
        CacheService<String, String> service = new CacheService<>(statsCache);

        service.put("k1", "v1"); service.put("k2", "v2"); service.put("k3", "v3");
        service.get("k1"); // hit
        service.get("k2"); // hit
        service.get("k3"); // hit
        service.get("k4"); // miss
        service.get("k5"); // miss

        CacheStats stats = service.getStats();
        System.out.printf("  Hits: %d | Misses: %d | Hit rate: %.0f%%%n",
                stats.getHits(), stats.getMisses(), stats.getHitRate() * 100);
        System.out.println("  Evictions: " + stats.getEvictions());

        System.out.println("\n=== Cache Library Demo Complete ===");
    }
}
