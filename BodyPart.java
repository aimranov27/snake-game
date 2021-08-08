/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class BodyPart extends GameObj {//implements Comparable {
    public static final int SIZE = 10;
    
    private Color color;
    
    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public BodyPart(int pX, int pY, int vX, int vY, int courtWidth, int courtHeight, Color color) {    	
        super(vX, vY, pX, pY, SIZE, SIZE, courtWidth, courtHeight);
        this.color = color;
    }
    
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }


}
