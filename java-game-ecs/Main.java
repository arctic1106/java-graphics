import javax.swing.*;
import game.GamePanel;

/**
 * Main - Entry point for the RTS Game.
 * 
 * ARCHITECTURE OVERVIEW:
 * 
 * This game demonstrates several key game architecture patterns:
 * 
 * 1. ENTITY-COMPONENT SYSTEM (ECS)
 *    - Entities: Unique IDs with attached components
 *    - Components: Pure data (Position, Health, Velocity)
 *    - Systems: Logic that processes entities with specific components
 *    - Why: Flexible, data-oriented, easy to extend
 * 
 * 2. FIXED TIMESTEP GAME LOOP
 *    - Logic updates at fixed 60Hz
 *    - Rendering happens as fast as possible
 *    - Why: Consistent behavior across all hardware
 * 
 * 3. SYSTEM-BASED ARCHITECTURE
 *    - Each system handles one concern
 *    - Systems are independent and composable
 *    - Why: Clean separation, easy to test, easy to modify
 * 
 * 4. COMMAND PATTERN
 *    - Player input creates command components
 *    - Systems execute those commands over time
 *    - Why: Decouples input from behavior, supports replays
 * 
 * 5. MVC SEPARATION
 *    - Model: World, Entities, Components
 *    - View: GamePanel rendering
 *    - Controller: InputHandler
 *    - Why: Clean architecture, testable
 * 
 * HOW TO PLAY:
 * - Left-click: Select a single unit
 * - Left-drag: Box-select multiple units
 * - Right-click ground: Move selected units
 * - Right-click enemy: Attack with selected units
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the game panel
            GamePanel gamePanel = new GamePanel();
            
            // Create the window
            JFrame frame = new JFrame("RTS Game Architecture Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null); // center on screen
            
            // Show the window
            frame.setVisible(true);
            
            // Print architecture info
            printArchitectureInfo();
        });
    }
    
    private static void printArchitectureInfo() {
        System.out.println("=== RTS Game Architecture Demo ===");
        System.out.println();
        System.out.println("Architecture Patterns Used:");
        System.out.println("  1. Entity-Component System (ECS)");
        System.out.println("  2. Fixed Timestep Game Loop");
        System.out.println("  3. System-based Architecture");
        System.out.println("  4. Command Pattern");
        System.out.println("  5. MVC Separation");
        System.out.println();
        System.out.println("Controls:");
        System.out.println("  Left-click:   Select unit");
        System.out.println("  Left-drag:    Box select");
        System.out.println("  Right-click:  Move/Attack");
        System.out.println();
        System.out.println("Blue units (Team 0) vs Red units (Team 1)");
        System.out.println("Try selecting your blue units and attacking the red ones!");
    }
}
