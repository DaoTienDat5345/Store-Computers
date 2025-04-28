package service;

import repository.ShiftManagerRepository;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ShiftManagerService {

    private final ShiftManagerRepository shiftManagerRepository;

    public ShiftManagerService() {
        this.shiftManagerRepository = new ShiftManagerRepository();
    }

    public List<Object[]> getEmployeesForShift(String shiftName) {
        try {
            if (shiftName == null || shiftName.trim().isEmpty()) {
                System.err.println("Service Warning: Tên ca làm việc không hợp lệ.");
                return Collections.emptyList();
            }
            return shiftManagerRepository.getEmployeesByShiftManager(shiftName);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi tải nhân viên theo ca - " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Thêm nhân viên vào ca làm.
     * @return String thông báo lỗi nếu có, null nếu thành công.
     */
    public String addEmployeeToShift(String employeeID, String shiftName, String shiftDay) {
        try {
            if (employeeID == null || employeeID.trim().isEmpty()) {
                return "Mã nhân viên không được để trống.";
            }
            boolean success = shiftManagerRepository.addEmployeeToShiftManager(employeeID, shiftName, shiftDay);
            return success ? null : "Không thể thêm nhân viên vào ca."; // Lỗi chung nếu không phải exception cụ thể
        } catch (ShiftManagerRepository.ShiftNotFoundException e) {
            return e.getMessage();
        } catch (ShiftManagerRepository.ShiftFullException e) {
            return e.getMessage();
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi SQL khi thêm nhân viên vào ca - " + e.getMessage());
            return "Lỗi cơ sở dữ liệu khi thêm nhân viên vào ca.";
        }
    }

     /**
     * Xóa nhân viên khỏi lịch làm việc.
     * @return true nếu thành công, false nếu thất bại.
     */
    public boolean deleteEmployeeFromShift(String employeeID) {
         try {
             if (employeeID == null || employeeID.trim().isEmpty()) {
                 System.err.println("Service Warning: Mã nhân viên không hợp lệ để xóa khỏi lịch.");
                 return false;
             }
            return shiftManagerRepository.deleteEmployeeFromScheduleManager(employeeID);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi xóa nhân viên khỏi lịch - " + e.getMessage());
            return false;
        }
    }
}