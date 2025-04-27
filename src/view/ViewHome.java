package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

import controller.HomeController;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

public class ViewHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnCustomer;
    private JButton btnManager;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewHome frame = new ViewHome();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ViewHome() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 571, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnCustomer = new JButton("Customer");
        btnCustomer.setForeground(Color.WHITE);
        btnCustomer.setBackground(new Color(153, 204, 255));
        btnCustomer.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnCustomer.setBounds(10, 172, 137, 36);
        btnCustomer.setFocusPainted(false);
        contentPane.add(btnCustomer);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("pic/customer.png"));
        lblNewLabel.setBounds(10, 47, 137, 115);
        contentPane.add(lblNewLabel);

        btnManager = new JButton("Manager");
        btnManager.setBackground(new Color(153, 204, 255));
        btnManager.setForeground(Color.WHITE);
        btnManager.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnManager.setBounds(410, 172, 137, 36);
        btnManager.setFocusPainted(false);
        contentPane.add(btnManager);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("pic/manager.png"));
        lblNewLabel_1.setBounds(420, 47, 137, 115);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setIcon(new ImageIcon("pic/background2.jpg"));
        lblNewLabel_2.setBounds(0, 0, 557, 274);
        contentPane.add(lblNewLabel_2);

        // Khởi tạo Controller
        new HomeController(this);
    }

    // Getter methods để Controller truy cập các thành phần UI
    public JButton getBtnCustomer() {
        return btnCustomer;
    }

    public JButton getBtnManager() {
        return btnManager;
    }
}