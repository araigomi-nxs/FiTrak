package layouts.widgets;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.Locale;

public class ContributionGridPanel extends JPanel {

    private static final String DB_URL = "jdbc:sqlite:FitrakAccount.db";

    public ContributionGridPanel(long userID, int weeks) {
        setLayout(new BorderLayout());


        // --- Left month labels ---
        JPanel monthPanel = new JPanel(new GridLayout(weeks, 1));
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusWeeks(weeks).with(java.time.DayOfWeek.SUNDAY);

        Month lastMonth = null;
        for (int w = 0; w < weeks; w++) {
            LocalDate weekStart = start.plusWeeks(w);
            Month currentMonth = weekStart.getMonth();

            JLabel monthLabel;
            if (currentMonth != lastMonth) {
                monthLabel = new JLabel(
                        currentMonth.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        SwingConstants.RIGHT
                );
                lastMonth = currentMonth;
            } else {
                monthLabel = new JLabel("", SwingConstants.RIGHT); // spacer
            }
            monthPanel.add(monthLabel);
        }
        add(monthPanel, BorderLayout.WEST);



        // --- Main grid: weeks stacked vertically, days left-to-right ---
        JPanel gridPanel = new JPanel(new GridLayout(weeks, 7, 2, 2));

        // Query activity counts
        Map<LocalDate, Integer> activityMap = new HashMap<>();
        String sql = "SELECT date(startDT) AS d, COUNT(*) AS c " +
                "FROM activities WHERE userID = ? GROUP BY date(startDT)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate d = LocalDate.parse(rs.getString("d"));
                    int count = rs.getInt("c");
                    activityMap.put(d, count);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching activities: " + e.getMessage());
        }

        // Fill grid: each row = one week, each column = day
        for (int w = 0; w < weeks; w++) {
            for (int d = 0; d < 7; d++) {
                LocalDate date = start.plusWeeks(w).plusDays(d);
                int count = activityMap.getOrDefault(date, 0);

                JPanel cell = new JPanel();
                Dimension squareSize = new Dimension(20, 20); // max size of each square

                cell.setPreferredSize(squareSize);
                cell.setMaximumSize(squareSize);
                cell.setMinimumSize(squareSize);
                cell.setSize(squareSize);
                cell.putClientProperty(FlatClientProperties.STYLE, "arc:5");
                cell.setToolTipText(date.toString() + " → " + count + " activities");

                // Color intensity based on count
                if (count == 0) {
                    cell.setBackground(new Color(230, 230, 230)); // light gray
                } else if (count < 3) {
                    cell.setBackground(new Color(205, 209, 111)); // light green
                } else if (count < 6) {
                    cell.setBackground(new Color(166, 172, 36)); // medium green
                } else {
                    cell.setBackground(new Color(138, 142, 0));   // dark green
                }

                gridPanel.add(cell);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // --- Bottom day labels ---
        JPanel dayLabelPanel = new JPanel(new GridLayout(1, 7));
        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            dayLabelPanel.add(lbl);
        }
        add(dayLabelPanel, BorderLayout.SOUTH);
    }

    // Demo runner

}
