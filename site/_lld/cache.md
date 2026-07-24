---
layout: lld
render_with_liquid: false
title: "Cache System"
system: cache
description: "LLD of Cache System"
files:
  - "Cache.java"
  - "CacheBuilder.java"
  - "CacheConfig.java"
  - "CacheEntry.java"
  - "CacheService.java"
  - "CacheStats.java"
  - "EvictionPolicy.java"
  - "EvictionStrategy.java"
  - "FIFOEvictionStrategy.java"
  - "LFUEvictionStrategy.java"
  - "LRUEvictionStrategy.java"
  - "Main.java"
  - "RandomEvictionStrategy.java"
  - "SimpleCache.java"
---

## Cache System

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="Cache.java">Cache.java</button>
<button class="tab-btn" data-tab="CacheBuilder.java">CacheBuilder.java</button>
<button class="tab-btn" data-tab="CacheConfig.java">CacheConfig.java</button>
<button class="tab-btn" data-tab="CacheEntry.java">CacheEntry.java</button>
<button class="tab-btn" data-tab="CacheService.java">CacheService.java</button>
<button class="tab-btn" data-tab="CacheStats.java">CacheStats.java</button>
<button class="tab-btn" data-tab="EvictionPolicy.java">EvictionPolicy.java</button>
<button class="tab-btn" data-tab="EvictionStrategy.java">EvictionStrategy.java</button>
<button class="tab-btn" data-tab="FIFOEvictionStrategy.java">FIFOEvictionStrategy.java</button>
<button class="tab-btn" data-tab="LFUEvictionStrategy.java">LFUEvictionStrategy.java</button>
<button class="tab-btn" data-tab="LRUEvictionStrategy.java">LRUEvictionStrategy.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="RandomEvictionStrategy.java">RandomEvictionStrategy.java</button>
<button class="tab-btn" data-tab="SimpleCache.java">SimpleCache.java</button>
</div>
<div class="tab-content active" id="Cache-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Optional;

/**
 * Abstract template for a generic key-value cache.
 * Defines the invariant flow for get/put operations and exposes
 * before/after hooks for subclasses to instrument behaviour.
 * Pattern: Template Method + Strategy (via EvictionStrategy)
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public abstract class Cache&lt;K, V&gt; {

    /** Called before a get operation. */
    protected void beforeGet(K key) {}

    /** Called after a successful get (value found). */
    protected void afterGet(K key, V value) {}

    /** Called before a put operation. */
    protected void beforePut(K key, V value) {}

    /** Called after a put operation. */
    protected void afterPut(K key, V value) {}

    // ---- Template methods (final to enforce the lifecycle) ----

    /**
     * Retrieves a value from the cache, invoking before/after hooks.
     *
     * @return the value wrapped in Optional, or empty on miss/expiry
     */
    public final Optional&lt;V&gt; get(K key) {
        beforeGet(key);
        Optional&lt;V&gt; result = doGet(key);
        result.ifPresent(v -&gt; afterGet(key, v));
        return result;
    }

    /**
     * Inserts or updates a cache entry, invoking before/after hooks.
     */
    public final void put(K key, V value) {
        beforePut(key, value);
        doPut(key, value);
        afterPut(key, value);
    }

    // ---- Abstract operations implemented by SimpleCache ----

    protected abstract Optional&lt;V&gt; doGet(K key);

    protected abstract void doPut(K key, V value);

    public abstract void remove(K key);

    public abstract void clear();

    public abstract int size();
}</code></pre>
</div>
<div class="tab-content" id="CacheBuilder-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

