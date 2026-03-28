package saeae.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Look and feel institucional: FlatLaf, sans-serif (Segoe UI / Arial), azul marino y gris claro.
 */
public final class UiTheme {
    public static final Color NAVY = new Color(0x1a237e);
    public static final Color LIGHT_GRAY = new Color(0xeceff1);
    public static final Color ACCENT = new Color(0x3949ab);
    public static final Color HEADER_ON_NAVY = Color.WHITE;

    private static final int BASE_SIZE = 13;

    private UiTheme() {
    }

    public static Font uiFont() {
        return uiFont(BASE_SIZE);
    }

    public static Font uiFont(int size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Set<String> families = new HashSet<>(Arrays.asList(ge.getAvailableFontFamilyNames()));
        if (families.contains("Segoe UI")) {
            return new Font("Segoe UI", Font.PLAIN, size);
        }
        if (families.contains("Arial")) {
            return new Font("Arial", Font.PLAIN, size);
        }
        return new Font(Font.SANS_SERIF, Font.PLAIN, size);
    }

    public static Font headingFont() {
        return uiFont(BASE_SIZE + 1).deriveFont(Font.BOLD);
    }

    public static void install() {
        FlatLightLaf.setup();
        Font base = uiFont(BASE_SIZE);
        UIManager.put("defaultFont", base);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("List.font", base);
        UIManager.put("Table.font", base);
        UIManager.put("TextField.font", base);
        UIManager.put("TextArea.font", base);
        UIManager.put("CheckBox.font", base);
        UIManager.put("TabbedPane.font", base);
        UIManager.put("TitledBorder.font", headingFont());

        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 8);
        UIManager.put("Panel.background", LIGHT_GRAY);
        UIManager.put("TabbedPane.background", LIGHT_GRAY);
        UIManager.put("TabbedPane.foreground", NAVY);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.selectedForeground", NAVY);
        UIManager.put("TabbedPane.hoverColor", new Color(0xe8eaf6));
        UIManager.put("TabbedPane.underlineColor", ACCENT);
        UIManager.put("TabbedPane.contentAreaColor", LIGHT_GRAY);
        UIManager.put("ScrollPane.background", LIGHT_GRAY);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("List.background", Color.WHITE);

        UIManager.put("Button.default.background", ACCENT);
        UIManager.put("Button.default.foreground", Color.WHITE);
    }
}
