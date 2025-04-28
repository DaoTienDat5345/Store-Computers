package view;

import javax.swing.*;
import java.awt.*;
// import backend.EditProductOnManager; // Không gọi trực tiếp backend
import service.ProductManagerService; // Gọi Service

public class ViewEditProduct extends JFrame {

    private JTextField txtName, txtCategory, txtDesc, txtPrice, txtPriceCost, txtImage, txtQuantity;
    private String productID;
    private Runnable onSuccess;
    private ProductManagerService productService; // Service để cập nhật

    public ViewEditProduct(String productID, String name, String categoryID, String description,
                           double price, double priceCost, String imagePath, int quantity,
                           Runnable onSuccess) {
        this.productID = productID;
        this.onSuccess = onSuccess;
        this.productService = new ProductManagerService(); // Khởi tạo service

        setTitle("Sửa sản phẩm (ID: " + productID + ")");
        setSize(400, 350); // Tăng chiều cao một chút
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Quan trọng
        setLayout(new GridLayout(8, 2, 5, 5));

        // Tạo và gán giá trị ban đầu
        txtName = new JTextField(name);
        txtCategory = new JTextField(categoryID);
        txtDesc = new JTextField(description);
        txtPrice = new JTextField(String.valueOf(price));
        txtPriceCost = new JTextField(String.valueOf(priceCost));
        txtImage = new JTextField(imagePath);
        txtQuantity = new JTextField(String.valueOf(quantity));

        // Thêm label và textfield
        add(new JLabel("Tên sản phẩm:")); add(txtName);
        add(new JLabel("Loại (CategoryID):")); add(txtCategory);
        add(new JLabel("Mô tả sản phẩm:")); add(txtDesc);
        add(new JLabel("Giá bán:")); add(txtPrice);
        add(new JLabel("Giá nhập:")); add(txtPriceCost);
        add(new JLabel("Đường dẫn ảnh:")); add(txtImage);
        add(new JLabel("Số lượng kho:")); add(txtQuantity);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Huỷ");

        add(btnUpdate);
        add(btnCancel);

        btnUpdate.addActionListener(e -> updateProduct());
        btnCancel.addActionListener(e -> dispose());
    }

    private void updateProduct() {
        // Lấy dữ liệu mới
        String name = txtName.getText().trim();
        String categoryId = txtCategory.getText().trim();
        String description = txtDesc.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String priceCostStr = txtPriceCost.getText().trim();
        String imagePath = txtImage.getText().trim();
        String quantityStr = txtQuantity.getText().trim();

        // Kiểm tra
        if (name.isEmpty() || categoryId.isEmpty() || priceStr.isEmpty() || priceCostStr.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên, Loại, Giá, Số lượng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            double priceCost = Double.parseDouble(priceCostStr);
            int quantity = Integer.parseInt(quantityStr);

            if (quantity < 0 || price < 0 || priceCost < 0) {
                 JOptionPane.showMessageDialog(this, "Giá và số lượng không được âm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Gọi service
            boolean success = productService.updateProduct(productID, name, categoryId, description, price, priceCost, imagePath, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (onSuccess != null) {
                    onSuccess.run();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật sản phẩm. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Giá bán, giá nhập và số lượng phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }
}