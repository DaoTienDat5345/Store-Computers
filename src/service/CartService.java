package service;

import repository.CartRepository;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import view.ViewPaymentQRDialog;
import utils.QRCodeHelper;
import view.ViewOrder;
import java.awt.EventQueue;

public class CartService {
    private CartRepository cartRepository = new CartRepository();
    
    private static final String BANK_BIN = "970436"; 
    private static final String ACCOUNT_NO = "1234567890"; 
    private static final String MERCHANT_NAME = "Cua hang ban may tinh"; 
    
    public void loadCart(DefaultTableModel tableModel, int customerID) {
        try {
            List<Object[]> cartItems = cartRepository.getCartItems(customerID);
            tableModel.setRowCount(0);
            for (Object[] item : cartItems) {
                Vector<Object> row = new Vector<>();
                row.add(false);
                row.add(item[0]);
                row.add(item[1]);
                row.add(false);
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải giỏ hàng: " + e.getMessage());
        }
    }

    public void deleteProductFromCart(int customerID, String productName) {
        try {
            cartRepository.deleteProductFromCart(customerID, productName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    public void addToCart(int customerID, String productName) {
        try {
            Object[] productInfo = cartRepository.getProductInfo(productName);
            if (productInfo == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm trong kho!");
                return;
            }

            String productID = (String) productInfo[0];
            int quantity = (int) productInfo[1];

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Sản phẩm đã hết hàng!");
                return;
            }

            if (cartRepository.checkProductInCart(customerID, productID)) {
                cartRepository.updateCartQuantity(customerID, productID);
            } else {
                cartRepository.insertIntoCart(customerID, productID);
            }

            JOptionPane.showMessageDialog(null, "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }

    public void checkoutSelectedItems(DefaultTableModel tableModel, int customerID, JFrame frame) {
    	List<String> selectedProductNames = new ArrayList<>();
        List<Object[]> checkoutItemsData; // Chỉ khai báo
        double totalAmount = 0.0; // Khởi tạo

        try {
            // 1. Lấy các sản phẩm được chọn
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    String productName = (String) tableModel.getValueAt(i, 1);
                    selectedProductNames.add(productName);
                }
            }

            if (selectedProductNames.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
                return;
            }

            // Lấy thông tin chi tiết và số lượng từ DB
            checkoutItemsData = cartRepository.getCheckoutItems(customerID, selectedProductNames);

            // Tạo DefaultTableModel tạm thời *sau khi* có dữ liệu và tính tổng
            DefaultTableModel tempModel = new DefaultTableModel(new String[]{"Chọn", "Tên sản phẩm", "Giá", "Số lượng", "Tổng", "Xóa"}, 0);
            // Tính tổng tiền SAU KHI lấy dữ liệu chính xác từ DB
            for (Object[] item : checkoutItemsData) {
                // ... (lấy productName, price, quantity từ item) ...
                String productName = (String) item[1];
                double price = (Double) item[2];
                int quantity = (Integer) item[3]; // Lấy số lượng từ DB
                double subTotal = price * quantity;
                totalAmount += subTotal; // <-- totalAmount được cập nhật ở đây

                // Thêm dòng vào tempModel
                Vector<Object> row = new Vector<>();
                row.add(true);
                row.add(productName);
                row.add(price);
                row.add(quantity);
                row.add(subTotal);
                row.add(false);
                tempModel.addRow(row);
            }

            // Kiểm tra lại tổng tiền sau khi tính toán
            if (totalAmount <= 0) {
                JOptionPane.showMessageDialog(frame, "Không thể tính tổng tiền hoặc không có sản phẩm hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Tạo nội dung QR Code
            String orderDescription = "TT DH " + customerID + " K" + System.currentTimeMillis()%1000;
            String qrData = QRCodeHelper.createVietQRString(BANK_BIN, ACCOUNT_NO, totalAmount, orderDescription);

            if (qrData == null) {
                JOptionPane.showMessageDialog(frame, "Không thể tạo dữ liệu QR.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- BẮT ĐẦU SỬA LỖI ---
            // Tạo biến final hoặc effectively final để dùng trong lambda
            final double finalTotalAmount = totalAmount; // Biến này là effectively final sau điểm này
            final DefaultTableModel finalTempModel = tempModel; // Cũng cần final/effectively final
            final List<String> finalSelectedProductNames = selectedProductNames; // Cũng cần final/effectively final
            final String finalQrData = qrData; // Cũng cần final/effectively final

            // 3. Hiển thị Dialog QR Code
            Runnable onPaymentConfirmed = () -> {
                // Mở ViewOrder trên EDT sử dụng các biến final/effectively final
                 EventQueue.invokeLater(() -> {
                     // Sử dụng các biến final/effectively final ở đây
                     ViewOrder orderView = new ViewOrder(customerID, finalSelectedProductNames, finalTotalAmount, finalTempModel, frame);
                     orderView.setVisible(true);
                     // frame.setVisible(false); // Tạm thời comment lại, để ViewOrder xử lý việc ẩn/hiện ViewCart
                 });
            };

            // Hiển thị dialog QR trên EDT sử dụng các biến final/effectively final
            EventQueue.invokeLater(() -> {
                ViewPaymentQRDialog qrDialog = new ViewPaymentQRDialog(frame, finalTotalAmount, finalQrData, onPaymentConfirmed);
                qrDialog.setVisible(true);
            });
            // --- KẾT THÚC SỬA LỖI ---

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi khi chuẩn bị thanh toán: " + e.getMessage());
        }
    }
}