import javax.swing.*;
import java.awt.*;

public class CustomCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // Set custom font and padding
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Alternate row colors for better readability
        if (!isSelected) {
            label.setBackground(index % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
        }
        
        // Customize selection color
        if (isSelected) {
            label.setBackground(new Color(34, 45, 65));
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(Color.BLACK);
        }
        
        return label;
    }
}
