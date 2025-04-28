package controller;

import view.ViewShiftPanel; // Panel chính
import service.ShiftManagerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShiftManagerController {

    private final ViewShiftPanel view;
    private final ShiftManagerService service;
    private String currentShift = "Sáng"; // Giữ ca hiện tại đang xem

    public ShiftManagerController(ViewShiftPanel view) {
        this.view = view;
        this.service = new ShiftManagerService();
        addActionListeners();
        loadShiftData(currentShift, view.getBtnMorning()); // Tải ca sáng mặc định
    }

    private void addActionListeners() {
        // Listener cho các nút chọn ca
        ActionListener shiftButtonListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            currentShift = clickedButton.getText(); // Lấy tên ca từ text của nút
            loadShiftData(currentShift, clickedButton);
        };
        view.getBtnMorning().addActionListener(shiftButtonListener);
        view.getBtnAfternoon().addActionListener(shiftButtonListener);
        view.getBtnEvening().addActionListener(shiftButtonListener);

        // Listener cho nút thêm nhân viên vào ca
        view.getBtnAdd().addActionListener(e -> addEmployeeToShift());

        // Listener cho nút xóa nhân viên khỏi ca
        view.getBtnDelete().addActionListener(e -> deleteEmployeeFromShift());
    }

    private void loadShiftData(String shiftName, JButton clickedButton) {
        List<Object[]> shiftEmployees = service.getEmployeesForShift(shiftName);
        view.displayShiftEmployees(shiftEmployees); // View cần có phương thức này
        view.highlightShiftButton(clickedButton); // View cần có phương thức này
    }

    private void addEmployeeToShift() {
        String employeeId = view.getEmployeeIdInput(); // View cần getter này
        String shiftName = view.getSelectedShift(); // View cần getter này
        String shiftDay = view.getSelectedDay();   // View cần getter này

        String errorMessage = service.addEmployeeToShift(employeeId, shiftName, shiftDay);

        if (errorMessage == null) {
            view.showMessage("Thêm nhân viên vào ca thành công!");
            loadShiftData(currentShift, findButtonByText(currentShift)); // Tải lại ca hiện tại
            view.clearEmployeeIdInput(); // Xóa input sau khi thêm
        } else {
            view.showMessage("Lỗi: " + errorMessage);
        }
    }

    private void deleteEmployeeFromShift() {
        int selectedRow = view.getEmployeeTable().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Vui lòng chọn một nhân viên để xóa khỏi lịch.");
            return;
        }
        String employeeId = view.getEmployeeTableModel().getValueAt(selectedRow, 0).toString();
        String employeeName = view.getEmployeeTableModel().getValueAt(selectedRow, 1).toString();


        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa " + employeeName + " (ID: " + employeeId + ") khỏi lịch làm việc?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = service.deleteEmployeeFromShift(employeeId);
            if (success) {
                view.showMessage("Xóa nhân viên khỏi lịch thành công!");
                loadShiftData(currentShift, findButtonByText(currentShift)); // Tải lại ca hiện tại
            } else {
                view.showMessage("Xóa nhân viên khỏi lịch thất bại.");
            }
        }
    }

    // Tiện ích tìm nút theo text (để highlight đúng nút khi reload)
    private JButton findButtonByText(String text) {
        if ("Sáng".equals(text)) return view.getBtnMorning();
        if ("Chiều".equals(text)) return view.getBtnAfternoon();
        if ("Tối".equals(text)) return view.getBtnEvening();
        return null;
    }
}