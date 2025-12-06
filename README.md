# Stardew Valley Volume Picker

As a "Bad-UI" implementation, this video player contains a volume control interface that transforms the mundane task of adjusting audio levels into a mini-game inspired by Stardew Valley's fishing mechanic.

## About Stardew Valley Fishing Game

Stardew Valley features a unique fishing mechanic where players must rapidly click to keep a green bar aligned with a moving fish icon within a vertical meter. The fish moves unpredictably, and the bar naturally falls due to "gravity" when not being clicked. Success requires maintaining alignment for several seconds until a progress bar fills completely.

This volume picker faithfully recreates that experience: users input their desired volume, which appears as a target marker on a vertical bar. A movable bar (controlled by clicking) must be kept hovering over the marker. However, just like the fish in Stardew Valley, the bar has gravity pulling it downward when not actively clicked, and the volume marker itself moves slightly, and even jumping to a completely different position when you're close to success. Only by maintaining alignment for the full duration can users actually set their volume.

## Project Structure

```

```

## Running the Program

```bash
# Navigate to project directory
cd PDP-UI-SDV-Volume/src

# Compile all files
javac *.java

# Run the program
java ui.player
```

## Reflection

### Expected User Behavior

Volume controls are among the most ubiquitous and standardized UI elements in digital interfaces. Users expect immediate, direct manipulation: click a slider, drag to the desired position, and release. The feedback is instant, the control is precise, and the interaction takes less than a second. This learned behavior is reinforced across operating systems, media players, streaming services, and virtually every application with audio. The expectation is that setting volume is a trivial, background task requiring minimal attention or effort.

### Design Subversion

This implementation weaponizes that expectation by introducing time, unpredictability, and physical engagement where users anticipate none. The familiar volume slider becomes an adversarial mini-game that actively resists completion. The "gravity" mechanic forces continuous clicking rather than a single action, transforming a passive gesture into active work. The marker's movement introduces uncertainty where users expect stability, and its dramatic jump near completion is a deliberate bait-and-switch that exploits the user's growing confidence. By requiring 5 full seconds of sustained alignment (plus the additional 3-second challenge after the jump), the design stretches what should be a fraction-of-a-second interaction into a protracted struggle. The cruelest aspect is that the interface looks conventional—users don't realize they're entering a game until they're already playing, and by then, they're committed to winning rather than simply adjusting their volume.

## Credits

- Inspired by the fishing mechanic from Stardew Valley by ConcernedApe
- Created as a final lab assignment exploring subversive UI design
- Concept based on r/badUIbattles community designs
