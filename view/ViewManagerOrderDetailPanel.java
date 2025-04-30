package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import service.OrderDetailManagerService;
import utils.ExcelExporter; 

public class ViewManagerOrderDetailPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnExportExcel; 
    private JLabel lblTotalAmount;
    private OrderDetailManagerService orderDetailService;
    private NumberFormat currencyFormat;

    public ViewManagerOrderDetailPanel() {
        this.orderDetailService = new OrderDetailManagerService();
        setLayout(new BorderLayout(0, 5));
        setBackground(new Color(230, 230, 250));

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        // Tạo bảng
        tableModel = new DefaultTableModel(new Object[]{
            "Mã chi tiết", "Mã đơn hàng", "Mã sản phẩm", "Số lượng",
            "Tổng tiền", "Loại BH", "Giá BH", "Ngày BĐ BH", "Ngày KT BH", "Ghi chú"
        }, 0) {
             @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getColumnModel().getColumn(4).setCellRenderer(createCurrencyRenderer()); 
        table.getColumnModel().getColumn(6).setCellRenderer(createCurrencyRenderer()); 

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        bottomPanel.setOpaque(false);

        lblTotalAmount = new JLabel("Tổng cộng: 0 VND");
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalAmount.setHorizontalAlignment(SwingConstants.LEFT);
        bottomPanel.add(lblTotalAmount, BorderLayout.WEST);

        JPanel buttonsRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)); 
        buttonsRightPanel.setOpaque(false);

        btnExportExcel = new JButton("Xuất Excel"); 
        btnExportExcel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonsRightPanel.add(btnExportExcel);

        btnRefresh = new JButton("Làm mới dữ liệu");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonsRightPanel.add(btnRefresh);

        bottomPanel.add(buttonsRightPanel, BorderLayout.EAST); 

        add(bottomPanel, BorderLayout.SOUTH);

        btnExportExcel.addActionListener(e -> {
             ExcelExporter.exportTableToExcel(table, this, "ChiTietDonHang.xlsx");
        });

        btnRefresh.addActionListener(e -> loadData());
        loadData();
    }

    // Hàm helper tạo renderer tiền tệ
    private DefaultTableCellRenderer createCurrencyRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                 if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                 } else {
                     c.setBackground(table.getSelectionBackground());
                 }
                 c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return c;
            }
        };
    }


    private void loadData() {
        List<Object[]> data = orderDetailService.getAllOrderDetails();
        tableModel.setRowCount(0);
        if (data != null && !data.isEmpty()) {
            for (Object[] row : data) {
                tableModel.addRow(row);
            }
        } else {
             System.out.println("Không có dữ liệu chi tiết đơn hàng để hiển thị.");
        }
        updateTotalAmountLabel();
    }

    private void updateTotalAmountLabel() {
        double total = 0.0;
        int subtotalColIndex = 4;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                Object value = tableModel.getValueAt(i, subtotalColIndex);
                if (value instanceof Number) {
                    total += ((Number) value).doubleValue();
                } else if (value != null) {
                    total += Double.parseDouble(value.toString());
                }
            } catch (NumberFormatException | NullPointerException e) {
                System.err.println("Lỗi parsing giá trị cột 'Tổng tiền' ở hàng " + i + ": " + tableModel.getValueAt(i, subtotalColIndex));
            }
        }
        lblTotalAmount.setText("Tổng cộng: " + currencyFormat.format(total));
    }

    public JButton getBtnExportExcel() {
        return btnExportExcel;
    }
}