package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.QLdatabase;
// import javax.swing.JOptionPane; // Không dùng
// import javax.swing.JTable; // Không dùng
// import javax.swing.table.DefaultTableModel; // Không dùng
// import java.awt.Color; // Không dùng
// import javax.swing.JButton; // Không dùng

public class ShiftManagerRepository {

    private static final int MAX_EMPLOYEES_PER_SHIFT = 2; // Giới hạn số nhân viên mỗi ca

    /**
     * Thêm một nhân viên vào một ca làm việc cụ thể trong lịch.
     * @param employeeID Mã nhân viên.
     * @param shiftName Tên ca (Sáng, Chiều, Tối).
     * @param shiftDay Ngày làm việc (Thứ 2 -> Chủ Nhật).
     * @return true nếu thêm thành công.
     * @throws SQLException Nếu có lỗi truy vấn CSDL.
     * @throws ShiftFullException Nếu ca làm đã đủ người.
     * @throws ShiftNotFoundException Nếu ca làm không tồn tại.
     */
    public boolean addEmployeeToShiftManager(String employeeID, String shiftName, String shiftDay)
            throws SQLException, ShiftFullException, ShiftNotFoundException {

        try (Connection conn = QLdatabase.getConnection()) {
            if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }

            // Lấy shiftID từ bảng Shifts
            String getShiftIDSQL = "SELECT shiftID FROM Shifts WHERE shiftName = ? AND shiftDay = ?";
            String shiftID = null;
            try (PreparedStatement stmt = conn.prepareStatement(getShiftIDSQL)) {
                stmt.setString(1, shiftName);
                stmt.setString(2, shiftDay);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    shiftID = rs.getString("shiftID");
                }
            }

            if (shiftID == null) {
                // JOptionPane.showMessageDialog(null, "Ca làm không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE); // Service xử lý
                throw new ShiftNotFoundException("Ca làm '" + shiftName + "' ngày '" + shiftDay + "' không tồn tại.");
            }

            if (isShiftFullManager(shiftID)) { // Gọi hàm kiểm tra trong cùng class
                // JOptionPane.showMessageDialog(null, "Ca làm này đã đủ số lượng nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE); // Service xử lý
                throw new ShiftFullException("Ca làm " + shiftID + " đã đủ " + MAX_EMPLOYEES_PER_SHIFT + " nhân viên.");
            }

            // Thêm vào bảng WorkShiftSchedule
            String insertWorkScheduleSQL = "INSERT INTO WorkShiftSchedule (employeesID, shiftID) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertWorkScheduleSQL)) {
                stmt.setString(1, employeeID);
                stmt.setString(2, shiftID);
                int result = stmt.executeUpdate();
          
                return result > 0;
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "Lỗi thêm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE); // Service xử lý
             System.err.println("Lỗi SQL khi thêm nhân viên vào ca (Manager): " + e.getMessage());
            throw e; // Ném lại lỗi
        } catch (Exception e) {
             System.err.println("Lỗi không xác định khi thêm nhân viên vào ca (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
    }

    /**
     * Xóa một nhân viên khỏi lịch làm việc (WorkShiftSchedule).
     * @param employeeID Mã nhân viên cần xóa khỏi lịch.
     * @return true nếu xóa thành công.
     * @throws SQLException Nếu có lỗi truy vấn CSDL.
     */
    public boolean deleteEmployeeFromScheduleManager(String employeeID) throws SQLException {
      
        String deleteWorkScheduleSQL = "DELETE FROM WorkShiftSchedule WHERE employeesID = ?";
        try (Connection conn = QLdatabase.getConnection()) {
             if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteWorkScheduleSQL)) {
                stmt.setString(1, employeeID);
                int result = stmt.executeUpdate();
                // JOptionPane.showMessageDialog(null, "Xóa lịch làm việc thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE); // Service xử lý
                // loadShiftEmployees.loadEmployees(shift, null, tableModel); // Controller gọi lại hàm load
                return result > 0;
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "Lỗi xóa lịch làm việc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE); // Service xử lý
            System.err.println("Lỗi SQL khi xóa nhân viên khỏi lịch (Manager): " + e.getMessage());
            throw e;
        } catch (Exception e) {
             System.err.println("Lỗi không xác định khi xóa nhân viên khỏi lịch (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
    }

    /**
     * Kiểm tra xem một ca làm việc đã đủ số lượng nhân viên tối đa hay chưa.
     * @param shiftID Mã ca làm việc.
     * @return true nếu ca đã đầy, false nếu chưa đầy.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn CSDL.
     */
    public boolean isShiftFullManager(String shiftID) throws SQLException {
        int totalEmployees = 0;
        String countEmployeesSQL = "SELECT COUNT(*) AS totalEmployees FROM WorkShiftSchedule WHERE shiftID = ?";

        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(countEmployeesSQL)) {
             if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }
            stmt.setString(1, shiftID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalEmployees = rs.getInt("totalEmployees");
            }
        } catch (SQLException e) {
             System.err.println("Lỗi SQL khi kiểm tra ca đầy (Manager): " + e.getMessage());
            throw e; // Ném lại lỗi
        } catch (Exception e) {
             System.err.println("Lỗi không xác định khi kiểm tra ca đầy (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
        return totalEmployees >= MAX_EMPLOYEES_PER_SHIFT;
    }

    /**
     * Lấy danh sách nhân viên (ID, Tên, Ngày làm) được phân công cho một ca cụ thể.
     * (Kết hợp logic từ LoadEmpShift và loadShiftEmployees)
     * @param shiftName Tên ca (Sáng, Chiều, Tối).
     * @return Danh sách các mảng Object chứa thông tin nhân viên [ID, Tên, Ngày làm].
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn CSDL.
     */
    public List<Object[]> getEmployeesByShiftManager(String shiftName) throws SQLException {
        List<Object[]> employeeList = new ArrayList<>();
        // Việc highlight button phải ở View/Controller
        // if (selectedButton != null) {
        //     highlightShift(selectedButton, buttons);
        // }

        String sql = "SELECT e.employeesID, e.employeesName, s.shiftDay " +
                     "FROM WorkShiftSchedule w " +
                     "JOIN Employees e ON w.employeesID = e.employeesID " +
                     "JOIN Shifts s ON w.shiftID = s.shiftID " +
                     "WHERE s.shiftName = ?";

        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }
            stmt.setString(1, shiftName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employeeList.add(new Object[]{
                    rs.getString("employeesID"),
                    rs.getString("employeesName"),
                    rs.getString("shiftDay")
                });
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE); // Service xử lý
            System.err.println("Lỗi SQL khi tải nhân viên theo ca (Manager): " + e.getMessage());
            throw e; // Ném lại lỗi
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi tải nhân viên theo ca (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
        return employeeList;
    }

    // --- Các lớp Exception tùy chỉnh để Service bắt và xử lý ---
    public static class ShiftFullException extends Exception {
        public ShiftFullException(String message) {
            super(message);
        }
    }

    public static class ShiftNotFoundException extends Exception {
        public ShiftNotFoundException(String message) {
            super(message);
        }
    }
}