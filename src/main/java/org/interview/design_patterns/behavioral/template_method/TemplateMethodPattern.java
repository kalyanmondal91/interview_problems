package org.interview.design_patterns.behavioral.template_method;

/**
 * Design Pattern: Template Method (Behavioral)
 *
 * INTENT: Define the skeleton of an algorithm in a base class, deferring
 * some steps to subclasses. Subclasses redefine certain steps without
 * changing the overall structure.
 *
 * STRUCTURE:
 *   - AbstractClass: defines the template method (FINAL) + abstract/hook methods
 *   - Abstract steps: MUST be implemented by subclasses
 *   - Hook methods: OPTIONAL override points with default implementations
 *   - ConcreteClass: implements abstract steps; optionally overrides hooks
 *
 * KEY INSIGHT: Template method calls steps in the right order.
 * Subclasses fill in the blanks -- they cannot change the order.
 *
 * REAL-WORLD EXAMPLES:
 *   - java.io.InputStream: read(byte[]) calls abstract read()
 *   - java.util.AbstractList: subclasses implement get() and size()
 *   - Spring JdbcTemplate: query(), update() are template methods
 *
 * SCENARIO: ETL (Extract-Transform-Load) data export pipeline.
 */
public class TemplateMethodPattern {

    // Abstract Class: defines the ETL skeleton
    static abstract class DataExporter {

        // TEMPLATE METHOD -- FINAL so subclasses cannot reorder steps
        public final void export(String destination) {
            System.out.println("\n=== Starting export to: " + destination + " ===");
            connect();
            String raw = extractData();
            String processed = transform(raw);
            if (shouldValidate()) {  // hook: can be disabled
                validate(processed);
            }
            load(processed, destination);
            disconnect();
            System.out.println("=== Export complete ===");
        }

        // Abstract steps -- subclasses MUST implement these
        protected abstract void connect();
        protected abstract String extractData();
        protected abstract String transform(String raw);
        protected abstract void load(String data, String destination);

        // Hook method -- subclasses MAY override (optional)
        protected boolean shouldValidate() { return true; }

        // Hook with default implementation
        protected void validate(String data) {
            System.out.println("  [Validate] Data OK (length=" + data.length() + ")");
        }

        // Concrete shared method -- not overridden
        protected void disconnect() {
            System.out.println("  [Cleanup] Closing connection.");
        }
    }

    // ConcreteClass 1: CSV to JSON exporter
    static class CsvToJsonExporter extends DataExporter {
        @Override
        protected void connect() {
            System.out.println("  [CSV] Opening CSV file");
        }

        @Override
        protected String extractData() {
            // Build multi-line CSV string safely
            String csv = "id,name,email" + "\n" + "1,Alice,alice@ex.com" + "\n" + "2,Bob,bob@ex.com";
            System.out.println("  [CSV] Extracted " + csv.split("\n").length + " rows");
            return csv;
        }

        @Override
        protected String transform(String raw) {
            String[] lines = raw.split("\n");
            String[] headers = lines[0].split(",");
            StringBuilder json = new StringBuilder("[");
            for (int i = 1; i < lines.length; i++) {
                String[] values = lines[i].split(",");
                json.append("{");
                for (int j = 0; j < headers.length; j++) {
                    json.append("\"").append(headers[j]).append("\":\"").append(values[j]).append("\"");
                    if (j < headers.length - 1) json.append(",");
                }
                json.append("}");
                if (i < lines.length - 1) json.append(",");
            }
            json.append("]");
            String result = json.toString();
            System.out.println("  [Transform] CSV -> JSON: " + result);
            return result;
        }

        @Override
        protected void load(String data, String destination) {
            System.out.println("  [Load] Writing JSON to: " + destination);
        }
    }

    // ConcreteClass 2: Database to XML exporter -- skips validation via hook
    static class DatabaseToXmlExporter extends DataExporter {
        @Override
        protected void connect() {
            System.out.println("  [DB] Connecting to PostgreSQL");
        }

        @Override
        protected String extractData() {
            System.out.println("  [DB] SELECT * FROM users -> 3 records");
            return "[(id=1,name=Alice),(id=2,name=Bob),(id=3,name=Charlie)]";
        }

        @Override
        protected String transform(String raw) {
            String xml = "<users><user><id>1</id><name>Alice</name></user>"
                       + "<user><id>2</id><name>Bob</name></user></users>";
            System.out.println("  [Transform] DB records -> XML");
            return xml;
        }

        @Override
        protected void load(String data, String destination) {
            System.out.println("  [Load] Uploading XML to: " + destination);
        }

        // HOOK OVERRIDE: disable validation for trusted DB source
        @Override
        protected boolean shouldValidate() {
            System.out.println("  [Hook] Skipping validation (trusted source)");
            return false;
        }
    }

    // ConcreteClass 3: API to CSV exporter with custom validation
    static class ApiToCsvExporter extends DataExporter {
        @Override
        protected void connect() {
            System.out.println("  [API] GET https://api.example.com/users");
        }

        @Override
        protected String extractData() {
            String json = "[{\"id\":1,\"name\":\"Alice\"},{\"id\":2,\"name\":\"Bob\"}]";
            System.out.println("  [API] Response received");
            return json;
        }

        @Override
        protected String transform(String raw) {
            String csv = "id,name" + "\n" + "1,Alice" + "\n" + "2,Bob";
            System.out.println("  [Transform] JSON -> CSV");
            return csv;
        }

        @Override
        protected void load(String data, String destination) {
            System.out.println("  [Load] Writing CSV to: " + destination);
        }

        // Custom validation override
        @Override
        protected void validate(String data) {
            if (!data.startsWith("id,")) {
                throw new IllegalStateException("CSV missing header row!");
            }
            System.out.println("  [Validate] CSV header check passed");
        }
    }

    public static void main(String[] args) {
        DataExporter exporter;

        exporter = new CsvToJsonExporter();
        exporter.export("output/users.json");

        exporter = new DatabaseToXmlExporter();
        exporter.export("s3://mybucket/users.xml");

        exporter = new ApiToCsvExporter();
        exporter.export("/tmp/export.csv");
    }
}
