package org.interview.design_patterns.behavioral.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================
 * Design Pattern: Memento (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Without violating encapsulation, capture and externalize an object's
 *   internal state so that the object can be restored to this state later.
 *
 * ALSO KNOWN AS: Snapshot, Token
 *
 * PROBLEM IT SOLVES:
 *   - You need to implement undo functionality.
 *   - You need to save/restore object state (game save, transaction rollback).
 *   - You can't expose internals to do so (would break encapsulation).
 *
 * STRUCTURE:
 *   - Originator: creates mementos of its own state; can restore from a memento
 *   - Memento: stores state snapshot; only Originator can read its content
 *   - Caretaker: holds and manages mementos; NEVER modifies them
 *
 * KEY INSIGHT:
 *   - Memento is opaque to the caretaker (only Originator sees inside it)
 *   - In Java: inner class / package-private constructor enforces this
 *
 * DIFFERENCE FROM COMMAND:
 *   - Command: undo by reversing an action; stores "what was done"
 *   - Memento: undo by restoring a snapshot; stores "full state"
 *
 * REAL-WORLD EXAMPLES:
 *   - Game save/load checkpoints
 *   - Ctrl+Z in text editors
 *   - Database transaction SAVEPOINT
 *   - Browser session state
 *
 * PROS:
 *   + Preserve encapsulation boundaries
 *   + Simple undo/redo by storing full snapshots
 *
 * CONS:
 *   - High memory usage if state is large or history is long
 *   - Caretaker must manage memento lifecycle (GC risk)
 *
 * SCENARIO:
 *   A game character level editor with undo/redo.
 *   Each state change saves a snapshot; undo restores the previous one.
 */
public class MementoPattern {

    // ================================================================
    // Memento — opaque snapshot of Originator's state
    // Declared as a static nested class: caretaker holds it but can't read it
    // Only the Originator can access the internal state.
    // ================================================================
    static final class GameStateMemento {
        // These fields are accessible only to GameCharacter (same outer class scope)
        private final String level;
        private final int    health;
        private final int    score;
        private final String location;
        private final String timestamp;

        private GameStateMemento(String level, int health, int score, String location) {
            this.level     = level;
            this.health    = health;
            this.score     = score;
            this.location  = location;
            this.timestamp = java.time.LocalTime.now().toString();
        }

        @Override
        public String toString() {
            return String.format("Snapshot[level=%s, hp=%d, score=%d, loc=%s @ %s]",
                level, health, score, location, timestamp);
        }
    }

    // ================================================================
    // Originator: GameCharacter
    // Creates mementos of its own state; can restore from a memento
    // ================================================================
    static class GameCharacter {
        private String level;
        private int    health;
        private int    score;
        private String location;

        GameCharacter(String level, int health, int score, String location) {
            this.level    = level;
            this.health   = health;
            this.score    = score;
            this.location = location;
        }

        // Game actions that modify state
        public void gainScore(int points) {
            score += points;
            System.out.printf("[Character] Score +%d → total %d%n", points, score);
        }

        public void takeDamage(int damage) {
            health = Math.max(0, health - damage);
            System.out.printf("[Character] Took %d damage → HP: %d%n", damage, health);
        }

        public void moveTo(String newLocation) {
            System.out.printf("[Character] Moving: %s → %s%n", location, newLocation);
            location = newLocation;
        }

        public void levelUp(String newLevel) {
            System.out.printf("[Character] Level up: %s → %s%n", level, newLevel);
            level = newLevel;
            health = 100; // restore HP on level up
        }

        /**
         * SAVE: Create a memento capturing current state.
         * The memento holds a snapshot — it doesn't share references.
         */
        public GameStateMemento save() {
            System.out.printf("[Character] 💾 Saving state: level=%s, hp=%d, score=%d, loc=%s%n",
                level, health, score, location);
            return new GameStateMemento(level, health, score, location);
        }

        /**
         * RESTORE: Restore state from a memento.
         * Only this class can access memento's private fields.
         */
        public void restore(GameStateMemento memento) {
            this.level    = memento.level;
            this.health   = memento.health;
            this.score    = memento.score;
            this.location = memento.location;
            System.out.printf("[Character] 🔄 Restored: level=%s, hp=%d, score=%d, loc=%s%n",
                level, health, score, location);
        }

        @Override
        public String toString() {
            return String.format("Character{level=%s, hp=%d, score=%d, loc=%s}",
                level, health, score, location);
        }
    }

    // ================================================================
    // Caretaker: GameHistory
    // Manages the memento stack for undo/redo.
    // NEVER reads/modifies memento content — only stores and returns them.
    // ================================================================
    static class GameHistory {
        private final Deque<GameStateMemento> undoStack = new ArrayDeque<>();
        private final Deque<GameStateMemento> redoStack = new ArrayDeque<>();
        private final GameCharacter           character;

        GameHistory(GameCharacter character) {
            this.character = character;
        }

        /** Save the current state and push onto undo stack */
        public void checkpoint() {
            GameStateMemento saved = character.save();
            undoStack.push(saved);
            redoStack.clear(); // new action invalidates redo history
            System.out.println("[History] Checkpoint saved. Undo stack size: " + undoStack.size());
        }

        /** Undo: restore previous state */
        public void undo() {
            if (undoStack.isEmpty()) {
                System.out.println("[History] Nothing to undo!");
                return;
            }
            // Save current state to redo stack before undoing
            redoStack.push(character.save());
            GameStateMemento previous = undoStack.pop();
            character.restore(previous);
            System.out.println("[History] Undo complete. Undo stack: " + undoStack.size());
        }

        /** Redo: re-apply previously undone state */
        public void redo() {
            if (redoStack.isEmpty()) {
                System.out.println("[History] Nothing to redo!");
                return;
            }
            undoStack.push(character.save());
            GameStateMemento redoState = redoStack.pop();
            character.restore(redoState);
            System.out.println("[History] Redo complete. Redo stack: " + redoStack.size());
        }

        /** List all saved checkpoints */
        public void listCheckpoints() {
            System.out.println("[History] Saved checkpoints:");
            undoStack.forEach(m -> System.out.println("  " + m));
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        GameCharacter hero    = new GameCharacter("Level-1", 100, 0, "Town");
        GameHistory   history = new GameHistory(hero);

        System.out.println("=== Initial state ===");
        System.out.println(hero);

        // Checkpoint before entering dungeon
        history.checkpoint();

        System.out.println("\n=== Dungeon exploration ===");
        hero.moveTo("Dungeon Entrance");
        hero.gainScore(500);
        hero.takeDamage(30);

        // Checkpoint mid-dungeon (good save point)
        history.checkpoint();

        System.out.println("\n=== Boss fight ===");
        hero.takeDamage(80);  // terrible fight — nearly dead!
        hero.gainScore(1000);
        System.out.println("After boss: " + hero);

        System.out.println("\n=== Undo (back to before boss fight) ===");
        history.undo();
        System.out.println("After undo: " + hero);

        System.out.println("\n=== Redo (replay boss fight result) ===");
        history.redo();
        System.out.println("After redo: " + hero);

        System.out.println("\n=== Undo all the way back to Town ===");
        history.undo(); // undo boss fight
        history.undo(); // undo dungeon exploration
        System.out.println("Back to start: " + hero);

        history.undo(); // nothing left to undo
    }
}
