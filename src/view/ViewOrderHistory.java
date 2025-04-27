package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import service.OrderHistoryService;

public class ViewOrderHistory extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private int currentCustomerID;
    private JButton btnViewDetails;
    private NumberFormat currencyFormat;

    public ViewOrderHistory(int customerID) {
        this.currentCustomerID = customerID;

        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        setTitle("Lịch sử đơn hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("LỊCH SỬ ĐƠN HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblTitle.setBounds(280, 10, 300, 40);
        contentPane.add(lblTitle);

        String[] headers = {"Mã đơn hàng", "Ngày đặt", "Tổng tiền"};
        tableModel = new DefaultTableModel(headers, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(20, 60, 760, 400);
        contentPane.add(scrollPane);

        orderTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
        orderTable.setRowHeight(30);
        orderTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < orderTable.getColumnCount(); i++) {
            orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        orderTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = currencyFormat.format((Double) value) + " VND";
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        btnViewDetails = new JButton("Xem chi tiết");
        btnViewDetails.setForeground(new Color(102, 255, 255));
        btnViewDetails.setBackground(new Color(153, 0, 204));
        btnViewDetails.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnViewDetails.setBounds(320, 480, 185, 40);
        btnViewDetails.setFocusPainted(false);
        contentPane.add(btnViewDetails);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("pic/History.jpg"));
        lblBackground.setBounds(-10, -10, 800, 600);
        contentPane.add(lblBackground);

        OrderHistoryService service = new OrderHistoryService();
        service.loadOrderHistory(tableModel, currentCustomerID);
        new controller.OrderHistoryController(this, service);
    }

    public JButton getBtnViewDetails() { return btnViewDetails; }
    public JTable getOrderTable() { return orderTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public int getCurrentCustomerID() { return currentCustomerID; }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ViewOrderHistory frame = new ViewOrderHistory(1);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}