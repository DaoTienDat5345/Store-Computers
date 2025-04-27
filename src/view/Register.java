package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Address;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import controller.LoginController;
import service.LoginService;

public class Register extends JFrame {
    private JComboBox<String> comboBoxDay;
    private JComboBox<String> comboBoxMonth;
    private JComboBox<String> comboBoxYear;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JComboBox<String> comboBox;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JTextField textField_2;
    private JButton btnNewButton;
    private JLabel lblNewLabel_11;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textFieldGmail;
    private JLabel lblNewLabel_5;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Register frame = new Register();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Register() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 763, 551);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("THÊM TÀI KHOẢNG");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblNewLabel.setBounds(245, 10, 248, 42);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Nhập tên:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1.setBounds(10, 56, 104, 35);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField.setBounds(166, 62, 248, 29);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("Số điện thoại:");
        lblNewLabel_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1_1.setBounds(10, 113, 146, 35);
        contentPane.add(lblNewLabel_1_1);

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_1.setBounds(166, 116, 248, 29);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblNewLabel_1_2 = new JLabel("Địa chỉ:");
        lblNewLabel_1_2.setForeground(Color.WHITE);
        lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1_2.setBounds(447, 62, 82, 35);
        contentPane.add(lblNewLabel_1_2);

        comboBox = new JComboBox<>();
        comboBox.setBackground(new Color(153, 102, 204));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Tahoma", Font.BOLD, 15));
        comboBox.setBounds(539, 64, 200, 35);
        contentPane.add(comboBox);

        lblNewLabel_2 = new JLabel("Nhập tên đăng ký:");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_2.setBounds(58, 235, 190, 35);
        contentPane.add(lblNewLabel_2);

        lblNewLabel_3 = new JLabel("Nhập mật khẩu:");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_3.setBounds(56, 301, 160, 35);
        contentPane.add(lblNewLabel_3);

        textField_2 = new JTextField();
        textField_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_2.setColumns(10);
        textField_2.setBounds(276, 236, 281, 35);
        contentPane.add(textField_2);

        btnNewButton = new JButton("Đăng ký");
        btnNewButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(204, 204, 255));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        btnNewButton.setBounds(315, 457, 190, 35);
        btnNewButton.setFocusPainted(false);
        contentPane.add(btnNewButton);

        lblNewLabel_11 = new JLabel("Xác nhận mật khẩu:");
        lblNewLabel_11.setForeground(Color.WHITE);
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_11.setBounds(56, 363, 216, 35);
        contentPane.add(lblNewLabel_11);

        textField_3 = new JPasswordField();
        textField_3.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_3.setBounds(276, 302, 281, 35);
        contentPane.add(textField_3);

        textField_4 = new JPasswordField();
        textField_4.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textField_4.setBounds(276, 364, 281, 35);
        contentPane.add(textField_4);

        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setForeground(Color.WHITE);
        lblGmail.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblGmail.setBounds(447, 113, 97, 35);
        contentPane.add(lblGmail);

        textFieldGmail = new JTextField();
        textFieldGmail.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textFieldGmail.setBounds(539, 116, 200, 29);
        contentPane.add(textFieldGmail);
        textFieldGmail.setColumns(10);

        JLabel lblDateOfBirth = new JLabel("Ngày sinh:");
        lblDateOfBirth.setForeground(Color.WHITE);
        lblDateOfBirth.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblDateOfBirth.setBounds(10, 170, 146, 35);
        contentPane.add(lblDateOfBirth);

        comboBoxDay = new JComboBox<>();
        comboBoxDay.setForeground(Color.WHITE);
        comboBoxDay.setBackground(new Color(153, 102, 204));
        comboBoxDay.setFont(new Font("Tahoma", Font.BOLD, 15));
        comboBoxDay.setBounds(166, 175, 104, 30);
        contentPane.add(comboBoxDay);

        comboBoxMonth = new JComboBox<>(utils.DateHandler.getMonths());
        comboBoxMonth.setForeground(Color.WHITE);
        comboBoxMonth.setBackground(new Color(153, 102, 204));
        comboBoxMonth.setFont(new Font("Tahoma", Font.BOLD, 15));
        comboBoxMonth.setBounds(276, 174, 110, 30);
        contentPane.add(comboBoxMonth);

        comboBoxYear = new JComboBox<>(utils.DateHandler.getYears());
        comboBoxYear.setForeground(Color.WHITE);
        comboBoxYear.setBackground(new Color(153, 102, 204));
        comboBoxYear.setFont(new Font("Tahoma", Font.BOLD, 15));
        comboBoxYear.setBounds(396, 174, 111, 30);
        contentPane.add(comboBoxYear);

        JRadioButton rdbtnNewRadioButton = new JRadioButton("Nam");
        rdbtnNewRadioButton.setBackground(new Color(153, 102, 204));
        rdbtnNewRadioButton.setForeground(Color.WHITE);
        rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        rdbtnNewRadioButton.setBounds(556, 175, 61, 21);
        contentPane.add(rdbtnNewRadioButton);

        JRadioButton rdbtnN = new JRadioButton("Nữ");
        rdbtnN.setBackground(new Color(153, 102, 204));
        rdbtnN.setForeground(Color.WHITE);
        rdbtnN.setFont(new Font("Tahoma", Font.BOLD, 15));
        rdbtnN.setBounds(630, 175, 61, 21);
        contentPane.add(rdbtnN);

        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnNewRadioButton);
        group.add(rdbtnN);

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

        JLabel lblNewLabel_12 = new JLabel("");
        lblNewLabel_12.setIcon(new ImageIcon("pic/login.jpg"));
        lblNewLabel_12.setBounds(0, 0, 749, 436);
        contentPane.add(lblNewLabel_12);
        
        JLabel lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setIcon(new ImageIcon("pic/Login2.jpg"));
        lblNewLabel_4.setBounds(-12, 429, 398, 96);
        contentPane.add(lblNewLabel_4);
        
        lblNewLabel_5 = new JLabel("");
        lblNewLabel_5.setIcon(new ImageIcon("pic/Login2.jpg"));
        lblNewLabel_5.setBounds(315, 429, 449, 96);
        contentPane.add(lblNewLabel_5);

        ActionListener updateDaysListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDaysComboBox();
            }
        };
        comboBoxMonth.addActionListener(updateDaysListener);
        comboBoxYear.addActionListener(updateDaysListener);

        updateDaysComboBox();

        ArrayList<Address> listTinh = Address.getDSTinh();
        if (listTinh.isEmpty()) {
            System.out.println("Danh sách tỉnh rỗng!");
        } else {
            for (Address add : listTinh) {
                comboBox.addItem(add.getTenTinh());
            }
        }

        LoginService loginService = new LoginService();
        new LoginController(this, loginService);
    }

    private void updateDaysComboBox() {
        int month = Integer.parseInt((String) comboBoxMonth.getSelectedItem());
        int year = Integer.parseInt((String) comboBoxYear.getSelectedItem());
        comboBoxDay.removeAllItems();
        String[] days = utils.DateHandler.getDays(month, year);
        for (String day : days) {
            comboBoxDay.addItem(day);
        }
    }

    public String getFullName() {
        return textField.getText().trim();
    }

    public String getPhone() {
        return textField_1.getText().trim();
    }

    public String getUserAddress() {
        return (String) comboBox.getSelectedItem();
    }

    public String getUserName() {
        return textField_2.getText().trim();
    }

    public String getUserPassword() {
        return new String(((JPasswordField) textField_3).getPassword()).trim();
    }

    public String getConfirmPassword() {
        return new String(((JPasswordField) textField_4).getPassword()).trim();
    }

    public String getGmail() {
        return textFieldGmail.getText().trim();
    }

    public int getDay() {
        return Integer.parseInt((String) comboBoxDay.getSelectedItem());
    }

    public int getMonth() {
        return Integer.parseInt((String) comboBoxMonth.getSelectedItem());
    }

    public int getYear() {
        return Integer.parseInt((String) comboBoxYear.getSelectedItem());
    }

    public String getUserSex() {
        JRadioButton rdbtnNewRadioButton = (JRadioButton) contentPane.getComponentAt(556, 175);
        JRadioButton rdbtnN = (JRadioButton) contentPane.getComponentAt(630, 175);
        if (rdbtnNewRadioButton.isSelected()) {
            return "Nam";
        } else if (rdbtnN.isSelected()) {
            return "Nữ";
        }
        return "";
    }

    public JButton getRegisterButton() {
        return btnNewButton;
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(contentPane, message, title, messageType);
    }

    public void navigateToLogin() {
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
}
