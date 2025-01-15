package Bank_Management.Controller;
import Bank_Management.Model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDbAo {
    public void sendDataToTable(Transaction tr)
    {
     try(Connection con=new DbConnection().getDbConnection()){
         String insertQuery="insert into transaction(accountId,transactionType,amount,remainingBalance,toTransfer)" +
                 "values(?,?,?,?,?);";
         PreparedStatement preparedStatement=con.prepareStatement(insertQuery);
         preparedStatement.setInt(1,tr.getAccountId());
         preparedStatement.setString(2,tr.getTransactionType());
         preparedStatement.setInt(3,tr.getAmount());
         preparedStatement.setInt(4,tr.getRemainingBalance());
         preparedStatement.setString(5,tr.getToTransfer());
         preparedStatement.executeUpdate();

         if(tr.getTransactionType().equalsIgnoreCase("Transfer")
                 && tr.getrTransactionType().equalsIgnoreCase("Credit")){
             String insertQuery2="insert into transaction(accountId,transactionType,amount,remainingBalance,toTransfer)" +
                     "values(?,?,?,?,?);";
             PreparedStatement ps2= con.prepareStatement(insertQuery2);
             ps2.setInt(1,tr.getrAccountId());
             ps2.setString(2,tr.getrTransactionType());
             ps2.setInt(3,tr.getrAmount());
             ps2.setInt(4,tr.getrRemainingBalance());
             ps2.setString(5,tr.getrFromTransferred());
             ps2.executeUpdate();
         }
     }catch (SQLException e){
         e.printStackTrace();
     }
    }
}
