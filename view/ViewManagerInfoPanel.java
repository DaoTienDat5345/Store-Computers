package view;

import model.Manager; // Cần tạo lớp Service để lấy đối tượng Manager
import service.ManagerInfoService; // Cần tạo Service này

import javax.swing.*;
import java.awt.*;

public class ViewManagerInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel lblImage;
    private JLabel lblName, lblUserName, lblId; // Ví dụ thông tin cần hiển thị
    private ManagerInfoService managerService;

    public ViewManagerInfoPanel(int managerID) { // Giả sử ID được truyền vào
        this.managerService = new ManagerInfoService();
        setLayout(new BorderLayout(20, 0)); // Thêm khoảng cách ngang
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Thêm padding

        // --- Panel trái (Ảnh) ---
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Dùng FlowLayout
        leftPanel.setPreferredSize(new Dimension(200, 200)); // Kích thước ảnh gợi ý
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(180, 180)); // Kích thước ảnh
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Thêm viền
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setVerticalAlignment(JLabel.CENTER);
        leftPanel.add(lblImage);

        // --- Panel phải (Thông tin) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        Font font = new Font("Arial", Font.PLAIN, 16);
        Font boldFont = new Font("Arial", Font.BOLD, 18);

        lblId = createInfoLabel("", boldFont); // ID có thể không cần hiển thị rõ
        lblName = createInfoLabel("", font);
        lblUserName = createInfoLabel("", font);
        // Thêm các label khác nếu cần

        rightPanel.add(createHeaderLabel("Thông tin Quản lý", boldFont));
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(lblId);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblName);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblUserName);
        // add các label khác

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // --- Load dữ liệu ---
        loadManagerInfo(managerID);
    }

    // Hàm tiện ích tạo JLabel
    private JLabel createInfoLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái
        return label;
    }
     private JLabel createHeaderLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái
        label.setForeground(Color.BLUE); // Màu tiêu đề
        return label;
    }


    private void loadManagerInfo(int managerID) {
        // Gọi Service để lấy thông tin Manager
        Manager manager = managerService.getManagerById(managerID); // Service cần có hàm này

        if (manager != null) {
            // Tạm thời chỉ hiển thị thông tin cơ bản từ model Manager đã có
            // String imagePath = manager.getImagePath(); // Giả sử model Manager có trường này
            String name = "Quản lý"; // Tên chưa có trong model Manager hiện tại
            String userName = manager.getUserManager();
            int id = manager.getManagerID();

            // Gán dữ liệu vào giao diện
            // lblImage.setIcon(createScaledIcon(imagePath, 180, 180)); // Hàm tạo icon scaled
            lblImage.setText("Ảnh QL"); // Placeholder nếu không có ảnh
            lblId.setText("ID: " + id);
            lblName.setText("Tên: " + name);
            lblUserName.setText("Username: " + userName);

        } else {
            // Hiển thị thông báo lỗi trên giao diện thay vì JOptionPane
             removeAll(); // Xóa các thành phần cũ
             add(new JLabel("Không thể tải thông tin quản lý (ID: " + managerID + ")"), BorderLayout.CENTER);
             revalidate();
             repaint();
        }
    }

    // Hàm tiện ích để scale ảnh (nếu cần)
    private ImageIcon createScaledIcon(String path, int width, int height) {
        if (path == null || path.isEmpty()) return null;
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Lỗi tải ảnh: " + path + " - " + e.getMessage());
            return null; // Trả về null nếu lỗi
        }
    }
}