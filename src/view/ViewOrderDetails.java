package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import model.Order;
import model.OrderDetail;
import service.OrderService;

public class ViewOrderDetails extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private int currentCustomerID;
    private Order order;
    private List<OrderDetail> orderDetails;
    private JTable detailTable;
    private DefaultTableModel tableModel;
    private NumberFormat currencyFormat;

    public ViewOrderDetails(int customerID, Order order, List<OrderDetail> details) {
        this.currentCustomerID = customerID;
        this.order = order;
        this.orderDetails = details;

        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        setTitle("Chi tiết đơn hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("CHI TIẾT ĐƠN HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblTitle.setBounds(300, 10, 300, 40);
        contentPane.add(lblTitle);

        JLabel lblRecipientInfo = new JLabel("Thông tin người nhận:");
        lblRecipientInfo.setForeground(Color.WHITE);
        lblRecipientInfo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblRecipientInfo.setBounds(20, 60, 200, 30);
        contentPane.add(lblRecipientInfo);

        JLabel lblRecipientName = new JLabel("Tên: " + order.getRecipientName());
        lblRecipientName.setForeground(Color.WHITE);
        lblRecipientName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblRecipientName.setBounds(20, 90, 300, 20);
        contentPane.add(lblRecipientName);

        JLabel lblPhone = new JLabel("Số điện thoại: " + order.getRecipientPhone());
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPhone.setBounds(20, 110, 300, 20);
        contentPane.add(lblPhone);

        JLabel lblAddress = new JLabel("Địa chỉ: " + order.getRecipientAddress());
        lblAddress.setForeground(Color.WHITE);
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblAddress.setBounds(20, 130, 300, 20);
        contentPane.add(lblAddress);

        JLabel lblOrderID = new JLabel("Mã hóa đơn: " + order.getOrderID());
        lblOrderID.setForeground(Color.WHITE);
        lblOrderID.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblOrderID.setBounds(20, 150, 300, 20);
        contentPane.add(lblOrderID);

        String[] headers = {"Tên sản phẩm", "Số lượng", "Giá mỗi sản phẩm", "Loại bảo hành", "Thời gian bắt đầu", "Thời gian kết thúc"};
        tableModel = new DefaultTableModel(headers, 0);
        detailTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBounds(20, 180, 760, 300);
        contentPane.add(scrollPane);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < detailTable.getColumnCount(); i++) {
            detailTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        detailTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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

        JButton btnClose = new JButton("Đóng");
        btnClose.setForeground(new Color(102, 255, 255));
        btnClose.setBackground(new Color(153, 0, 204));
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnClose.setBounds(320, 500, 150, 40);
        contentPane.add(btnClose);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("D:\\icon\\backgroundlaptoptim.jpg"));
        lblBackground.setBounds(-10, -10, 800, 600);
        contentPane.add(lblBackground);

        OrderService service = new OrderService();
        service.loadOrderDetails(tableModel, orderDetails, order, null);
        btnClose.addActionListener(e -> dispose());
    }
}