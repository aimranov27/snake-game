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
public abstract class SpecialPoint extends Point {
    
    public SpecialPoint(int pX, int pY, int courtWidth, int courtHeight, Color color) {
        super(pX, pY, courtWidth, courtHeight, color);
    }
    
    public abstract int pointChanger(int input);
}
