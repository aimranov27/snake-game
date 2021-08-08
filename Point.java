/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and that all objects
 * created by this constructor share the same image data (i.e. img is static). This is important for
 * efficiency: your program will go very slowly if you try to create a new BufferedImage every time
 * the draw method is invoked.
 */
public class Point extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private Color color;
    
    public Point(int pX, int pY, int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, pX, pY, SIZE, SIZE, courtWidth, courtHeight);
        
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
    	g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
