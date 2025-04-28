package service;

import model.Manager;
import database.QLdatabase; // Cần truy cập DB để lấy thông tin Manager
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerInfoService {

    // Tạm thời chưa cần Repository riêng nếu chỉ lấy thông tin đơn giản
    // private ManagerRepository managerRepository;

    public ManagerInfoService() {
        // this.managerRepository = new ManagerRepository();
    }

    /**
     * Lấy thông tin của một Manager dựa vào ID.
     * Hiện tại chỉ lấy thông tin có sẵn trong model Manager (ID, username, password).
     * Cần mở rộng bảng Manager và Model/Repository nếu muốn lấy thêm thông tin (tên, tuổi, ảnh...).
     * @param managerID ID của Manager.
     * @return Đối tượng Manager hoặc null nếu không tìm thấy hoặc lỗi.
     */
    public Manager getManagerById(int managerID) {
        // Nếu bảng Manager có nhiều thông tin hơn, nên tạo ManagerRepository
        // Tạm thời truy vấn trực tiếp để lấy thông tin cơ bản
        String sql = "SELECT managerID, userManager, userPasswordManager FROM Manager WHERE managerID = ?"; // Giả sử có cột managerID
        Manager manager = null;

        // --- Phần truy vấn lấy thông tin Manager ---
        // Lưu ý: Đoạn code này nên được chuyển vào một Repository (ví dụ: ManagerRepository)
        // nếu cấu trúc cần chặt chẽ hơn hoặc cần lấy nhiều thông tin phức tạp.
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                 System.err.println("Service Error (ManagerInfo): Không thể kết nối CSDL.");
                 return null;
            }

            ps.setInt(1, managerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    manager = new Manager(
                            rs.getInt("managerID"),
                            rs.getString("userManager"),
                            rs.getString("userPasswordManager")
                            // Thêm các trường khác nếu có trong bảng Manager
                    );
                } else {
                     System.out.println("Service Info (ManagerInfo): Không tìm thấy Manager với ID: " + managerID);
                }
            }
        } catch (SQLException e) {
            System.err.println("Service Error (ManagerInfo): Lỗi SQL khi lấy thông tin Manager - " + e.getMessage());
            // e.printStackTrace(); // In chi tiết lỗi để debug
        } catch (Exception e) {
            System.err.println("Service Error (ManagerInfo): Lỗi không xác định - " + e.getMessage());
            // e.printStackTrace();
        }
        // --- Hết phần truy vấn ---

        return manager;
    }

    // Thêm các phương thức khác liên quan đến thông tin Manager nếu cần
}