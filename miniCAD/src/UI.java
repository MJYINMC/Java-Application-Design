import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class UI {
	public static void main(String[] args) {
		UI miniCAD = new UI();
		miniCAD.initUI();
	}

	public void initUI() {
		JFrame mainFrame = new JFrame("miniCAD");
		// ��������
		// ���ô�С���ԣ�ʹ�ñ߿򲼾�
		mainFrame.setSize(1600, 900);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ��������
		PaintCanvas canvas = new PaintCanvas();
		Listener ls = new Listener(canvas);
		canvas.setBackground(Color.white);
        canvas.addMouseListener(ls);
		canvas.addMouseMotionListener(ls);
		canvas.addKeyListener(ls);

		// ����������
		JMenuBar menuBar = new JMenuBar();
        JMenu menu=new JMenu("�ļ�(F)");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem item=new JMenuItem("�½�(N)",KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
        menu.add(item);
        item=new JMenuItem("��(O)",KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
        menu.add(item);
        item=new JMenuItem("����(S)",KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
        menu.add(item);
        menu.addSeparator();
        item=new JMenuItem("�˳�(E)",KeyEvent.VK_E);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
        menu.add(item);
		menuBar.add(menu);

		// ����������
		JPanel ToolKit = new JPanel();
		ToolKit.setLayout(new GridLayout(6, 1));
		ToolKit.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// ���ù���������
		Font font = new Font("����", Font.BOLD, 25);
		JLabel text1 = new JLabel("������", JLabel.CENTER);
		text1.setFont(font);
		ToolKit.add(text1);
		JPanel functionArea = new JPanel();
		functionArea.setLayout(new GridLayout(3, 2, 30, 30));
		String[] btnstr = { "ѡ�񹤾�", "ֱ��", "����", "Բ", "�ı���", "�Ƴ�"};
		for (int i = 0; i < btnstr.length; i++) {
			JButton btn = new JButton(btnstr[i]);
			functionArea.add(btn);
			btn.addActionListener(ls);
		}
		ToolKit.add(functionArea);

		// ������ɫ������
		JLabel text2 = new JLabel("��ɫ��", JLabel.CENTER);
		text2.setFont(font);
		ToolKit.add(text2);
		JPanel colorArea = new JPanel();
		colorArea.setLayout(new GridLayout(3, 2));
		Color[] btnco = { Color.red, Color.blue, Color.yellow, Color.green, Color.black };
		for (int i = 0; i < btnco.length; i++) {
			JButton btn = new JButton("");
			btn.setBackground(btnco[i]);
			btn.setPreferredSize(new Dimension(50, 30));
			colorArea.add(btn);
			btn.addActionListener(ls);
		}
		ToolKit.add(colorArea);

		// ����ͼ�����������
		JLabel text3 = new JLabel("ͼ�����", JLabel.CENTER);
		text3.setFont(font);
		ToolKit.add(text3);
		JPanel enhanceArea = new JPanel();
		enhanceArea.setLayout(new GridLayout(2,2,30,30));
		JButton thicker = new JButton("�Ӵ�");
		JButton thinner = new JButton("��ϸ");
		JButton zoom_in = new JButton("�Ŵ�");
		JButton zoom_out = new JButton("��С");
		thicker.addActionListener(ls);
		thinner.addActionListener(ls);
		zoom_in.addActionListener(ls);
		zoom_out.addActionListener(ls);
		enhanceArea.add(zoom_in);
		enhanceArea.add(zoom_out);
		enhanceArea.add(thicker);
		enhanceArea.add(thinner);
		ToolKit.add(enhanceArea);

		// ��ӻ�ͼ���������Ͳ˵���        
		mainFrame.add(menuBar, BorderLayout.NORTH);
		mainFrame.add(canvas, BorderLayout.CENTER);
		mainFrame.add(ToolKit, BorderLayout.EAST);

		// ������ʾ
		mainFrame.setVisible(true);
	}
}

