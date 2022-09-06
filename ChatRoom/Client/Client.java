package Client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

interface Receiver extends Runnable {
    void MsgRecv();

    void stopRecv();
}

public class Client extends JFrame {
    private JTextField host;
    private JTextField port;
    private JButton connect;

    private JTextField nickname;
    private JTextField message;
    private JButton send;

    private JTextArea chatContext;
    private JScrollPane scrollPanel;

    private Socket s;
    private BufferedReader in;
    private PrintStream out;
    private Thread receiver;

    public Client() {
        host = new JTextField("127.0.0.1", 30);
        port = new JTextField("8080", 7);
        connect = new JButton("建立连接");

        nickname = new JTextField("昵称", 7);
        message = new JTextField("Hello World!", 30);
        send = new JButton("发送消息");

        chatContext = new JTextArea();
        scrollPanel = new JScrollPane(chatContext);
        scrollPanel.setPreferredSize(new Dimension(400, 500));

        s = null;
        in = null;
        out = null;
        receiver = null;
    }

    public void initUI() {
        this.setSize(600, 800);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 20));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1));
        inputPanel.add(new JLabel("服务器地址", JLabel.LEFT));
        inputPanel.add(host);
        inputPanel.add(new JLabel("端口号", JLabel.LEFT));
        inputPanel.add(port);
        inputPanel.add(new JLabel("昵称", JLabel.LEFT));
        inputPanel.add(nickname);
        inputPanel.add(new JLabel("消息", JLabel.LEFT));
        inputPanel.add(message);
        this.add(inputPanel);
        this.add(connect);
        this.add(send);
        this.add(scrollPanel);
        this.setVisible(true);
    }

    void initLogic() {
        connect.addActionListener(e -> {
            if (e.getActionCommand().equals("建立连接")) {
                try {
                    Integer.parseInt(port.getText());
                    chatContext.append(String.format("尝试连接到%s:%s\n", host.getText(), port.getText()));
                    chatContext.update(chatContext.getGraphics());
                    s = new Socket(host.getText(), Integer.parseInt(port.getText()));
                    in = new BufferedReader(
                            new InputStreamReader(
                                    s.getInputStream()));
                    out = new PrintStream(
                            s.getOutputStream());

                    receiver = new Thread(
                            new Runnable() {
                                public void run() {
                                    try {
                                        while (true) {
                                            String line = in.readLine();
                                            System.out.println(line);
                                            if (line == null)
                                                break;
                                            else if (!line.equals(""))
                                                chatContext.append(line + '\n');
                                            ;
                                        }
                                    } catch (IOException e) {
                                    }
                                }
                            });
                    receiver.start();
                    chatContext.append("连接成功!\n");
                    connect.setText("断开连接");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "地址格式错误!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "连接失败!\n");
                    chatContext.append("连接失败!\n");
                }
            } else {
                try {
                    s.close();
                    chatContext.append("断开连接成功!\n");
                    connect.setText("建立连接");
                    s = null;
                    in = null;
                    out = null;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "内部错误!\n");
                    chatContext.append("断开连接失败!\n");
                }
            }
        });

        send.addActionListener(e -> {
            if (out != null) {
                out.println(nickname.getText() + ": " + message.getText());
                out.flush();
            }
        });

    }

    public static void main(String[] args) {
        Client myclient = new Client();
        myclient.initUI();
        myclient.initLogic();
    }
}