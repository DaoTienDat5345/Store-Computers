package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; // Cần để nhận dữ liệu
import controller.EmployeeManagerController; // Import Controller

public class ViewEmployeesPanel extends JPanel { // Đổi thành JPanel

    private static final long serialVersionUID = 1L;
    private JTable employeeTable;
    private DefaultTableModel tableModel; // Đổi tên từ table thành tableModel cho rõ ràng
    private JButton buttonAdd, buttonDelete, buttonEdit;
    // private EmployeeManagerController controller; // Controller sẽ quản lý

    public ViewEmployeesPanel() {
        setLayout(new BorderLayout());
        // JPanel panel = new JPanel(new BorderLayout()); // Không cần panel phụ này

        // ID, Họ Tên, Lương, Vị trí, SĐT, giới tính
        tableModel = new DefaultTableModel(new Object[]{"ID", "Họ Tên", "Lương", "Vị trí", "SĐT", "Giới tính"}, 0){
             @Override
            public boolean isCellEditable(int row, int column) {
               return false; // Không cho sửa trực tiếp
            }
        };
        employeeTable = new JTable(tableModel);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel paneButtons = new JPanel(); // FlowLayout mặc định là đủ
        buttonAdd = new JButton("Thêm NV");
        buttonDelete = new JButton("Xoá NV");
        buttonEdit = new JButton("Sửa NV");
        paneButtons.add(buttonAdd);
        paneButtons.add(buttonDelete);
        paneButtons.add(buttonEdit);
        add(paneButtons, BorderLayout.SOUTH);

        // KHÔNG loadEmployees() ở đây
        // KHÔNG addActionListeners ở đây

        // Khởi tạo Controller
        new EmployeeManagerController(this);
    }

    // Phương thức để Controller cập nhật bảng
    public void displayEmployees(List<Object[]> employeeData) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        if (employeeData != null) {
            for (Object[] row : employeeData) {
                tableModel.addRow(row);
            }
        }
    }

    // Phương thức để Controller hiển thị thông báo
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Getters cho Controller
    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public DefaultTableModel getEmployeeTableModel() {
        return tableModel;
    }

    public JButton getButtonAdd() {
        return buttonAdd;
    }

    public JButton getButtonDelete() {
        return buttonDelete;
    }

    public JButton getButtonEdit() {
        return buttonEdit;
    }
}