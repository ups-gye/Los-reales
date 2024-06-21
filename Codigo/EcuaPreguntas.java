import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcuaPreguntas extends JFrame {

    public EcuaPreguntas() {
     
        setTitle("EcuaPreguntas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

     
        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon("image.jpg").getImage());
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

      
        TitlePanel titlePanel = new TitlePanel("EcuaPreguntas");
        titlePanel.setPreferredSize(new Dimension(800, 200));
        titlePanel.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(titlePanel, gbc);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 300, 20, 300));

        JButton newGameButton = createCloudButton("Nueva Partida");
        JButton instructionsButton = createCloudButton("Instrucciones");
        JButton exitButton = createCloudButton("Salir");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para nueva partida
                JOptionPane.showMessageDialog(null, "Nueva Partida seleccionada");
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para instrucciones
                JOptionPane.showMessageDialog(null, "Instrucciones seleccionadas");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para salir
                System.exit(0);
            }
        });

        buttonPanel.add(newGameButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(exitButton);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Añadir el panel principal a la ventana
        add(mainPanel);
    }

    private JButton createCloudButton(String text) {
        return new CloudButton(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EcuaPreguntas().setVisible(true);
            }
        });
    }

   
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image image) {
            this.backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }


    class CloudButton extends JButton {
        public CloudButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Dibujar forma de nube
            g2.setColor(new Color(0, 153, 76));
            g2.fillRoundRect(0, 0, width, height, height, height);

            // Dibujar borde de nube
            g2.setColor(new Color(0, 102, 51));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, width, height, height, height);

            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No dibujar el borde predeterminado
        }
    }


    class TitlePanel extends JPanel {
        private String title;
        private float hue = 0.0f;

        public TitlePanel(String title) {
            this.title = title;
            Timer timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hue += 0.01f;
                    if (hue > 1.0f) {
                        hue = 0.0f;
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("Comic Sans MS", Font.BOLD, 80); // Tamaño de fuente más grande
            g2d.setFont(font);

            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(title)) / 2;
            int y = (getHeight() / 2) + fm.getAscent() / 2;

            // Dibujar texto con sombra para efecto 3D
            g2d.setColor(Color.GRAY);
            g2d.drawString(title, x + 5, y + 5);

            // Dibujar texto con efecto de brillo
            g2d.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));
            g2d.drawString(title, x, y);

            // Colores de la bandera de Ecuador
            Color[] colors = { Color.YELLOW, Color.BLUE, Color.RED };
            x = (getWidth() - fm.stringWidth(title)) / 2; // Reset x position

            for (int i = 0; i < title.length(); i++) {
                g2d.setColor(colors[i % colors.length]);
                g2d.drawString(String.valueOf(title.charAt(i)), x, y);
                x += fm.charWidth(title.charAt(i));
            }
        }
    }
}
