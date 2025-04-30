package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import controller.EmployeeManagerController;
import utils.ExcelExporter; 

public class ViewEmployeesPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton buttonAdd, buttonDelete, buttonEdit, btnExportExcel; 
    private NumberFormat currencyFormat;

    public ViewEmployeesPanel() {
        setLayout(new BorderLayout(0, 5));
        setBackground(new Color(248, 249, 250));

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Họ Tên", "Lương", "Vị trí", "SĐT", "Giới tính"}, 0){
             @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        employeeTable = new JTable(tableModel);

        
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(28);
        employeeTable.getColumnModel().getColumn(2).setCellRenderer(createSalaryRenderer()); 


        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel paneButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        paneButtons.setBackground(new Color(240, 242, 245));
        buttonAdd = new JButton("Thêm NV");
        buttonDelete = new JButton("Xoá NV");
        buttonEdit = new JButton("Sửa NV");
        btnExportExcel = new JButton("Xuất Excel"); 

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);
        buttonAdd.setFont(buttonFont);
        buttonEdit.setFont(buttonFont);
        buttonDelete.setFont(buttonFont);
        btnExportExcel.setFont(buttonFont); 

        paneButtons.add(buttonAdd);
        paneButtons.add(buttonEdit); 
        paneButtons.add(buttonDelete);
        paneButtons.add(btnExportExcel); 
        add(paneButtons, BorderLayout.SOUTH);

        
        btnExportExcel.addActionListener(e -> {
            ExcelExporter.exportTableToExcel(employeeTable, this, "DanhSachNhanVien.xlsx");
        });

        new EmployeeManagerController(this);
    }

    private DefaultTableCellRenderer createSalaryRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                 if (!isSelected) {
                     c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                 } else {
                     c.setBackground(table.getSelectionBackground());
                 }
                 c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return c;
            }
        };
    }

     public void displayEmployees(List<Object[]> employeeData) {
        tableModel.setRowCount(0);
        if (employeeData != null) {
            for (Object[] row : employeeData) {
                tableModel.addRow(row);
            }
        }
    }
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }
    public JTable getEmployeeTable() { return employeeTable; }
    public DefaultTableModel getEmployeeTableModel() { return tableModel; }
    public JButton getButtonAdd() { return buttonAdd; }
    public JButton getButtonDelete() { return buttonDelete; }
    public JButton getButtonEdit() { return buttonEdit; }
    public JButton getBtnExportExcel() { return btnExportExcel; } 
}