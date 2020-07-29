import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ClientHandler extends Thread {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final Socket s;
    private final CopyOnWriteArrayList<Day> week;
    private final List<Socket> clients;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, CopyOnWriteArrayList<Day> week, List<Socket> clients) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.week = week;
        this.clients = clients;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                // Ask user what he wants
                dos.writeUTF("Podaj rodzaj operacji(set/unset), dzień tygodnia(pon/wt/sr/czw/pt) i godzinę wizyty rozdzielone przecinkami:");
                // receive the answer from client
                received = dis.readUTF();
                if (received.equals("exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                //read day of the week from user
                String delims = "[,]";
                String[] tokens = received.split(delims);
                if (tokens[0].equals("set") && tokens[1].equals("pon")) {
                    set(received, 0);
                } else if (tokens[0].equals("set") && tokens[1].equals("wt")) {
                    set(received, 1);
                } else if (tokens[0].equals("set") && tokens[1].equals("sr")) {
                    set(received, 2);
                } else if (tokens[0].equals("set") && tokens[1].equals("czw")) {
                    set(received, 3);
                } else if (tokens[0].equals("set") && tokens[1].equals("pt")) {
                    set(received, 4);
                } else if (tokens[0].equals("unset") && tokens[1].equals("pon")) {
                    unset(received, 0);
                } else if (tokens[0].equals("unset") && tokens[1].equals("wt")) {
                    unset(received, 1);
                } else if (tokens[0].equals("unset") && tokens[1].equals("sr")) {
                    unset(received, 2);
                } else if (tokens[0].equals("unset") && tokens[1].equals("czw")) {
                    unset(received, 3);
                } else if (tokens[0].equals("unset") && tokens[1].equals("pt")) {
                    unset(received, 4);
                } else {
                    dos.writeUTF("Niepoprawne polecenie");
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Zła liczba argumentów");
                e.printStackTrace();
                break;
            }
        }
        try {
            // closing resources
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void set(String received, int index) {
        week.get(index).setDay(received, getId());
        for (Socket s : clients) {
            DataOutputStream dos1 = null;
            try {
                dos1 = new DataOutputStream(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!dos1.equals(this.dos) && week.get(index).error().equals("")) {
                try {
                    dos1.writeUTF(week.get(0).show() + week.get(1).show() + week.get(2).show() + week.get(3).show() + week.get(4).show());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    dos.writeUTF(week.get(0).show() + week.get(1).show() + week.get(2).show() + week.get(3).show() + week.get(4).show() + week.get(index).error());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unset(String received, int index) {
        week.get(index).unsetDay(received, getId());
        for (Socket s : clients) {
            DataOutputStream dos1 = null;
            try {
                dos1 = new DataOutputStream(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!dos1.equals(this.dos) && week.get(index).error().equals("")) {
                try {
                    dos1.writeUTF(week.get(0).show() + week.get(1).show() + week.get(2).show() + week.get(3).show() + week.get(4).show());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    dos.writeUTF(week.get(0).show() + week.get(1).show() + week.get(2).show() + week.get(3).show() + week.get(4).show() + week.get(index).error());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}