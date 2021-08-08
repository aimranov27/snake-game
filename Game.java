/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	// Create a file that will store information when the game is saved  
	private final File FILE = 
			new File("C:\\Users\\Owner\\eclipse-workspace\\hw9\\files\\SavedGame.txt");
	
    public void run() {
    	
    	// Top-level frame in which game components live
        JFrame frame = new JFrame("SNAKE");
        frame.setLocation(700, 200);
    	
        // Frame that holds the instructions for the game
    	JFrame instructions = new JFrame("Instructions");
    	instructions.setLocation(700, 200);
    	
    	// The text of the instructions
    	final JTextArea text = new JTextArea("Welcome to snake! This is a remake of the old "
    			+ "game of snake. The goal is to help the snake eat its points, which are "
    			+ "represented by red squares in the screen. The player uses the arrow keys to "
    			+ "control the snake but they must be careful not to hit its body/tail or the "
    			+ "walls. The additional feature of this snake game is that there is another "
    			+ "special point, represented by a blue square, which can be any of 3 possible "
    			+ "points: one that triples the score, one that halves it, and one that makes the "
    			+ "user lose. Additionally, there is the additional feature of saving a game and "
    			+ "reloading at any point, so if the player loses they can reload a previously "
    			+ "saved game and continue from there. One important note for the player is that "
    			+ "you should avoid pressing two arrow keys simultaneously when you turn as that "
    			+ "may result in the snake \"breaking\". You can still make quick turns with the "
    			+ "snake, but you should just not be too quick. In the even that that happens you "
    			+ "can reset the game or load a previous game. Have fun! \n"
    			+ "Click OK to start the game!");
    	
    	text.setFont(new Font("Times New Roman", Font.BOLD, 20));
    	text.setLineWrap(true);
    	text.setWrapStyleWord(true);
    	text.setEditable(false);
    	
    	JScrollPane textInstructions = new JScrollPane(text);
    	textInstructions.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	textInstructions.setPreferredSize(new Dimension(500, 500));
    	
    	// Add a button to start the game
    	final JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instructions.setVisible(false);
                frame.setVisible(true);
            }
        });
    	
    	instructions.add(textInstructions, BorderLayout.NORTH);
    	instructions.add(okButton, BorderLayout.SOUTH);
    	
    	
    	// Put the instructions on the screen
        instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructions.setVisible(true);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Control panel
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new GridLayout(1,2));
        frame.add(control_panel, BorderLayout.NORTH);
        final JLabel score = new JLabel("Score: 0");
        control_panel.add(score);

        // Main playing area
        final GameCourt court = new GameCourt(status, score);
        frame.add(court, BorderLayout.CENTER);
        
        // A reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        // A pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.pause();
            }
        });
        control_panel.add(pause);
        
     // A save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		FILE.createNewFile();
					court.save(new PrintWriter(FILE));
				} catch (FileNotFoundException e1) {
					System.out.println("File Not Found");
				} catch (IOException e1) {
					System.out.println("IO Exception occurred");
				} 
            }
        });
        control_panel.add(save);
        
     // A load button
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		BufferedReader in = new BufferedReader(new FileReader(FILE));
					if (FILE.exists()) {
						court.load(in);
					}
				} catch (FileNotFoundException e1) {
					System.out.println("File Not Found");
				} catch (IOException e1) {
					System.out.println("IO Exception occurred");
				} 
            }
        });
        control_panel.add(load);

        // Put the game frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);

        // Start game
        court.reset();
        
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}