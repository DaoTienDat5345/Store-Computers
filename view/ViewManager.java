package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ViewManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private JPanel sidebar;

    // --- Định nghĩa màu sắc THEME CHUYÊN NGHIỆP ---
    private static final Color SIDEBAR_BACKGROUND = new Color(44, 62, 80);    // Xanh Navy đậm / Xám xanh
    private static final Color TEXT_COLOR_SIDEBAR = new Color(236, 240, 241);  // Chữ màu trắng ngà
    private static final Color BUTTON_DEFAULT_BG = new Color(52, 73, 94);     // Đậm hơn nền sidebar một chút
    private static final Color BUTTON_HOVER_BG = new Color(82, 103, 124);    // Sáng hơn khi hover (điều chỉnh)
    private static final Color BUTTON_SELECTED_BG = new Color(52, 152, 219);  // Xanh dương sáng làm điểm nhấn
    private static final Color BUTTON_SELECTED_TEXT = Color.WHITE;            // Chữ trắng khi chọn
    private static final Color SEPARATOR_COLOR = new Color(100, 110, 120);    // Màu xám cho đường kẻ
    private static final Color MAIN_CONTENT_BACKGROUND = new Color(236, 240, 241); // Nền nội dung trắng ngà (hoặc trắng tinh Color.WHITE)


    // --- Tên các Panel trong CardLayout ---
    private static final String MAIN_SCREEN = "Màn hình chính";
    private static final String SHIFT_MANAGER = "Quản lý ca làm";
    private static final String EMPLOYEE_MANAGER = "Quản lý Nhân Viên";
    private static final String PRODUCT_MANAGER = "Quản lý hàng hoá";
    private static final String ORDER_DETAIL_MANAGER = "Quản lý đơn hàng";

    private Map<String, JButton> menuButtons = new HashMap<>();
    private JButton selectedButton = null;

    public ViewManager() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar ---
        sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BACKGROUND); // *** Nền Navy/Xám xanh ***
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // --- Tiêu đề Sidebar ---
        JLabel title = new JLabel("QUẢN LÝ", SwingConstants.CENTER);
        title.setForeground(TEXT_COLOR_SIDEBAR); // *** Chữ trắng ngà ***
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, title.getPreferredSize().height + 20));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(25));

        // --- Main Content ---
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(MAIN_CONTENT_BACKGROUND); // *** Nền trắng ngà / trắng ***

        // Thêm các Panel vào CardLayout
        int defaultManagerId = 1;
        // Các panel con có thể giữ nền sáng mặc định của chúng
        mainContent.add(new ViewManagerInfoPanel(defaultManagerId), MAIN_SCREEN);
        mainContent.add(new ViewShiftPanel(), SHIFT_MANAGER);
        mainContent.add(new ViewEmployeesPanel(), EMPLOYEE_MANAGER);
        mainContent.add(new ViewProductPanel(), PRODUCT_MANAGER);
        mainContent.add(new ViewManagerOrderDetailPanel(), ORDER_DETAIL_MANAGER);


        // --- Tạo các nút menu ---
        String[] buttonLabels = {MAIN_SCREEN, SHIFT_MANAGER, EMPLOYEE_MANAGER, PRODUCT_MANAGER, ORDER_DETAIL_MANAGER};
        for (String label : buttonLabels) {
            JButton button = createMenuButton(label);
            menuButtons.put(label, button);
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(8));
        }

        selectButton(menuButtons.get(MAIN_SCREEN));

        // --- Thêm vào Frame ---
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(SEPARATOR_COLOR);
        separator.setBackground(SEPARATOR_COLOR);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(separator, BorderLayout.WEST);
        centerPanel.add(mainContent, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        cardLayout.show(mainContent, MAIN_SCREEN);
    }


    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT_COLOR_SIDEBAR); // *** Chữ trắng ngà ***
        button.setBackground(BUTTON_DEFAULT_BG);   // *** Nền nút Navy/Xám ***
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addActionListener(e -> {
            selectButton(button);
            cardLayout.show(mainContent, text);
        });

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


    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(BUTTON_DEFAULT_BG);         
            selectedButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            selectedButton.setForeground(TEXT_COLOR_SIDEBAR);        
        }
        selectedButton = button;
        if (selectedButton != null) {
             selectedButton.setBackground(BUTTON_SELECTED_BG);       
             selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
             selectedButton.setForeground(BUTTON_SELECTED_TEXT);     
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