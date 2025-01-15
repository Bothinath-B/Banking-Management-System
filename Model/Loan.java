package Bank_Management.Model;
import Bank_Management.Controller.DbConnection;

import java.sql.*;
import java.util.Scanner;

public class Loan {
    private int loanId;
    private int customerId;
    private String loanType;
    private int amount;
    private double interestRate;
    private String status;

    public int getLoanId() {
        return loanId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    Scanner sc=new Scanner(System.in);
    public void getLoan(String un,String ps) {
        int id = 0;
        try (Connection con = new DbConnection().getDbConnection()) {
            String cusid = "select customerId from customer where email=(?) and password=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(cusid);
            preparedStatement.setString(1, un);
            preparedStatement.setString(2, ps);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.customerId=id;
        System.out.print("Enter the loan amount:");
        String LAmount=sc.nextLine();
        this.amount=Integer.parseInt(LAmount);
        String lt;
        System.out.print("Enter the loanType[personal,home,education]:");
        lt=sc.nextLine();
        if(isValidLoantype(lt)) this.loanType=lt;
        else{
            while (!isValidLoantype(lt))
            {
                System.out.println("Invalid loanType!");
                System.out.print("Please enter the correct loanType:");
                lt=sc.nextLine();
            }
            this.loanType=lt;
        }
        if(getLoanType().equalsIgnoreCase("personal"))
        {
            setInterestRate(10.5);
        }
        else if(getLoanType().equalsIgnoreCase("home")){
            setInterestRate(7.5);
        }
        else{
            setInterestRate(8.5);
        }
    }
    public boolean isValidLoantype(String loanT)
    {
        if(loanT.equalsIgnoreCase("personal")||
        loanT.equalsIgnoreCase("home")||
                loanT.equalsIgnoreCase("education")) return true;

        return false;
    }

    public boolean isLoanAlreadyNotExists()
    {
        try(Connection con=new DbConnection().getDbConnection()){
            String query="select * from loan";
            Statement statement= con.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
              if(this.customerId==rs.getInt("customerId"))
                  return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public void getLoanDetail(String un,String ps)
    {
        int id = 0;
        try (Connection con = new DbConnection().getDbConnection()) {
            String cusid = "select customerId from customer where email=(?) and password=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(cusid);
            preparedStatement.setString(1, un);
            preparedStatement.setString(2, ps);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(Connection con=new DbConnection().getDbConnection())
        {
            boolean loanexist=false;
            String query="select * from loan where customerId="+id;
            Statement statement=con.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                loanexist=true;
                System.out.println("Your LoanId:"+rs.getInt(1));
                System.out.println("Loan Type:"+rs.getString(3));
                System.out.println("Amount:"+rs.getInt(4));
                System.out.println("InterestRate:"+rs.getDouble(5)+"%");
                if(rs.getString(6).equalsIgnoreCase("pending"))
                    System.out.println("Status:Please wait still "+rs.getString(6)+"!");
                else{
                    System.out.println("Status:"+rs.getString(6));
                }
            }
            if(loanexist==false) System.out.println("No loan Applied!");
            System.out.println();
        }catch (SQLException E){
            E.printStackTrace();
        }
    }

    public void getAllLoans(){
        try(Connection con=new DbConnection().getDbConnection()){
            String query="{call getAllLoanDetail()}";
            CallableStatement callableStatement=con.prepareCall(query);
            ResultSet rs=callableStatement.executeQuery(query);
            int count=0;
            while (rs.next()){
                count++;
                if(count==1){
                    System.out.printf("%-8s %-12s %-10s %-10s %-12s %-8s %-10s %-10s %-12s %-8s%n",
                            "loanId", "customerId", "name", "accountId", "accountType", "balance",
                            "loanType", "loanAmount", "interestRate", "status");
                    System.out.println("-----------------------------------------------------" +
                             "--------------------------------------------------------");
                }
                System.out.printf("%-8d %-12d %-10s %-10d %-12s %-8.2f %-10s %-10.2f %-12.2f %-8s%n",
                        rs.getInt("loanId"),
                        rs.getInt("customerId"),
                        rs.getString("name"),
                        rs.getInt("accountId"),
                        rs.getString("accountType"),
                        rs.getDouble("balance"),
                        rs.getString("loanType"),
                        rs.getDouble("loanAmount"),
                        rs.getDouble("interestRate"),
                        rs.getString("status"));
            }
            System.out.println();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateStatus(int id,String stat){
        try (Connection con=new DbConnection().getDbConnection()){
            String updateQuery="update loan set status=(?) where loanId=(?)";
            PreparedStatement preparedStatement= con.prepareStatement(updateQuery);
            preparedStatement.setString(1,stat);
            preparedStatement.setInt(2,id);
            int done= preparedStatement.executeUpdate();
            if(done>0) System.out.println("Status Updated!");
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
