import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	
	private static Connection connection;
	private static final String host = "jdbc:postgresql://db.doc.ic.ac.uk:5432/dk2709";
	private static final String username = "dk2709";
	private static final String password = "hzYrR3W5pL";
	
	public Connector() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(host,username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}

}
