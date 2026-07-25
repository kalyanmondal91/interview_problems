package org.interview.design_patterns.structural.facade;

/**
 * ============================================================
 * Design Pattern: Facade (Structural)
 * ============================================================
 *
 * INTENT:
 *   Provide a simplified, unified interface to a complex subsystem.
 *   The facade doesn't prevent clients from using the subsystem directly
 *   if they need fine-grained control.
 *
 * PROBLEM IT SOLVES:
 *   - Complex subsystems with many classes and dependencies are hard to use.
 *   - Client code becomes tightly coupled to subsystem internals.
 *   - You want to layer a system and expose only what the client needs.
 *
 * STRUCTURE:
 *   - Facade: provides simple methods that internally orchestrate the subsystem
 *   - Subsystem classes: do the actual work; unaware of the facade
 *   - Client: calls only the facade; decoupled from subsystem complexity
 *
 * DIFFERENCE FROM ADAPTER:
 *   - Adapter: makes an existing interface work like another (1-to-1)
 *   - Facade: provides a simpler interface over MULTIPLE complex classes
 *
 * REAL-WORLD EXAMPLES:
 *   - Spring JdbcTemplate (hides Connection/Statement/ResultSet complexity)
 *   - SLF4J (hides Log4J/Logback internals)
 *   - Home theater system (one "watch movie" button does everything)
 *   - E-commerce checkout (hides payment, inventory, shipping subsystems)
 *
 * PROS:
 *   + Isolates clients from subsystem complexity
 *   + Promotes weak coupling
 *   + Simplifies code on the client side
 *
 * CONS:
 *   - Facade can become a "god object" coupled to all subsystem classes
 *   - Clients lose fine-grained control (if they need it)
 *
 * SCENARIO:
 *   Home theater system. To watch a movie, many devices must be coordinated:
 *   projector, amp, DVD player, lights, screen. The facade reduces
 *   this to: watchMovie("Inception") and endMovie().
 */
public class FacadePattern {

    // ================================================================
    // Subsystem classes — complex, low-level, not designed for end-users
    // ================================================================

    static class Projector {
        public void on()             { System.out.println("[Projector] Power ON"); }
        public void off()            { System.out.println("[Projector] Power OFF"); }
        public void setInput(String s){ System.out.println("[Projector] Input → " + s); }
        public void wideScreenMode() { System.out.println("[Projector] Widescreen mode activated"); }
    }

    static class Amplifier {
        public void on()                  { System.out.println("[Amplifier] Power ON"); }
        public void off()                 { System.out.println("[Amplifier] Power OFF"); }
        public void setVolume(int level)  { System.out.println("[Amplifier] Volume → " + level); }
        public void setSurroundSound()    { System.out.println("[Amplifier] 7.1 Surround Sound ON"); }
        public void setInput(String src)  { System.out.println("[Amplifier] Source → " + src); }
    }

    static class DvdPlayer {
        public void on()              { System.out.println("[DVD] Player ON"); }
        public void off()             { System.out.println("[DVD] Player OFF"); }
        public void play(String movie){ System.out.println("[DVD] Playing: \" + movie + \""); }
        public void stop()            { System.out.println("[DVD] Stopped"); }
        public void eject()           { System.out.println("[DVD] Disc ejected"); }
    }

    static class Screen {
        public void down()  { System.out.println("[Screen] Lowering screen..."); }
        public void up()    { System.out.println("[Screen] Raising screen..."); }
    }

    static class TheaterLights {
        public void dim(int level)  { System.out.println("[Lights] Dimming to " + level + "%"); }
        public void on()            { System.out.println("[Lights] Full brightness"); }
    }

    static class PopcornPopper {
        public void on()    { System.out.println("[Popcorn] Popper ON"); }
        public void off()   { System.out.println("[Popcorn] Popper OFF"); }
        public void pop()   { System.out.println("[Popcorn] Popping popcorn!"); }
    }

    // ================================================================
    // FACADE
    // Provides simple, high-level methods.
    // Internally orchestrates the 6 subsystem classes.
    // Client doesn't need to know any of them.
    // ================================================================
    static class HomeTheaterFacade {
        // Holds references to all subsystem components
        private final Projector     projector;
        private final Amplifier     amp;
        private final DvdPlayer     dvd;
        private final Screen        screen;
        private final TheaterLights lights;
        private final PopcornPopper popper;

        HomeTheaterFacade(Projector projector, Amplifier amp, DvdPlayer dvd,
                          Screen screen, TheaterLights lights, PopcornPopper popper) {
            this.projector = projector;
            this.amp       = amp;
            this.dvd       = dvd;
            this.screen    = screen;
            this.lights    = lights;
            this.popper    = popper;
        }

        /**
         * ONE simple call to watch a movie.
         * Internally coordinates all 6 subsystem components.
         */
        public void watchMovie(String movie) {
            System.out.println("\n=== Get ready to watch " + movie + "! ===");
            popper.on();
            popper.pop();
            lights.dim(10);
            screen.down();
            projector.on();
            projector.wideScreenMode();
            projector.setInput("DVD");
            amp.on();
            amp.setInput("DVD");
            amp.setSurroundSound();
            amp.setVolume(7);
            dvd.on();
            dvd.play(movie);
        }

        /**
         * ONE simple call to end the movie session.
         * Shuts everything down in the right order.
         */
        public void endMovie() {
            System.out.println("\n=== Shutting down the theater ===");
            dvd.stop();
            dvd.eject();
            dvd.off();
            amp.off();
            projector.off();
            screen.up();
            lights.on();
            popper.off();
            System.out.println("Goodnight!");
        }

        /**
         * Extra facade method: listen to music (different configuration)
         */
        public void listenToMusic(String source) {
            System.out.println("\n=== Music Mode: " + source + " ===");
            amp.on();
            amp.setInput(source);
            amp.setVolume(5);
            // no projector, no screen, no DVD needed
            lights.dim(40);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        // Create subsystem objects (normally hidden from client)
        Projector     projector = new Projector();
        Amplifier     amp       = new Amplifier();
        DvdPlayer     dvd       = new DvdPlayer();
        Screen        screen    = new Screen();
        TheaterLights lights    = new TheaterLights();
        PopcornPopper popper    = new PopcornPopper();

        // Create the facade
        HomeTheaterFacade theater = new HomeTheaterFacade(
            projector, amp, dvd, screen, lights, popper
        );

        // Client uses simple, human-readable methods
        theater.watchMovie("Inception");

        System.out.println("\n... [2 hours later] ...");
        theater.endMovie();

        theater.listenToMusic("Bluetooth");
    }
}
