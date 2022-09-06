import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class DrawPad extends JFrame {

	public static void main(String[] args) {
		DrawPad bo = new DrawPad();
		bo.initUI();
	}

	public void initUI() {
		setTitle("DrawPad");
		setSize(1280, 720);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());
		setVisible(true);
		Listener draw = new Listener(this.getGraphics());
		addMouseMotionListener(draw);
	}
}
