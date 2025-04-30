package view;

import javax.swing.*;
import java.awt.*;
// import backend.EditOnManager; // Không gọi trực tiếp backend
import service.EmployeeManagerService; // Gọi Service

public class ViewEditEmpForManager extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtName, txtPosition, txtSalary, txtPhone, txtSex;
    // private JButton buttonUpdate, buttonCancel; // Sửa tên biến
    private EmployeeManagerService employeeService; // Service để cập nhật
    private int employeeId; // Lưu ID nhân viên cần sửa
    private Runnable onUpdated; // Callback sau khi cập nhật thành công

    public ViewEditEmpForManager(int id, String position, double salary, String name, String phone, String sex, Runnable onUpdated) {
        this.employeeId = id;
        this.onUpdated = onUpdated;
        this.employeeService = new EmployeeManagerService(); // Khởi tạo Service

        setTitle("Sửa thông tin nhân viên (ID: " + id + ")");
        setSize(400, 300); // Giảm kích thước một chút
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Quan trọng
        setLayout(new GridLayout(6, 2, 10, 10)); // 6 hàng là đủ

        // Tạo và gán giá trị ban đầu
        txtPosition = new JTextField(position);
        txtSalary = new JTextField(String.valueOf(salary));
        txtName = new JTextField(name);
        txtPhone = new JTextField(phone);
        txtSex = new JTextField(sex);

        // Thêm label và textfield
        add(new JLabel("Tên:")); add(txtName);
        add(new JLabel("Chức vụ:")); add(txtPosition);
        add(new JLabel("Lương:")); add(txtSalary);
        add(new JLabel("SĐT:")); add(txtPhone);
        add(new JLabel("Giới tính:")); add(txtSex);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");

        add(btnUpdate);
        add(btnCancel);

        btnUpdate.addActionListener(e -> updateEmployee());
        btnCancel.addActionListener(e -> dispose());
    }

    private void updateEmployee() {
        // Lấy dữ liệu mới từ form
        String newName = txtName.getText().trim();
        String newPos = txtPosition.getText().trim();
        String newSalStr = txtSalary.getText().trim();
        String newPhone = txtPhone.getText().trim();
        String newSex = txtSex.getText().trim();

        // Kiểm tra đầu vào
        if (newName.isEmpty() || newPos.isEmpty() || newSalStr.isEmpty() || newPhone.isEmpty() || newSex.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }

        try {
            double newSal = Double.parseDouble(newSalStr);
             if (newSal < 0) {
                 JOptionPane.showMessageDialog(this, "Lương không được âm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
             }

            // Gọi service để cập nhật
            boolean success = employeeService.updateEmployee(newPos, newSal, newName, newPhone, newSex, employeeId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (onUpdated != null) {
                    onUpdated.run(); // Gọi callback
                }
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật nhân viên. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Lương phải là một số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt lỗi chung khác
             JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }
}