/* Μουστάκας Γεώργιος 321 / 2011 102
   Χατζηαναστασιάδης Μιχαήλ Μάριος 321 / 2011 176
   Σωτηρέλης Χρήστος 321 / 2012 182
*/
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChatClient extends JFrame {

    //dhlwsh antikeimenwn gia ta stoixeia tou grafikou perivalontos
    private final JPanel contentPane;
    private JTextField username;
    private JTextField hostnameTextField;

    private final JLabel lblUsername;
    private final JButton btnLogin;
    private JLabel lblPleaseInsert;
    private JLabel connectedUsers;
    private JLabel lblEx;
    private JTextField textField;
    final JPanel panel;
    private Client yo;
    final List list;
    final JTextArea textArea_1 = new JTextArea();
    final JTextArea textArea = new JTextArea();
    private JLabel lblWelcome;

    //main klasi pou trexei to termatiko tou Client
    public static void main(String[] args) throws IOException {

        ChatClient frame = new ChatClient();
        frame.setVisible(true);

    }

    //setters kai getters
    String getusername() {
        return username.getText();
    }

    public void refresh_list(ArrayList<String> A) {
        list.removeAll();
        for (String user : A) {

            list.add(user);
        }

    }

    public void Message_display(String U, String M, String time) {
        textArea_1.append(U + " | " + time
                + " says:" + "\n" + M + "\n");

    }

    public void username_setText_null() {
        username.setText("");
    }

    public void lblEx_setVisible_true() {
        lblEx.setVisible(true);
    }

    public void lblEx_setVisible_false() {
        lblEx.setVisible(false);
    }

    public void panel_setVisible_true() {
        panel.setVisible(true);
    }

    public void panel_setVisible_false() {
        panel.setVisible(false);
    }

    public void lblPleaseInsert_setVisible_true() {
        lblPleaseInsert.setVisible(true);
    }

    public void lblPleaseInsert_setVisible_false() {
        lblPleaseInsert.setVisible(false);
    }

    //Συνάρτηση που ενημερώνει το gui με το πλήθος των συνδεδεμένων χρηστών
    public void updateConnectedUsers() {
        connectedUsers.setText("Connected Users: " + list.getItemCount());
    }

    private ChatClient getSelf() {
        return this;
    }

    public ChatClient() throws IOException {
        //dimiourgia titlou sto parathiro
        super("Chat Room");

        textArea_1.setForeground(Color.blue.brighter());
        list = new List();
        //stoixeia parathirou
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 890, 650);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.orange);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        //dimiourgia panel gia gia tin topothetisi twn stoixeiwn tou grafikou perivalontos
        //pou sxetizete me to login
        panel = new JPanel();
        panel.setBackground(Color.orange);
        panel.setLayout(null);
        panel.setBounds(0, 0, 882, 653);
        contentPane.add(panel);

        username = new JTextField();
        username.setBounds(346, 246, 207, 22);
        panel.add(username);
        username.setColumns(10);

        lblUsername = new JLabel("Username: ");
        lblUsername.setBounds(346, 222, 77, 22);
        panel.add(lblUsername);

        hostnameTextField = new JTextField("localhost");
        hostnameTextField.setBounds(346, 290, 207, 22);
        panel.add(hostnameTextField);
        hostnameTextField.setColumns(10);

        JLabel hostnameLabel = new JLabel("Hostname/IP Address: ");
        hostnameLabel.setBounds(346, 268, 150, 22);
        panel.add(hostnameLabel);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(456, 330, 97, 25);
        panel.add(btnLogin);

        lblPleaseInsert = new JLabel("Please insert an username");
        lblPleaseInsert.setForeground(Color.RED);
        lblPleaseInsert.setBounds(346, 310, 170, 22);
        lblPleaseInsert.setVisible(false);
        panel.add(lblPleaseInsert);

        lblEx = new JLabel("Username exists");
        lblEx.setForeground(Color.RED);
        lblEx.setBounds(346, 310, 157, 22);
        lblEx.setVisible(false);
        panel.add(lblEx);

        //ActionListener tou koumpiou pou kanei to login 
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //ean to pedio den einai keno proxwraei stin pragmatopoihsh tou login
                if (!username.getText().equals("")) {
                    try {
                        if (panel.isVisible()) {
                            if (lblWelcome != null) {
                                lblWelcome.setText(null);
                            }
                            //dimiourgia antikeimenou tupou Client gia tin enarksi tou socket
                            yo = new Client(getSelf(), hostnameTextField.getText());
                        }

                    } catch (java.net.UnknownHostException | java.net.NoRouteToHostException e) {
                        JOptionPane.showMessageDialog(null, "Wrong Hostname / IP Address");
                    } catch (IOException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    panel.setVisible(true);

                    //Στέλνουμε το username
                    yo.sendUsername();
                    
                    // Label που δείχνει το όνομα που έχουμε συνδεθεί
                    lblWelcome = new JLabel("You are connected as: " + username.getText());
                    lblWelcome.setForeground(Color.BLUE);
                    lblWelcome.setBounds(10, 565, 300, 30);
                    lblWelcome.setVisible(true);
                    contentPane.add(lblWelcome);

                } else {
                    // Ζητάμε απο τον χρήστη να γράψει κάποιο κείμενο
                    lblPleaseInsert.setVisible(true);
                    lblEx.setVisible(false);
                }

            }
        });

        //dimiourgia antikeimenwn gia ta stoixeia pou sxetizontai me to kommati tou
        //chat room
        JScrollPane scrollbar_1 = new JScrollPane(textArea_1);
        scrollbar_1.setBounds(10, 10, 593, 508);
        textArea_1.setLineWrap(true);
        textArea_1.setWrapStyleWord(true);
        textArea_1.setEditable(false);
        contentPane.add(scrollbar_1);

        textArea.setLineWrap(true);
        JScrollPane scrollbar = new JScrollPane(textArea);
        scrollbar.setBounds(10, 520, 593, 40);
        contentPane.add(scrollbar);

        JScrollPane scrollbar_2 = new JScrollPane(list);
        scrollbar_2.setBounds(609, 30, 265, 486);
        contentPane.add(scrollbar_2);

        connectedUsers = new JLabel();
        connectedUsers.setBounds(609, 2, 265, 30);
        contentPane.add(connectedUsers);

        JButton btnSend = new JButton("Send");

        //ActionListener tou koumpiou pou kanei tin apostoli tou minimatos
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //den epitrepei ton xristi na steilei keno minima
                if (!textArea.getText().equals("")) {

                    yo.sendmsg(textArea.getText());
                    textArea.setText("");

                }

            }
        });

        btnSend.setBounds(488, 565, 115, 30);
        contentPane.add(btnSend);
    }

}
