
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexao {

    private static Connection con = null;
    public Connection criarconexcao() throws SQLException, ClassNotFoundException {
     String dbName = "teste";String userName = "root"; String password = "";
     String hostname = "localhost";String port = "3306";
     String jdbcUrl = "jdbc:mysql://" + hostname + ":"
     + port + "/" + dbName + "?user=" + userName + "&password=" + password+"&autoReconnect=true";
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(jdbcUrl);
        
        return con;
    }
    
    
    
    
}
