import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class JugadorTCP {

    private String ip;
    private int puerto;
    private String username;
    private PrintWriter out_socket;
    private BufferedReader in_socket;
    private JTextArea areaMensajes;
    private JTextField campoMensaje;
    private JButton botonEnviar;
    private JFrame frame;

    public JugadorTCP(String ip, int puerto, String username) {
        this.ip = ip;
        this.puerto = puerto;
        this.username = username;
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Jugador - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaMensajes);

        campoMensaje = new JTextField();
        campoMensaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void conectar() {
        try {
            Socket socket = new Socket(ip, puerto);
            out_socket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out_socket.println(username); // Enviar nombre de usuario al servidor

            new Thread(() -> {
                String input;
                try {
                    while ((input = in_socket.readLine()) != null) {
                        if (input.startsWith("PREGUNTA:")) {
                            mostrarPregunta(input.substring(9));
                        } else {
                            String mensaje = input;
                            SwingUtilities.invokeLater(() -> areaMensajes.append(mensaje + "\n"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: Host desconocido.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error de entrada/salida.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarMensaje() {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            out_socket.println(mensaje);
            campoMensaje.setText("");
        }
    }

    private void mostrarPregunta(String pregunta) {
        String[] partes = pregunta.split(";");
        if (partes.length == 5) {
            String preguntaTexto = partes[0];
            String[] opciones = {partes[1], partes[2], partes[3], partes[4]};

            JFrame preguntaFrame = new JFrame("Pregunta");
            preguntaFrame.setSize(400, 300);
            preguntaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(5, 1));
            panel.add(new JLabel(preguntaTexto));

            ButtonGroup grupoOpciones = new ButtonGroup();
            for (String opcion : opciones) {
                JRadioButton botonOpcion = new JRadioButton(opcion);
                grupoOpciones.add(botonOpcion);
                panel.add(botonOpcion);
            }

            JButton botonResponder = new JButton("Responder");
            botonResponder.addActionListener(e -> {
                ButtonModel seleccion = grupoOpciones.getSelection();
                if (seleccion != null) {
                    String respuesta = seleccion.getActionCommand();
                    out_socket.println("RESPUESTA:" + respuesta);
                    preguntaFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(preguntaFrame, "Debe seleccionar una respuesta.");
                }
            });

            panel.add(botonResponder);
            preguntaFrame.add(panel);
            preguntaFrame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        String ip = JOptionPane.showInputDialog("Ingrese la IP del servidor:");
        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto del servidor:"));
        String username = JOptionPane.showInputDialog("Ingrese su nombre de usuario:");

        JugadorTCP jugador = new JugadorTCP(ip, puerto, username);
        jugador.conectar();
    }
}
