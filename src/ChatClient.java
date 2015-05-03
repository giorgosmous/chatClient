
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChatClient extends JFrame {

    //dhlwsh antikeimenwn gia ta stoixeia tou grafikou perivalontos
    private final JPanel contentPane;
    private JTextField username;
    private final JLabel lblUsername;
    private final JButton btnLogin;
    private JLabel lblPleaseInsert;
    private JLabel connectedUsers;
    private final JLabel lblEx;
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

    public void Message_display(String U, String M) {
        textArea_1.append(U + ": " + " " + M + "\n");

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
    
    public void updateConnectedUsers() {
        connectedUsers.setText("Connected Users: " + list.getItemCount());
    }

    public ChatClient() throws IOException {
        //dimiourgia titlou sto parathiro
        super("Chat Room");

        //dimiourgia antikeimenou tupou Client gia tin enarksi tou socket
        yo = new Client(this, "localhost");

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

        btnLogin = new JButton("Login");
        btnLogin.setBounds(456, 292, 97, 25);
        panel.add(btnLogin);

        lblPleaseInsert = new JLabel("Please insert an username");
        lblPleaseInsert.setForeground(Color.RED);
        lblPleaseInsert.setBounds(346, 270, 157, 22);
        lblPleaseInsert.setVisible(false);
        panel.add(lblPleaseInsert);

        lblEx = new JLabel("Exists");
        lblEx.setForeground(Color.RED);
        lblEx.setBounds(346, 270, 157, 22);
        lblEx.setVisible(false);
        panel.add(lblEx);

        //ActionListener tou koumpiou pou kanei to login 
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //ean to pedio den einai keno proxwraei stin prgmatopoihsh tou login
                if (!username.getText().equals("")) {

                    panel.setVisible(true);
                    yo.sendUsername();
                    lblWelcome = new JLabel("You are connected as: " + username.getText());
                    lblWelcome.setForeground(Color.BLUE);
                    lblWelcome.setBounds(10, 565, 300, 30);
                    lblWelcome.setVisible(true);
                    contentPane.add(lblWelcome);
                    //ena einai keno emfanizete katallilo minima
                } else {

                    lblPleaseInsert.setVisible(true);

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
