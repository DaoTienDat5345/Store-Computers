package service;

import repository.CartRepository;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CartService {
    private CartRepository cartRepository = new CartRepository();

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
        List<String> selectedProducts = new ArrayList<>();
        double totalAmount = 0.0;

        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    String productName = (String) tableModel.getValueAt(i, 1);
                    double price = (Double) tableModel.getValueAt(i, 2);
                    selectedProducts.add(productName);
                }
            }

            if (selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
                return;
            }

            List<Object[]> checkoutItems = cartRepository.getCheckoutItems(customerID, selectedProducts);
            DefaultTableModel tempModel = new DefaultTableModel(new String[]{"Chọn", "Tên sản phẩm", "Giá", "Số lượng", "Tổng", "Xóa"}, 0);

            for (Object[] item : checkoutItems) {
                String productName = (String) item[1];
                double price = (Double) item[2];
                int quantity = (Integer) item[3];
                totalAmount += price * quantity;

                Vector<Object> row = new Vector<>();
                row.add(true);
                row.add(productName);
                row.add(price);
                row.add(quantity);
                row.add(price * quantity);
                row.add(false);
                tempModel.addRow(row);
            }

            view.ViewOrder orderView = new view.ViewOrder(customerID, selectedProducts, totalAmount, tempModel, frame);
            orderView.setVisible(true);
            frame.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi kiểm tra giỏ hàng: " + e.getMessage());
        }
    }
}