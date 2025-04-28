package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Products; // Cần model Products
import controller.ProductManagerController; // Import Controller

public class ViewProductPanel extends JPanel { // Đổi thành JPanel

    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnEdit, btnDelete;
    // private ProductManagerController controller; // Controller sẽ quản lý View này

    public ViewProductPanel() {
        setLayout(new BorderLayout());

        // ID, Tên, Loại, Mô tả, Giá bán, Giá nhập, Ảnh, SL
        model = new DefaultTableModel(new Object[]{"ID", "Tên", "Loại ID", "Mô tả", "Giá bán", "Giá nhập", "Ảnh Path", "Số lượng"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
               // Ngăn không cho sửa trực tiếp trên bảng
               return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xoá");

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        add(panel, BorderLayout.SOUTH);

        // KHÔNG loadData() ở đây nữa, Controller sẽ gọi
        // KHÔNG addActionListeners ở đây nữa, Controller sẽ làm

        // Khởi tạo Controller và truyền View này vào
        new ProductManagerController(this); // Quan trọng!
    }

    // Phương thức để Controller cập nhật bảng
    public void displayProducts(List<Products> list) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        if (list != null) {
            for (Products p : list) {
                model.addRow(new Object[]{
                        p.getProductID(), // Sử dụng getter từ model
                        p.getProductName(),
                        p.getCategoryID(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getPriceCost(),
                        p.getImagePath(),
                        p.getQuantity()
                });
            }
        }
    }

    // Phương thức để Controller hiển thị thông báo
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Getters cho Controller sử dụng
    public JTable getProductTable() {
        return table;
    }

    public DefaultTableModel getProductTableModel() {
        return model;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }
}