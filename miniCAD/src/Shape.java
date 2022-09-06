import java.awt.*;
import java.io.Serializable;

abstract class Shape implements Serializable {
    int width;
    int height;
    Point anchor;
    Color color = Color.BLACK;
    float thickness = 3.0f;

    abstract boolean InRegion(Point p);

    Shape() {
        this.width = 0;
        this.height = 0;
        this.anchor = new Point(0, 0);
    }

    Shape(Point anchor, int width, int height, Color c) {
        this.width = width;
        this.height = height;
        this.anchor = anchor;
        if (c != null)
            color = c;
    }

    void move(int dx, int dy) {
        anchor.x += dx;
        anchor.y += dy;
    }

    void set_color(Color c) {
        color = c;
    }

    void set(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness));
        g.setColor(color);
    }

    abstract void draw(Graphics2D g);

    void zoom_in() {
        this.width *= 1.1;
        this.height *= 1.1;
    }

    void zoom_out() {
        this.width /= 1.1;
        this.height /= 1.1;
    }

    void thicker() {
        this.thickness *= 1.1;
    }

    void thinner() {
        this.thickness /= 1.1;
    }
}

class Line extends Shape {
    Point start;
    Point end;

    // ( (x2-x1)(y0-y1) - (y2-y1)(x0-x1) ) / sqrt((x2-x1)^2 + (y2-y1)^2))
    Line() {
        super();
    }

    Line(Point anchor, int width, int height, Point start, Point end, Color c) {
        super(anchor, width, height, c);
        this.start = start;
        this.end = end;
    }

    @Override
    void move(int dx, int dy) {
        super.move(dx, dy);
        start.x += dx;
        start.y += dy;
        end.x += dx;
        end.y += dy;
    }

    @Override
    boolean InRegion(Point p) {
        double d = Math.abs(1.0 * ((end.x - start.x) * (p.y - start.y) - (end.y - start.y) * (p.x - start.x))
                / Math.sqrt(width * width + height * height));
        return d < 20 &
                p.x >= anchor.x &
                p.x <= anchor.x + width &
                p.y >= anchor.y &
                p.y <= anchor.y + height;
    }

    @Override
    void draw(Graphics2D g) {
        set(g);
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    @Override
    void zoom_in() {
        long dx = Math.round(0.1 * (this.end.x - this.start.x));
        long dy = Math.round(0.1 * (this.end.y - this.start.y));
        this.end.x += dx;
        this.end.y += dy;
        this.width += dx;
        this.height += dy;
    }

    @Override
    void zoom_out() {
        long dx = Math.round(0.1 * (this.end.x - this.start.x));
        long dy = Math.round(0.1 * (this.end.y - this.start.y));
        this.end.x -= dx;
        this.end.y -= dy;
        this.width -= dx;
        this.height -= dy;
    }
}

class Rectangle extends Shape {
    Rectangle() {
        super();
    }

    Rectangle(Point anchor, int width, int height, Color c) {
        super(anchor, width, height, c);
    }

    @Override
    boolean InRegion(Point p) {
        return p.x >= anchor.x &
                p.x <= anchor.x + width &
                p.y >= anchor.y &
                p.y <= anchor.y + height;
    }

    @Override
    void draw(Graphics2D g) {
        set(g);
        g.drawRect(anchor.x, anchor.y, width, height);
    }
}

class Oval extends Shape {
    Oval() {
        super();
    }

    Oval(Point anchor, int width, int height, Color c) {
        super(anchor, width, height, c);
    }

    @Override
    boolean InRegion(Point p) {
        double dx = p.x - (anchor.x + width / 2);
        double dy = p.y - (anchor.y + height / 2);
        return dx * dx / (width * width / 4) + dy * dy / (height * height / 4) <= 1.0;
    }

    @Override
    void draw(Graphics2D g) {
        set(g);
        g.drawOval(anchor.x, anchor.y, width, height);
        ;
    }
}

class InputBox extends Rectangle {

    String s;
    int size = 20;
    Font font = new Font("ºÚÌå", Font.PLAIN, size);

    InputBox(Point pos, Color c, String input, Graphics2D g) {
        if (c != null)
            color = c;
        anchor.x = pos.x;
        anchor.y = pos.y;
        s = input;
    }

    @Override
    void draw(Graphics2D g) {
        set(g);
        Font font = new Font("ºÚÌå", Font.PLAIN, size);
        FontMetrics fm = g.getFontMetrics(font);
        width = fm.stringWidth(s);
        height = fm.getHeight();
        g.setFont(font);
        g.drawString(s, anchor.x, anchor.y + fm.getHeight());
    }

    @Override
    void zoom_in() {
        this.size += 2;
    }

    @Override
    void zoom_out() {
        this.size -= 2;
    }
}
