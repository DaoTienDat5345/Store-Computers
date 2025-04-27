package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    private final int width;
    private final int height;

    public ImageRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        if (value instanceof String) {
            String imagePath = (String) value;
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
                label.setText("");
            } catch (Exception e) {
                label.setIcon(null);
                label.setText("Lỗi tải hình ảnh");
            }
        } else {
            label.setIcon(null);
            label.setText("Không có hình ảnh");
        }

        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
        } else {
            label.setBackground(table.getBackground());
        }

        return label;
    }
}