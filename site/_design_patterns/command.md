---
layout: design_pattern
title: "Command"
category: "behavioral"
difficulty: "Medium"
also_known_as: "Action, Transaction"
intent: "Encapsulate a request as an object, letting you parameterize clients with different requests, queue or log requests, and support undoable operations."
tags: [behavioral, command, undo]
render_with_liquid: false
---

## Intent

Encapsulate a request as an object, letting you parameterize clients with different requests, queue or log requests, and support undoable operations.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Command** | Declares execute() and undo() |
| **ConcreteCommand** | Implements a specific action on a Receiver |
| **Receiver** | The object that performs the actual work |
| **Invoker** | Holds and triggers commands; maintains history |
| **Client** | Creates ConcreteCommand objects |

## Pros

- Decouples sender from receiver
- Commands can be queued, logged, replayed, undone
- Supports composite commands (macros)
- Open/Closed

## Cons

- Code can explode with many small command classes
- Undo can be complex (state needs to be stored)

## Real-World Examples

- `Text editor undo/redo stack`
- `Transaction rollback in databases`
- `GUI buttons/menus`
- `Job queues and message brokers`

## Variants

- Simple Command
- Composite Command (Macro)
- Queued Command
- Transactional Command

## Full Java Implementation

```java
package org.interview.design_patterns.behavioral.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================
 * Design Pattern: Command (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Encapsulate a request as an object, thereby letting you
 *   parameterize clients with different requests, queue or log
 *   requests, and support undoable operations.
 *
 * ALSO KNOWN AS: Action, Transaction
 *
 * PROBLEM IT SOLVES:
 *   - You need to decouple the sender of a request from its receiver.
 *   - You need to support undo/redo, queuing, logging, or macro-recording.
 *   - You want to schedule requests for later execution.
 *
 * STRUCTURE:
 *   - Command (interface): declares execute() and undo()
 *   - ConcreteCommand: implements a specific action on a Receiver
 *   - Receiver: the object that performs the actual work
 *   - Invoker: holds and triggers commands; maintains command history
 *   - Client: creates ConcreteCommand objects and sets their receivers
 *
 * REAL-WORLD EXAMPLES:
 *   - Text editor undo/redo stack
 *   - Transaction rollback in databases
 *   - GUI buttons/menus (each maps to a Command)
 *   - Job queues and message brokers
 *   - Macro recording in office suites
 *
 * PROS:
 *   + Decouples sender from receiver
 *   + Commands can be queued, logged, replayed, undone
 *   + Supports composite commands (macros)
 *   + Open/Closed: add new commands without changing invoker
 *
 * CONS:
 *   - Code can explode with many small command classes
 *   - Undo can be complex (state needs to be stored)
 *
 * SCENARIO:
 *   Text editor with undo/redo: each keystroke is a Command object.
 *   The history stack enables multi-level undo.
 */
public class CommandPattern {

    // ================================================================
    // Command Interface
    // Both execute() and undo() are required for undo/redo support
    // ================================================================
    interface Command {
        void execute();
        void undo();
    }

    // ================================================================
    // Receiver: TextEditor
    // Contains the actual text manipulation logic (the "real work")
    // Commands delegate to methods of this class.
    // ================================================================
    static class TextEditor {
        private final StringBuilder text = new StringBuilder();

        public void insertText(String s, int pos) {
            text.insert(pos, s);
        }

        public void deleteText(int pos, int length) {
            text.delete(pos, pos + length);
        }

        public String getText() { return text.toString(); }

        public int length()    { return text.length(); }
    }

    // ================================================================
    // ConcreteCommand 1: InsertCommand
    // Encapsulates a text insertion; knows how to undo it (delete)
    // ================================================================
    static class InsertCommand implements Command {
        private final TextEditor editor;
        private final String     insertedText;
        private final int        position;

        InsertCommand(TextEditor editor, String insertedText, int position) {
            this.editor       = editor;
            this.insertedText = insertedText;
            this.position     = position;
        }

        @Override
        public void execute() {
            editor.insertText(insertedText, position);
            System.out.printf("  [Insert] '%s' at pos %d → %s%n",
                insertedText, position, editor.getText());
        }

        @Override
        public void undo() {
            // Undo insert = delete the same text we inserted
            editor.deleteText(position, insertedText.length());
            System.out.printf("  [Undo Insert] Removed '%s' → %s%n",
                insertedText, editor.getText());
        }
    }

    // ================================================================
    // ConcreteCommand 2: DeleteCommand
    // Encapsulates deletion; stores deleted text to undo
    // ================================================================
    static class DeleteCommand implements Command {
        private final TextEditor editor;
        private final int        position;
        private final int        length;
        private String           deletedText; // saved for undo

        DeleteCommand(TextEditor editor, int position, int length) {
            this.editor   = editor;
            this.position = position;
            this.length   = length;
        }

        @Override
        public void execute() {
            // Save what we're about to delete so we can restore it on undo
            deletedText = editor.getText().substring(position, position + length);
            editor.deleteText(position, length);
            System.out.printf("  [Delete] Removed '%s' at pos %d → %s%n",
                deletedText, position, editor.getText());
        }

        @Override
        public void undo() {
            // Undo delete = re-insert the saved text
            editor.insertText(deletedText, position);
            System.out.printf("  [Undo Delete] Restored '%s' → %s%n",
                deletedText, editor.getText());
        }
    }

    // ================================================================
    // ConcreteCommand 3: MacroCommand (Composite Command)
    // A sequence of commands executed as one atomic unit
    // ================================================================
    static class MacroCommand implements Command {
        private final java.util.List<Command> commands = new java.util.ArrayList<>();

        public void addCommand(Command cmd) {
            commands.add(cmd);
        }

        @Override
        public void execute() {
            System.out.println("  [Macro] Executing " + commands.size() + " commands:");
            commands.forEach(Command::execute);
        }

        @Override
        public void undo() {
            System.out.println("  [Macro] Undoing " + commands.size() + " commands (reverse order):");
            // Undo in REVERSE order (LIFO)
            for (int i = commands.size() - 1; i >= 0; i--) {
                commands.get(i).undo();
            }
        }
    }

    // ================================================================
    // Invoker: EditorController
    // Executes commands and maintains undo/redo history stacks
    // ================================================================
    static class EditorController {
        private final Deque<Command> undoStack = new ArrayDeque<>();
        private final Deque<Command> redoStack = new ArrayDeque<>();

        /**
         * Execute a command and push it onto the undo stack.
         * Any redo history is invalidated (new action branching).
         */
        public void execute(Command command) {
            command.execute();
            undoStack.push(command);
            redoStack.clear(); // new action invalidates redo history
        }

        /**
         * Undo the last executed command.
         * Moves it to the redo stack for potential redo.
         */
        public void undo() {
            if (undoStack.isEmpty()) {
                System.out.println("  [Controller] Nothing to undo.");
                return;
            }
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }

        /**
         * Redo the last undone command.
         * Moves it back to the undo stack.
         */
        public void redo() {
            if (redoStack.isEmpty()) {
                System.out.println("  [Controller] Nothing to redo.");
                return;
            }
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        TextEditor     editor     = new TextEditor();
        EditorController controller = new EditorController();

        System.out.println("=== Text Editor with Undo/Redo ===");
        System.out.println("Initial text: \" + editor.getText() + \"");

        // Execute commands
        controller.execute(new InsertCommand(editor, "Hello", 0));
        controller.execute(new InsertCommand(editor, " World", 5));
        controller.execute(new InsertCommand(editor, "!", 11));
        controller.execute(new DeleteCommand(editor, 0, 5)); // delete "Hello"

        System.out.println("\n--- Undo x3 ---");
        controller.undo(); // undo delete
        controller.undo(); // undo "!"
        controller.undo(); // undo " World"

        System.out.println("\n--- Redo x2 ---");
        controller.redo();
        controller.redo();

        System.out.println("\n=== Macro Command (atomic batch) ===");
        MacroCommand macro = new MacroCommand();
        macro.addCommand(new InsertCommand(editor, " Beautiful", 5));
        macro.addCommand(new InsertCommand(editor, " Day", editor.length()));

        controller.execute(macro);
        System.out.println("After macro: \" + editor.getText() + \"");

        System.out.println("\n--- Undo macro (undoes all steps) ---");
        controller.undo();
        System.out.println("After undo:  \" + editor.getText() + \"");
    }
}

```
