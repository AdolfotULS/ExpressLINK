package expresslink.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class LogCard extends JPanel {
    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected final Color backgroundColor;
    protected final Font titleFont = new Font("Segoe UI", Font.BOLD, 14);
    protected final Font contentFont = new Font("Segoe UI", Font.PLAIN, 12);

    protected JLabel titleLabel;
    protected JLabel dateLabel;
    protected JTextArea contentArea;
    protected Date logDate;

    public LogCard(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        initializeCard();
    }

    private void initializeCard() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(backgroundColor);

        // Header Panel (Title and Date)
        JPanel headerPanel = new JPanel(new BorderLayout(5, 0));
        headerPanel.setBackground(backgroundColor);

        titleLabel = new JLabel();
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);

        dateLabel = new JLabel();
        dateLabel.setFont(contentFont);
        dateLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        // Content Area
        contentArea = new JTextArea();
        contentArea.setFont(contentFont);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(backgroundColor);
        contentArea.setForeground(Color.WHITE);
        contentArea.setBorder(null);

        add(headerPanel, BorderLayout.NORTH);
        add(contentArea, BorderLayout.CENTER);
    }

    public Date getLogDate() {
        return logDate;
    }

    protected void setLogDate(Date date) {
        this.logDate = date;
        this.dateLabel.setText(DATE_FORMAT.format(date));
    }

    // Implementing cards should override these methods
    protected abstract void setLogContent(Object logObject);
}