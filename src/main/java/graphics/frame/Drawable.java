/**
 * Created by Philipp Polland on 21-May-20.
 */
package graphics.frame;

import java.awt.*;


public interface Drawable {
    void paintElement(Graphics g, float xOffset, float yOffset, float multiplyer);

    String getIdentifyer();
}
