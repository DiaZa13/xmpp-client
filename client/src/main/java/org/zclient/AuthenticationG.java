package org.zclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthenticationG extends JFrame {
    private JPanel mainPanel;
    private JPanel register;
    private JPanel logIn;
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnSignup;
    private JLabel lblLogin;
    private JButton btnSignin;
    private JTextField txtUsernameL;
    private JPasswordField txtPasswordL;
    private JLabel lblSingup;

    public static void main(String[] args) {

        JFrame frame = new AuthenticationG("Sign Up");
        frame.setVisible(true);
    }

    public AuthenticationG(String title) {
        super(title);

        // Initializing the connection to the server
//        connection.start();
//        this.authentication = new Authentication(connection.getStream());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        createUIComponents();

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

//        Teclado tecla = new Teclado();
//        Mouse mouse = new Mouse();
        Listener listener = new Listener();

        register.setVisible(true);
        logIn.setVisible(false);
        btnSignup.addActionListener(listener);


        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        lblLogin.addMouseListener(mouse);


    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(e.getSource() == btnSignup) {
                String username = txtUsername.getText();
                String email = txtEmail.getText();
                char [] char_password = txtPassword.getPassword();
                String password = new String(char_password);

                if (username.contentEquals("")|| email.contentEquals("") || password.contentEquals("")) {
                    JOptionPane.showMessageDialog(null, "Please complete all required fields");

                }


            }

        }
    }

}
