package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import service.LoginService;
import java.sql.SQLException;

public class ForgotPassword extends JFrame {
    private JTextField textFieldEmail;
    private JTextField textFieldPhone;

    public ForgotPassword() {
        setTitle("Quên mật khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 505, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("LẤY LẠI MẬT KHẨU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(162, 10, 200, 30);
        contentPane.add(lblTitle);

        JLabel lblEmail = new JLabel("Gmail:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmail.setBounds(10, 60, 100, 25);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(131, 62, 250, 25);
        contentPane.add(textFieldEmail);

        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPhone.setBounds(10, 100, 111, 25);
        contentPane.add(lblPhone);

        textFieldPhone = new JTextField();
        textFieldPhone.setBounds(131, 102, 250, 25);
        contentPane.add(textFieldPhone);

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.setBounds(162, 150, 156, 30);
        contentPane.add(btnSubmit);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("pic/Forgotpassword.jpg"));
        lblBackground.setBounds(-351, 0, 887, 290);
        contentPane.add(lblBackground);
        
        JButton btnTroVe = new JButton("");
        btnTroVe.setIcon(new ImageIcon("pic/back.png"));
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setBackground(new Color(153, 102, 204));
        btnTroVe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                EventQueue.invokeLater(() -> {
                    try {
                        Login loginFrame = new Login();
                        loginFrame.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
        btnTroVe.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        btnTroVe.setBounds(0, 0, 48, 35);
        contentPane.add(btnTroVe);

        btnSubmit.addActionListener(e -> handleSubmit());
    }

    private void handleSubmit() {
        String email = textFieldEmail.getText().trim();
        String phone = textFieldPhone.getText().trim();

        if (email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        LoginService service = new LoginService();
        try {
            String personalCode = service.verifyForgotPassword(email, phone);
            if (personalCode != null) {
                ShowPersonalCode showCodeFrame = new ShowPersonalCode(email, personalCode);
                showCodeFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gmail hoặc số điện thoại không đúng!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}