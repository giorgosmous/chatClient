package chatclient;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giorgosmous
 */
public class Client {
    public Client() {
        try {
            // Ανοίγουμε νέο socket
            Socket sock = new Socket("127.0.0.1", 6666);
            
            // Αποθηκεύουμε τις δύο ροές
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            
            // Αποστέλλουμε την εντολή start για να ξεκινήσει η συναλλαγή
            out.writeObject("START");
            
            // Περιμένουμε μέχρι να λάβουμε ανταπόκριση απο τον server
            try {
                System.out.println("Server " + (String) in.readObject());
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
