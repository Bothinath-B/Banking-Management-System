package Bank_Management.Model;
import Bank_Management.Controller.CustomerDbAO;
import Bank_Management.Controller.DbConnection;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String password;
    private String panNo;
    private String phone;
    private String address;
    Scanner sc = new Scanner(System.in);
    public void setNewData()
    {
        System.out.print("Enter your fullName:");
        this.name= sc.nextLine();
        System.out.print("Enter your email:");
        String mailId=sc.nextLine();
        if(checkEmail(mailId)) this.email=mailId;
        else
        {
           while (!checkEmail(mailId))
           {
               System.out.println("Invalid email!");
               System.out.print("Please enter the correct email:");
               mailId=sc.nextLine();
           }
           this.email=mailId;
        }
        System.out.print("Enter your PAN number:");
        String tempPan=sc.nextLine();
        if(isValidPAN(tempPan))
        {
            this.panNo=tempPan;
        }
        else
        {
            while (!isValidPAN(tempPan))
            {
                System.out.println("Invalid PAN number!");
                System.out.print("Please enter the valid PAN number:");
                tempPan=sc.nextLine();
            }
            this.panNo=tempPan;
        }
        System.out.print("Enter your phone number:");
        String phoneNo=sc.nextLine();
        if(phoneNo.length()==10) this.phone=phoneNo;
        else{
            while (phoneNo.length()!=10)
            {
                System.out.println("Invalid phoneNumber!");
                System.out.print("Please enter the correct phoneNumber:");
                phoneNo=sc.next();
            }
            this.phone=phoneNo;
        }
        System.out.print("Enter your address:");
        this.address=sc.nextLine();
        System.out.print("Enter the password:");
        String pass=sc.nextLine();
        if(checkPassword(pass)) this.password=pass;
        else{
            while (!checkPassword(pass))
            {
                System.out.println("Invalid password!");
                System.out.println("The password must contains atleast one upperCase," +
                        "one special character"+" and one number and password length must be 8 or greater");
                System.out.print("Please enter the correct password:");
                pass=sc.nextLine();
            }
            this.password=pass;
        }
    }

    public boolean checkEmail(String str)
    {
        String reg="^[a-zA-Z0-9]+[0-9]+@gmail+\\.com$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(str);
        if(matcher.matches()) return true;
        return false;
    }

    public boolean checkPassword(String pas)
    {
        String reg="^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%&*/])[A-Za-z0-9@#$%&*/]{8,}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher= pattern.matcher(pas);
        if(matcher.matches()) return true;
        return  false;
    }

    public  boolean isValidPAN(String pan)
    {
        String reg="^[A-Z]{5}[0-9]{4}[A-Z]$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(pan);
        if(matcher.matches()) return true;

        return false;
    }

    public boolean isThisNewData() throws SQLException
    {
        Connection con= new DbConnection().getDbConnection();
        Statement statement=con.createStatement();
        String query="select panNo from account";
        ResultSet rs=statement.executeQuery(query);
        while (rs.next())
        {
            if(this.panNo.equals(rs.getString(1))) return false;
        }
        return true;
    }

    public String getPANNo() {
        return panNo;
    }

    public int  sendDataToTable() throws SQLException {
        CustomerDbAO customerDbAO=new CustomerDbAO();
        int flag=customerDbAO.setDataToTable(this.name, this.email, this.panNo,this.phone, this.address, this.password);
        this.customerId=flag;
        return flag;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public boolean isValidate(String uName,String passCode) throws SQLException
    {
        Connection con= new DbConnection().getDbConnection();
        Statement statement= con.createStatement();
        String query="select * from Customer";
        ResultSet rs=statement.executeQuery(query);
        while (rs.next())
        {
            if(uName.equals(rs.getString(3)) && passCode.equals(rs.getString(4)))
            {
                System.out.println();
                return true;
            }
        }
        return false;
    }

    public void getAccountDetails(String u,String P) throws SQLException
    {
        int id=0;
        try(Connection con=new DbConnection().getDbConnection()){
            String cusid="select customerId from customer where email=(?) and password=(?)";
            System.out.println();
            PreparedStatement preparedStatement=con.prepareStatement(cusid);
            preparedStatement.setString(1,u);
            preparedStatement.setString(2,P);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                id=rs.getInt("customerId");
            }
            String query1="select * from customer where customerId="+id;
            String query2="select * from account where customerId="+id;
            Statement stmt1=con.createStatement();
            Statement stmt2=con.createStatement();
            ResultSet rs1=stmt1.executeQuery(query1);
            ResultSet rs2=stmt2.executeQuery(query2);
            while (rs1.next() && rs2.next())
            {
                System.out.println("Welcome "+rs1.getString("name")+"!");
                System.out.println("CustomerId:"+rs1.getInt("customerId")
                        + "\t\t\t AccountNo:"+rs2.getInt("accountId"));
                System.out.println("Email:"+rs1.getString("email")
                        +"\t AccountType:"+rs2.getString("accountType"));
                System.out.println("PhoneNo:"+rs1.getString("phone")
                        +"\t\t PANNo:"+ rs1.getString("panNo"));
                System.out.println();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
   public void getBalance(String u,String P)
   {
       int id=0;
       try(Connection con=new DbConnection().getDbConnection()){
           String cusid="select customerId from customer where email=(?) and password=(?)";
           System.out.println();
           PreparedStatement preparedStatement=con.prepareStatement(cusid);
           preparedStatement.setString(1,u);
           preparedStatement.setString(2,P);
           ResultSet rs=preparedStatement.executeQuery();
           while (rs.next())
           {
               id=rs.getInt("customerId");
           }
           String query="select * from account where customerId="+id;
           Statement statement= con.createStatement();
           ResultSet r=statement.executeQuery(query);
           while (r.next())
           {
               System.out.println("Remaining Balance:"+r.getInt("balance"));
           }
           System.out.println();
       }catch (SQLException e){
           e.printStackTrace();
       }
   }
}
