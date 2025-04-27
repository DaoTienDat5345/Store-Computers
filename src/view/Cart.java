package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;

public class Cart extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private final String[] headers = {"Chọn", "Tên sản phẩm", "Giá", "Xóa"};
    private int currentCustomerID;
    private service.CartService cartService = new service.CartService();

    public Cart(int customerID) {
        setResizable(false);
        this.currentCustomerID = customerID;
        setTitle("Giỏ hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1004, 700);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("GIỎ HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblTitle.setBounds(419, 10, 200, 40);
        contentPane.add(lblTitle);

        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                if (columnIndex == 2) return Double.class;
                if (columnIndex == 3) return Object.class;
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        scrollPane.setBounds(10, 60, 970, 400);
        contentPane.add(scrollPane);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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

        TableColumn deleteColumn = table.getColumnModel().getColumn(3);
        deleteColumn.setCellRenderer(new TrashCanRenderer());
        deleteColumn.setPreferredWidth(50);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (column == 3 && row >= 0) {
                    String productName = (String) tableModel.getValueAt(row, 1);
                    cartService.deleteProductFromCart(currentCustomerID, productName);
                    tableModel.removeRow(row);
                }
            }
        });

        JButton btnCheckout = new JButton("Thanh toán");
        btnCheckout.setForeground(new Color(102, 255, 255));
        btnCheckout.setBackground(new Color(153, 0, 204));
        btnCheckout.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnCheckout.setBounds(419, 488, 150, 40);
        btnCheckout.setFocusPainted(false);
        contentPane.add(btnCheckout);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("pic/backgroundlap.jpg"));
        lblBackground.setBounds(-57, -26, 1303, 700);
        contentPane.add(lblBackground);

        cartService.loadCart(tableModel, currentCustomerID);

        btnCheckout.addActionListener(e -> cartService.checkoutSelectedItems(tableModel, currentCustomerID, this));
    }

    class TrashCanRenderer extends JLabel implements TableCellRenderer {
        private final ImageIcon trashIcon;

        public TrashCanRenderer() {
            ImageIcon originalIcon = new ImageIcon("pic/trash-icon.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            trashIcon = new ImageIcon(scaledImage);
            setIcon(trashIcon);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Cart frame = new Cart(1);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}