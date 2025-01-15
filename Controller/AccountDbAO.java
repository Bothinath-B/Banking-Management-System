package Bank_Management.Controller;
import Bank_Management.Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDbAO {
    public int setDataToTable(Account ac,int cId) throws SQLException
    {
        Connection con=new DbConnection().getDbConnection();
        String query="insert into account(customerId,panNo,accountType,balance) values(?,?,?,?);";
        PreparedStatement preparedStatement= con.prepareStatement(query);
        preparedStatement.setInt(1,cId);
        preparedStatement.setString(3,ac.getAccountType());
        preparedStatement.setString(2,ac.getUniqueID());
        preparedStatement.setInt(4,ac.getBalance());
        int done=preparedStatement.executeUpdate();
        return done;
    }
}
