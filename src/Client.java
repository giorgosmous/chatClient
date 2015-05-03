/* Μουστάκας Γεώργιος 321 / 2011 102
   Χατζηαναστασιάδης Μιχαήλ Μάριος 321 / 2011 176
   Σωτηρέλης Χρήστος 321 / 2012 182
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client extends Thread {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final SSLSocket sock;
    ChatClient CC;
    String Ex;
    private static SSLSocketFactory sslFactory;

    public Client(ChatClient Ch, String hostname) throws IOException {

        CC = Ch;

        try {
            //Δημιουργούμε αντικειμενο SSLContext που περιέχει τα στοιχεία της σύνδεσης

            SSLContext context = SSLContext.getInstance("SSL");
            //Περνάμε τα keystore και truststore καθως και το random number (για τη κρυπτογραφηση)
            context.init(null, null, new SecureRandom());
            sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ανοίγουμε νέο socket
        sock = (SSLSocket) sslFactory.createSocket(hostname, 6666);
        //Ορίζουμε τις κρυπτογραφικες σουίτες μόνο για SSL (για αποφυγή χρήσης άλλων ανεπιθύμητων κρυπτογραφικών σουιτών)
        String[] enabledsuites = sock.getEnabledCipherSuites();
        String[] preferedsuites = {"SSL_DH_anon_WITH_RC4_128_MD5"};

        //τις τοποθετούμε στις ενεργές σουίτες
        sock.setEnabledCipherSuites(preferedsuites);

        //ξεκινάμε το handshake
        sock.startHandshake();
        
        // Αποηθηκεύουμε τις ροές
        out = new ObjectOutputStream(sock.getOutputStream());
        in = new ObjectInputStream(sock.getInputStream());

        try {
            do {
                out.writeObject("START");
            } while (!((String) in.readObject()).equals("WAITING"));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Μέθοδος αποστολής μηνύματος
    public void sendmsg(String s) {
        try {
            // Αποστέλλουμε πρώτα ένα String Message και ύστερα το μήνυμα 
            // που θέλουμε να στείλουμε
            out.writeObject("MESSAGE");
            out.writeObject(s);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Μέθοδος που επιστρέφει άν υπάρχει ο χρήστης
    public String getEx() {
        return Ex;
    }

    @Override
    public void run() {
        try {
            String command;
            
            // Loop πρωτοκόλλου
            while ((command = (String) in.readObject()) != null) {
                
                if (command.equals("MESSAGE")) {
                    Message m = (Message) in.readObject();
                    System.out.println(m.getUsername() + " say: " + m.getMessage());
                    CC.Message_display(m.getUsername(), m.getMessage(), m.getTimestamp());
                }
                
                if (command.equals("USERLIST")) {
                    CC.refresh_list((ArrayList<String>) in.readObject());
                    System.out.print("getting userlist");
                    CC.updateConnectedUsers();
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Μέθοδος αποστολής του username στον server
    public void sendUsername() {
        try {
            out.writeObject("USERNAME");
            out.writeObject(CC.getusername());
            
            // Εφόσον ο χρήστης δεν υπάρχει
            if (((String) in.readObject()).equals("DOES NOT EXIST")) {
                CC.panel_setVisible_false();
                
                // Κάνουμε εκκίνηση του thread για να ξεκινήσει το πρωτόκολλο
                start();
            } else {
                CC.lblEx_setVisible_true();
                CC.lblPleaseInsert_setVisible_false();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

// Βοηθητική Κλάση που μας ενημερώνει για την εξέλιξη του SSL Handshake
abstract class MyHandshakeListener implements HandshakeCompletedListener {

    public void handshakeCompleted(HandshakeCompletedEvent e) {
        System.out.println("Handshake succesful!");
        System.out.println("Using cipher suite: " + e.getCipherSuite());
    }
}
