package org.interview.system_design.lld.shoppingcart;

/**
 * Product entity. Immutable except for mutable stock quantity.
 */
public class Product {

    private final String        productId;
    private final String        name;
    private final String        description;
    private final double        price;
    private final String        category;
    private       ProductStatus status;
    private       int           stockQuantity;

    public Product(String productId, String name, String description,
                   double price, String category,
                   ProductStatus status, int stockQuantity) {
        this.productId     = productId;
        this.name          = name;
        this.description   = description;
        this.price         = price;
        this.category      = category;
        this.status        = status;
        this.stockQuantity = stockQuantity;
    }

    public String        getProductId()     { return productId; }
    public String        getName()          { return name; }
    public String        getDescription()   { return description; }
    public double        getPrice()         { return price; }
    public String        getCategory()      { return category; }
    public ProductStatus getStatus()        { return status; }
    public int           getStockQuantity() { return stockQuantity; }

    public void setStatus(ProductStatus status)       { this.status = status; }
    public void setStockQuantity(int stockQuantity)   { this.stockQuantity = stockQuantity; }

    public boolean isAvailable() {
        return status == ProductStatus.IN_STOCK || status == ProductStatus.LOW_STOCK;
    }

    @Override
    public String toString() {
        return String.format("Product[%s] %s $%.2f (%s)", productId, name, price, status);
    }
}
