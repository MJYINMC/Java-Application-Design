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

    Listener(PaintCanvas canvas) {
        c = null;
        type = ShapeType.MOUSE;
        this.canvas = canvas;
        start = new Point();
        end = new Point();
        jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("miniCAD file", "obj");
        jfc.setFileFilter(filter);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public void actionPerformed(ActionEvent e) {
        cmd = e.getActionCommand();
        System.out.println(cmd);
        switch (cmd) {
            case "选择工具":
                type = ShapeType.MOUSE;
                break;
            case "直线":
                type = ShapeType.LINE;
                break;
            case "矩形":
                type = ShapeType.RECT;
                break;
            case "圆":
                type = ShapeType.OVAL;
                break;
            case "文本框":
                input = JOptionPane.showInputDialog(null, "键入文字", "输入", JOptionPane.INFORMATION_MESSAGE);
                if (input != null) {
                    type = ShapeType.INPUTBOX;
                    System.out.println(input);
                }
                break;
            case "移除":
                canvas.remove();
                break;
            case "放大":
                canvas.zoom_in();
                break;
            case "缩小":
                canvas.zoom_out();
                break;
            case "加粗":
                canvas.thicker();
                break;
            case "变细":
                canvas.thinner();
                break;
            case "新建(N)":
                canvas.flush();
                break;
            case "打开(O)":
                jfc.setSelectedFile(null);
                jfc.showOpenDialog(null);
                File openFile = jfc.getSelectedFile();
                if (openFile == null) {
                    JOptionPane.showMessageDialog(null, "请选择文件！", "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                try {
                    canvas.open(openFile);
                } catch (Exception except) {
                    JOptionPane.showMessageDialog(null, except.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "保存(S)":
                jfc.setSelectedFile(null);
                jfc.showSaveDialog(null);
                File saveFile = jfc.getSelectedFile();
                if (saveFile == null) {
                    JOptionPane.showMessageDialog(null, "请选择保存路径！", "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if (!saveFile.getAbsolutePath().endsWith("obj")) {
                    saveFile = new File(saveFile.getAbsolutePath() + ".obj");
                }
                System.out.println(saveFile.getAbsolutePath());
                try {
                    canvas.save(saveFile);
                } catch (Exception except) {
                    System.out.println(except);
                    JOptionPane.showMessageDialog(null, except.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "退出(E)":
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
        if (type == ShapeType.MOUSE)
            canvas.check(new Point(e.getX(), e.getY()));
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased");
        end.move(e.getX(), e.getY());
        if (type != ShapeType.MOUSE)
            canvas.add(start, end, type, c, input);
        else
            canvas.move(start, end, true);
    }

    public void mouseDragged(MouseEvent e) {
        end.move(e.getX(), e.getY());
        if (type != ShapeType.MOUSE)
            canvas.drawing(start, end, type, c, input);
        else
            canvas.move(start, end, false);
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
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
        }
        ;
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }
}
