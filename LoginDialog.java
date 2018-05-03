import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;

/**
 * LoginDialog is an extension of Dialog that checks for username and password before allowing the user to log in
 * Uses Singleton design pattern
 */
public class LoginDialog extends JDialog {

    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passLabel = new JLabel("Password: ");
    private JTextField username = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JButton loginButton = new JButton("Login");
    private JLabel errorText = new JLabel("");
    private Component errorTextFiller;
    private Boolean loggedIn = false;


    private LoginDialog(JFrame parent)
    {
        super(SwingUtilities.getWindowAncestor(parent));
        createGUI();
    }

    public static void show(JFrame parent)
    {
        new LoginDialog(parent);
    }

    private void verifyLoginInfo()
    {
        if (username.getText().equals("admin") && Arrays.toString(password.getPassword()).equals("[a, d, m, i, n]"))
        {
            this.dispose();
            loggedIn = true;
        }
        else
        {
            errorTextFiller.setVisible(false);
            errorText.setForeground(Color.RED);
            if (!"admin".equals(username.getText()))
            {
                errorText.setText("Incorrect username");
            }
            else if (!"[a, d, m, i, n]".equals(password.getPassword()))
            {
                errorText.setText("Incorrect password");
            }
        }
    }

    private void createGUI()
    {
        setTitle("Login");
        this.setResizable(false);
        this.setModal(true);
        this.setModalityType(APPLICATION_MODAL);
        JPanel p1 = new JPanel();
        p1.add(usernameLabel);
        p1.add(username);
        JPanel p2 = new JPanel();
        p2.add(passLabel);
        p2.add(password);
        loginButton.addActionListener(e->{
            verifyLoginInfo();
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!loggedIn)
                {
                    System.exit(0);
                }
            }
        });

        loginButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                {
                    verifyLoginInfo();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        JPanel pButton = new JPanel();
        pButton.add(loginButton);


        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent,BoxLayout.Y_AXIS));
        parent.add(p1);
        parent.add(p2);
        errorText.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorTextFiller = Box.createRigidArea(new Dimension(0,16));
        parent.add(errorTextFiller);
        parent.add(errorText);
        parent.add(pButton);
        this.add(parent);
        pack();
        this.setSize(new Dimension(280,140));
        setLocationRelativeTo(null);


        username.setText("admin");
        password.setText("admin");

        setVisible(true);
    }
}
