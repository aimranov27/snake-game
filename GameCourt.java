/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	// an array list that will contain the special points of the game
	private ArrayList<SpecialPoint> specialPoints = new ArrayList<SpecialPoint>();
    
	//the special points
	private SpecialPoint triplePoints;
	private SpecialPoint decreasePoints;
	private SpecialPoint deathPoint;
	
    // a current special point holder
    SpecialPoint currentSpecial;
    
	// a variable that keeps track of the score
	private int currentScore = 0;
	
	// a list of the turns of the snake
	private LinkedList<Turn> turns = new LinkedList<Turn>();
    
    // the snake head
    private BodyPart head;
    
    // the snake tail
    private BodyPart tail;
    
    // the snake
    private LinkedList<BodyPart> snake = new LinkedList<BodyPart>();
    
    // the regular point that the snake eats
    private Point point;
    
    // a variable for the last pressed key
    private int lastPressed;
    
    // whether or not the player can pause the game
    private boolean shouldPause = true;
    
    // whether the game is running
    public boolean playing = false;
    
    // current status text, i.e. "Running..."
    private JLabel status;
    
    // current score
    private JLabel score; 
    
    // Game constants
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;
    
    // snake speed
    public static final int SNAKE_VELOCITY = 10;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 60;

    public GameCourt(JLabel status, JLabel score) {
    	
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK, SNAKE_VELOCITY));
        
        // creates a timer
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the court area.
        setFocusable(true);
        
        // generate a special point
        generatePoint(false);
        
        // create a head for the snake with a random location
        head = new BodyPart((int) (Math.random() * 370) + 10, (int) (Math.random() * 370) + 10, 
        		SNAKE_VELOCITY, 0, COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        
        // initially assign the tail to be the same as the head
        tail = head;
        
        // generate a regular point
        generatePoint(true);
        
        //add the head to the snake
        snake.addFirst(head);
        
        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                	if (lastPressed != KeyEvent.VK_RIGHT && lastPressed != KeyEvent.VK_LEFT) {
                		head.setVx(-SNAKE_VELOCITY);
                		head.setVy(0);
                		turns.addLast(new Turn(head.getPx(), head.getPy(), head.getVx(), 
                				head.getVy(), COURT_WIDTH, COURT_HEIGHT));
                	} 
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                	if (lastPressed != KeyEvent.VK_RIGHT && lastPressed != KeyEvent.VK_LEFT) {
                		head.setVx(SNAKE_VELOCITY);
                		head.setVy(0);
                		turns.addLast(new Turn(head.getPx(), head.getPy(), head.getVx(), 
                				head.getVy(), COURT_WIDTH, COURT_HEIGHT));
                	}
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                	if (lastPressed != KeyEvent.VK_UP && lastPressed != KeyEvent.VK_DOWN) {
                		head.setVy(SNAKE_VELOCITY);
                		head.setVx(0);
                		turns.addLast(new Turn(head.getPx(), head.getPy(), head.getVx(), 
                				head.getVy(), COURT_WIDTH, COURT_HEIGHT));
                	}
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                	if (lastPressed != KeyEvent.VK_UP && lastPressed != KeyEvent.VK_DOWN) {
                		head.setVy(-SNAKE_VELOCITY);
                		head.setVx(0);
                		turns.addLast(new Turn(head.getPx(), head.getPy(), head.getVx(), 
                				head.getVy(), COURT_WIDTH, COURT_HEIGHT));
                	}
                }
                lastPressed = e.getKeyCode();
                }

            }
        );

        this.status = status;
        this.score = score;
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	snake = new LinkedList<BodyPart>();
    	head = new BodyPart((int) (Math.random() * 370) + 10, (int) (Math.random() * 370) + 10, 
        		0, 0, COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
    	tail = head;
    	snake.addFirst(head);
    	generatePoint(true);
        turns = new LinkedList<Turn>();
        lastPressed = -1;
        currentScore = 0;
        shouldPause = true;
        
        // generate a special point
        generatePoint(false);

        playing = true;
        status.setText("Running...");
        score.setText("Score: 0");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * Pause the game.
     */
    public void pause() {
    	// the shouldPause variable is used to avoid pausing and unpausing the game after the 
    	// player has lost 
    	if (shouldPause) {
    		playing = !playing;
    		status.setText("Paused");
    	}
        
    	// update the status depending on whether the game is paused or not
        if (playing) {
        	status.setText("Running...");
        	requestFocusInWindow();
        }
    }
    
    /**
     * Save the game and/or the score.
     * @param the writer that will store the state in the file
     * @throws IOException 
     */
    public void save(PrintWriter out) throws IOException {
    	// save the snake length
    	out.println(snake.size());
    	
    	// save the location and speed of each part of the snake
    	for (BodyPart i : snake) {
    		out.println(i.getPx());
    		out.println(i.getPy());
    		out.println(i.getVx());
    		out.println(i.getVy());
    	}
    	
        out.println(point.getPx());
        out.println(point.getPy());
        out.println(currentSpecial.getPx());
        out.println(currentSpecial.getPy());
        
        // save the number of turns that need to be completed
        out.println(turns.size());
        
        // save the location and speed of each turn
        for (Turn i : turns) {
        	out.println(i.getPx());
    		out.println(i.getPy());
    		out.println(i.getVx());
    		out.println(i.getVy());
        }
        
        out.println(lastPressed);
        out.println(currentScore);
        
        if (playing) {
        	out.println(1);
        } else {
        	out.println(0);
        }
        if (shouldPause) {
        	out.println(1);
        } else {
        	out.println(0);
        }
        // close the file
        out.close();
        
        requestFocusInWindow();
    }
    
    /**
     * Load the game and/or the score.
     * @throws IOException 
     */
    public void load(BufferedReader in) throws IOException {
    	// re-read all the saved information 
    	int snakeSize = Integer.parseInt(in.readLine());
    	snake = new LinkedList<BodyPart>();
    	for (int i = 0; i < snakeSize; i++) {
    		BodyPart newBodyPart = new BodyPart(Integer.parseInt(in.readLine()), 
    				Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), 
    				Integer.parseInt(in.readLine()), COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
    		snake.add(newBodyPart);
    	}
    	head = snake.getFirst();
    	tail = snake.getLast();
    	point.setPx(Integer.parseInt(in.readLine()));
    	point.setPy(Integer.parseInt(in.readLine()));
    	currentSpecial.setPx(Integer.parseInt(in.readLine()));
    	currentSpecial.setPy(Integer.parseInt(in.readLine()));
    	
    	int numTurns = Integer.parseInt(in.readLine());
    	turns = new LinkedList<Turn>();
    	for (int i = 0; i < numTurns; i++) {
    		Turn newTurn = new Turn(Integer.parseInt(in.readLine()), 
    				Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), 
    				Integer.parseInt(in.readLine()), COURT_WIDTH, COURT_HEIGHT);
    		turns.add(newTurn);
        }
        
        lastPressed = Integer.parseInt(in.readLine());
        currentScore = Integer.parseInt(in.readLine());
        
        // update the score board
        score.setText("Score: " + currentScore);
        
        if (Integer.parseInt(in.readLine()) == 1) {
        	playing = true;
        } else {
        	playing = false;
        }
        if (Integer.parseInt(in.readLine()) == 1) {
        	shouldPause = true;
        } else {
        	shouldPause = false;
        }
        // close the file
        in.close();
        
        // update the GUI
        repaint();
        
        requestFocusInWindow();
    }
    
    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    
    void tick() {
        if (playing) {
            // advance the snake in its current direction.
        	for (BodyPart i : snake) {
        		i.move();
        		boolean shouldRemove = false;
        		
        		// turn the body parts
        		for (Turn j : turns) {
        			shouldRemove = (snake.size() == 1);
        			if (j.getPx() == i.getPx() && j.getPy() == i.getPy()) {
        				i.setVx(j.getVx());
        				i.setVy(j.getVy());
        				shouldRemove = (i == tail);
        				break;
        			}
        		}
        		
        		// a turn is removed from the turns list either as long as the tail is of length 1
        		// or when its tail reaches the turn location
        		if (shouldRemove) {
        			turns.removeFirst();
        		}
        	}
            
            // check if the snake eats a point
            if (head.intersects(point)) {
            	// update the score
            	currentScore++;
            	score.setText("Score: " + currentScore);
            	
            	generatePoint(true);
            	
            	// save the current tail and add a new body part depending on its velocity
            	BodyPart previousLast = snake.getLast();
            	if (tail.getVx() == 0) {
            		if (tail.getVy() < 0) {
            		tail = new BodyPart(previousLast.getPx(), previousLast.getPy() + 
            				previousLast.getHeight(), previousLast.getVx(), previousLast.getVy(), 
            				COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
            		} else {
            			tail = new BodyPart(previousLast.getPx(), previousLast.getPy() - 
                				previousLast.getHeight(), previousLast.getVx(), previousLast.getVy(), 
                				COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
            		}
            	} else {
            		if (tail.getVx() < 0) {
            			tail = new BodyPart(previousLast.getPx() + previousLast.getWidth(), 
                    		previousLast.getPy(), previousLast.getVx(), previousLast.getVy(), 
                    		COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
            		} else {
            			tail = new BodyPart(previousLast.getPx() - previousLast.getWidth(), 
                        		previousLast.getPy(), previousLast.getVx(), previousLast.getVy(), 
                        		COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
            		}
            	}
                snake.addLast(tail);
            
              // check if the snake eats a special point    
            } else if (head.intersects(currentSpecial)) {
            	// update the score depending on the pointChanger 
            	// method of the special point
            	
            	// the 10 is just a test value for the pointChanger
            	if (currentSpecial.pointChanger(10) == 0) {
            		playing = false;
            		status.setText("You lose!");
            		shouldPause = false;
            	}
            	currentScore = currentSpecial.pointChanger(currentScore);
            	
            	score.setText("Score: " + currentScore);
            	
            	generatePoint(false);
            	
            // check for the losing conditions
            } else {
            	if (head.hitWall() != null) {
            		playing = false;
                    status.setText("You lose!");
                    shouldPause = false;
            	}
            	
            	for (int i = 1; i < snake.size(); i++) {
            		if (head.intersects(snake.get(i))) {
                        playing = false;
                        status.setText("You lose!");
                        shouldPause = false;
                        break;
            		}
            	}
            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (BodyPart i : snake) {
        	i.draw(g);
        }
        point.draw(g);
        currentSpecial.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    /**
     * This is a helper method that finds a point location which is not intersecting with the snake
     * @param temp, some temporary point
     * @return the newly generated point
     */
    private Point findPointThatDoesNotHitSnake(Point temp) {
    	// generate some random coordinates
    	int pX = (int) (Math.random() * 370) + 10;
		int pY = (int) (Math.random() * 370) + 10;
		
		// update the temporary point with those coordinates
		temp = new Point(pX, pY, COURT_WIDTH, COURT_HEIGHT, Color.RED);
		
		// update the coordinates if the temporary point intersects any of the body parts
        for (BodyPart i : snake) {
        	if (i.intersects(temp)) {
        		// recursively call the method
        		temp = findPointThatDoesNotHitSnake(temp);
        	}
        }
        return temp;
    }
    
    /**
     * This is a helper method that generates a regular or special point
     * @param regular, where true means that it will generate a regular 
     * 		  		   point and false is a special point
     */
    private void generatePoint(boolean regular) {
    	Point a = findPointThatDoesNotHitSnake(new Point((int) (Math.random() * 370) + 10, 
    			(int) (Math.random() * 370) + 10, COURT_WIDTH, COURT_HEIGHT, Color.RED));
    	int pX = a.getPx();
    	int pY = a.getPy();
    	if (regular) {
    		point = new Point(pX, pY, COURT_WIDTH, COURT_HEIGHT, Color.RED);
    	} else {
    		specialPoints = new ArrayList<SpecialPoint>();
        	triplePoints = new TriplePoints(pX, pY, COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
            decreasePoints = new DecreasePoints(pX, pY, COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
            deathPoint = new DeathPoint(pX, pY, COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
            specialPoints.add(triplePoints);
            specialPoints.add(decreasePoints);
            specialPoints.add(deathPoint);
        	currentSpecial = specialPoints.get((int) (Math.random() * (specialPoints.size())));
    	}
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the size of the list holding the special points
     */
    public int getSpecialPointsListSize() {
    	return specialPoints.size();
    }
    
    /**
     * This method is used mainly for testing purposes
     */
    public void setCurrentSpecialPoint(SpecialPoint newSpecial) {
    	currentSpecial = newSpecial;
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the length of the snake
     */
    public int getSnakeLength() {
    	return snake.size();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the length of the turns list
     */
    public int getTurnsLength() {
    	return turns.size();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return a copy of the turns linked list
     */
    public LinkedList<Turn> getTurns() {
    	//create a copy of the turns to preserve encapsulation
    	LinkedList<Turn> turnsCopy = new LinkedList<Turn>();
    	for (Turn i : turns) {
    		Turn turnCopy = new Turn(i.getPx(), i.getPy(), i.getVx(), i.getVy(), 
    				COURT_WIDTH, COURT_HEIGHT);
    		turnsCopy.addLast(turnCopy);
    	}
    	return turnsCopy;
    }
    
    /**
     * This method is used mainly for testing purposes
     */
    public void setTurns(LinkedList<Turn> newTurns) {
    	turns = newTurns;
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the x coordinate of the head of the snake
     */
    public int getHeadPx() {
    	return head.getPx();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the y coordinate of the head of the snake
     */
    public int getHeadPy() {
    	return head.getPy();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new x coordinate of the location of the head
     */
    public void setHeadPx(int newPx) {
    	head.setPx(newPx);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new y coordinate of the location of the head
     */
    public void setHeadPy(int newPy) {
    	head.setPy(newPy);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new x coordinate of the velocity of the head
     */
    public void setHeadVx(int newVx) {
    	head.setVx(newVx);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new y coordinate of the velocity of the head
     */
    public void setHeadVy(int newVy) {
    	head.setVy(newVy);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return a copy of the point of the game
     */
    public Point getPoint() {
    	return (new Point(point.getPx(), point.getPy(), COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new x coordinate of the location of the point
     */
    public void setPointPx(int newPx) {
    	point.setPx(newPx);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new y coordinate of the location of the point
     */
    public void setPointPy(int newPy) {
    	point.setPy(newPy);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new x coordinate of the location of the special point
     */
    public void setSpecialPointPx(int newPx) {
    	currentSpecial.setPx(newPx);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new y coordinate of the location of the special point
     */
    public void setSpecialPointPy(int newPy) {
    	currentSpecial.setPy(newPy);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the current score
     */
    public int getScore() {
    	return currentScore;
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the playing boolean
     */
    public boolean getPlaying() {
    	return playing;
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the x coordinate of the tail of the snake
     */
    public int getTailPx() {
    	return tail.getPx();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the y coordinate of the tail of the snake
     */
    public int getTailPy() {
    	return tail.getPy();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new x coordinate of the tail of the snake
     */
    public void setTailPx(int newPx) {
    	tail.setPx(newPx);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the new y coordinate of the tail of the snake
     */
    public void setTailPy(int newPy) {
    	tail.setPy(newPy);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the x coordinate of the velocity of the snake
     */
    public int getTailVx() {
    	return tail.getVx();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the y coordinate of the velocity of the snake
     */
    public int getTailVy() {
    	return tail.getVy();
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the boolean describing whether the head intersects point of the game
     */
    public boolean headIntersectsPoint() {
    	return head.intersects(point);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the point to check if the head intersects it
     * @return the boolean describing whether the head intersects the given point
     */
    public boolean headIntersectsPoint(Point p) {
    	return head.intersects(p);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the boolean describing whether the head intersects the tail of the snake
     */
    public boolean headIntersectsTail() {
    	return head.intersects(tail);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @param the special point to check if the head intersects it
     * @return the boolean describing whether the head intersects given special point
     */
    public boolean headIntersectsSpecialPoint(SpecialPoint p) {
    	return head.intersects(p);
    }
    
    /**
     * This method is used mainly for testing purposes
     * @return the direction in which the snake hits the wall
     */
    public Direction headHitsWall() {
    	return head.hitWall();
    }
}