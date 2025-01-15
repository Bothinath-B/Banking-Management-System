package Bank_Management.Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DbConnection {
    public Connection getDbConnection() throws SQLException
    {
        String url="jdbc:mysql://localhost:3306/bankManagement";
        String user="root";
        String pass="Pass";
        Connection con= DriverManager.getConnection(url,user,pass);
        con.setAutoCommit(true);
        return con;
    }
}