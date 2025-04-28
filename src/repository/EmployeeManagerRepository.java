package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.QLdatabase;
// import java.awt.Component; // Không dùng trong repository
// import javax.swing.JOptionPane; // Không dùng trong repository

public class EmployeeManagerRepository {


    public boolean addEmployeeManager(String position, double salary, String name, String phone, String sex) throws SQLException {
        String sql = "INSERT INTO Employees (position, salary, employeesName, employeesPhone, employeesSex) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }

            ps.setString(1, position);
            ps.setDouble(2, salary);
            ps.setString(3, name);
            ps.setString(4, phone);
            ps.setString(5, sex);

            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException ex) {
            // JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhân viên."); // Tầng Service xử lý
            System.err.println("Lỗi SQL khi thêm nhân viên (Manager): " + ex.getMessage());
            throw ex;
        } catch (Exception e) {
             System.err.println("Lỗi không xác định khi thêm nhân viên (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
    }

    public boolean deleteEmployeeByIDManager(int id) throws SQLException {
        // Việc xác nhận (JOptionPane.showConfirmDialog) nên được thực hiện ở tầng View/Controller
        String sql = "DELETE FROM Employees WHERE employeesID = ?";
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

             if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            // Thông báo thành công/thất bại nên được xử lý ở tầng gọi (Service/Controller)
            // if (rowsAffected > 0) {
            //     JOptionPane.showMessageDialog(parentComponent, "Xoá nhân viên thành công.");
            //     onDeleted.run(); // cập nhật lại bảng
            // } else {
            //     JOptionPane.showMessageDialog(parentComponent, "Không tìm thấy nhân viên để xoá.");
            // }
            return rowsAffected > 0;
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(parentComponent, "Lỗi khi xoá nhân viên."); // Tầng Service xử lý
            System.err.println("Lỗi SQL khi xóa nhân viên (Manager): " + e.getMessage());
            throw e;
        } catch (Exception e) {
             System.err.println("Lỗi không xác định khi xóa nhân viên (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
    }

   
    public boolean updateEmployeeManager(String position, double salary, String name, String phone, String sex, int id) throws SQLException {
        // Đã kiểm tra lại thứ tự tham số cho phù hợp với code gốc bạn cung cấp
        String sql = "UPDATE Employees SET position = ?, salary = ?, employeesName = ?, employeesPhone = ?, employeesSex = ? WHERE employeesID = ?";
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

             if (conn == null) {
                 throw new SQLException("Không thể kết nối đến database!");
            }

            ps.setString(1, position); // Sửa lại thứ tự cho đúng câu SQL
            ps.setDouble(2, salary);
            ps.setString(3, name);
            ps.setString(4, phone);
            ps.setString(5, sex);
            ps.setInt(6, id);

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật nhân viên (Manager): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi cập nhật nhân viên (Manager): " + e.getMessage());
            throw new SQLException("Lỗi không xác định: " + e.getMessage(), e);
        }
    }
}
