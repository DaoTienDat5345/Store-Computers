package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; // Thêm import
import java.awt.*;
import java.util.List;
import controller.ShiftManagerController;

public class ViewShiftPanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton btnMorning, btnAfternoon, btnEvening, btnAdd, btnDelete;
    private JTextField txtEmployeeID;
    private JComboBox<String> cbShift;
    private JComboBox<String> cbDay;

    public ViewShiftPanel() {
        setLayout(new BorderLayout(0, 5)); 
        setBackground(new Color(248, 249, 250)); 

        JPanel tableAndShiftButtonsPanel = new JPanel(new BorderLayout());
        tableAndShiftButtonsPanel.setOpaque(false);

        JPanel shiftButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        shiftButtonPanel.setOpaque(false);
        btnMorning = new JButton("Sáng");
        btnAfternoon = new JButton("Chiều");
        btnEvening = new JButton("Tối");
       
        Font shiftButtonFont = new Font("Segoe UI", Font.PLAIN, 12);
        btnMorning.setFont(shiftButtonFont); btnAfternoon.setFont(shiftButtonFont); btnEvening.setFont(shiftButtonFont);

        shiftButtonPanel.add(btnMorning);
        shiftButtonPanel.add(btnAfternoon);
        shiftButtonPanel.add(btnEvening);
        tableAndShiftButtonsPanel.add(shiftButtonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Tên Nhân Viên", "Ngày Làm"}, 0){
             @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        employeeTable = new JTable(tableModel);

        // --- Áp dụng Style cho Bảng ---
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(28);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        employeeTable.getTableHeader().setBackground(new Color(180, 180, 220));
        employeeTable.getTableHeader().setForeground(Color.BLACK);
        employeeTable.setSelectionBackground(new Color(150, 190, 230));
        employeeTable.setSelectionForeground(Color.BLACK);
        employeeTable.setGridColor(new Color(210, 210, 210));

        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                 if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                 } else {
                     c.setBackground(table.getSelectionBackground());
                 }
                 c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return c;
            }
        };
        employeeTable.setDefaultRenderer(Object.class, defaultRenderer);

        // Điều chỉnh độ rộng cột (tùy chọn)
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(80); 
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120); 

        // --- Hết phần Style Bảng ---

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableAndShiftButtonsPanel.add(scrollPane, BorderLayout.CENTER);

        add(tableAndShiftButtonsPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        formPanel.setBackground(new Color(240, 242, 245)); 
        formPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0)); 

        formPanel.add(new JLabel("Mã NV:"));
        txtEmployeeID = new JTextField(8); 
        formPanel.add(txtEmployeeID);

        formPanel.add(new JLabel("Ca Làm:"));
        cbShift = new JComboBox<>(new String[]{"Sáng", "Chiều", "Tối"});
        formPanel.add(cbShift);

        formPanel.add(new JLabel("Thứ Làm Việc:"));
        cbDay = new JComboBox<>(new String[]{"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"});
        formPanel.add(cbDay);

        btnAdd = new JButton("Thêm vào ca");
        btnDelete = new JButton("Xóa khỏi lịch");
        
        Font actionButtonFont = new Font("Segoe UI", Font.PLAIN, 12);
        btnAdd.setFont(actionButtonFont); btnDelete.setFont(actionButtonFont);

        formPanel.add(btnAdd);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH);

        new ShiftManagerController(this);
    }

    public void displayShiftEmployees(List<Object[]> employeeData) {
        tableModel.setRowCount(0);
        if (employeeData != null) {
            for (Object[] row : employeeData) {
                tableModel.addRow(row);
            }
        }
    }

    public void highlightShiftButton(JButton selectedButton) {
        Color defaultBG = UIManager.getColor("Button.background"); 
        Color selectedBG = Color.YELLOW; 

       
        btnMorning.setBackground(defaultBG);
        btnAfternoon.setBackground(defaultBG);
        btnEvening.setBackground(defaultBG);

        
        if (selectedButton != null) {
            selectedButton.setBackground(selectedBG);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearEmployeeIdInput() {
        txtEmployeeID.setText("");
    }

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