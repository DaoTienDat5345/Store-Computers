package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ProductDetail extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblImage, lblProductName, lblPrice, lblDescription;
    private JTextArea txtDescription;
    private JLabel lblNewLabel;
    private JButton btnBuy;
    private JButton btnAddToCart;
    private String productName;
    private double price;
    private String imagePath;
    private int currentCustomerID;
    private service.ProductService productService;
    private service.CartService cartService;

    public ProductDetail(String productName, double price, String imagePath, int customerID) {
        setResizable(false);
        this.productName = productName;
        this.price = price;
        this.imagePath = imagePath;
        this.currentCustomerID = customerID;
        this.productService = new service.ProductService();
        this.cartService = new service.CartService();

        setTitle("Chi tiết sản phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblProductName = new JLabel(productName);
        lblProductName.setForeground(Color.WHITE);
        lblProductName.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblProductName.setBounds(114, 10, 366, 30);
        contentPane.add(lblProductName);

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);
        lblPrice = new JLabel("Giá: " + currencyFormat.format(price) + " VND");
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblPrice.setBounds(24, 304, 255, 30);
        contentPane.add(lblPrice);

        lblImage = new JLabel();
        lblImage.setBounds(24, 92, 255, 215);
        contentPane.add(lblImage);

        lblDescription = new JLabel("Mô tả sản phẩm");
        lblDescription.setForeground(Color.WHITE);
        lblDescription.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblDescription.setBounds(375, 50, 149, 30);
        contentPane.add(lblDescription);

        txtDescription = new JTextArea();
        txtDescription.setBounds(340, 92, 236, 215);
        contentPane.add(txtDescription);
        txtDescription.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setEditable(false);

        btnBuy = new JButton("Mua");
        btnBuy.setForeground(new Color(102, 255, 255));
        btnBuy.setBackground(new Color(153, 0, 204));
        btnBuy.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnBuy.setBounds(186, 405, 167, 36);
        btnBuy.setFocusPainted(false);
        contentPane.add(btnBuy);

        btnAddToCart = new JButton("Thêm vào giỏ");
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setBackground(Color.RED);
        btnAddToCart.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnAddToCart.setBounds(384, 405, 192, 36);
        btnAddToCart.setFocusPainted(false);
        contentPane.add(btnAddToCart);

        lblNewLabel = new JLabel("");
        try {
            String backgroundPath = "pic/backgroundlaptoptim.jpg";
            System.out.println("Attempting to load background image from: " + backgroundPath);
            ImageIcon backgroundIcon = new ImageIcon(backgroundPath);
            if (backgroundIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                lblNewLabel.setIcon(backgroundIcon);
                System.out.println("Background image loaded successfully: " + backgroundPath);
            } else {
                System.out.println("Failed to load background image (load status failed): " + backgroundPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while loading background image: " + e.getMessage());
        }
        lblNewLabel.setBounds(-323, -21, 963, 484);
        contentPane.add(lblNewLabel);

        loadProductDetails(productName, imagePath);

        btnAddToCart.addActionListener(e -> cartService.addToCart(currentCustomerID, productName));
        btnBuy.addActionListener(e -> buyNow());
    }

    private void loadProductDetails(String productName, String imagePath) {
        try {
            String description = productService.getProductDescription(productName);
            txtDescription.setText(description != null ? description : "Không có mô tả.");

            if (imagePath != null && !imagePath.isEmpty()) {
                System.out.println("Attempting to load product image from: " + imagePath);
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(img));
                    System.out.println("Product image loaded successfully: " + imagePath);
                } else {
                    lblImage.setText("Không thể tải hình ảnh (load status failed): " + imagePath);
                    System.out.println("Product image load status failed for: " + imagePath);
                }
            } else {
                lblImage.setText("Đường dẫn hình ảnh không hợp lệ.");
                System.out.println("Product image path is null or empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblImage.setText("Lỗi tải hình ảnh: " + e.getMessage());
            System.out.println("Exception while loading product image: " + e.getMessage());
        }
    }

    private void buyNow() {
        if (!productService.checkProductAvailability(productName, 1)) {
            return;
        }

        List<String> selectedProducts = new ArrayList<>();
        selectedProducts.add(productName);

        DefaultTableModel tempModel = new DefaultTableModel(new String[]{"Chọn", "Tên sản phẩm", "Giá", "Số lượng", "Tổng", "Xóa"}, 0);
        Vector<Object> row = new Vector<>();
        row.add(true);
        row.add(productName);
        row.add(price);
        row.add(1);
        row.add(price);
        row.add(false);
        tempModel.addRow(row);

        ViewOrder orderView = new ViewOrder(currentCustomerID, selectedProducts, price, tempModel, this);
        orderView.setVisible(true);
        setVisible(false);
    }
}