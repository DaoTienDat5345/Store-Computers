package service;

import repository.OrderHistoryRepository;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;
import model.Order;
import model.OrderDetail;

public class OrderHistoryService {
    private OrderHistoryRepository orderHistoryRepository = new OrderHistoryRepository();

    public void loadOrderHistory(DefaultTableModel tableModel, int customerID) {
        try {
            List<Object[]> orders = orderHistoryRepository.getOrderHistory(customerID);
            tableModel.setRowCount(0);
            for (Object[] order : orders) {
                Vector<Object> row = new Vector<>();
                row.add(order[0]);
                row.add(order[1]);
                row.add(order[2]);
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải lịch sử đơn hàng: " + e.getMessage());
        }
    }

    public Order viewSelectedOrderDetails(String orderID, int customerID) {
        try {
            Order selectedOrder = orderHistoryRepository.getOrderById(orderID);
            if (selectedOrder == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy đơn hàng!");
                return null;
            }

            List<OrderDetail> details = orderHistoryRepository.getOrderDetails(orderID);
            for (OrderDetail detail : details) {
                if (selectedOrder.getRecipientName() == null && detail.getNote() != null) {
                    String note = detail.getNote();
                    if (note.contains("Tên:")) selectedOrder.setRecipientName(note.split("Tên:")[1].split(";")[0].trim());
                    if (note.contains("SĐT:")) selectedOrder.setRecipientPhone(note.split("SĐT:")[1].split(";")[0].trim());
                    if (note.contains("Địa chỉ:")) selectedOrder.setRecipientAddress(note.split("Địa chỉ:")[1].trim());
                }
            }

            if (selectedOrder != null) {
                view.ViewOrderDetails detailView = new view.ViewOrderDetails(customerID, selectedOrder, details);
                detailView.setVisible(true);
            }
            return selectedOrder;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải chi tiết đơn hàng: " + e.getMessage());
            return null;
        }
    }
}