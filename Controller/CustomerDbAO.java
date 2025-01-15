package Bank_Management.Controller;
import java.sql.*;

public class CustomerDbAO {
    public static int setDataToTable(String name,String emailId,String PAN,String phoneNum,String Address,String pass)
            throws SQLException
    {
        Connection con=new DbConnection().getDbConnection();
        String query="insert into customer(name,email,panNo,phone,address,password) values(?,?,?,?,?,?);";
        PreparedStatement preparedStatement= con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,emailId);
        preparedStatement.setString(3,PAN);
        preparedStatement.setString(4,phoneNum);
        preparedStatement.setString(5,Address);
        preparedStatement.setString(6,pass);
        int done = preparedStatement.executeUpdate();
        if (done > 0) {
            ResultSet rs =preparedStatement.getGeneratedKeys();
            if (rs.next()) {

                return rs.getInt(1);
            }
        }
        return done;
    }
    public void toDeleteLastRecord()
    {
        try
        {
            Connection con= new DbConnection().getDbConnection();
            Statement statement= con.createStatement();
            String selectQuery="select customerId from customer order by customerId desc limit 1";
            ResultSet rs=statement.executeQuery(selectQuery);
            int lastAddedCusId=0;
            while (rs.next())
            {
                lastAddedCusId=rs.getInt(1);
                System.out.println(lastAddedCusId);
            }
            String deleteQuery="delete from customer where customerId="+lastAddedCusId;
            lastAddedCusId-=1;
            Statement statement1=con.createStatement();
            statement1.executeUpdate(deleteQuery);
            System.out.println("toDeleteLastRecord is called");
            String resetCusId="alter table customer auto_increment="+lastAddedCusId;
            Statement statement3= con.createStatement();
            statement3.executeUpdate(resetCusId);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
