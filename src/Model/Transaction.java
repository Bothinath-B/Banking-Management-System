package Bank_Management.Model;
import Bank_Management.Controller.DbConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionType;
    private int amount;
    private int remainingBalance;
    private String toTransfer="self";
    private Timestamp transactionDate;

    private int rAccountId;
    private int rAmount;
    private int rRemainingBalance;
    private String rTransactionType;
    private String rFromTransferred;

    Scanner sc=new Scanner(System.in);

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(int remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getToTransfer() {
        return toTransfer;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }


    public int getrAccountId() {
        return rAccountId;
    }

    public void setrAccountId(int rAccountId) {
        this.rAccountId = rAccountId;
    }

    public String getrTransactionType() {
        return rTransactionType;
    }

    public void setrTransactionType(String rTransactionType) {
        this.rTransactionType = rTransactionType;
    }

    public String getrFromTransferred() {
        return rFromTransferred;
    }

    public void setrFromTransferred(String rFromTransfered) {
        this.rFromTransferred = rFromTransfered;
    }

    public int getrAmount() {
        return rAmount;
    }

    public void setrAmount(int rAmount) {
        this.rAmount = rAmount;
    }

    public int getrRemainingBalance() {
        return rRemainingBalance;
    }

    public void setrRemainingBalance(int rRemainingBalance) {
        this.rRemainingBalance = rRemainingBalance;
    }

    public void makeTransaction(String userN, String passW)
    {
        int cid = 0;
        int aid=0;
        int bal=0;

        //to get customerID
        try (Connection con = new DbConnection().getDbConnection()) {
            String cusid = "select customerId from customer where email=(?) and password=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(cusid);
            preparedStatement.setString(1, userN);
            preparedStatement.setString(2, passW);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cid = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // end -to get customerID

        // to get accountId,balance
        try (Connection con = new DbConnection().getDbConnection()) {
            String accid = "select accountId,balance from account where customerId=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(accid);
            preparedStatement.setInt(1, cid);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                aid = rs.getInt("accountId");
                bal=rs.getInt("balance");
            }
            setAccountId(aid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //end - to get accountId,balance

        System.out.println("---Transfer Panel---");
        System.out.print("Enter the transactionType [deposit/withdrawal/transfer]:");
        String str=sc.nextLine();
        if(isValidTransactionType(str)) setTransactionType(str);
        while (!isValidTransactionType(str))
        {
            System.out.println("Invalid transactionType!");
            System.out.print("Please enter the correct transactionType:");
            str=sc.nextLine();
        }
        setTransactionType(str);
        if(getTransactionType().equalsIgnoreCase("Transfer"))
        {
            System.out.print("Enter the accountId to Transfer:");
            this.toTransfer=sc.nextLine();
        }

        System.out.print("Enter the amount:");
        String am=sc.nextLine();
        int amo=Integer.parseInt(am);
        if (amo != 0 && getTransactionType().equalsIgnoreCase("deposit")) {
            setAmount(amo);
        }
        else if (amo == 0 && getTransactionType().equalsIgnoreCase("deposit")) {
            while (amo == 0) {
                System.out.println("Amount must be greater than 0.");
                System.out.print("Enter the amount:");
                am = sc.nextLine();
                amo = Integer.parseInt(am);
            }
            setAmount(amo);}
        else if (amo != 0 && (getTransactionType().equalsIgnoreCase("withdrawal")
                    || getTransactionType().equalsIgnoreCase("transfer")) && amo < (bal - 1000) && bal>1000) {
                setAmount(amo);
            }
        else {
            if(bal>1000){
            while (amo == 0 || amo > (bal - 1000)) {

                System.out.println("Amount must be greater than 0 or Minimum balance is reached!");
                    System.out.print("Enter the amount:");
                    am = sc.nextLine();
                    amo = Integer.parseInt(am);
                }
                setAmount(amo);
            }
            else System.out.println("Minimum balance already reached!");
        }
    }

    // Business logic
    public  boolean isValidTransactionType(String ch)
    {
        if(ch.equalsIgnoreCase("deposit") ||
                ch.equalsIgnoreCase("withdrawal")||
                ch.equalsIgnoreCase("transfer") ) return true;
        return false;
    }


    public boolean isValidTransaction(String user,String pass)
    {
        int cid = 0;
        int aid=0;
        int bal=0;
        try (Connection con = new DbConnection().getDbConnection()) {
            String cusid = "select customerId from customer where email=(?) and password=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(cusid);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cid = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection con = new DbConnection().getDbConnection()) {

            // to get accountId,balance from account
            String accid = "select accountId,balance from account where customerId=(?)";
            System.out.println();
            PreparedStatement preparedStatement = con.prepareStatement(accid);
            preparedStatement.setInt(1, cid);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                aid = rs.getInt("accountId");
                bal = rs.getInt("balance");
            }
            setAccountId(aid);
            //End - to get accountId,balance from account

            //updating account table  - newBalance (Deposit)
            if (this.transactionType.equalsIgnoreCase("deposit")) {
                int setAmount = bal + getAmount();
                setRemainingBalance(setAmount);
                String accountUpdateQuery = "update account set balance=(?) where accountId=(?)";
                PreparedStatement preparedStatement1 = con.prepareStatement(accountUpdateQuery);
                preparedStatement1.setInt(1, setAmount);
                preparedStatement1.setInt(2, getAccountId());
                int done = preparedStatement1.executeUpdate();
                if (done > 0) {
                    System.out.println("Transaction completed!");
                    System.out.println(getAmount() + " is added!");
                    System.out.println("Balance:" + setAmount);
                    System.out.println();
                    return true;
                }
            }

            //updating account table  - newBalance (Withdrawal)
            else if (this.transactionType.equalsIgnoreCase("withdrawal")) {
                int setAmount = bal - getAmount();
                setRemainingBalance(setAmount);
                String accountUpdateQuery = "update account set balance=(?) where accountId=(?)";
                PreparedStatement preparedStatement1 = con.prepareStatement(accountUpdateQuery);
                preparedStatement1.setInt(1, setAmount);
                preparedStatement1.setInt(2, getAccountId());
                int done = preparedStatement1.executeUpdate();
                if (done > 0 && getAmount() != 0) {
                    System.out.println("Transaction completed!");
                    System.out.println(getAmount() + " is added!");
                    System.out.println("Balance:" + setAmount);
                    System.out.println();
                    return true;
                }
            }

            //updating account table  - newBalance (Transfer)
            else {
                if (!getToTransfer().equalsIgnoreCase("self")) {
                    int toAccId = Integer.parseInt(getToTransfer());
                    String checkQuery = "select * from account where accountId=" + toAccId;
                    Statement statement = con.createStatement();
                    ResultSet rc = statement.executeQuery(checkQuery);
                    if (rc.next()) {
                        setrAccountId(toAccId);
                        setrTransactionType("Credit");
                        String rFrom="";
                        rFrom+=getAccountId();
                        setrFromTransferred(rFrom);
                        setrAmount(getAmount());
                        // sender balance update
                        int setAmount = bal - getAmount();
                        setRemainingBalance(setAmount);
                        String accountUpdateQuery = "update account set balance=(?) where accountId=(?)";
                        PreparedStatement preparedStatement2 = con.prepareStatement(accountUpdateQuery);
                        preparedStatement2.setInt(1, setAmount);
                        preparedStatement2.setInt(2, getAccountId());
                        int done1 = preparedStatement2.executeUpdate();
                        // End - sender balance update

                        //get receiver Old Balance
                        String receiverOldBalanceQuery = "select balance from account where accountId="+toAccId;
                        Statement rOB= con.createStatement();
                        ResultSet rOldB=rOB.executeQuery(receiverOldBalanceQuery);
                        int receiverOldBalance=0;
                        while (rOldB.next())
                        {
                            receiverOldBalance=rOldB.getInt("balance");
                        }
                        //End - get receiver Old Balance


                        int receiverUpdatedBalance=receiverOldBalance+getAmount();
                        setrRemainingBalance(receiverUpdatedBalance);
                        String receiverBalanceUpdate="update account set balance=(?) where accountId=(?)";
                        PreparedStatement preparedStatement3= con.prepareStatement(receiverBalanceUpdate);
                        preparedStatement3.setInt(1,receiverUpdatedBalance);
                        preparedStatement3.setInt(2,toAccId);
                        int done2=preparedStatement3.executeUpdate();
                        if (done1 > 0 && done2 > 0 && getAmount() != 0) {
                            System.out.println("Transaction completed!");
                            System.out.println(getAmount() + " is deducted!");
                            System.out.println("Balance:" + setAmount);
                            System.out.println();
                            return true;
                        }
                    } else {
                        System.out.println("To Transfer account is not found!");
                        System.out.println();
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getTransactionDetails(String uName,String pWord)
    {
        try(Connection con=new DbConnection().getDbConnection()){
            int cuId=0;
            String findCustomerIdQuery="select customerId from customer where email=(?) and password=(?)";
            PreparedStatement ps= con.prepareStatement(findCustomerIdQuery);
            ps.setString(1,uName);
            ps.setString(2,pWord);
            ResultSet r=ps.executeQuery();
            while (r.next()){
                cuId=r.getInt(1);
            }

            int accId=0;
            String findAccountIdQuery="select accountId from account where customerId="+cuId;
            Statement st= con.createStatement();
            ResultSet rc=st.executeQuery(findAccountIdQuery);
            while (rc.next())
            {
                accId=rc.getInt(1);
            }

            String findAllTransactions="select * from Transaction where accountId="+accId;
            Statement statement= con.createStatement();
            ResultSet rs=statement.executeQuery(findAllTransactions);
            System.out.println();
            int count=0;
                while (rs.next()) {
                    count++;
                    if(count==1)
                    {
                        System.out.println("transactionId\t\tdate"+"  "+"\t\ttransactionType\t\t"+"   "
                                +"transactionDetail\t\t\t\tFrom\\To\t"+"   "+"remainingBalance");
                    }
                    System.out.print("   "+rs.getInt(1)+"\t\t\t");
                    Timestamp timestamp = rs.getTimestamp(7);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    System.out.print("  "+sdf.format(timestamp)+"\t\t");
                    System.out.print(rs.getString(3)+"\t\t");
                    if(rs.getString(3).equalsIgnoreCase("deposit")){
                        System.out.print("    "+rs.getInt(5)+" is credited to the account"+"\t\t");
                    } else if (rs.getString(3).equalsIgnoreCase("withdrawal")) {
                        System.out.print(rs.getString(5)+" is debited from the account"+"\t\t");
                    }else {
                        System.out.print(rs.getString(5)+" is debited from the account"+"\t\t");
                    }
                    System.out.print(" "+rs.getString(4)+"\t\t");
                    System.out.print("   "+rs.getInt(6));
                    System.out.println();
                }
            if(count==0) System.out.println("No transaction is made so far!");
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
