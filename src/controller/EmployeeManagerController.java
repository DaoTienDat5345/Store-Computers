package controller;

import view.ViewEmployeesPanel; // Panel chính
import view.ViewAddEmpForManager;
import view.ViewEditEmpForManager;
import service.EmployeeManagerService;

import javax.swing.*;
import java.util.List;

public class EmployeeManagerController {

    private final ViewEmployeesPanel view;
    private final EmployeeManagerService service;

    public EmployeeManagerController(ViewEmployeesPanel view) {
        this.view = view;
        this.service = new EmployeeManagerService();
        addActionListeners();
        loadEmployeeData();
    }

    private void addActionListeners() {
        view.getButtonAdd().addActionListener(e -> showAddEmployeeDialog());
        view.getButtonEdit().addActionListener(e -> showEditEmployeeDialog());
        view.getButtonDelete().addActionListener(e -> deleteEmployee());
    }

    public void loadEmployeeData() {
        List<Object[]> employees = service.getAllEmployees();
        view.displayEmployees(employees); // View cần có phương thức này
    }

    private void showAddEmployeeDialog() {
        ViewAddEmpForManager addDialog = new ViewAddEmpForManager(this::loadEmployeeData);
        addDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addDialog.setVisible(true);
    }

    private void showEditEmployeeDialog() {
        int selectedRow = view.getEmployeeTable().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Vui lòng chọn một nhân viên để sửa.");
            return;
        }

        // Lấy dữ liệu từ bảng của View
        int id = (int) view.getEmployeeTableModel().getValueAt(selectedRow, 0);
        String name = view.getEmployeeTableModel().getValueAt(selectedRow, 1).toString();
        double salary = (double) view.getEmployeeTableModel().getValueAt(selectedRow, 2);
        String position = view.getEmployeeTableModel().getValueAt(selectedRow, 3).toString();
        String phone = view.getEmployeeTableModel().getValueAt(selectedRow, 4).toString();
        String sex = view.getEmployeeTableModel().getValueAt(selectedRow, 5).toString();

        ViewEditEmpForManager editDialog = new ViewEditEmpForManager(id, position, salary, name, phone, sex, this::loadEmployeeData);
        editDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editDialog.setVisible(true);
    }

    private void deleteEmployee() {
        int selectedRow = view.getEmployeeTable().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Vui lòng chọn một nhân viên để xóa.");
            return;
        }
        int employeeId = (int) view.getEmployeeTableModel().getValueAt(selectedRow, 0);
        String employeeName = view.getEmployeeTableModel().getValueAt(selectedRow, 1).toString();


        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa nhân viên: " + employeeName + " (ID: " + employeeId + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = service.deleteEmployee(employeeId);
            if (success) {
                view.showMessage("Xóa nhân viên thành công!");
                loadEmployeeData(); // Tải lại
            } else {
                view.showMessage("Xóa nhân viên thất bại.");
            }
        }
    }
}