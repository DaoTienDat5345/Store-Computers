package view;

import javax.swing.*;
import java.awt.*;
// import backend.Add_DeleteOnManager; // Không gọi backend
import service.EmployeeManagerService; // Gọi Service

public class ViewAddEmpForManager extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtName, txtPosition, txtSalary, txtPhone, txtSex;
    private JButton buttonAdd, buttonCancel;
    private EmployeeManagerService employeeService; // Service để thêm
    private Runnable onEmployeeAdded; // Callback

    public ViewAddEmpForManager(Runnable onEmployeeAdded) {
        this.onEmployeeAdded = onEmployeeAdded;
        this.employeeService = new EmployeeManagerService(); // Khởi tạo

        setTitle("Thêm nhân viên mới");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Quan trọng
        setLayout(new GridLayout(6, 2, 10, 10)); // 6 hàng

        // Tạo components
        txtName = new JTextField();
        txtPosition = new JTextField();
        txtSalary = new JTextField();
        txtPhone = new JTextField();
        txtSex = new JTextField();

        // Thêm vào frame
        add(new JLabel("Họ tên: ")); add(txtName);
        add(new JLabel("Vị trí: ")); add(txtPosition);
        add(new JLabel("Lương: ")); add(txtSalary);
        add(new JLabel("Số điện thoại: ")); add(txtPhone);
        add(new JLabel("Giới tính (Nam/Nữ): ")); add(txtSex); // Gợi ý input

        buttonAdd = new JButton("Thêm");
        buttonCancel = new JButton("Huỷ");
        add(buttonAdd);
        add(buttonCancel);

        // Add listeners
        buttonAdd.addActionListener(e -> addEmployee());
        buttonCancel.addActionListener(e -> dispose());
    }

     private void addEmployee() {
        // Lấy dữ liệu
        String name = txtName.getText().trim();
        String position = txtPosition.getText().trim();
        String salaryStr = txtSalary.getText().trim();
        String phone = txtPhone.getText().trim();
        String sex = txtSex.getText().trim();

        // Kiểm tra
         if (name.isEmpty() || position.isEmpty() || salaryStr.isEmpty() || phone.isEmpty() || sex.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
         }
         // Kiểm tra giới tính đơn giản
         if (!sex.equalsIgnoreCase("Nam") && !sex.equalsIgnoreCase("Nữ")) {
              JOptionPane.showMessageDialog(this, "Giới tính phải là 'Nam' hoặc 'Nữ'.", "Lỗi", JOptionPane.ERROR_MESSAGE);
              return;
         }


        try {
            double salary = Double.parseDouble(salaryStr);
             if (salary < 0) {
                 JOptionPane.showMessageDialog(this, "Lương không được âm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
             }

            // Gọi service
            boolean success = employeeService.addEmployee(position, salary, name, phone, sex);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (onEmployeeAdded != null) {
                    onEmployeeAdded.run(); // Gọi callback
                }
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên. Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Lương phải là một số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }

}