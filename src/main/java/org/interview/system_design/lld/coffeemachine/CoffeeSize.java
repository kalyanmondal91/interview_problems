package org.interview.system_design.lld.coffeemachine;

/**
 * Enum representing coffee sizes with a multiplier applied to base cost.
 */
public enum CoffeeSize {
    SMALL(0.8),
    MEDIUM(1.0),
    LARGE(1.3);

    private final double sizeMultiplier;

    CoffeeSize(double sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;
    }

    public double getSizeMultiplier() {
        return sizeMultiplier;
    }
}
