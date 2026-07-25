package org.interview.design_patterns.creational.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Design Pattern: Builder (Creational)
 *
 * INTENT: Separate the construction of a complex object from its representation
 * so that the same construction process can create different representations.
 *
 * PROBLEM IT SOLVES:
 *   - Telescoping constructor anti-pattern: many optional parameters.
 *   - Objects that need step-by-step construction with validation.
 *
 * STRUCTURE:
 *   - Product: the complex object being built
 *   - Builder (interface): declares steps to build product parts
 *   - ConcreteBuilder: implements building steps; provides getResult()
 *   - Director (optional): orchestrates builder calls in specific order
 *
 * VARIANTS:
 *   1. Classic GoF: Director + Builder interface
 *   2. Fluent/Inner Builder (most common in Java): builder as static inner class
 *
 * REAL-WORLD EXAMPLES:
 *   - java.lang.StringBuilder
 *   - Lombok @Builder
 *   - OkHttp Request.Builder
 *   - SQL query builders
 */
public class BuilderPattern {

    // ================================================================
    // Product: immutable HTTP request with many optional fields
    // ================================================================
    static final class HttpRequest {
        private final String method;
        private final String url;
        private final List<String> headers;
        private final String body;
        private final int timeoutSeconds;
        private final boolean followRedirects;

        private HttpRequest(Builder builder) {
            this.method          = builder.method;
            this.url             = builder.url;
            this.headers         = Collections.unmodifiableList(new ArrayList<>(builder.headers));
            this.body            = builder.body;
            this.timeoutSeconds  = builder.timeoutSeconds;
            this.followRedirects = builder.followRedirects;
        }

        @Override
        public String toString() {
            return "HttpRequest{" +
                   "method='" + method + "'" +
                   ", url='" + url + "'" +
                   ", headers=" + headers +
                   ", body='" + (body != null ? body : "<none>") + "'" +
                   ", timeout=" + timeoutSeconds + "s" +
                   ", followRedirects=" + followRedirects +
                   '}';
        }

        // ============================================================
        // Fluent Builder (static inner class)
        // ============================================================
        static final class Builder {
            private final String method;
            private final String url;
            private List<String> headers       = new ArrayList<>();
            private String       body          = null;
            private int          timeoutSeconds = 30;
            private boolean      followRedirects = true;

            Builder(String method, String url) {
                if (method == null || method.isBlank()) throw new IllegalArgumentException("method required");
                if (url    == null || url.isBlank())    throw new IllegalArgumentException("url required");
                this.method = method.toUpperCase();
                this.url    = url;
            }

            public Builder header(String header) { this.headers.add(header); return this; }
            public Builder body(String body)      { this.body = body; return this; }
            public Builder timeout(int seconds)   {
                if (seconds <= 0) throw new IllegalArgumentException("timeout must be positive");
                this.timeoutSeconds = seconds; return this;
            }
            public Builder followRedirects(boolean follow) { this.followRedirects = follow; return this; }

            public HttpRequest build() {
                if (body != null && method.equals("GET")) {
                    System.out.println("Warning: GET request has a body (unusual)");
                }
                return new HttpRequest(this);
            }
        }
    }

    // ================================================================
    // Classic GoF: Director + Builder (House building example)
    // ================================================================
    interface HouseBuilder {
        void buildFoundation();
        void buildWalls();
        void buildRoof();
        void buildWindows();
        House getResult();
    }

    static class House {
        private String foundation, walls, roof, windows;

        void setFoundation(String f) { this.foundation = f; }
        void setWalls(String w)      { this.walls = w; }
        void setRoof(String r)       { this.roof = r; }
        void setWindows(String w)    { this.windows = w; }

        @Override
        public String toString() {
            return "House{foundation='" + foundation + "', walls='" + walls +
                   "', roof='" + roof + "', windows='" + windows + "'}";
        }
    }

    static class WoodenHouseBuilder implements HouseBuilder {
        private final House house = new House();
        @Override public void buildFoundation() { house.setFoundation("Concrete slab"); }
        @Override public void buildWalls()      { house.setWalls("Pine wood panels"); }
        @Override public void buildRoof()       { house.setRoof("Shingle roof"); }
        @Override public void buildWindows()    { house.setWindows("Double-pane wooden frames"); }
        @Override public House getResult()      { return house; }
    }

    static class BrickHouseBuilder implements HouseBuilder {
        private final House house = new House();
        @Override public void buildFoundation() { house.setFoundation("Deep piled foundation"); }
        @Override public void buildWalls()      { house.setWalls("Red brick walls"); }
        @Override public void buildRoof()       { house.setRoof("Clay tile roof"); }
        @Override public void buildWindows()    { house.setWindows("UPVC double-glazed windows"); }
        @Override public House getResult()      { return house; }
    }

    // Director knows the ORDER to call builder steps
    static class HouseDirector {
        private HouseBuilder builder;
        void setBuilder(HouseBuilder builder) { this.builder = builder; }

        House buildMinimalHouse() {
            builder.buildFoundation();
            builder.buildWalls();
            builder.buildRoof();
            return builder.getResult();
        }

        House buildFullHouse() {
            builder.buildFoundation();
            builder.buildWalls();
            builder.buildRoof();
            builder.buildWindows();
            return builder.getResult();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Fluent Builder (HTTP Request) ===");

        HttpRequest getRequest = new HttpRequest.Builder("GET", "https://api.example.com/users")
            .header("Authorization: Bearer token123")
            .header("Accept: application/json")
            .timeout(10)
            .build();
        System.out.println(getRequest);

        HttpRequest postRequest = new HttpRequest.Builder("POST", "https://api.example.com/users")
            .header("Content-Type: application/json")
            .body("{name:Alice,email:alice@example.com}")
            .timeout(30)
            .followRedirects(false)
            .build();
        System.out.println(postRequest);

        System.out.println("\n=== Classic GoF Builder (House) ===");
        HouseDirector director = new HouseDirector();

        director.setBuilder(new WoodenHouseBuilder());
        House woodenHouse = director.buildFullHouse();
        System.out.println("Wooden: " + woodenHouse);

        director.setBuilder(new BrickHouseBuilder());
        House brickHouse = director.buildFullHouse();
        System.out.println("Brick:  " + brickHouse);

        director.setBuilder(new BrickHouseBuilder());
        House minimalHouse = director.buildMinimalHouse();
        System.out.println("Minimal: " + minimalHouse);
    }
}
