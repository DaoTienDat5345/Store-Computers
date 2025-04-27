package view;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.LoginController;
import service.LoginService;
import java.awt.*;

public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField textField_1;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        setResizable(false);
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 434, 527);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("WELLCOME");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblNewLabel_1.setBounds(135, 194, 196, 50);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel = new JLabel("CELLCOMPSTORE");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 38));
        lblNewLabel.setBounds(10, 46, 367, 68);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon("pic/logo.png"));
        lblNewLabel_3.setBounds(354, 59, 56, 50);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setIcon(new ImageIcon("pic/login3.jpg"));
        lblNewLabel_2.setBounds(-146, 0, 587, 184);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_4 = new JLabel("UserName:");
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_4.setBounds(10, 263, 115, 34);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_4_1 = new JLabel("Password:");
        lblNewLabel_4_1.setForeground(Color.WHITE);
        lblNewLabel_4_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_4_1.setBounds(10, 326, 115, 34);
        contentPane.add(lblNewLabel_4_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField.setColumns(10);
        textField.setBounds(126, 262, 233, 35);
        contentPane.add(textField);

        textField_1 = new JPasswordField();
        textField_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_1.setColumns(10);
        textField_1.setBounds(126, 327, 233, 35);
        contentPane.add(textField_1);

        btnNewButton = new JButton("Đăng nhập");
        btnNewButton.setBackground(new Color(204, 102, 255));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnNewButton.setBounds(147, 415, 149, 34);
        btnNewButton.setFocusPainted(false);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Bạn đã có tài khoản chưa? Đăng ký ở đây!");
        btnNewButton_1.setBackground(new Color(204, 204, 255));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnNewButton_1.setBounds(85, 459, 325, 21);
        contentPane.add(btnNewButton_1);

        JButton btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setBackground(new Color(204, 204, 255));
        btnForgotPassword.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnForgotPassword.setBounds(210, 372, 149, 21);
        contentPane.add(btnForgotPassword);

        JLabel lblNewLabel_5 = new JLabel("");
        lblNewLabel_5.setIcon(new ImageIcon("pic/login2.jpg"));
        lblNewLabel_5.setBounds(0, 184, 420, 306);
        contentPane.add(lblNewLabel_5);

        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Register dangKyFrame = new Register();
                dangKyFrame.setVisible(true);
                dispose();
            }
        });

//        btnForgotPassword.addActionListener(e -> {
//            ForgotPassword forgotFrame = new ForgotPassword();
//            forgotFrame.setVisible(true);
//            dispose();
//        });

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

    public void navigateToBuy(int customerID) {
        Buy view = new Buy(customerID);
        view.setVisible(true);
        dispose();
    }
}