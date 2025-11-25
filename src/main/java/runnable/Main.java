package runnable;

import DAO.test.SyncAccManager;
import DAO.test.SyncActManager;
import com.formdev.flatlaf.FlatLightLaf;
import layouts.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws Exception {

        FlatLightLaf.setup();
        Font poppins = new Font("Poppins", Font.PLAIN, 12);
        Font poppinsSmall = new Font("Poppins", Font.PLAIN, 10);

        UIManager.put("Component.focusedBorderColor", new Color(220, 228, 55, 255));


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
        UIManager.put("ScrollBar.track", new Color(0,0,0,0));
        UIManager.put("ScrollBar.hoverTrackColor", new Color(0,0,0,0));
        UIManager.put("ScrollBar.pressedTrackColor", new Color(0,0,0,0));
        UIManager.put("ScrollBar.trackInsets", new Insets(0,0,0,0));

        UIManager.put("ScrollBar.thumb", new Color(255, 255, 255, 223));
        UIManager.put("ScrollBar.hoverThumbColor", new Color(255, 255, 255, 223));
        UIManager.put("ScrollBar.pressedThumbColor", new Color(222, 228, 109, 255));

        UIManager.put("Component.borderColor",  new Color(202, 208, 216));
        UIManager.put("PasswordField.showRevealButton",  true);
        Icon eyeIcon = new ImageIcon("src/main/resources/images/Eye.png");
        UIManager.put("PasswordField.revealIcon",   eyeIcon);

        UIManager.put("TableHeader.font", poppins );
        UIManager.put("Table.font", poppinsSmall );

        SyncAccManager syncAccManager = new SyncAccManager();
        syncAccManager.startSyncThread();

        SyncActManager syncActManager = new SyncActManager();
        syncActManager.syncAllActivities();

        //AccountSyncManager accountSyncManager = new AccountSyncManager();
        //accountSyncManager.startSyncThread();

        //ActivitySyncManager activitySyncManager = new ActivitySyncManager();
        //activitySyncManager.startActSyncThread();

        SwingUtilities.invokeLater(() -> {
            try {
                new LoginForm();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}