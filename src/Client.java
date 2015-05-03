
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
            context.init(null, null, null);
            sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ανοίγουμε νέο socket
        sock = (SSLSocket) sslFactory.createSocket(hostname, 6666);
        //Ορίζουμε τις κρυπτογραφικες σουίτες μόνο για SSL (για αποφυγή χρήσης άλλων ανεπιθύμητων κρυπτογραφικών σουιτών)
        String[] enabledsuites = sock.getEnabledCipherSuites();
        String[] preferedsuites = {"SSL_RSA_WITH_RC4_128_SHA",
            "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
            "SSL_RSA_WITH_RC4_128_MD5"
        };

        //τις τοποθετούμε στις ενεργές σουίτες
        sock.setEnabledCipherSuites(sock.getSupportedCipherSuites());

        //ξεκινάμε το handshake
        sock.startHandshake();

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

    public void sendmsg(String s) {

        try {

            out.writeObject("MESSAGE");
            out.writeObject(s);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getEx() {
        return Ex;
    }

    @Override
    public void run() {
        try {
            String command;
            while ((command = (String) in.readObject()) != null) {
                if (command.equals("MESSAGE")) {
                    Message m = (Message) in.readObject();
                    System.out.println(m.getUsername() + " say: " + m.getMessage());
                    CC.Message_display(m.getUsername(), m.getMessage());

                }
                System.out.println("while run");
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

    public void sendUsername() {
        try {

            out.writeObject("USERNAME");
            out.writeObject(CC.getusername());
            if (((String) in.readObject()).equals("DOES NOT EXIST")) {
                CC.panel_setVisible_false();
                start();
            } else {
                CC.lblEx_setVisible_true();
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
//κλάση για την ειδοποίηση νέας επιτυχούς χειραψίας

abstract class MyHandshakeListener implements HandshakeCompletedListener {

    public void handshakeCompleted(HandshakeCompletedEvent e) {
        System.out.println("Handshake succesful!");
        System.out.println("Using cipher suite: " + e.getCipherSuite());
    }
}