/**
 * Fluent builder for constructing a configured Cache instance.
 * Pattern: Builder
 *
 * &lt;pre&gt;
 * Cache&amp;lt;String, String&amp;gt; cache = new CacheBuilder&amp;lt;String, String&amp;gt;()
 *     .maxCapacity(100)
 *     .ttlSeconds(300)
 *     .evictionPolicy(EvictionPolicy.LRU)
 *     .build();
 * &lt;/pre&gt;
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public class CacheBuilder&lt;K, V&gt; {

    private int maxCapacity = 100;
    private long ttlSeconds = 0;
    private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;

    public CacheBuilder&lt;K, V&gt; maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public CacheBuilder&lt;K, V&gt; ttlSeconds(long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
        return this;
    }

    public CacheBuilder&lt;K, V&gt; evictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        return this;
    }

    /** Builds and returns a configured SimpleCache. */
    public Cache&lt;K, V&gt; build() {
        CacheConfig config = new CacheConfig(maxCapacity, ttlSeconds, evictionPolicy);
        EvictionStrategy&lt;K, V&gt; strategy = createStrategy(evictionPolicy);
        return new SimpleCache&lt;&gt;(config, strategy);
    }

    @SuppressWarnings(&quot;unchecked&quot;)
    private EvictionStrategy&lt;K, V&gt; createStrategy(EvictionPolicy policy) {
        switch (policy) {
            case LRU:    return new LRUEvictionStrategy&lt;&gt;();
            case LFU:    return new LFUEvictionStrategy&lt;&gt;();
            case FIFO:   return new FIFOEvictionStrategy&lt;&gt;();
            case RANDOM: return new RandomEvictionStrategy&lt;&gt;();
            default:     throw new IllegalArgumentException(&quot;Unknown policy: &quot; + policy);
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="CacheConfig-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

/**
 * Immutable configuration for a cache instance.
 * Created via CacheBuilder.
 */
public class CacheConfig {

    private final int maxCapacity;
    private final long ttlSeconds;       // 0 = no TTL
    private final EvictionPolicy evictionPolicy;

    public CacheConfig(int maxCapacity, long ttlSeconds, EvictionPolicy evictionPolicy) {
        if (maxCapacity &lt;= 0) throw new IllegalArgumentException(&quot;maxCapacity must be positive&quot;);
        this.maxCapacity = maxCapacity;
        this.ttlSeconds = ttlSeconds;
        this.evictionPolicy = evictionPolicy;
    }

    public int getMaxCapacity() { return maxCapacity; }
    public long getTtlSeconds() { return ttlSeconds; }
    public EvictionPolicy getEvictionPolicy() { return evictionPolicy; }

    @Override
    public String toString() {
        return String.format(&quot;CacheConfig{maxCapacity=%d, ttl=%ds, policy=%s}&quot;,
                maxCapacity, ttlSeconds, evictionPolicy);
    }
}</code></pre>
</div>
<div class="tab-content" id="CacheEntry-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.time.Instant;

/**
 * Wrapper around a cached value with metadata used by eviction strategies.
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public class CacheEntry&lt;K, V&gt; {

    private final K key;
    private final V value;
    private final Instant createdAt;
    private Instant lastAccessedAt;
    private long accessCount;
    private final long ttlSeconds; // 0 = no TTL

    public CacheEntry(K key, V value, long ttlSeconds) {
        this.key = key;
        this.value = value;
        this.createdAt = Instant.now();
        this.lastAccessedAt = this.createdAt;
        this.accessCount = 0;
        this.ttlSeconds = ttlSeconds;
    }

    /** Records an access, updating lastAccessedAt and incrementing count. */
    public void recordAccess() {
        this.lastAccessedAt = Instant.now();
        this.accessCount++;
    }

    /** Returns true if this entry has a TTL and has expired. */
    public boolean isExpired() {
        if (ttlSeconds &lt;= 0) return false;
        return Instant.now().isAfter(createdAt.plusSeconds(ttlSeconds));
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastAccessedAt() { return lastAccessedAt; }
    public long getAccessCount() { return accessCount; }
    public long getTtlSeconds() { return ttlSeconds; }
}</code></pre>
</div>
<div class="tab-content" id="CacheService-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Optional;

/**
 * Service wrapper around a Cache that tracks statistics.
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public class CacheService&lt;K, V&gt; {

    private final Cache&lt;K, V&gt; cache;
    private final CacheStats stats = new CacheStats();

    public CacheService(Cache&lt;K, V&gt; cache) {
        this.cache = cache;
    }

    /**
     * Gets a value, recording hit or miss statistics.
     */
    public Optional&lt;V&gt; get(K key) {
        Optional&lt;V&gt; result = cache.get(key);
        if (result.isPresent()) {
            stats.recordHit();
        } else {
            stats.recordMiss();
        }
        return result;
    }

    /**
     * Puts a value; detects evictions by comparing sizes before/after.
     */
    public void put(K key, V value) {
        int sizeBefore = cache.size();
        cache.put(key, value);
        int sizeAfter = cache.size();
        // If size didn&#x27;t grow, something was evicted (and the key is new)
        if (sizeAfter &lt;= sizeBefore &amp;&amp; !get(key).isPresent()) {
            stats.recordEviction();
        }
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
        stats.reset();
    }

    public int size() { return cache.size(); }

    public CacheStats getStats() { return stats; }
}</code></pre>
</div>
<div class="tab-content" id="CacheStats-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

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
        return String.format(&quot;CacheStats{hits=%d, misses=%d, evictions=%d, hitRate=%.1f%%}&quot;,
                getHits(), getMisses(), getEvictions(), getHitRate());
    }
}</code></pre>
</div>
<div class="tab-content" id="EvictionPolicy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

