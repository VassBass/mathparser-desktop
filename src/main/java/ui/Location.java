package ui;

import java.awt.*;

public class Location {
    public static Point CENTER (Component parent, Component child) {
        int x0 = parent.getLocation().x;
        int pw = parent.getWidth();
        int cw = child.getWidth();

        int y0 = parent.getLocation().y;
        int ph = parent.getHeight();
        int ch = child.getHeight();

        int x = x0 + (pw/2) - (cw/2);
        int y = y0 + (ph/2) - (ch/2);

        return new Point(x,y);
    }
}
