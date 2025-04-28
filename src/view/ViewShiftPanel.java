package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; // Cần để nhận dữ liệu
import controller.ShiftManagerController; // Import Controller

public class ViewShiftPanel extends JPanel { // Đổi thành JPanel
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton btnMorning, btnAfternoon, btnEvening, btnAdd, btnDelete;
    private JTextField txtEmployeeID;
    private JComboBox<String> cbShift;
    private JComboBox<String> cbDay;
    // private ShiftManagerController controller; // Controller sẽ quản lý

    public ViewShiftPanel() {
        setLayout(new BorderLayout());

        // Panel chứa bảng và các nút chọn ca
        JPanel tableAndShiftButtonsPanel = new JPanel(new BorderLayout());

        // Buttons panel (Phía trên bảng)
        JPanel shiftButtonPanel = new JPanel();
        btnMorning = new JButton("Sáng");
        btnAfternoon = new JButton("Chiều");
        btnEvening = new JButton("Tối");
        shiftButtonPanel.add(btnMorning);
        shiftButtonPanel.add(btnAfternoon);
        shiftButtonPanel.add(btnEvening);
        tableAndShiftButtonsPanel.add(shiftButtonPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Tên Nhân Viên", "Ngày Làm"}, 0){
             @Override
            public boolean isCellEditable(int row, int column) {
               return false; // Không cho sửa trực tiếp
            }
        };
        employeeTable = new JTable(tableModel);
        tableAndShiftButtonsPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        add(tableAndShiftButtonsPanel, BorderLayout.CENTER); // Thêm panel này vào CENTER của ViewShiftPanel

        // Form panel for add/delete (Phía dưới bảng)
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Dùng FlowLayout cho đơn giản
        formPanel.add(new JLabel("Mã NV:"));
        txtEmployeeID = new JTextField(10); // Đặt kích thước cột gợi ý
        formPanel.add(txtEmployeeID);

        formPanel.add(new JLabel("Ca Làm:"));
        cbShift = new JComboBox<>(new String[]{"Sáng", "Chiều", "Tối"});
        formPanel.add(cbShift);

        formPanel.add(new JLabel("Thứ Làm Việc:"));
        cbDay = new JComboBox<>(new String[]{"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"});
        formPanel.add(cbDay);

        btnAdd = new JButton("Thêm vào ca");
        btnDelete = new JButton("Xóa khỏi lịch");
        formPanel.add(btnAdd);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH); // Thêm panel form vào SOUTH

        // KHÔNG load default shift hay add listeners ở đây

        // Khởi tạo Controller
        new ShiftManagerController(this);
    }

     // Phương thức để Controller cập nhật bảng
    public void displayShiftEmployees(List<Object[]> employeeData) {
        tableModel.setRowCount(0);
        if (employeeData != null) {
            for (Object[] row : employeeData) {
                tableModel.addRow(row);
            }
        }
    }

    // Phương thức để Controller highlight nút
    public void highlightShiftButton(JButton selectedButton) {
        // Đặt lại màu cho tất cả các nút
        btnMorning.setBackground(UIManager.getColor("Button.background")); // Màu mặc định
        btnAfternoon.setBackground(UIManager.getColor("Button.background"));
        btnEvening.setBackground(UIManager.getColor("Button.background"));

        // Highlight nút được chọn
        if (selectedButton != null) {
            selectedButton.setBackground(Color.YELLOW); // Hoặc màu highlight khác
        }
    }

     // Phương thức để Controller hiển thị thông báo
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Xóa nội dung trường nhập Mã NV
    public void clearEmployeeIdInput() {
        txtEmployeeID.setText("");
    }

    // Getters cho Controller
    public JTable getEmployeeTable() { return employeeTable; }
    public DefaultTableModel getEmployeeTableModel() { return tableModel; }
    public JButton getBtnMorning() { return btnMorning; }
    public JButton getBtnAfternoon() { return btnAfternoon; }
    public JButton getBtnEvening() { return btnEvening; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public String getEmployeeIdInput() { return txtEmployeeID.getText().trim(); }
    public String getSelectedShift() { return (String) cbShift.getSelectedItem(); }
    public String getSelectedDay() { return (String) cbDay.getSelectedItem(); }

}