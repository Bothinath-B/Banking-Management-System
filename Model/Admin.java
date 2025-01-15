package Bank_Management.Model;

import Bank_Management.Controller.DbConnection;

import java.sql.*;

public class Admin {
    private int adminId;
    private String userName;
    private String password;

    public boolean isExists(String u, String p) {
        try (Connection con = new DbConnection().getDbConnection()) {
            String query = "select * from admin";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getString(2).equals(u)
                        && rs.getString(3).equals(p)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void viewAllCustomer(){
        try(Connection con=new DbConnection().getDbConnection()){
            String query="{call getAllCustomer()}";
            Statement statement=con.createStatement();
            ResultSet rs=statement.executeQuery(query);
            int count=0;
            System.out.println();
            while (rs.next()){
                count++;
                if(count==1){
                    System.out.printf("%-10s %-15s %-25s %-15s %-25s %-10s %-10s %-10s %-10s %-15s %-10s %-10s %-10s%n",
                            "customerId", "name", "email", "phone", "address",
                            "accountId", "accountType", "balance", "loanId",
                            "loanType", "loanAmount", "interestRate", "loanStatus");
                    System.out.println("-".repeat(195));
                }
                System.out.printf("%-10s %-15s %-25s %-15s %-25s %-10s %-10s %-10s %-10s %-15s %-10s %-10s %-10s%n",
                        rs.getString("customerId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("accountId"),
                        rs.getString("accountType"),
                        rs.getString("balance"),
                        rs.getString("loanId"),
                        rs.getString("loanType"),
                        rs.getString("loanAmount"),
                        rs.getString("interestRate"),
                        rs.getString("loanStatus"));
            }
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void getAllTransaction(){
        try(Connection con=new DbConnection().getDbConnection()){
            String query="select * from transaction";
            Statement statement=con.createStatement();
            ResultSet r=statement.executeQuery(query);
            int count=0;
            System.out.println();
            while (r.next()){
                count++;
                if(count==1){
                    System.out.printf("%-15s %-10s %-15s %-10s %-10s %-20s %-25s%n",
                            "transactionId", "accountId", "transactionType", "toTransfer",
                            "amount", "remainingBalance", "transactionDate");
                    System.out.println("-".repeat(110));
                }
                System.out.printf("%-15s %-10s %-15s %-10s %-10s %-20s %-25s%n",
                        r.getInt("transactionId"),
                        r.getInt("accountId"),
                        r.getString("transactionType"),
                        r.getString("toTransfer"),
                        r.getInt("amount"),
                        r.getInt("remainingBalance"),
                        r.getTimestamp("transactionDate"));
            }
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void getFinancialReport(){
        try(Connection con=new DbConnection().getDbConnection()){
            String query="{call generateFinancialReport()}";
            Statement statement= con.createStatement();
            ResultSet rs=statement.executeQuery(query);
            int count=0;
            System.out.println();
            while (rs.next()){
                count++;
                if(count==1){
                    System.out.printf("%-12s %-15s %-10s %-10s %-15s %-15s %-12s %-15s %-10s %-15s\n",
                            "customerId", "name", "accountId", "balance", "pending_loans", "approved_loans",
                            "totalDeposit", "totalWithdrawal", "totalSent", "totalReceived");
                }
                int customerId = rs.getInt("customerId");
                String name = rs.getString("name");
                int accountId = rs.getInt("accountId");
                double balance = rs.getDouble("balance");
                String pendingLoans = rs.getString("pending_loans");
                String approvedLoans = rs.getString("approved_loans");
                String totalDeposit = rs.getString("totalDeposit");
                String totalWithdrawal = rs.getString("totalWithdrawal");
                String totalSent = rs.getString("totalSent");
                String totalReceived = rs.getString("totalReceived");

                // Replace null values with '-'
                pendingLoans = (pendingLoans == null) ? "-" : pendingLoans;
                approvedLoans = (approvedLoans == null) ? "-" : approvedLoans;
                totalDeposit = (totalDeposit == null) ? "-" : totalDeposit;
                totalWithdrawal = (totalWithdrawal == null) ? "-" : totalWithdrawal;
                totalSent = (totalSent == null) ? "-" : totalSent;
                totalReceived = (totalReceived == null) ? "-" : totalReceived;

                System.out.printf("%-12d %-15s %-10d %-10.2f %-15s %-15s %-12s %-15s %-10s %-15s\n",
                        customerId, name, accountId, balance, pendingLoans, approvedLoans,
                        totalDeposit, totalWithdrawal, totalSent, totalReceived);
            }
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}