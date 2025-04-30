package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowPersonalCode extends JFrame {
    private String email;
    private String personalCode;

    public ShowPersonalCode(String email, String personalCode) {
        this.email = email;
        this.personalCode = personalCode;

        setTitle("Mã cá nhân");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 401, 205);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("MÃ CÁ NHÂN CỦA BẠN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(78, 10, 246, 30);
        contentPane.add(lblTitle);

        JLabel lblCode = new JLabel("Mã: " + personalCode);
        lblCode.setForeground(Color.WHITE);
        lblCode.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCode.setBounds(111, 50, 169, 30);
        contentPane.add(lblCode);

        JButton btnCopy = new JButton("Sao chép");
        btnCopy.setForeground(Color.WHITE);
        btnCopy.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnCopy.setBackground(new Color(153, 102, 204));
        btnCopy.setBounds(141, 90, 100, 30);
        btnCopy.setFocusPainted(false);
        btnCopy.setBorderPainted(false);
        btnCopy.setContentAreaFilled(true);
        btnCopy.setOpaque(true);
        contentPane.add(btnCopy);

        JButton btnChangePassword = new JButton("Đi đến đổi mật khẩu");
        btnChangePassword.setBackground(new Color(153, 0, 204));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnChangePassword.setBounds(217, 140, 160, 21);
        contentPane.add(btnChangePassword);

        JLabel lblBackground = new JLabel("");
        ImageIcon originalIcon = new ImageIcon("pic/login2.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(400, 205, Image.SCALE_SMOOTH);
        lblBackground.setIcon(new ImageIcon(scaledImage));
        lblBackground.setBounds(0, 0, 400, 205);
        contentPane.add(lblBackground);
        
        JButton btnQuayLai = new JButton("Quay lại đăng nhập");
        btnQuayLai.setBounds(10, 140, 160, 21);
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBackground(new Color(153, 102, 204));
        btnQuayLai.setFont(new Font("Tahoma", Font.BOLD, 10));
        contentPane.add(btnQuayLai);
        contentPane.setComponentZOrder(btnQuayLai, 0);

        btnQuayLai.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Nút Quay lại được nhấn!");
                dispose();
                EventQueue.invokeLater(() -> {
                    try {
                        Login loginFrame = new Login();
                        loginFrame.setVisible(true);
                        System.out.println("Màn hình Login đã mở.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi quay lại màn hình đăng nhập: " + ex.getMessage());
                    }
                });
            }
        });

        btnQuayLai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnQuayLai.setBackground(new Color(153, 102, 204).brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnQuayLai.setBackground(new Color(153, 102, 204));
            }
        });

        btnCopy.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(personalCode), null);
            JOptionPane.showMessageDialog(this, "Đã sao chép mã!");
        });

        btnChangePassword.addActionListener(e -> {
            ChangePassword changeFrame = new ChangePassword(email, personalCode);
            changeFrame.setVisible(true);
            dispose();
        });
    }
}