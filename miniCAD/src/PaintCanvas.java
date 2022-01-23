import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;

public class PaintCanvas extends Canvas{
    
    private Image iBuffer;
    private Graphics2D gBuffer;
    private ArrayList<Shape> list = new ArrayList<Shape>();
    Shape selected;

    
    private void clear_buf(){
        if(iBuffer == null){
            iBuffer = createImage(this.getSize().width, this.getSize().height);
            gBuffer = (Graphics2D) iBuffer.getGraphics();
        }
        gBuffer.setColor(getBackground());
        gBuffer.fillRect(0, 0 , this.getSize().width, this.getSize().height);
    }

    private void draw_shapes(){
        for (Shape entity : list) {
            entity.draw(gBuffer);
        }
        if(selected != null){
            final BasicStroke s = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 5, 5 }, 0.0f);
            gBuffer.setStroke(s);            
            gBuffer.setColor(Color.RED);
            gBuffer.drawRect(selected.anchor.x, selected.anchor.y, selected.width, selected.height);
        }
    }

    @Override
    public void update(Graphics g) {
        g.drawImage(iBuffer, 0, 0, this);
    }

    public void check(Point p){
        clear_buf();
        selected = null;

        for (Shape entity : list) {
            if(entity.InRegion(p)){
                selected = entity;
                break;
            }
        }
        draw_shapes();
        repaint();
    }

    public void flush() {
        list.clear();
        selected = null;
        clear_buf();
        draw_shapes();
        repaint();    
    }

    public void drawing(Point start, Point end, ShapeType type, Color c, String input){
        clear_buf();
        gBuffer.setStroke(new BasicStroke(2.0f));
        if(c==null)
            gBuffer.setColor(Color.BLACK);
        else
            gBuffer.setColor(c);
        Point anchor = new Point(Math.min(start.x, end.x), Math.min(start.y , end.y));
        switch (type) {
            case LINE:
                gBuffer.drawLine(start.x, start.y, end.x, end.y);
                break;
            case RECT:
                gBuffer.drawRect(anchor.x, anchor.y, Math.abs(start.x - end.x), Math.abs(start.y - end.y));
                break;
            case OVAL:
                gBuffer.drawOval(anchor.x, anchor.y, Math.abs(start.x - end.x), Math.abs(start.y - end.y));
                break;
            case INPUTBOX:
                Font font = new Font("ºÚÌå", Font.PLAIN, 20);
                FontMetrics fm = gBuffer.getFontMetrics(font);
                gBuffer.setFont(font);
                gBuffer.drawString(input, end.x, end.y + fm.getHeight());
            default:
                break;
        }
        draw_shapes();
        repaint();
    }


    public void add(Point start, Point end, ShapeType type, Color c, String input) {
        Point anchor = new Point(Math.min(start.x, end.x), Math.min(start.y , end.y));
        switch (type) {
            case LINE:
                list.add(new Line(anchor, Math.abs(start.x - end.x), Math.abs(start.y - end.y), new Point(start), new Point(end), c));
                break;
            case RECT:
                list.add(new Rectangle(anchor, Math.abs(start.x - end.x), Math.abs(start.y - end.y), c));
                break;
            case OVAL:
                list.add(new Oval(anchor, Math.abs(start.x - end.x), Math.abs(start.y - end.y), c));
                break;
            case INPUTBOX:
                list.add(new InputBox(end, c, input, gBuffer));
                break;
            default:
                break;
        }
    }

    public void move(int dx, int dy) {
        clear_buf();
        if(selected != null){
            selected.move(dx, dy);
        }
        draw_shapes();
        repaint();
    }

    public void move(Point start, Point end, boolean commit) {
        if(selected != null){
            clear_buf();
            selected.move(end.x - start.x, end.y - start.y);
            draw_shapes();
            if(!commit)
                selected.move(start.x - end.x, start.y - end.y);
            repaint();
        }
    }

    public void set_color(Color c) {
        if(selected != null){
            clear_buf();
            selected.set_color(c);
            draw_shapes();
            repaint();
        }
    }

    public void thicker() {
        if(selected != null){
            clear_buf();
            selected.thicker();
            draw_shapes();
            repaint();
        }
    }
    public void thinner() {
        if(selected != null){
            clear_buf();
            selected.thinner();
            draw_shapes();
            repaint();
        }
    }
    public void zoom_in() {
        if(selected != null){
            clear_buf();
            selected.zoom_in();
            draw_shapes();
            repaint();    
        }
    }

    public void zoom_out() {
        if(selected != null){
            clear_buf();
            selected.zoom_out();
            draw_shapes();
            repaint();    
        }
    }

    public void remove() {
        clear_buf();
        if(selected != null){
            list.remove(selected);
            selected = null;
        }
        draw_shapes();
        repaint();
    }

    public void open(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        flush();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
        list = (ArrayList<Shape>) in.readObject();
        in.close();
        clear_buf();
        draw_shapes();
        repaint();
    }

    public void save(File file) throws FileNotFoundException, IOException{
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
        out.writeObject(list);
        out.flush();
        out.close();
    }
}

