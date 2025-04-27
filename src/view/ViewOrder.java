package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import model.Address;
import model.Order;
import model.OrderDetail;
import service.OrderService;

public class ViewOrder extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private int currentCustomerID;
    private List<String> selectedProducts;
    private double totalAmount;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private Order order;
    private List<OrderDetail> orderDetails;
    private JLabel lblTotal;
    private JFrame viewCart;
    private DefaultTableModel cartModel;
    private DefaultTableModel cartModelBackup;
    private JButton btnConfirm, btnCancel, btnBack;
    private NumberFormat currencyFormat;
    private JTextField txtRecipientName, txtPhone;
    private JComboBox<String> comboBoxProvince;

    public ViewOrder(int customerID, List<String> products, double total, DefaultTableModel cartModel) {
        this(customerID, products, total, cartModel, null);
    }

    public ViewOrder(int customerID, List<String> products, double total, DefaultTableModel cartModel, JFrame viewCart) {
        this.currentCustomerID = customerID;
        this.selectedProducts = products;
        this.totalAmount = total;
        this.cartModel = cartModel;

        Vector<Object> columnIdentifiers = new Vector<>();
        for (int i = 0; i < cartModel.getColumnCount(); i++) {
            columnIdentifiers.add(cartModel.getColumnName(i));
        }
        this.cartModelBackup = new DefaultTableModel(new Vector<>(cartModel.getDataVector()), columnIdentifiers);

        this.viewCart = viewCart;
        this.order = new Order("", LocalDate.now(), totalAmount, customerID);
        this.orderDetails = new ArrayList<>();

        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        if (viewCart != null) {
            viewCart.setVisible(false);
        }

        setTitle("Xác nhận đơn hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 650);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("XÁC NHẬN ĐƠN HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblTitle.setBounds(160, 10, 300, 40);
        contentPane.add(lblTitle);

        JLabel lblDate = new JLabel("Ngày đặt: " + LocalDate.now());
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDate.setBounds(20, 60, 200, 30);
        contentPane.add(lblDate);

        lblTotal = new JLabel("Tổng tiền: " + currencyFormat.format(totalAmount) + " VND");
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTotal.setBounds(20, 90, 250, 30);
        contentPane.add(lblTotal);

        JLabel lblRecipientInfo = new JLabel("Thông tin người nhận:");
        lblRecipientInfo.setForeground(Color.WHITE);
        lblRecipientInfo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblRecipientInfo.setBounds(20, 130, 200, 30);
        contentPane.add(lblRecipientInfo);

        JLabel lblRecipientName = new JLabel("Tên người nhận:");
        lblRecipientName.setForeground(Color.WHITE);
        lblRecipientName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblRecipientName.setBounds(20, 170, 120, 30);
        contentPane.add(lblRecipientName);

        txtRecipientName = new JTextField();
        txtRecipientName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtRecipientName.setBounds(140, 170, 420, 30);
        contentPane.add(txtRecipientName);

        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPhone.setBounds(20, 210, 120, 30);
        contentPane.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtPhone.setBounds(140, 210, 420, 30);
        contentPane.add(txtPhone);

        JLabel lblAddress = new JLabel("Địa chỉ:");
        lblAddress.setForeground(Color.WHITE);
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblAddress.setBounds(20, 250, 120, 30);
        contentPane.add(lblAddress);

        comboBoxProvince = new JComboBox<>();
        comboBoxProvince.setFont(new Font("Tahoma", Font.PLAIN, 14));
        comboBoxProvince.setBounds(140, 250, 420, 30);
        contentPane.add(comboBoxProvince);

        String[] headers = {"Tên sản phẩm", "Số lượng", "Tổng tiền", "Loại bảo hành"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(20, 290, 540, 280);
        contentPane.add(scrollPane);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        orderTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        orderTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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

        TableCellEditor quantityEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Object getCellEditorValue() {
                String value = ((JTextField) getComponent()).getText();
                try {
                    int qty = Integer.parseInt(value);
                    return qty > 0 ? qty : 1;
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        };
        orderTable.getColumnModel().getColumn(1).setCellEditor(quantityEditor);

        JComboBox<String> warrantyCombo = new JComboBox<>(new String[]{"Thường", "Vàng"});
        orderTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(warrantyCombo));

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setForeground(new Color(102, 255, 255));
        btnConfirm.setBackground(new Color(153, 0, 204));
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnConfirm.setBorder(BorderFactory.createEmptyBorder());
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBounds(150, 580, 150, 40);
        contentPane.add(btnConfirm);

        btnCancel = new JButton("Hủy");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBackground(Color.RED);
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnCancel.setBorder(BorderFactory.createEmptyBorder());
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(320, 580, 150, 40);
        contentPane.add(btnCancel);

        btnBack = new JButton("Quay lại");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(0, 153, 255));
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnBack.setBorder(BorderFactory.createEmptyBorder());
        btnBack.setFocusPainted(false);
        btnBack.setBounds(20, 580, 120, 40);
        contentPane.add(btnBack);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon("pic/backgroundlaptoptim.jpg"));
        lblBackground.setBounds(-10, -10, 600, 650);
        contentPane.add(lblBackground);

        OrderService service = new OrderService();
        service.loadOrderDetails(tableModel, orderDetails, order, cartModel);
        new controller.OrderController(this, service);

        loadCustomerInfoInBackground();
    }

    private void loadCustomerInfoInBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private String fullName = "";
            private String phone = "";
            private String address = "";
            private ArrayList<Address> listTinh = new ArrayList<>();

            @Override
            protected Void doInBackground() throws Exception {
                OrderService service = new OrderService();
                try {
                    fullName = service.getCustomerInfo(currentCustomerID, "fullName");
                    phone = service.getCustomerInfo(currentCustomerID, "phone");
                    address = service.getCustomerInfo(currentCustomerID, "userAddress");
                    listTinh = Address.getDSTinh();
                } catch (Exception e) {
                    System.err.println("Error loading customer info: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        JOptionPane.showMessageDialog(ViewOrder.this,
                            "Không thể tải thông tin khách hàng. Vui lòng nhập thông tin thủ công.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    } else {
                        txtRecipientName.setText(fullName);
                        txtPhone.setText(phone);
                    }

                    for (Address add : listTinh) {
                        comboBoxProvince.addItem(add.getTenTinh());
                    }
                    if (!address.isEmpty()) {
                        comboBoxProvince.setSelectedItem(address);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ViewOrder.this,
                        "Lỗi khi hiển thị thông tin khách hàng: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void updateTotalLabel() {
        lblTotal.setText("Tổng tiền: " + currencyFormat.format(order.getTotalAmount()) + " VND");
    }

    public String getRecipientName() { return txtRecipientName.getText().trim(); }
    public String getRecipientPhone() { return txtPhone.getText().trim(); }
    public String getRecipientAddress() { return (String) comboBoxProvince.getSelectedItem(); }
    public JButton getBtnConfirm() { return btnConfirm; }
    public JButton getBtnCancel() { return btnCancel; }
    public JButton getBtnBack() { return btnBack; }
    public JTable getOrderTable() { return orderTable; }
    public Order getOrder() { return order; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public int getCurrentCustomerID() { return currentCustomerID; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JFrame getViewCart() { return viewCart; }
}