package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.Locale;
import utils.QRCodeHelper; // Import helper

public class ViewPaymentQRDialog extends JDialog {

    private static final int QR_CODE_SIZE = 250; // Kích thước ảnh QR

    /**
     * Khởi tạo Dialog hiển thị mã QR.
     * @param owner Frame cha.
     * @param amount Số tiền cần thanh toán.
     * @param qrData Chuỗi dữ liệu để tạo QR (ví dụ: chuỗi VietQR).
     * @param paymentConfirmationCallback Runnable sẽ được gọi khi người dùng nhấn "Đã thanh toán".
     */
    public ViewPaymentQRDialog(Frame owner, double amount, String qrData, Runnable paymentConfirmationCallback) {
        super(owner, "Quét mã để thanh toán", true); // true = modal dialog
        setSize(350, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // BorderLayout với khoảng cách

        // --- Panel Nội dung ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(contentPanel, BorderLayout.CENTER);

        // --- Hiển thị QR Code ---
        JLabel qrCodeLabel = new JLabel();
        qrCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qrCodeLabel.setPreferredSize(new Dimension(QR_CODE_SIZE, QR_CODE_SIZE));
        qrCodeLabel.setMinimumSize(new Dimension(QR_CODE_SIZE, QR_CODE_SIZE));
        qrCodeLabel.setMaximumSize(new Dimension(QR_CODE_SIZE, QR_CODE_SIZE));
        qrCodeLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        BufferedImage qrImage = QRCodeHelper.generateQRCodeImage(qrData, QR_CODE_SIZE, QR_CODE_SIZE);
        if (qrImage != null) {
            qrCodeLabel.setIcon(new ImageIcon(qrImage));
        } else {
            qrCodeLabel.setText("Lỗi tạo mã QR");
            qrCodeLabel.setForeground(Color.RED);
            qrCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        contentPanel.add(qrCodeLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        // --- Thông tin thanh toán ---
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        JLabel amountLabel = new JLabel("Số tiền: " + currencyFormat.format(amount));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        JLabel instructionLabel = new JLabel("Quét mã bằng ứng dụng Ngân hàng/Ví điện tử.");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(15));


        // --- Nút xác nhận ---
        JButton confirmButton = new JButton("Tôi đã thanh toán / Tiếp tục");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> {
            if (paymentConfirmationCallback != null) {
                paymentConfirmationCallback.run(); // Gọi callback
            }
            dispose(); // Đóng dialog
        });
        contentPanel.add(confirmButton);

        pack(); // Tự điều chỉnh kích thước dựa trên nội dung
        setMinimumSize(new Dimension(300, 400)); // Kích thước tối thiểu
    }
}