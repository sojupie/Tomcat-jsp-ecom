package olle_christoffer.utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class JDBC {

    private static volatile String url;
    private static volatile String user;
    private static volatile String password;

    private JDBC() {
    }

    public static Connection getConnection() throws SQLException {
        ensureLoaded();
        return DriverManager.getConnection(url, user, password);
    }

    private static synchronized void ensureLoaded() throws SQLException {
        if (url != null) {
            return; // om redan är inläst
        }

        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "5432");
        String name = env("DB_NAME", "webshop");

        user = env("DB_USER", "webshop");
        password = env("DB_PASSWORD", "webshop_dev");
        url = "jdbc:postgresql://" + host + ":" + port + "/" + name;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL-drivrutinen saknas pa classpath. "
                    + "Kontrollera beroendet org.postgresql:postgresql i pom.xml.", e);
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value != null && !value.isBlank()) ? value : fallback;
    }
}