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
		// 创建窗体
		// 设置大小属性，使用边框布局
		mainFrame.setSize(1600, 900);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 创建画板
		PaintCanvas canvas = new PaintCanvas();
		Listener ls = new Listener(canvas);
		canvas.setBackground(Color.white);
		canvas.addMouseListener(ls);
		canvas.addMouseMotionListener(ls);
		canvas.addKeyListener(ls);

		// 创建任务栏
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("文件(F)");
		menu.setMnemonic(KeyEvent.VK_F);
		JMenuItem item = new JMenuItem("新建(N)", KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
		menu.add(item);
		item = new JMenuItem("打开(O)", KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
		menu.add(item);
		item = new JMenuItem("保存(S)", KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("退出(E)", KeyEvent.VK_E);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		item.addActionListener(ls);
		menu.add(item);
		menuBar.add(menu);

		// 创建工具箱
		JPanel ToolKit = new JPanel();
		ToolKit.setLayout(new GridLayout(6, 1));
		ToolKit.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// 设置功能区属性
		Font font = new Font("黑体", Font.BOLD, 25);
		JLabel text1 = new JLabel("功能区", JLabel.CENTER);
		text1.setFont(font);
		ToolKit.add(text1);
		JPanel functionArea = new JPanel();
		functionArea.setLayout(new GridLayout(3, 2, 30, 30));
		String[] btnstr = { "选择工具", "直线", "矩形", "圆", "文本框", "移除" };
		for (int i = 0; i < btnstr.length; i++) {
			JButton btn = new JButton(btnstr[i]);
			functionArea.add(btn);
			btn.addActionListener(ls);
		}
		ToolKit.add(functionArea);

		// 设置颜色区属性
		JLabel text2 = new JLabel("颜色区", JLabel.CENTER);
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

		// 设置图像调整区属性
		JLabel text3 = new JLabel("图像调整", JLabel.CENTER);
		text3.setFont(font);
		ToolKit.add(text3);
		JPanel enhanceArea = new JPanel();
		enhanceArea.setLayout(new GridLayout(2, 2, 30, 30));
		JButton thicker = new JButton("加粗");
		JButton thinner = new JButton("变细");
		JButton zoom_in = new JButton("放大");
		JButton zoom_out = new JButton("缩小");
		thicker.addActionListener(ls);
		thinner.addActionListener(ls);
		zoom_in.addActionListener(ls);
		zoom_out.addActionListener(ls);
		enhanceArea.add(zoom_in);
		enhanceArea.add(zoom_out);
		enhanceArea.add(thicker);
		enhanceArea.add(thinner);
		ToolKit.add(enhanceArea);

		// 添加绘图，功能区和菜单栏
		mainFrame.add(menuBar, BorderLayout.NORTH);
		mainFrame.add(canvas, BorderLayout.CENTER);
		mainFrame.add(ToolKit, BorderLayout.EAST);

		// 开启显示
		mainFrame.setVisible(true);
	}
}