/** Supported cache eviction policies. */
public enum EvictionPolicy {
    LRU,   // Least Recently Used
    LFU,   // Least Frequently Used
    FIFO,  // First In, First Out
    RANDOM // Random eviction
}</code></pre>
</div>
<div class="tab-content" id="EvictionStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Strategy interface for cache eviction algorithms.
 * Pattern: Strategy
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public interface EvictionStrategy&lt;K, V&gt; {
    /**
     * Selects and returns the key that should be evicted from the cache.
     *
     * @param entries current cache entries
     * @return the key to evict
     */
    K evict(Map&lt;K, CacheEntry&lt;K, V&gt;&gt; entries);
}</code></pre>
</div>
<div class="tab-content" id="FIFOEvictionStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry that was created earliest (first in, first out).
 */
public class FIFOEvictionStrategy&lt;K, V&gt; implements EvictionStrategy&lt;K, V&gt; {

    @Override
    public K evict(Map&lt;K, CacheEntry&lt;K, V&gt;&gt; entries) {
        return entries.entrySet().stream()
                .min((a, b) -&gt; a.getValue().getCreatedAt().compareTo(b.getValue().getCreatedAt()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -&gt; new IllegalStateException(&quot;Cache is empty&quot;));
    }
}</code></pre>
</div>
<div class="tab-content" id="LFUEvictionStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry with the lowest access count (ties broken by oldest access time).
 */
public class LFUEvictionStrategy&lt;K, V&gt; implements EvictionStrategy&lt;K, V&gt; {

    @Override
    public K evict(Map&lt;K, CacheEntry&lt;K, V&gt;&gt; entries) {
        return entries.entrySet().stream()
                .min((a, b) -&gt; {
                    int cmp = Long.compare(a.getValue().getAccessCount(), b.getValue().getAccessCount());
                    if (cmp != 0) return cmp;
                    return a.getValue().getLastAccessedAt().compareTo(b.getValue().getLastAccessedAt());
                })
                .map(Map.Entry::getKey)
                .orElseThrow(() -&gt; new IllegalStateException(&quot;Cache is empty&quot;));
    }
}</code></pre>
</div>
<div class="tab-content" id="LRUEvictionStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry with the oldest lastAccessedAt timestamp.
 */
public class LRUEvictionStrategy&lt;K, V&gt; implements EvictionStrategy&lt;K, V&gt; {

