package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; // Thêm import
import java.awt.*;
import java.text.NumberFormat; // Thêm import
import java.util.List;
import java.util.Locale; // Thêm import
import model.Products;
import controller.ProductManagerController;

public class ViewProductPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnEdit, btnDelete;
    private NumberFormat currencyFormat; 

    public ViewProductPanel() {
        setLayout(new BorderLayout(0, 5)); 
        setBackground(new Color(248, 249, 250)); 

        
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        model = new DefaultTableModel(new Object[]{"ID", "Tên", "Loại ID", "Mô tả", "Giá bán", "Giá nhập", "Ảnh Path", "Số lượng"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        table = new JTable(model);

        // --- Áp dụng Style cho Bảng ---
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28); 
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(180, 180, 220)); 
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(150, 190, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(210, 210, 210));

        // Renderer mặc định (căn giữa và màu xen kẽ)
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                 if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                 } else {
                     c.setBackground(table.getSelectionBackground()); 
                 }
               
                 c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, defaultRenderer);

        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT); 
                 if (!isSelected) {
                     c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                 } else {
                     c.setBackground(table.getSelectionBackground());
                 }
                 c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return c;
            }
        };
        table.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer); 
        table.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer); 

        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  
        table.getColumnModel().getColumn(1).setPreferredWidth(200); 
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  
        table.getColumnModel().getColumn(3).setPreferredWidth(150); 
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150); 
        table.getColumnModel().getColumn(7).setPreferredWidth(70);  

        // --- Hết phần Style Bảng ---

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE); 
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); 
        panel.setBackground(new Color(240, 242, 245)); 
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xoá");
      
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);
        btnAdd.setFont(buttonFont); btnEdit.setFont(buttonFont); btnDelete.setFont(buttonFont);

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        add(panel, BorderLayout.SOUTH);

        new ProductManagerController(this);
    }

    public void displayProducts(List<Products> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Products p : list) {
                model.addRow(new Object[]{
                        p.getProductID(),
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

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public JTable getProductTable() { return table; }
    public DefaultTableModel getProductTableModel() { return model; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
}