
import java.awt.event.*;
import java.awt.*;
import java.lang.Math.*;
public class Listener implements MouseMotionListener{
    private Graphics2D g2d;
	private Integer prev_x = null, prev_y = null;
    Listener(Graphics g){
        g2d =  (Graphics2D) g;
    }
    public void mouseDragged(MouseEvent e) {
		if(prev_x == null || prev_y == null){
			prev_x = e.getX();
			prev_y = e.getY();
		}else{
			int cur_x = e.getX();
			int cur_y = e.getY();
			double dx = cur_x - prev_x;
			double dy = cur_y - prev_y;
			Double width = 12 - 1.1*Math.log((dx*dx + dy*dy));
			width = width < 0 ? 1 : width;
			g2d.setStroke(new BasicStroke(width.floatValue()));
			switch (e.getModifiers()) {
				case InputEvent.BUTTON1_MASK:
					g2d.setColor(Color.black);
					break;
				case InputEvent.BUTTON3_MASK:
					g2d.setColor(Color.white);
					break;
				default:
					return;
			}
			g2d.drawLine(prev_x, prev_y, cur_x, cur_y);
			prev_x = cur_x;
			prev_y = cur_y;
		}
	}

	public void mouseMoved(MouseEvent e) {
		prev_x = e.getX();
		prev_y = e.getY();
	}
}
