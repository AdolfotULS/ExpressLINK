package expresslink.view.components;

import javax.swing.BorderFactory;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class NotificationPopup {
    private final JWindow window;

    public NotificationPopup(JFrame parent, String message) {
        // Crear una ventana sin bordes
        window = new JWindow(parent);

        // Configurar el contenido
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50, 200)); // Fondo semitransparente
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(messageLabel, BorderLayout.CENTER);

        // Agregar el panel a la ventana
        window.getContentPane().add(panel);
        window.setSize(300, 100);

        // Posición de la ventana centrada en el JFrame
        int x = parent.getX() + (parent.getWidth() - window.getWidth()) / 2;
        int y = parent.getY() + (parent.getHeight() - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    public void showNotification(int duration) {
        window.setVisible(true);

        // Ocultar la notificación después de un tiempo
        Timer timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                window.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