    @Override
    public K evict(Map&lt;K, CacheEntry&lt;K, V&gt;&gt; entries) {
        return entries.entrySet().stream()
                .min((a, b) -&gt; a.getValue().getLastAccessedAt()
                        .compareTo(b.getValue().getLastAccessedAt()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -&gt; new IllegalStateException(&quot;Cache is empty&quot;));
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

/**
 * Demo runner for the Cache Library LLD.
 * Patterns: Strategy (eviction), Template Method, Builder
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // -- Scenario 1: LRU Cache -- verify LRU eviction order --
        System.out.println(&quot;\n=== Scenario 1: LRU Cache (capacity=3) ===&quot;);
        Cache&lt;String, String&gt; lruCache = new CacheBuilder&lt;String, String&gt;()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.LRU)
                .build();

        lruCache.put(&quot;A&quot;, &quot;Apple&quot;);
        lruCache.put(&quot;B&quot;, &quot;Banana&quot;);
        lruCache.put(&quot;C&quot;, &quot;Cherry&quot;);
        System.out.println(&quot;  Put A, B, C -- size: &quot; + lruCache.size());

        lruCache.get(&quot;A&quot;); // Access A -&gt; refreshes A&#x27;s recency (B is now LRU)
        System.out.println(&quot;  get(A) -- A refreshed&quot;);

        lruCache.put(&quot;D&quot;, &quot;Date&quot;); // evicts B (least recently used)
        System.out.println(&quot;  put(D) -&gt; evicts B (LRU). Contains B: &quot; + lruCache.get(&quot;B&quot;).isPresent());
        System.out.println(&quot;  Contains A: &quot; + lruCache.get(&quot;A&quot;).isPresent());
        System.out.println(&quot;  Contains D: &quot; + lruCache.get(&quot;D&quot;).isPresent());

        // -- Scenario 2: LFU Cache -- verify frequency-based eviction --
        System.out.println(&quot;\n=== Scenario 2: LFU Cache (capacity=3) ===&quot;);
        Cache&lt;String, Integer&gt; lfuCache = new CacheBuilder&lt;String, Integer&gt;()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.LFU)
                .build();

        lfuCache.put(&quot;X&quot;, 1); lfuCache.put(&quot;Y&quot;, 2); lfuCache.put(&quot;Z&quot;, 3);
        lfuCache.get(&quot;X&quot;); lfuCache.get(&quot;X&quot;); // freq(X)=2
        lfuCache.get(&quot;Y&quot;);                    // freq(Y)=1
        // Z has freq=0 (never accessed after put) -&gt; should be evicted
        lfuCache.put(&quot;W&quot;, 4);
        System.out.println(&quot;  After put(W): Contains Z (freq=0): &quot; + lfuCache.get(&quot;Z&quot;).isPresent());
        System.out.println(&quot;  Contains X (freq=2): &quot;  + lfuCache.get(&quot;X&quot;).isPresent());
        System.out.println(&quot;  Contains Y (freq=1): &quot;  + lfuCache.get(&quot;Y&quot;).isPresent());
        System.out.println(&quot;  Contains W (new): &quot;     + lfuCache.get(&quot;W&quot;).isPresent());

        // -- Scenario 3: FIFO Cache --
        System.out.println(&quot;\n=== Scenario 3: FIFO Cache (capacity=3) ===&quot;);
        Cache&lt;String, String&gt; fifoCache = new CacheBuilder&lt;String, String&gt;()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.FIFO)
                .build();

        fifoCache.put(&quot;P1&quot;, &quot;v1&quot;);
        fifoCache.put(&quot;P2&quot;, &quot;v2&quot;);
        fifoCache.put(&quot;P3&quot;, &quot;v3&quot;);
        fifoCache.get(&quot;P1&quot;); // access P1 -- FIFO doesn&#x27;t change order
        fifoCache.put(&quot;P4&quot;, &quot;v4&quot;); // evicts P1 (first in)
        System.out.println(&quot;  Contains P1 (first in): &quot; + fifoCache.get(&quot;P1&quot;).isPresent());
        System.out.println(&quot;  Contains P2: &quot;            + fifoCache.get(&quot;P2&quot;).isPresent());
        System.out.println(&quot;  Contains P4 (new): &quot;      + fifoCache.get(&quot;P4&quot;).isPresent());

        // -- Scenario 4: TTL expiry --
        System.out.println(&quot;\n=== Scenario 4: TTL expiry (1 second) ===&quot;);
        Cache&lt;String, String&gt; ttlCache = new CacheBuilder&lt;String, String&gt;()
                .maxCapacity(10)
                .evictionPolicy(EvictionPolicy.LRU)
                .ttlSeconds(1)
                .build();

        ttlCache.put(&quot;session-1&quot;, &quot;user-abc&quot;);
        System.out.println(&quot;  Immediately after put: &quot; + ttlCache.get(&quot;session-1&quot;).orElse(&quot;MISSING&quot;));
        Thread.sleep(1200); // wait for TTL to expire
        System.out.println(&quot;  After 1.2s (TTL=1s):   &quot; + ttlCache.get(&quot;session-1&quot;).orElse(&quot;EXPIRED&quot;));

        // -- Scenario 5: CacheService hit/miss stats --
        System.out.println(&quot;\n=== Scenario 5: CacheService -- hit rate and stats ===&quot;);
        Cache&lt;String, String&gt; statsCache = new CacheBuilder&lt;String, String&gt;()
                .maxCapacity(5)
                .evictionPolicy(EvictionPolicy.LRU)
                .build();
        CacheService&lt;String, String&gt; service = new CacheService&lt;&gt;(statsCache);

        service.put(&quot;k1&quot;, &quot;v1&quot;); service.put(&quot;k2&quot;, &quot;v2&quot;); service.put(&quot;k3&quot;, &quot;v3&quot;);
        service.get(&quot;k1&quot;); // hit
        service.get(&quot;k2&quot;); // hit
        service.get(&quot;k3&quot;); // hit
        service.get(&quot;k4&quot;); // miss
        service.get(&quot;k5&quot;); // miss

        CacheStats stats = service.getStats();
        System.out.printf(&quot;  Hits: %d | Misses: %d | Hit rate: %.0f%%%n&quot;,
                stats.getHits(), stats.getMisses(), stats.getHitRate() * 100);
        System.out.println(&quot;  Evictions: &quot; + stats.getEvictions());

        System.out.println(&quot;\n=== Cache Library Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="RandomEvictionStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Evicts a randomly chosen entry.
 */
public class RandomEvictionStrategy&lt;K, V&gt; implements EvictionStrategy&lt;K, V&gt; {

    private final Random random = new Random();

    @Override
    public K evict(Map&lt;K, CacheEntry&lt;K, V&gt;&gt; entries) {
        List&lt;K&gt; keys = new ArrayList&lt;&gt;(entries.keySet());
        if (keys.isEmpty()) throw new IllegalStateException(&quot;Cache is empty&quot;);
        return keys.get(random.nextInt(keys.size()));
    }
}</code></pre>
</div>
<div class="tab-content" id="SimpleCache-java">
<pre><code class="language-java">package org.interview.system_design.lld.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete cache implementation backed by a LinkedHashMap.
 * Eviction is delegated to the configured EvictionStrategy.
 *
 * @param &lt;K&gt; key type
 * @param &lt;V&gt; value type
 */
public class SimpleCache&lt;K, V&gt; extends Cache&lt;K, V&gt; {

    private final Map&lt;K, CacheEntry&lt;K, V&gt;&gt; store = new LinkedHashMap&lt;&gt;();
    private final CacheConfig config;
    private final EvictionStrategy&lt;K, V&gt; evictionStrategy;

    public SimpleCache(CacheConfig config, EvictionStrategy&lt;K, V&gt; evictionStrategy) {
        this.config = config;
        this.evictionStrategy = evictionStrategy;
    }

    @Override
    protected Optional&lt;V&gt; doGet(K key) {
        CacheEntry&lt;K, V&gt; entry = store.get(key);
        if (entry == null) return Optional.empty();

        // Evict expired entries lazily
        if (entry.isExpired()) {
            store.remove(key);
            return Optional.empty();
        }

        entry.recordAccess();
        return Optional.of(entry.getValue());
    }

    @Override
    protected void doPut(K key, V value) {
        if (store.containsKey(key)) {
            // Replace existing entry
            store.put(key, new CacheEntry&lt;&gt;(key, value, config.getTtlSeconds()));
            return;
        }

        if (store.size() &gt;= config.getMaxCapacity()) {
            K evictKey = evictionStrategy.evict(store);
            store.remove(evictKey);
        }

        store.put(key, new CacheEntry&lt;&gt;(key, value, config.getTtlSeconds()));
    }

    @Override
    public void remove(K key) {
        store.remove(key);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public int size() {
        return store.size();
    }

    /** Exposes the raw entry map for inspection (e.g., by CacheService stats). */
    public Map&lt;K, CacheEntry&lt;K, V&gt;&gt; getStore() {
        return store;
    }
}</code></pre>
</div>
</div>
