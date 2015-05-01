package chatclient;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorgosmous
 */


public class Client {
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public Client(String hostname) {        
        try {
            // Ανοίγουμε νέο socket
            Socket sock = new Socket(hostname, 6666);
            
            // Αποθηκεύουμε τις δύο ροές
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            
            // Αποστέλλουμε την εντολή start για να ξεκινήσει η συναλλαγή
            out.writeObject("START");
            
            // Περιμένουμε μέχρι να λάβουμε ανταπόκριση απο τον server
            try {
                String response = (String) in.readObject();
                if (response.equals("WAITING")) {
                    out.writeObject("USERNAME");
                    out.writeObject("USER2");
                }
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("writesomething :");
                out.writeObject(sc.nextLine());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

}
