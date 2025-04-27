package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.LoginController;
import service.LoginService;

public class LoginManager extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField textField_1;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginManager frame = new LoginManager();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginManager() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 511, 510);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("CELLCOMPSTORE");
        lblNewLabel.setBackground(Color.WHITE);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 38));
        lblNewLabel.setBounds(23, 34, 367, 68);
        contentPane.add(lblNewLabel);

        ImageIcon originalIcon = new ImageIcon("pic/logo.png");
        Image img = originalIcon.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
        JLabel lblNewLabel_3 = new JLabel(new ImageIcon(img));
        lblNewLabel_3.setBounds(381, 46, 56, 56);
        contentPane.add(lblNewLabel_3);

        ImageIcon originalBg = new ImageIcon("pic/loginm2.jpg");
        Image bgImage = originalBg.getImage().getScaledInstance(631, 200, Image.SCALE_SMOOTH);
        JLabel lblNewLabel_1 = new JLabel(new ImageIcon(bgImage));
        lblNewLabel_1.setBounds(-134, 0, 631, 185);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("ĐĂNG NHẬP");
        lblNewLabel_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel_1_1.setBounds(149, 195, 196, 50);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_4 = new JLabel("UserName:");
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_4.setBounds(10, 253, 115, 34);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_4_1 = new JLabel("Password:");
        lblNewLabel_4_1.setForeground(Color.WHITE);
        lblNewLabel_4_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_4_1.setBounds(10, 312, 115, 34);
        contentPane.add(lblNewLabel_4_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField.setColumns(10);
        textField.setBounds(128, 255, 233, 35);
        contentPane.add(textField);

        textField_1 = new JPasswordField();
        textField_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_1.setColumns(10);
        textField_1.setBounds(128, 313, 233, 35);
        contentPane.add(textField_1);

        btnNewButton = new JButton("Đăng nhập");
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnNewButton.setBackground(new Color(204, 102, 255));
        btnNewButton.setBounds(161, 372, 149, 34);
        btnNewButton.setFocusPainted(false);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setIcon(new ImageIcon("pic/loginm.jpg"));
        lblNewLabel_2.setBounds(0, 183, 497, 290);
        contentPane.add(lblNewLabel_2);

        LoginService loginService = new LoginService();
        new LoginController(this, loginService);
    }

    public String getUsername() {
        return textField.getText();
    }

    public String getPassword() {
        return new String(textField_1.getPassword());
    }

    public JButton getLoginButton() {
        return btnNewButton;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void hideWindow() {
        setVisible(false);
    }
}