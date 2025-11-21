package runnable;

import com.formdev.flatlaf.FlatLightLaf;
import layouts.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        FlatLightLaf.setup();
        Font poppins = new Font("Poppins", Font.PLAIN, 12);
        Font poppinsSmall = new Font("Poppins", Font.PLAIN, 10);

        UIManager.put("Component.focusedBorderColor", new Color(220, 228, 55));
        UIManager.put("RadioButton.icon.focusedBorderColor", new Color(220, 228, 55));
        UIManager.put("RadioButton.icon.focusedBorderColor", new Color(220, 228, 55));
        UIManager.put("Button.borderColor", new Color(220, 228, 55));
        UIManager.put("Button.hoverBorderColor", new Color(220, 228, 55));
        UIManager.put("TextField.focusedBorderColor", new Color(220, 228, 55));
        UIManager.put("PasswordField.focusedBorderColor", new Color(220, 228, 55));
        UIManager.put("Button.focusedBorderColor", new Color(220, 228, 55));

        UIManager.put("TextField.innerFocusWidth", 0);
        UIManager.put("PasswordField.innerFocusWidth", 0);
        UIManager.put("Button.innerFocusWidth", 0);
        UIManager.put("Table.selectionBackground", new Color(220, 228, 55));
        UIManager.put("TabbedPane.underlineColor", new Color(220, 228, 55));
        UIManager.put("TabbedPane.cardTabarc ", 20);
        UIManager.put("TabbedPane.buttonArc ", 20);
        UIManager.put("List.selectionBackground", new Color(39, 65, 75));
        UIManager.put("List.select  ionArc", 20);
        UIManager.put("List.selectionInsets", new Insets(3, 1, 3, 1));
        UIManager.put("Button.toolbar.hoverForeground",  new Color(220, 228, 55));
        UIManager.put("Button.toolbar.hoverBackground",  new Color(31, 52, 62));
        UIManager.put("Button.toolbar.pressedBackground",  new Color(39, 65, 75));
        UIManager.put("TableHeader.font", poppins );
        UIManager.put("Table.font", poppinsSmall );



        SwingUtilities.invokeLater(() -> {
            try {
                new LoginForm();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}