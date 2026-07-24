package org.interview.system_design.lld.pricingengine;

/**
 * Chain-of-Responsibility handler for pre/post-processing pricing context.
 * Handlers can enrich the context (e.g., set flags) before rules are evaluated,
 * or perform post-processing (e.g., rounding, currency conversion).
 * Pattern: Chain of Responsibility
 */
public abstract class PricingChain {

    private PricingChain next;

    /** Links the next handler; returns next for fluent chaining. */
    public PricingChain setNext(PricingChain next) {
        this.next = next;
        return next;
    }

    /**
     * Processes the context, optionally modifying it, then delegates to the next handler.
     *
     * @param context the mutable pricing context
     */
    public final void process(PricingContext context) {
        doProcess(context);
        if (next != null) next.process(context);
    }

    /** Subclasses implement their specific processing step here. */
    protected abstract void doProcess(PricingContext context);
}
