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
public class DeathPoint extends SpecialPoint {
    
    public DeathPoint(int pX, int pY, int courtWidth, int courtHeight, Color color) {
        super(pX, pY, courtWidth, courtHeight, color);
    }
    
    @Override
    public int pointChanger(int points) {
    	return 0;
    }
}
