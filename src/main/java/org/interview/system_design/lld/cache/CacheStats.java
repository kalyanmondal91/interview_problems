package org.interview.system_design.lld.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe statistics collector for cache operations.
 */
public class CacheStats {

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);

    public void recordHit() { hits.incrementAndGet(); }
    public void recordMiss() { misses.incrementAndGet(); }
    public void recordEviction() { evictions.incrementAndGet(); }

    public long getHits() { return hits.get(); }
    public long getMisses() { return misses.get(); }
    public long getEvictions() { return evictions.get(); }

    public long getTotalRequests() { return hits.get() + misses.get(); }

    /** Returns the hit rate as a percentage (0.0–100.0). */
    public double getHitRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (hits.get() * 100.0) / total;
    }

    public void reset() {
        hits.set(0);
        misses.set(0);
        evictions.set(0);
    }

    @Override
    public String toString() {
        return String.format("CacheStats{hits=%d, misses=%d, evictions=%d, hitRate=%.1f%%}",
                getHits(), getMisses(), getEvictions(), getHitRate());
    }
}
