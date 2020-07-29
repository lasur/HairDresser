import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5056);
        //initialization of each day
        Day pon = new Day("Poniedziałek");
        Day wt = new Day("Wtorek");
        Day sr = new Day("Środa");
        Day czw = new Day("Czwartek");
        Day pt = new Day("Piątek");
        //adding days to an arraylist
        CopyOnWriteArrayList<Day> week = new CopyOnWriteArrayList<>();
        week.add(pon);
        week.add(wt);
        week.add(sr);
        week.add(czw);
        week.add(pt);
        List<Socket> clients = new ArrayList<Socket>();
        // running infinite loop for getting client request
        while (true) {
            Socket s = null;
            try {
                // socket object to receive incoming client requests
                s = ss.accept();
                clients.add(s);
                System.out.println("A new client is connected : " + s);
                // obtaining input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                System.out.println("Assigning new thread for this client");
                //create a new thread object
                ClientHandler t = new ClientHandler(s, dis, dos, week, clients);
                // Invoking the start() method
                t.start();
            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}