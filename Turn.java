/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Turn extends GameObj{
	
	public final static int SIZE = 0;

    public Turn(int pX, int pY, int vX, int vY, int courtWidth, int courtHeight) {
    	super(vX, vY, pX, pY, SIZE, SIZE, courtWidth, courtHeight);
    }

	@Override
	public void draw(Graphics g) {
		// There is no draw method for this object	
	}

	
    
}