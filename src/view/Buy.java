package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;
import utils.ImageRenderer;

public class Buy extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TimKiem;
    private JTable table;
    private final String[] header = {"Tên sản phẩm", "Giá bán", "Hình ảnh"};
    private final DefaultTableModel tb = new DefaultTableModel(header, 0);
    private final String[] categoryIDs = {"", "L001", "P001", "C001", "R001", "S001", "G001", "N001"};
    private int currentCustomerID;
    private service.ProductService productService = new service.ProductService();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Buy frame = new Buy(1);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Buy(int customerID) {
        setResizable(false);
        this.currentCustomerID = customerID;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1004, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel searchSortPanel = new JPanel();
        searchSortPanel.setBackground(new Color(153, 102, 204));
        searchSortPanel.setForeground(new Color(153, 204, 255));
        searchSortPanel.setLayout(null);
        searchSortPanel.setBounds(24, 94, 956, 60);
        searchSortPanel.setBorder(new LineBorder(new Color(102, 204, 255), 2, true));
        contentPane.add(searchSortPanel);

        JLabel lblLoi = new JLabel("Loại:");
        lblLoi.setForeground(Color.WHITE);
        lblLoi.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLoi.setBounds(10, 10, 62, 40);
        searchSortPanel.add(lblLoi);

        JComboBox<String> ComBoPL = new JComboBox<>();
        ComBoPL.setForeground(Color.WHITE);
        ComBoPL.setBackground(new Color(153, 102, 204));
        ComBoPL.setFont(new Font("Tahoma", Font.BOLD, 15));
        ComBoPL.setBounds(66, 15, 161, 35);
        ComBoPL.addItem("Không lựa chọn");
        ComBoPL.addItem("Laptop");
        ComBoPL.addItem("PC");
        ComBoPL.addItem("CPU");
        ComBoPL.addItem("RAM");
        ComBoPL.addItem("SSD");
        ComBoPL.addItem("GPU");
        ComBoPL.addItem("Nguồn");
        ComBoPL.setSelectedIndex(0);
        searchSortPanel.add(ComBoPL);

        JLabel lblGi = new JLabel("Giá:");
        lblGi.setForeground(Color.WHITE);
        lblGi.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblGi.setBounds(250, 10, 62, 40);
        searchSortPanel.add(lblGi);

        JComboBox<String> comboSort = new JComboBox<>();
        comboSort.setForeground(Color.WHITE);
        comboSort.setBackground(new Color(153, 102, 204));
        comboSort.setFont(new Font("Tahoma", Font.BOLD, 15));
        comboSort.addItem("Không sắp xếp");
        comboSort.addItem("Sắp xếp theo giá tăng dần");
        comboSort.addItem("Sắp xếp theo giá giảm dần");
        comboSort.setSelectedIndex(0);
        comboSort.setBounds(296, 15, 233, 35);
        searchSortPanel.add(comboSort);

        TimKiem = new JTextField();
        TimKiem.setFont(new Font("Tahoma", Font.BOLD, 15));
        TimKiem.setBounds(699, 10, 233, 40);
        searchSortPanel.add(TimKiem);
        TimKiem.setColumns(10);

        JButton btnNewButton = new JButton("Tìm kiếm");
        btnNewButton.setForeground(new Color(153, 255, 255));
        btnNewButton.setBackground(new Color(153, 0, 204));
        btnNewButton.setFont(new Font("Verdana", Font.BOLD, 20));
        btnNewButton.setBounds(539, 12, 150, 37);
        btnNewButton.setFocusPainted(false);
        searchSortPanel.add(btnNewButton);

        Component horizontalStrut = Box.createHorizontalStrut(20);
        horizontalStrut.setBounds(20, 160, 960, 12);
        contentPane.add(horizontalStrut);

        JPanel BoxMenu = new JPanel();
        BoxMenu.setBounds(10, 180, 967, 503);
        contentPane.add(BoxMenu);
        BoxMenu.setLayout(null);

        table = new JTable(tb);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(30); // Đặt lại chiều cao hàng mặc định như ban đầu
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
        scrollPane.setBounds(10, 10, 947, 483);
        BoxMenu.add(scrollPane);
        BoxMenu.setComponentZOrder(scrollPane, 0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = currencyFormat.format((Double) value);
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JLabel lblNewLabel = new JLabel("CELLCOMP");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setBounds(361, 9, 300, 40);
        contentPane.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 50));

        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.setBackground(Color.BLACK);
        btnNewButton_1.setIcon(new ImageIcon("pic/Close-icon.png"));
        btnNewButton_1.setBounds(10, 0, 41, 40);
        contentPane.add(btnNewButton_1);

        JLabel lblNewLabel_1_1 = new JLabel("");
        lblNewLabel_1_1.setIcon(new ImageIcon("pic/login2.jpg"));
        lblNewLabel_1_1.setBounds(0, -145, 612, 433);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel("");
        lblNewLabel_1_1_1.setIcon(new ImageIcon("pic/login2.jpg"));
        lblNewLabel_1_1_1.setBounds(-300, 247, 612, 433);
        contentPane.add(lblNewLabel_1_1_1);

        JLabel lblNewLabel_1_1_2 = new JLabel("");
        lblNewLabel_1_1_2.setIcon(new ImageIcon("pic/login2.jpg"));
        lblNewLabel_1_1_2.setBounds(671, 230, 612, 433);
        contentPane.add(lblNewLabel_1_1_2);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setBackground(Color.BLACK);
        lblNewLabel_1.setIcon(new ImageIcon("pic/logo.png"));
        lblNewLabel_1.setBounds(641, 10, 48, 48);
        contentPane.add(lblNewLabel_1);

        JButton btnNewButton_2 = new JButton("");
        btnNewButton_2.setBackground(new Color(153, 0, 204));
        btnNewButton_2.setIcon(new ImageIcon("pic/cart-icon.png"));
        btnNewButton_2.setBounds(928, 0, 62, 40);
        btnNewButton_2.setFocusable(false);
        contentPane.add(btnNewButton_2);

        JButton btnViewHis = new JButton("Lịch sử");
        btnViewHis.setForeground(Color.WHITE);
        btnViewHis.setBackground(new Color(153, 0, 204));
        btnViewHis.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnViewHis.setBounds(741, 0, 180, 40);
        btnViewHis.setFocusPainted(false);
        contentPane.add(btnViewHis);

        JLabel lblNewLabel_1_2 = new JLabel("");
        lblNewLabel_1_2.setIcon(new ImageIcon("pic/login2.jpg"));
        lblNewLabel_1_2.setBounds(536, -145, 612, 433);
        contentPane.add(lblNewLabel_1_2);

        btnNewButton_1.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất không?",
                    "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                view.Login view = new view.Login();
                view.setVisible(true);
                dispose();
            }
        });

        ComBoPL.addActionListener(e -> {
            int selectedIndex = ComBoPL.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedCategoryID = categoryIDs[selectedIndex];
                int sortIndex = comboSort.getSelectedIndex();
                if (sortIndex == 0) {
                    loadtable("", selectedCategoryID);
                } else if (sortIndex == 1) {
                    loadtable("ORDER BY price ASC", selectedCategoryID);
                } else if (sortIndex == 2) {
                    loadtable("ORDER BY price DESC", selectedCategoryID);
                }
            }
        });

        comboSort.addActionListener(e -> {
            int selectedIndex = comboSort.getSelectedIndex();
            int categoryIndex = ComBoPL.getSelectedIndex();
            String selectedCategoryID = (categoryIndex >= 0) ? categoryIDs[categoryIndex] : "";
            if (selectedIndex == 0) {
                loadtable("", selectedCategoryID);
            } else if (selectedIndex == 1) {
                loadtable("ORDER BY price ASC", selectedCategoryID);
            } else if (selectedIndex == 2) {
                loadtable("ORDER BY price DESC", selectedCategoryID);
            }
        });

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = TimKiem.getText().trim();
                searchProduct(keyword);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Mouse clicked on table");
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    System.out.println("Selected row: " + row);
                    try {
                        String productName = tb.getValueAt(row, 0).toString();
                        Object priceObj = tb.getValueAt(row, 1);
                        double price = (priceObj instanceof Double) ? (Double) priceObj : Double.parseDouble(priceObj.toString().replaceAll("[^0-9]", ""));
                        String imagePath = tb.getValueAt(row, 2).toString();
                        System.out.println("Opening ProductDetail: " + productName + ", " + price + ", " + imagePath);
                        ProductDetail detailFrame = new ProductDetail(productName, price, imagePath, currentCustomerID);
                        detailFrame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi mở ProductDetail: " + e.getMessage());
                    }
                } else {
                    System.out.println("No row selected at click point");
                }
            }
        });

        btnNewButton_2.addActionListener(e -> {
            view.Cart cart = new view.Cart(currentCustomerID);
            cart.setVisible(true);
        });

        btnViewHis.addActionListener(e -> {
            view.ViewOrderHistory historyView = new view.ViewOrderHistory(currentCustomerID);
            historyView.setVisible(true);
        });

        loadtable("ORDER BY productName ASC", "");
    }

    public void loadtable(String order, String categoryID) {
        productService.loadProducts(tb, order, categoryID);
        table.setModel(tb);
        table.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer(100, 100)); // Giữ nguyên kích thước 100x100 như code cũ
        table.setRowHeight(100); // Chiều cao hàng khớp với kích thước hình ảnh
        tb.fireTableDataChanged();
    }

    public void searchProduct(String keyword) {
        productService.searchProducts(tb, keyword);
        table.setModel(tb);
        table.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer(100, 100)); // Giữ nguyên kích thước 100x100 như code cũ
        table.setRowHeight(100); // Chiều cao hàng khớp với kích thước hình ảnh
        tb.fireTableDataChanged();
    }
}