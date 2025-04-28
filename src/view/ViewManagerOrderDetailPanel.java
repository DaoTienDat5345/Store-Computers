package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; // Để nhận dữ liệu
import service.OrderDetailManagerService; // Cần tạo Service này

public class ViewManagerOrderDetailPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private OrderDetailManagerService orderDetailService; // Service để load data

    public ViewManagerOrderDetailPanel() {
        this.orderDetailService = new OrderDetailManagerService(); // Khởi tạo
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 250));

        // Tạo bảng
        tableModel = new DefaultTableModel(new Object[]{
            "Mã chi tiết", "Mã đơn hàng", "Mã sản phẩm", "Số lượng",
            "Tổng tiền", "Loại BH", "Giá BH", "Ngày BĐ BH", "Ngày KT BH", "Ghi chú" // Rút gọn tên cột
        }, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
               return false; // Không cho sửa
            }
        };
        table = new JTable(tableModel);
        // Có thể set độ rộng cột ở đây nếu muốn
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel chứa nút làm mới
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRefresh = new JButton("Làm mới dữ liệu");
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        // Thêm listener cho nút làm mới
        btnRefresh.addActionListener(e -> loadData());

        // Tải dữ liệu lần đầu
        loadData();
    }

    private void loadData() {
        // Gọi Service để lấy dữ liệu
        List<Object[]> data = orderDetailService.getAllOrderDetails(); // Service cần có hàm này

        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        if (data != null && !data.isEmpty()) {
            for (Object[] row : data) {
                tableModel.addRow(row);
            }
        } else {
            // Có thể hiển thị thông báo nếu không có dữ liệu
             System.out.println("Không có dữ liệu chi tiết đơn hàng để hiển thị.");
        }
        // Không cần JOptionPane ở đây, lỗi nên được xử lý/log ở Service
    }
}