package Bank_Management.Controller;
import Bank_Management.Model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoanDbAo {
    public void sendDataToTable(Loan La)
    {
        try(Connection con=new DbConnection().getDbConnection()){
            {
                String query="insert into loan (customerId,amount,loanType,interestRate) values(?,?,?,?);";
                PreparedStatement preparedStatement= con.prepareStatement(query);
                preparedStatement.setInt(1,La.getCustomerId());
                preparedStatement.setInt(2,La.getAmount());
                preparedStatement.setString(3,La.getLoanType());
                preparedStatement.setDouble(4, La.getInterestRate());
                int done=preparedStatement.executeUpdate();
                if(done>0) System.out.println("Loan applied! Wait for the response.");
                System.out.println();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
