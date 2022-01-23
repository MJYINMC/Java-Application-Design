import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.*;

public class Listener implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    String cmd;
    Color c;
    String input;
    ShapeType type;
    PaintCanvas canvas;
    Point start;
    Point end;
    JFileChooser jfc;

    Listener(PaintCanvas canvas){
        c = null;
        type = ShapeType.MOUSE;
        this.canvas = canvas;
        start = new Point();
        end = new Point();
        jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("miniCAD file","obj");
        jfc.setFileFilter(filter);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);              
    }

    public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
        System.out.println(cmd);
        switch (cmd) {
            case "ѡ�񹤾�":
                type = ShapeType.MOUSE;
                break;
            case "ֱ��":
                type = ShapeType.LINE;
                break;
            case "����":
                type = ShapeType.RECT;
                break;            
            case "Բ":
                type = ShapeType.OVAL;
                break;
            case "�ı���":
                input = JOptionPane.showInputDialog(null, "��������","����",JOptionPane.INFORMATION_MESSAGE);
                if(input != null){
                    type = ShapeType.INPUTBOX;
                    System.out.println(input);
                }
                break;
            case "�Ƴ�":
                canvas.remove();
                break;
            case "�Ŵ�":
                canvas.zoom_in();
                break;
            case "��С":
                canvas.zoom_out();
                break;
            case "�Ӵ�":
                canvas.thicker();
                break;
            case "��ϸ":
                canvas.thinner();
                break;
            case "�½�(N)":
                canvas.flush();
                break;
            case "��(O)":
                jfc.setSelectedFile(null);
                jfc.showOpenDialog(null);  
                File openFile = jfc.getSelectedFile();
                if(openFile == null){
                    JOptionPane.showMessageDialog(null, "��ѡ���ļ���", "����", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                try {
                    canvas.open(openFile);
                } catch (Exception except) {
                    JOptionPane.showMessageDialog(null, except.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "����(S)":
                jfc.setSelectedFile(null);
                jfc.showSaveDialog(null);
                File saveFile = jfc.getSelectedFile();
                if(saveFile == null){
                    JOptionPane.showMessageDialog(null, "��ѡ�񱣴�·����", "����", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if(!saveFile.getAbsolutePath().endsWith("obj")){
	        		saveFile = new File(saveFile.getAbsolutePath()+".obj");
                }
                System.out.println(saveFile.getAbsolutePath());
                try {
                    canvas.save(saveFile);
                } catch (Exception except) {
                    System.out.println(except);
                    JOptionPane.showMessageDialog(null, except.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                }                
                break;
            case "�˳�(E)":
                System.exit(0);
                break;
            default:
                break;
        }

        if (cmd.equals("")) {
			JButton btn = (JButton) e.getSource();
			c = btn.getBackground();
            canvas.set_color(c);
		}
	}

    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked");

	}

	public void mousePressed(MouseEvent e) {
        System.out.println("mousePressed");
        start.move(e.getX(), e.getY());
        if(type == ShapeType.MOUSE)
            canvas.check(new Point(e.getX(), e.getY()));
	}

    public void mouseEntered(MouseEvent e) {
	
    }

	public void mouseExited(MouseEvent e) {
	
    }

	public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased");
        end.move(e.getX(), e.getY());
        if(type != ShapeType.MOUSE)
            canvas.add(start, end, type, c, input);
        else
            canvas.move(start, end, true);
	}

    public void mouseDragged(MouseEvent e) {
        end.move(e.getX(), e.getY());
        if(type != ShapeType.MOUSE)
            canvas.drawing(start, end, type, c, input);
        else   
            canvas.move(start, end, false);
    }

    public void mouseMoved(MouseEvent e) {

    }


	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                canvas.move(-5, 0);
                break;
            case KeyEvent.VK_RIGHT:
                canvas.move(5, 0);
                break;      
            case KeyEvent.VK_UP:
                canvas.move(0, -5);
                break;      
            case KeyEvent.VK_DOWN:
                canvas.move(0, 5);
                break;      
            default:
                break;
        };
	}
 
	public void keyReleased(KeyEvent e) {

	}
 
	public void keyTyped(KeyEvent e) {

    }
}
