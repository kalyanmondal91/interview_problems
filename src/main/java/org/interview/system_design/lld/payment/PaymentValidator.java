package org.interview.system_design.lld.payment;

/**
 * Abstract Chain-of-Responsibility handler for payment validation.
 * Each concrete validator either passes the request to the next handler
 * or returns an error message to short-circuit the chain.
 * Pattern: Chain of Responsibility
 */
public abstract class PaymentValidator {

    /** Next validator in the chain; null means end of chain. */
    private PaymentValidator next;

    /** Links the next handler and returns it for fluent chaining. */
    public PaymentValidator setNext(PaymentValidator next) {
        this.next = next;
        return next;
    }

    /**
     * Validates the request.
     *
     * @param request payment to validate
     * @return null if valid (or next handler accepts it), or an error message string
     */
    public final String validate(PaymentRequest request) {
        String error = doValidate(request);
        if (error != null) return error;               // short-circuit on first failure
        if (next != null) return next.validate(request); // pass down the chain
        return null;                                    // all validators passed
    }

    /**
     * Subclasses implement their specific validation logic here.
     *
     * @return null if valid, or a non-null error description
     */
    protected abstract String doValidate(PaymentRequest request);
}
