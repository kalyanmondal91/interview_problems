---
layout: design_pattern
title: "Interpreter"
category: "behavioral"
difficulty: "Hard"
intent: "Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language."
tags: [behavioral, interpreter, dsl]
render_with_liquid: false
---

## Intent

Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **AbstractExpression** | Declares interpret(Context) method |
| **TerminalExpression** | Directly evaluates a leaf (literal, variable) |
| **NonTerminalExpression** | Evaluates by combining child expressions |
| **Context** | Global state/variables accessible during interpretation |

## Pros

- Easy to change/extend grammar
- Each rule is its own class

## Cons

- Complex grammars need many classes
- Parser generators (ANTLR) are better for complex grammars
- Interpreting is slower than native code

## Real-World Examples

- `SQL parsers`
- `Regular expression engines`
- `Mathematical expression evaluators`
- `Spring EL, OGNL configuration parsers`

## Variants

- AST-based interpreter
- Stack-based interpreter (RPN)

## Full Java Implementation

```java
package org.interview.design_patterns.behavioral.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * ============================================================
 * Design Pattern: Interpreter (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Given a language, define a representation for its grammar along
 *   with an interpreter that uses the representation to interpret
 *   sentences in the language.
 *
 * PROBLEM IT SOLVES:
 *   - Interpret a simple language or rule set (SQL subsets, regex, math expressions).
 *   - You need a lightweight DSL (Domain Specific Language).
 *   - You want extensible grammar where new expressions can be added.
 *
 * STRUCTURE:
 *   - AbstractExpression (interface): declares interpret(Context) method
 *   - TerminalExpression: directly evaluates a leaf (literal, variable)
 *   - NonTerminalExpression: evaluates by combining child expressions
 *   - Context: global state/variables accessible during interpretation
 *   - Client: builds the AST (Abstract Syntax Tree) from expressions
 *
 * REAL-WORLD EXAMPLES:
 *   - SQL parsers
 *   - Regular expression engines
 *   - Mathematical expression evaluators
 *   - Configuration file parsers (Spring EL, OGNL)
 *   - Scripting engines (Groovy, Rhino)
 *
 * PROS:
 *   + Easy to change/extend grammar
 *   + Implementing the grammar is straightforward
 *
 * CONS:
 *   - Complex grammars need many classes (hard to maintain)
 *   - For complex grammars, parser generators (ANTLR) are better
 *   - Efficiency: interpreting is slower than native code
 *
 * SCENARIO:
 *   Mathematical expression interpreter.
 *   Parses and evaluates arithmetic expressions with variables.
 *   Example: "(x + 5) * (y - 2)" given x=10, y=7 → (10+5)*(7-2) = 75
 */
public class InterpreterPattern {

    // ================================================================
    // Context: stores variable assignments (the "environment")
    // ================================================================
    static class Context {
        private final Map<String, Integer> variables = new HashMap<>();

        public void assign(String name, int value) {
            variables.put(name, value);
        }

        public int lookup(String name) {
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            return variables.get(name);
        }
    }

    // ================================================================
    // AbstractExpression — all expressions implement this
    // ================================================================
    interface Expression {
        int interpret(Context context);
    }

    // ================================================================
    // Terminal Expressions — leaf nodes of the AST
    // ================================================================

    /** Literal number: 42 */
    static class NumberExpression implements Expression {
        private final int value;

        NumberExpression(int value) { this.value = value; }

        @Override
        public int interpret(Context context) {
            return value; // just return the literal value
        }

        @Override public String toString() { return String.valueOf(value); }
    }

    /** Variable reference: x, y, total */
    static class VariableExpression implements Expression {
        private final String name;

        VariableExpression(String name) { this.name = name; }

        @Override
        public int interpret(Context context) {
            return context.lookup(name); // look up in the context (environment)
        }

        @Override public String toString() { return name; }
    }

    // ================================================================
    // Non-Terminal Expressions — internal AST nodes combining sub-expressions
    // ================================================================

    /** Addition: left + right */
    static class AddExpression implements Expression {
        private final Expression left, right;

        AddExpression(Expression left, Expression right) {
            this.left  = left;
            this.right = right;
        }

        @Override
        public int interpret(Context context) {
            return left.interpret(context) + right.interpret(context);
        }

        @Override public String toString() { return "(" + left + " + " + right + ")"; }
    }

    /** Subtraction: left - right */
    static class SubtractExpression implements Expression {
        private final Expression left, right;

        SubtractExpression(Expression left, Expression right) {
            this.left  = left;
            this.right = right;
        }

        @Override
        public int interpret(Context context) {
            return left.interpret(context) - right.interpret(context);
        }

        @Override public String toString() { return "(" + left + " - " + right + ")"; }
    }

    /** Multiplication: left * right */
    static class MultiplyExpression implements Expression {
        private final Expression left, right;

        MultiplyExpression(Expression left, Expression right) {
            this.left  = left;
            this.right = right;
        }

        @Override
        public int interpret(Context context) {
            return left.interpret(context) * right.interpret(context);
        }

        @Override public String toString() { return "(" + left + " * " + right + ")"; }
    }

    /** Division: left / right (with divide-by-zero check) */
    static class DivideExpression implements Expression {
        private final Expression left, right;

        DivideExpression(Expression left, Expression right) {
            this.left  = left;
            this.right = right;
        }

        @Override
        public int interpret(Context context) {
            int divisor = right.interpret(context);
            if (divisor == 0) throw new ArithmeticException("Division by zero!");
            return left.interpret(context) / divisor;
        }

        @Override public String toString() { return "(" + left + " / " + right + ")"; }
    }

    /** Negation: -expression */
    static class NegateExpression implements Expression {
        private final Expression inner;

        NegateExpression(Expression inner) { this.inner = inner; }

        @Override
        public int interpret(Context context) {
            return -inner.interpret(context);
        }

        @Override public String toString() { return "(-" + inner + ")"; }
    }

    // ================================================================
    // BONUS: Simple postfix (RPN) parser
    // Converts "x 5 + y 2 - *" → AST using a stack
    // ================================================================
    static class RpnParser {
        /**
         * Parse a Reverse Polish Notation expression string into an AST.
         * Tokens separated by spaces. Operands: numbers or single-char variables.
         * Operators: + - * /
         */
        public Expression parse(String rpnExpression) {
            Stack<Expression> stack = new Stack<>();

            for (String token : rpnExpression.split("\s+")) {
                switch (token) {
                    case "+" -> {
                        Expression r = stack.pop();
                        Expression l = stack.pop();
                        stack.push(new AddExpression(l, r));
                    }
                    case "-" -> {
                        Expression r = stack.pop();
                        Expression l = stack.pop();
                        stack.push(new SubtractExpression(l, r));
                    }
                    case "*" -> {
                        Expression r = stack.pop();
                        Expression l = stack.pop();
                        stack.push(new MultiplyExpression(l, r));
                    }
                    case "/" -> {
                        Expression r = stack.pop();
                        Expression l = stack.pop();
                        stack.push(new DivideExpression(l, r));
                    }
                    default -> {
                        // Try parsing as a number; otherwise treat as variable
                        try {
                            stack.push(new NumberExpression(Integer.parseInt(token)));
                        } catch (NumberFormatException e) {
                            stack.push(new VariableExpression(token));
                        }
                    }
                }
            }

            if (stack.size() != 1) {
                throw new IllegalArgumentException("Invalid RPN expression: " + rpnExpression);
            }
            return stack.pop(); // the root of the AST
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        Context ctx = new Context();
        ctx.assign("x", 10);
        ctx.assign("y", 7);
        ctx.assign("z", 3);

        System.out.println("=== Manually Built AST ===");
        // Build: (x + 5) * (y - 2)
        Expression expr1 = new MultiplyExpression(
            new AddExpression(new VariableExpression("x"), new NumberExpression(5)),
            new SubtractExpression(new VariableExpression("y"), new NumberExpression(2))
        );
        System.out.printf("Expression: %s%n", expr1);
        System.out.printf("With x=%d, y=%d → %d%n", 10, 7, expr1.interpret(ctx));
        // Expected: (10+5) * (7-2) = 15 * 5 = 75

        // Build: z * (x - y) + 8
        Expression expr2 = new AddExpression(
            new MultiplyExpression(
                new VariableExpression("z"),
                new SubtractExpression(new VariableExpression("x"), new VariableExpression("y"))
            ),
            new NumberExpression(8)
        );
        System.out.printf("%nExpression: %s%n", expr2);
        System.out.printf("With x=%d, y=%d, z=%d → %d%n", 10, 7, 3, expr2.interpret(ctx));
        // Expected: 3 * (10-7) + 8 = 3*3 + 8 = 17

        // Negation
        Expression expr3 = new NegateExpression(new AddExpression(
            new VariableExpression("x"), new NumberExpression(2)
        ));
        System.out.printf("%nExpression: %s%n", expr3);
        System.out.printf("With x=%d → %d%n", 10, expr3.interpret(ctx));
        // Expected: -(10+2) = -12

        System.out.println("\n=== RPN Parser ===");
        RpnParser parser = new RpnParser();

        // "x 5 + y 2 - *" == (x+5) * (y-2)
        String rpn1 = "x 5 + y 2 - *";
        Expression parsed1 = parser.parse(rpn1);
        System.out.printf("RPN: %-20s → AST: %s%n", rpn1, parsed1);
        System.out.printf("With x=%d, y=%d → %d%n", 10, 7, parsed1.interpret(ctx));

        // "z x y - * 8 +" == z*(x-y) + 8
        String rpn2 = "z x y - * 8 +";
        Expression parsed2 = parser.parse(rpn2);
        System.out.printf("%nRPN: %-20s → AST: %s%n", rpn2, parsed2);
        System.out.printf("With x=%d, y=%d, z=%d → %d%n", 10, 7, 3, parsed2.interpret(ctx));

        // Division
        String rpn3 = "x z /";
        Expression parsed3 = parser.parse(rpn3);
        System.out.printf("%nRPN: %-20s → AST: %s%n", rpn3, parsed3);
        System.out.printf("With x=%d, z=%d → %d%n", 10, 3, parsed3.interpret(ctx));

        // Test variable reassignment
        System.out.println("\n=== Variable reassignment ===");
        ctx.assign("x", 20);
        System.out.printf("Now x=20: %s → %d%n", parsed1, parsed1.interpret(ctx));
    }
}

```
