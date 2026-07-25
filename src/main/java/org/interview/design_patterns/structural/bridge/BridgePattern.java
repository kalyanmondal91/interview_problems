package org.interview.design_patterns.structural.bridge;

/**
 * ============================================================
 * Design Pattern: Bridge (Structural)
 * ============================================================
 *
 * INTENT:
 *   Decouple an abstraction from its implementation so that the two
 *   can vary independently.
 *
 * PROBLEM IT SOLVES:
 *   Without Bridge, combining two dimensions of variation leads to
 *   a class explosion. E.g., Shape × Color → CircleRed, CircleBlue,
 *   SquareRed, SquareBlue... With Bridge: shapes + renderers stay separate.
 *
 * STRUCTURE:
 *   - Abstraction: high-level control layer; holds reference to Implementor
 *   - RefinedAbstraction: extends Abstraction with more specific behavior
 *   - Implementor: interface for implementation classes
 *   - ConcreteImplementor: platform-specific implementation
 *
 * KEY INSIGHT:
 *   "Prefer composition over inheritance." Bridge replaces inheritance
 *   with composition along one axis, allowing both to evolve independently.
 *
 * DIFFERENCE FROM ADAPTER:
 *   - Adapter: makes things work AFTER they are designed (retrofitting)
 *   - Bridge: designed UP FRONT so abstraction and impl can vary independently
 *
 * REAL-WORLD EXAMPLES:
 *   - Java AWT: Window (abstraction) + Peer (OS-specific implementor)
 *   - JDBC: Connection (abstraction) + Driver (implementor)
 *   - Remote controls (abstraction) + Devices (implementors)
 *
 * PROS:
 *   + Platform-independent abstractions
 *   + Open/Closed for both abstraction and implementation
 *   + Hide implementation details from client
 *
 * CONS:
 *   - More complex when there's only one implementation
 *
 * SCENARIO:
 *   Remote controls (abstraction) that work with different devices
 *   (TV, Radio). Each can be extended independently:
 *   add AdvancedRemote without touching devices; add Stereo without touching remotes.
 */
public class BridgePattern {

    // ================================================================
    // Implementor — device implementation interface
    // ================================================================
    interface Device {
        boolean isEnabled();
        void enable();
        void disable();
        int getVolume();
        void setVolume(int percent);
        int getChannel();
        void setChannel(int channel);
        String getName();
    }

    // ================================================================
    // ConcreteImplementors — platform-specific
    // ================================================================
    static class Television implements Device {
        private boolean on     = false;
        private int     volume  = 30;
        private int     channel = 1;

        @Override public boolean isEnabled()          { return on; }
        @Override public void enable()                { on = true;  System.out.println("[TV] Powered ON"); }
        @Override public void disable()               { on = false; System.out.println("[TV] Powered OFF"); }
        @Override public int  getVolume()             { return volume; }
        @Override public void setVolume(int percent)  { volume = Math.max(0, Math.min(100, percent)); System.out.println("[TV] Volume: " + volume); }
        @Override public int  getChannel()            { return channel; }
        @Override public void setChannel(int channel) { this.channel = channel; System.out.println("[TV] Channel: " + channel); }
        @Override public String getName()             { return "Samsung TV"; }
    }

    static class Radio implements Device {
        private boolean on     = false;
        private int     volume  = 50;
        private int     channel = 88; // FM frequency × 10

        @Override public boolean isEnabled()          { return on; }
        @Override public void enable()                { on = true;  System.out.println("[Radio] Powered ON"); }
        @Override public void disable()               { on = false; System.out.println("[Radio] Powered OFF"); }
        @Override public int  getVolume()             { return volume; }
        @Override public void setVolume(int percent)  { volume = Math.max(0, Math.min(100, percent)); System.out.println("[Radio] Volume: " + volume); }
        @Override public int  getChannel()            { return channel; }
        @Override public void setChannel(int channel) { this.channel = channel; System.out.printf("[Radio] Frequency: %.1f MHz%n", channel / 10.0); }
        @Override public String getName()             { return "Sony Radio"; }
    }

    // ================================================================
    // Abstraction — remote control
    // Holds a reference to Device (the bridge/implementor side)
    // ================================================================
    static class RemoteControl {
        // THE BRIDGE: reference to implementor, set via constructor or setter
        protected Device device;

        RemoteControl(Device device) {
            this.device = device;
        }

        public void togglePower() {
            System.out.println("Remote: togglePower → " + device.getName());
            if (device.isEnabled()) {
                device.disable();
            } else {
                device.enable();
            }
        }

        public void volumeDown() {
            System.out.println("Remote: volumeDown");
            device.setVolume(device.getVolume() - 10);
        }

        public void volumeUp() {
            System.out.println("Remote: volumeUp");
            device.setVolume(device.getVolume() + 10);
        }

        public void channelDown() {
            System.out.println("Remote: channelDown");
            device.setChannel(device.getChannel() - 1);
        }

        public void channelUp() {
            System.out.println("Remote: channelUp");
            device.setChannel(device.getChannel() + 1);
        }
    }

    // ================================================================
    // RefinedAbstraction — extends control with extra features
    // Both RemoteControl and AdvancedRemote can work with ANY Device
    // ================================================================
    static class AdvancedRemoteControl extends RemoteControl {
        AdvancedRemoteControl(Device device) {
            super(device);
        }

        /** Extra feature: mute */
        public void mute() {
            System.out.println("AdvancedRemote: mute");
            device.setVolume(0);
        }

        /** Extra feature: jump to specific channel */
        public void jumpTo(int channel) {
            System.out.println("AdvancedRemote: jumpTo " + channel);
            device.setChannel(channel);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Basic Remote + TV ===");
        Device tv = new Television();
        RemoteControl tvRemote = new RemoteControl(tv);
        tvRemote.togglePower();   // turn on
        tvRemote.volumeUp();
        tvRemote.volumeUp();
        tvRemote.channelUp();

        System.out.println("\n=== Advanced Remote + TV ===");
        AdvancedRemoteControl advTvRemote = new AdvancedRemoteControl(tv);
        advTvRemote.mute();
        advTvRemote.jumpTo(42);
        advTvRemote.togglePower(); // turn off

        System.out.println("\n=== Advanced Remote + Radio ===");
        // Same AdvancedRemote class, different device — no new remote class needed!
        Device radio = new Radio();
        AdvancedRemoteControl radioRemote = new AdvancedRemoteControl(radio);
        radioRemote.togglePower();
        radioRemote.volumeUp();
        radioRemote.jumpTo(1013); // 101.3 MHz
        radioRemote.mute();
    }
}
