package Bank_Management.Model;
import java.sql.SQLException;
import java.util.Scanner;

public class Account {
    private int accountId;
    private int customerId;
    private String accountType;
    private String uniqueID;
    private int balance;
    Scanner sc=new Scanner(System.in);

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setNewData(Customer cu) throws SQLException
    {
        System.out.print("Enter the accountType[Savings/Current]:");
        String aT=sc.nextLine();
        if(isvalidAcType(aT)) this.accountType=aT;
        else
        {
            while (!isvalidAcType(aT))
            {
                System.out.println("Invalid accountType!");
                System.out.print("Please enter the correct accountType:");
                aT=sc.nextLine();
            }
            this.accountType=aT;
        }
        int amount=0;
        System.out.print("Enter the amount [min.1000]:");
        amount=sc.nextInt();
        if(amount>=1000)
        {
            this.balance=amount;
        }
        else {
            while (amount < 1000) {
                System.out.println("---Min.Balance is 1000---");
                System.out.print("please enter the amount greater than 1000:");
                amount = sc.nextInt();
            }
            this.balance=amount;
        }
        this.uniqueID=cu.getPANNo();
    }

    public boolean isvalidAcType(String acT)
    {
        if(acT.equalsIgnoreCase("Savings")
                || acT.equalsIgnoreCase("Current")) return true;
        return false;
    }

    public int getCustomerId() {
        return customerId;
    }
}
