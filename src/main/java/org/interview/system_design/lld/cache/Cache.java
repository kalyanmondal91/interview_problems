package org.interview.system_design.lld.cache;

import java.util.Optional;

/**
 * Abstract template for a generic key-value cache.
 * Defines the invariant flow for get/put operations and exposes
 * before/after hooks for subclasses to instrument behaviour.
 * Pattern: Template Method + Strategy (via EvictionStrategy)
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class Cache<K, V> {

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
    public final Optional<V> get(K key) {
        beforeGet(key);
        Optional<V> result = doGet(key);
        result.ifPresent(v -> afterGet(key, v));
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

    protected abstract Optional<V> doGet(K key);

    protected abstract void doPut(K key, V value);

    public abstract void remove(K key);

    public abstract void clear();

    public abstract int size();
}
