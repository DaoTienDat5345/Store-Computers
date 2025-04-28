package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage; // Chỉ cần để tạo icon trống nếu cần
import java.util.HashMap;
import java.util.Map;

public class ViewManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private JPanel sidebar; // Biến instance để truy cập từ phương thức khác

    // --- Định nghĩa màu sắc ---
    private static final Color SIDEBAR_BACKGROUND = new Color(45, 52, 54); // Xám đậm
    private static final Color BUTTON_DEFAULT_BG = new Color(99, 110, 114); // Xám nhạt hơn
    private static final Color BUTTON_HOVER_BG = new Color(129, 140, 144); // Sáng hơn khi hover
    private static final Color BUTTON_SELECTED_BG = new Color(0, 119, 182); // Xanh dương cho mục được chọn
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SEPARATOR_COLOR = new Color(108, 117, 125);

    // --- Tên các Panel trong CardLayout ---
    private static final String MAIN_SCREEN = "Màn hình chính";
    private static final String SHIFT_MANAGER = "Quản lý ca làm";
    private static final String EMPLOYEE_MANAGER = "Quản lý Nhân Viên";
    private static final String PRODUCT_MANAGER = "Quản lý hàng hoá";
    private static final String ORDER_DETAIL_MANAGER = "Quản lý đơn hàng";

    // Lưu trữ các nút menu để quản lý trạng thái selected
    private Map<String, JButton> menuButtons = new HashMap<>();
    private JButton selectedButton = null;

    // --- Không cần đường dẫn Icon nữa ---

    public ViewManager() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar ---
        sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // --- Tiêu đề Sidebar ---
        JLabel title = new JLabel("QUẢN LÝ", SwingConstants.CENTER); // Chỉ còn tiêu đề "QUẢN LÝ"
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, title.getPreferredSize().height + 20));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(25));

        // --- Main Content ---
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(new Color(240, 242, 245));

        // Thêm các Panel vào CardLayout
        int defaultManagerId = 1;
        mainContent.add(new ViewManagerInfoPanel(defaultManagerId), MAIN_SCREEN);
        mainContent.add(new ViewShiftPanel(), SHIFT_MANAGER);
        mainContent.add(new ViewEmployeesPanel(), EMPLOYEE_MANAGER);
        mainContent.add(new ViewProductPanel(), PRODUCT_MANAGER);
        mainContent.add(new ViewManagerOrderDetailPanel(), ORDER_DETAIL_MANAGER);

        // --- Tạo các nút menu (không có icon) ---
        String[] buttonLabels = {MAIN_SCREEN, SHIFT_MANAGER, EMPLOYEE_MANAGER, PRODUCT_MANAGER, ORDER_DETAIL_MANAGER};
        for (String label : buttonLabels) {
            JButton button = createMenuButton(label); // Gọi hàm không có iconPath
            menuButtons.put(label, button);
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(8));
        }

        // Đặt nút đầu tiên là selected mặc định
        selectButton(menuButtons.get(MAIN_SCREEN));

        // --- Thêm vào Frame ---
        add(sidebar, BorderLayout.WEST);

        // Thêm đường kẻ phân cách
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(SEPARATOR_COLOR);
        separator.setBackground(SEPARATOR_COLOR);
        // Add separator giữa sidebar và main content
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(sidebar, BorderLayout.WEST);
        contentPane.add(separator, BorderLayout.CENTER); // Separator ở giữa
        contentPane.add(mainContent, BorderLayout.EAST); // Main content ở East

        // Hoặc cách khác để separator hiển thị đúng
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(separator, BorderLayout.WEST); // Separator ở bên trái của centerPanel
        centerPanel.add(mainContent, BorderLayout.CENTER); // Main content chiếm phần còn lại

        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);


        // Hiển thị màn hình chính mặc định
        cardLayout.show(mainContent, MAIN_SCREEN);
    }

    // Hàm tạo nút menu (không có icon)
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT_COLOR);
        button.setBackground(BUTTON_DEFAULT_BG);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        // Điều chỉnh padding nếu cần sau khi bỏ icon
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Tăng padding trái
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Action listener để chuyển CardLayout VÀ chọn nút
        button.addActionListener(e -> {
            selectButton(button);
            cardLayout.show(mainContent, text);
        });

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(BUTTON_HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 if (button != selectedButton) {
                    button.setBackground(BUTTON_DEFAULT_BG);
                 }
            }
        });

        return button;
    }

    // Hàm để cập nhật trạng thái nút được chọn (giữ nguyên)
    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(BUTTON_DEFAULT_BG);
            selectedButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            selectedButton.setForeground(TEXT_COLOR);
        }
        selectedButton = button;
        if (selectedButton != null) {
             selectedButton.setBackground(BUTTON_SELECTED_BG);
             selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
             selectedButton.setForeground(Color.WHITE);
        }
    }

    public static void main(String[] args) {
        try {
             for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
             }
        } catch (Exception e) {
             System.err.println("Không thể cài đặt Look and Feel Nimbus, sử dụng mặc định.");
        }
        SwingUtilities.invokeLater(() -> new ViewManager().setVisible(true));
    }
}