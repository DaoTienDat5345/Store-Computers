package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import service.LoginService;
import java.sql.SQLException;

public class ChangePassword extends JFrame {
    private String email;
    private String personalCode;
    private JTextField textFieldCode;
    private JPasswordField textFieldNewPassword;
    private JPasswordField textFieldConfirmPassword;

    public ChangePassword(String email, String personalCode) {
        this.email = email;
        this.personalCode = personalCode;

        setTitle("Đổi mật khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 505, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setForeground(Color.RED);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(158, 10, 200, 30);
        contentPane.add(lblTitle);

        JLabel lblCode = new JLabel("Mã cá nhân:");
        lblCode.setForeground(Color.WHITE);
        lblCode.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCode.setBounds(10, 60, 100, 25);
        contentPane.add(lblCode);

        textFieldCode = new JTextField();
        textFieldCode.setBounds(120, 62, 250, 25);
        contentPane.add(textFieldCode);

        JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
        lblNewPassword.setForeground(Color.WHITE);
        lblNewPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewPassword.setBounds(10, 100, 118, 25);
        contentPane.add(lblNewPassword);

        textFieldNewPassword = new JPasswordField();
        textFieldNewPassword.setBounds(121, 102, 250, 25);
        contentPane.add(textFieldNewPassword);

        JLabel lblConfirmPassword = new JLabel("Xác nhận:");
        lblConfirmPassword.setForeground(Color.WHITE);
        lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblConfirmPassword.setBounds(10, 140, 100, 25);
        contentPane.add(lblConfirmPassword);

        textFieldConfirmPassword = new JPasswordField();
        textFieldConfirmPassword.setBounds(120, 142, 250, 25);
        contentPane.add(textFieldConfirmPassword);

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.setForeground(Color.RED);
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnSubmit.setBackground(new Color(153, 102, 255));
        btnSubmit.setBounds(158, 177, 182, 36);
        contentPane.add(btnSubmit);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("pic/ChangePassword.jpg"));
        lblBackground.setBounds(-153, -46, 691, 375);
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
                        ShowPersonalCode showPersonalCodeFrame = new ShowPersonalCode(email, personalCode);
                        showPersonalCodeFrame.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi quay lại màn hình mã cá nhân: " + ex.getMessage());
                    }
                });
            }
        });
        btnTroVe.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        btnTroVe.setBounds(0, 0, 48, 35);
        contentPane.add(btnTroVe);

        btnSubmit.addActionListener(e -> handleChangePassword());
    }

    private void handleChangePassword() {
        String code = textFieldCode.getText().trim();
        String newPassword = new String(textFieldNewPassword.getPassword()).trim();
        String confirmPassword = new String(textFieldConfirmPassword.getPassword()).trim();

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã cá nhân!");
            return;
        }
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập mật khẩu mới!");
            return;
        }
        if (confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập xác nhận mật khẩu!");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
            return;
        }

        LoginService service = new LoginService();
        try {
            boolean success = service.changePassword(email, code, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Mã cá nhân không đúng!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}