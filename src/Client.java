import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client
{
    public static void main(String[] args)
    {
        try
        {
            Scanner scn = new Scanner(System.in);
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");
            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            String toSend="";
            //making new thread to listen to server
            ListenIncoming t=new ListenIncoming(s);
            t.start();
            // the following loop performs the exchange of information between client and client handler
            while (true)
            {
                toSend = scn.next();
                dos.writeUTF(toSend);
                // If client sends exit,close this connection
                // and then break from the while loop
                if(toSend.equals("exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
            }
            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ListenIncoming extends Thread {

    private final Socket socket;
    private DataInputStream dis;

    public ListenIncoming(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                String fromServer = dis.readUTF();
                System.out.println(fromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}