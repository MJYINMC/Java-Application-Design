package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.Scanner;

public class Server {
    private int port;
    private msgSender msgSender;
    private ExecutorService threadPool;

    public Server(int port) {
        this.msgSender = new msgSender();
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
    }

    void start() {
        this.threadPool.execute(() -> {
            try {
                ServerSocket socket = new ServerSocket(port);
                System.out.println(
                        String.format("Listening on %s:%s", socket.getInetAddress().toString(), socket.getLocalPort()));
                while (true) {
                    Socket clientSocket = socket.accept();
                    threadPool.execute(new Client(clientSocket, msgSender));
                    System.out
                            .println(String.format("[Remote]%s has connected!", clientSocket.getRemoteSocketAddress()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.threadPool.execute(msgSender);
    }

    public static void main(String[] args) {
        int port;
        if (args.length < 1) {
            System.out.print("Input port number: ");
            Scanner in = new Scanner(System.in);
            port = in.nextInt();
            in.close();
        } else {
            port = Integer.parseInt(args[0]);
        }
        Server myserver = new Server(port);
        myserver.start();
    }
}

interface SendMsg {
    void sendMsg(String msg);

    Boolean getStatus();
}

class Client implements Runnable, SendMsg {
    Socket socket;
    BufferedReader in;
    PrintStream out;
    msgSender msgSender;
    boolean connected;

    Client(Socket s, msgSender msgSender) {
        this.socket = s;
        this.msgSender = msgSender;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));
            out = new PrintStream(
                    s.getOutputStream());

            msgSender.addClient(this);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String line;
        try {
            while (connected) {
                line = in.readLine();
                if (line == null)
                    break;
                else if (!line.equals(""))
                    msgSender.broadcast(line);
            }

        } catch (IOException e) {

        } finally {
            connected = false;
            System.out.println(String.format("[Remote]%s has disconnected!", socket.getRemoteSocketAddress()));
            try {
                socket.close();
                msgSender.removeClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String msg) {
        out.println(msg);
        out.flush();
    };

    public Boolean getStatus() {
        return !connected;
    };
}

class msgSender implements Runnable {
    private final ArrayList<Client> Clients;
    private final Queue<String> msgQueue;

    public void broadcast(String msg) {
        synchronized (this) {
            msgQueue.add(msg);
            this.notify();
        }
    }

    public void addClient(Client s) {
        synchronized (this) {
            Clients.add(s);
        }
    }

    public void removeClient() {
        synchronized (this) {
            Clients.removeIf(Client::getStatus);
        }
    }

    public msgSender() {
        Clients = new ArrayList<Client>();
        msgQueue = new LinkedList<String>();
    }

    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    while (!msgQueue.isEmpty()) {
                        String msg = msgQueue.peek();
                        msgQueue.remove();
                        for (Client s : Clients) {
                            s.sendMsg(msg);
                        }
                    }
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}