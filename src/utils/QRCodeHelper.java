package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeHelper {

    /**
     * Tạo ảnh BufferedImage của mã QR từ một chuỗi text.
     *
     * @param text   Nội dung cần mã hóa thành QR code.
     * @param width  Chiều rộng mong muốn của ảnh QR.
     * @param height Chiều cao mong muốn của ảnh QR.
     * @return BufferedImage chứa mã QR, hoặc null nếu có lỗi.
     */
    public static BufferedImage generateQRCodeImage(String text, int width, int height) {
        if (text == null || text.trim().isEmpty()) {
            System.err.println("Nội dung QR không được để trống.");
            return null;
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // Mức sửa lỗi (L, M, Q, H)
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Đảm bảo hỗ trợ tiếng Việt
        hints.put(EncodeHintType.MARGIN, 1); // Đặt lề nhỏ (1 block)

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.err.println("Lỗi khi tạo ma trận QR code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi BufferedImage thành byte array (ví dụ để lưu hoặc truyền đi).
     *
     * @param image Ảnh BufferedImage.
     * @param format Định dạng ảnh (ví dụ "png").
     * @return Mảng byte của ảnh, hoặc null nếu lỗi.
     */
    public static byte[] imageToBytes(BufferedImage image, String format) {
         if (image == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Lỗi khi chuyển đổi ảnh sang bytes: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

     /**
     * Chuyển đổi byte array thành BufferedImage.
     *
     * @param imageData Mảng byte của ảnh.
     * @return BufferedImage, hoặc null nếu lỗi.
     */
    public static BufferedImage bytesToImage(byte[] imageData) {
         if (imageData == null || imageData.length == 0) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageData)) {
            return ImageIO.read(bais);
        } catch (IOException e) {
             System.err.println("Lỗi khi chuyển đổi bytes sang ảnh: " + e.getMessage());
             e.printStackTrace();
             return null;
        }
    }
    public static String createVietQRString(String bankBin, String accountNumber, double amount, String description) {
        if (bankBin == null || bankBin.isEmpty() || accountNumber == null || accountNumber.isEmpty()) {
             System.err.println("Mã BIN ngân hàng và số tài khoản không được để trống.");
             return null; // Hoặc ném IllegalArgumentException
        }
        if (amount < 0) {
             System.err.println("Số tiền không hợp lệ.");
             return null; // Hoặc ném IllegalArgumentException
        }

        long amountInt = (long) amount; // VietQR thường dùng số nguyên

        String payloadFormatIndicator = "000201";
        String pointOfInitiationMethod = "010212";

        // Merchant Account Information (Tag 38)
        String guid = "A000000727";
        String bankInfoTag = "00";
        String bankBinField = "00" + String.format("%02d", bankBin.length()) + bankBin; // Sửa lại length
        String merchantInfoTag = "01";
        String accountNumberField = "01" + String.format("%02d", accountNumber.length()) + accountNumber;
        String merchantAccountInfo = guid + bankInfoTag + bankBinField + merchantInfoTag + accountNumberField;
        String merchantAccountInfoField = "38" + String.format("%02d", merchantAccountInfo.length()) + merchantAccountInfo;

        String transactionCurrency = "5303704";
        String transactionAmountField = "";
        if (amountInt > 0) {
            String amountStr = String.valueOf(amountInt);
            transactionAmountField = "54" + String.format("%02d", amountStr.length()) + amountStr;
        }

        String countryCode = "5802VN";

        // Additional Data Field Template (Tag 62)
        String descriptionSanitized = (description == null) ? "" : description.replaceAll("[^a-zA-Z0-9 ]", "").toUpperCase(); // Chuẩn hóa và viết hoa không dấu
         // Giới hạn độ dài mô tả nếu cần
        if (descriptionSanitized.length() > 50) { // Ví dụ giới hạn 50 ký tự
            descriptionSanitized = descriptionSanitized.substring(0, 50);
        }

        String purposeOfTransactionTag = "08";
        String purposeOfTransactionField = purposeOfTransactionTag + String.format("%02d", descriptionSanitized.length()) + descriptionSanitized;
        String additionalDataField = "62" + String.format("%02d", purposeOfTransactionField.length()) + purposeOfTransactionField;

        // Chuỗi chưa có CRC
        String qrStringWithoutCRC = payloadFormatIndicator + pointOfInitiationMethod + merchantAccountInfoField +
                                   transactionCurrency + transactionAmountField + countryCode + additionalDataField;

        
        String crc = "ABCD"; // Placeholder
        String crcField = "6304" + crc;

        return qrStringWithoutCRC + crcField;
    }
}