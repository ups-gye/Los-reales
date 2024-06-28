import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class HiloSocketG extends Thread {
    private Socket socket;
    private ServidorG servidor;
    private PrintWriter out_socket;
    private String nombreUsuario;

    public HiloSocketG(Socket socket, ServidorG servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    public void enviarMensaje(String mensaje) {
        out_socket.println(mensaje);
        out_socket.flush();
    }

    @Override
    public void run() {
        String ipJugador = socket.getInetAddress().getHostAddress();
        try {
            BufferedReader in_socket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out_socket = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);

            // Leer el nombre de usuario
            nombreUsuario = in_socket.readLine();
            servidor.distribuirMensaje(nombreUsuario + " (" + ipJugador + ") se ha conectado.");

            // Mostrar ventana de confirmación en el servidor
            JOptionPane.showMessageDialog(servidor, "Se ha conectado el jugador: " + nombreUsuario, "Jugador Conectado", JOptionPane.INFORMATION_MESSAGE);

            String input;
            while ((input = in_socket.readLine()) != null) {
                servidor.distribuirMensaje(nombreUsuario + ": " + input);
                System.out.println("Mensaje recibido de " + nombreUsuario + " (" + ipJugador + "): " + input);
                if (input.equals("adios")) {
                    break;
                }
            }

            System.out.println("Jugador desconectado: " + nombreUsuario + " (" + ipJugador + ")");
            servidor.eliminarJugador(this, ipJugador); // Actualizamos el servidor para eliminar al cliente

            this.socket.close();
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + nombreUsuario + " (" + ipJugador + ")");
            servidor.eliminarJugador(this, ipJugador); // Aseguramos que el cliente se elimina si hay un error
        }
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
