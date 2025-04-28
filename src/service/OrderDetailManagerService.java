package service;

import database.QLdatabase; // Cần để lấy dữ liệu
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// Có thể cần import model OrderDetail nếu muốn trả về List<OrderDetail>
// import model.OrderDetail;

public class OrderDetailManagerService {

    // Có thể tạo OrderDetailRepository riêng hoặc thêm vào OrderHistoryRepository
    // private OrderDetailRepository orderDetailRepository;

    public OrderDetailManagerService() {
        // this.orderDetailRepository = new OrderDetailRepository();
    }

    /**
     * Lấy tất cả các bản ghi từ bảng OrderDetails.
     * @return Danh sách các mảng Object, mỗi mảng đại diện cho một hàng trong bảng.
     *         Trả về danh sách rỗng nếu không có dữ liệu hoặc lỗi.
     */
    public List<Object[]> getAllOrderDetails() {
        List<Object[]> detailsList = new ArrayList<>();
        String sql = "SELECT orderDetailsID, orderID, productID, Quantity, Subtotal, warrantyType, warrantyPrice, warrantyStartDate, warrantyEndDate, note FROM OrderDetails ORDER BY orderID, orderDetailsID"; // Sắp xếp để dễ nhìn

        // --- Phần truy vấn lấy tất cả OrderDetails ---
        // Lưu ý: Đoạn code này nên được chuyển vào Repository phù hợp
        try (Connection conn = QLdatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

             if (conn == null) {
                 System.err.println("Service Error (OrderDetailManager): Không thể kết nối CSDL.");
                 return Collections.emptyList();
            }

            while (rs.next()) {
                Object[] row = {
                    rs.getString("orderDetailsID"),
                    rs.getString("orderID"),
                    rs.getString("productID"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Subtotal"),
                    rs.getString("warrantyType"),
                    rs.getDouble("warrantyPrice"),
                    rs.getDate("warrantyStartDate"),
                    rs.getDate("warrantyEndDate"),
                    rs.getString("note")
                };
                detailsList.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Service Error (OrderDetailManager): Lỗi SQL khi tải chi tiết đơn hàng - " + e.getMessage());
            // e.printStackTrace();
            return Collections.emptyList(); // Trả về rỗng khi lỗi
        } catch (Exception e) {
             System.err.println("Service Error (OrderDetailManager): Lỗi không xác định - " + e.getMessage());
             // e.printStackTrace();
             return Collections.emptyList();
        }
        // --- Hết phần truy vấn ---

        return detailsList;
    }

    // Có thể thêm các phương thức lọc, tìm kiếm chi tiết đơn hàng khác cho Manager ở đây
}