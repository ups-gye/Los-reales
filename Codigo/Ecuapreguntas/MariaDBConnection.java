import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Registrar el driver JDBC de MariaDB
            Class.forName("org.mariadb.jdbc.Driver");

            // Establecer la conexión
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos!");
            } else {
                System.out.println("Fallo al conectarse a la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
