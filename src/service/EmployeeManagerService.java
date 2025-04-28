package service;

import repository.EmployeeManagerRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import database.QLdatabase; // Cần để lấy dữ liệu getAllEmployees
import java.sql.Connection; // Cần để lấy dữ liệu getAllEmployees
import java.sql.PreparedStatement; // Cần để lấy dữ liệu getAllEmployees
import java.sql.ResultSet; // Cần để lấy dữ liệu getAllEmployees


public class EmployeeManagerService {

    private final EmployeeManagerRepository employeeManagerRepository;

    public EmployeeManagerService() {
        this.employeeManagerRepository = new EmployeeManagerRepository();
    }

     // Cần thêm phương thức để lấy danh sách nhân viên
    public List<Object[]> getAllEmployees() {
        List<Object[]> employeeList = new ArrayList<>();
        String sql = "SELECT employeesID, employeesName, salary, position, employeesPhone, employeesSex FROM Employees";
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (conn == null) {
                System.err.println("Service Error: Không thể kết nối CSDL để lấy danh sách nhân viên.");
                return Collections.emptyList();
            }

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("employeesID"),
                    rs.getString("employeesName"),
                    rs.getDouble("salary"),
                    rs.getString("position"),
                    rs.getString("employeesPhone"),
                    rs.getString("employeesSex")
                };
                employeeList.add(row);
            }
        } catch (SQLException e) {
             System.err.println("Service Error: Lỗi SQL khi tải danh sách nhân viên - " + e.getMessage());
             return Collections.emptyList();
        } catch (Exception e) {
             System.err.println("Service Error: Lỗi không xác định khi tải danh sách nhân viên - " + e.getMessage());
             return Collections.emptyList();
        }
        return employeeList;
    }


    public boolean addEmployee(String position, double salary, String name, String phone, String sex) {
        try {
            // Logic kiểm tra đầu vào (ví dụ: định dạng sđt, lương > 0)
            if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
                 System.err.println("Service Warning: Tên hoặc SĐT không hợp lệ.");
                 return false;
            }
            return employeeManagerRepository.addEmployeeManager(position, salary, name, phone, sex);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi thêm nhân viên - " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(String position, double salary, String name, String phone, String sex, int id) {
        try {
             // Logic kiểm tra đầu vào
             if (id <= 0) {
                  System.err.println("Service Warning: ID nhân viên không hợp lệ.");
                  return false;
             }
            return employeeManagerRepository.updateEmployeeManager(position, salary, name, phone, sex, id);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi cập nhật nhân viên - " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int id) {
        try {
             if (id <= 0) {
                  System.err.println("Service Warning: ID nhân viên không hợp lệ.");
                  return false;
             }
            // Có thể kiểm tra nghiệp vụ trước khi xóa (ví dụ: còn trong lịch làm việc không?)
            return employeeManagerRepository.deleteEmployeeByIDManager(id);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi xóa nhân viên - " + e.getMessage());
            return false;
        }
    }
}