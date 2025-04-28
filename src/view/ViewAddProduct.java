package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Import Service và Controller nếu cần xử lý phức tạp hơn,
// nhưng trong trường hợp dialog đơn giản, chỉ cần Runnable là đủ.
import service.ProductManagerService; // Service để gọi hàm add

public class ViewAddProduct extends JFrame {
    private JTextField txtId, txtName, txtCategory, txtDesc, txtPrice, txtPriceCost, txtImage, txtQuantity;
    private Runnable onSuccess;
    private ProductManagerService productService; // Service để thực hiện thêm

    public ViewAddProduct(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.productService = new ProductManagerService(); // Khởi tạo service

        setTitle("Thêm sản phẩm");
        setSize(400, 400);
        setLocationRelativeTo(null); // Hiển thị giữa màn hình cha
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // KHÔNG NÊN dùng EXIT_ON_CLOSE cho dialog
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng dialog này thôi
        setLayout(new GridLayout(9, 2, 5, 5));

        txtId = new JTextField();
        txtName = new JTextField();
        txtCategory = new JTextField();
        txtDesc = new JTextField();
        txtPrice = new JTextField();
        txtPriceCost = new JTextField();
        txtImage = new JTextField();
        txtQuantity = new JTextField();

        add(new JLabel("ID:")); add(txtId);
        add(new JLabel("Tên:")); add(txtName);
        add(new JLabel("Loại (CategoryID):")); add(txtCategory); // Ghi rõ là ID
        add(new JLabel("Mô tả:")); add(txtDesc);
        add(new JLabel("Giá bán:")); add(txtPrice);
        add(new JLabel("Giá nhập:")); add(txtPriceCost);
        add(new JLabel("Đường dẫn ảnh:")); add(txtImage); // Ghi rõ là path
        add(new JLabel("Số lượng:")); add(txtQuantity);

        JButton btnAdd = new JButton("Thêm");
        JButton btnCancel = new JButton("Huỷ");

        add(btnAdd);
        add(btnCancel);

        btnAdd.addActionListener(e -> addProduct()); // Gọi hàm xử lý riêng
        btnCancel.addActionListener(e -> dispose()); // Đóng dialog
    }

    private void addProduct() {
         // Lấy dữ liệu từ các JTextField
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String categoryId = txtCategory.getText().trim();
        String description = txtDesc.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String priceCostStr = txtPriceCost.getText().trim();
        String imagePath = txtImage.getText().trim();
        String quantityStr = txtQuantity.getText().trim();

        // Kiểm tra dữ liệu đầu vào cơ bản
        if (id.isEmpty() || name.isEmpty() || categoryId.isEmpty() || priceStr.isEmpty() || priceCostStr.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (ID, Tên, Loại, Giá, Số lượng).", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

            // Gọi Service để thêm sản phẩm
            boolean success = productService.addProduct(id, name, categoryId, description, price, priceCost, imagePath, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (onSuccess != null) {
                    onSuccess.run(); // Gọi callback để View chính cập nhật
                }
                dispose(); // Đóng dialog sau khi thành công
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại. Vui lòng kiểm tra lại thông tin hoặc ID đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá bán, giá nhập và số lượng phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt lỗi chung khác nếu có từ Service/Repository
             JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }
}