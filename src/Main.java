package Bank_Management;

import Bank_Management.Controller.AccountDbAO;
import Bank_Management.Controller.LoanDbAo;
import Bank_Management.Controller.TransactionDbAo;
import Bank_Management.Model.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void menu()
    {
        System.out.println("Welcome to Banking!");
        System.out.println("1.User Menu");
        System.out.println("2.Admin Menu");
        System.out.println("3.Exit");
    }
    public static void userMenu()
    {
        System.out.println("1.Register");
        System.out.println("2.Login");
        System.out.println("3.Back");
    }
    public static void userSubMenu()
    {
        System.out.println("1.View Account Details");
        System.out.println("2.Check Balance");
        System.out.println("3.View Transaction History");
        System.out.println("4.Make a Transaction");
        System.out.println("5.Apply for a Loan");
        System.out.println("6.View Loan Status");
        System.out.println("7.Logout");
    }

    public static void adminMenu()
    {
        System.out.println("1.Log in");
        System.out.println("2.Exit");
    }
    public static void adminSubMenu()
    {
        System.out.println("1.Approve/Reject Loan Applications");
        System.out.println("2.View all users");
        System.out.println("3.View all Transactions");
        System.out.println("4.Generate Financial Reports");
        System.out.println("5.Logout");
    }
    public static void main(String[] args) throws SQLException {
        Scanner sc=new Scanner(System.in);
        int Option;
        do {
            menu();
            System.out.print("Enter your option:");
            Option=sc.nextInt();
            switch (Option) {
                case 1 -> {
                    int userMenuOption;
                    do {
                        System.out.println();
                        System.out.println("---UserMenu---");
                        userMenu();
                        System.out.print("Enter your userMenuOption:");
                        userMenuOption = sc.nextInt();
                        sc.nextLine();
                        switch (userMenuOption) {
                            case 1 -> {
                                System.out.println();
                                System.out.println("---Welcome to Banking---");
                                Customer cu = new Customer();
                                Account ac = new Account();
                                AccountDbAO acDb = new AccountDbAO();
                                cu.setNewData();
                                ac.setNewData(cu);
                                try {
                                    int cusId;
                                    if (cu.isThisNewData()) {
                                        cusId = cu.sendDataToTable();
                                        if (acDb.setDataToTable(ac, cusId) > 0)
                                            System.out.println("Data is added!");
                                    } else {
                                        System.out.println("data already exists!. please login.");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            case 2 -> {
                                System.out.println();
                                System.out.println("---Login Page---");
                                int userSubMenuOption;
                                String username, password;
                                Customer c = new Customer();
                                System.out.print("Enter the userName:");
                                username = sc.nextLine();
                                System.out.print("Enter the password:");
                                password = sc.nextLine();
                                try {
                                    if (c.isValidate(username, password)) {
                                        System.out.println();
                                    } else {
                                        while (!c.isValidate(username, password)) {
                                            System.out.println("Wrong userName and Password.TryAgain!");
                                            System.out.print("Enter the userName:");
                                            username = sc.nextLine();
                                            System.out.print("Enter the password:");
                                            password = sc.nextLine();
                                        }
                                    }
                                    System.out.println("Welcome back!");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                do {
                                    Loan l = new Loan();
                                    LoanDbAo loanDbAo = new LoanDbAo();
                                    Transaction t = new Transaction();
                                    TransactionDbAo transactionDbAo = new TransactionDbAo();
                                    System.out.println("---userSubMenu---");
                                    userSubMenu();
                                    System.out.println();
                                    System.out.print("Enter your userSubMenuOption:");
                                    userSubMenuOption = sc.nextInt();
                                    switch (userSubMenuOption) {
                                        case 1 -> c.getAccountDetails(username, password);
                                        case 2 -> c.getBalance(username, password);
                                        case 3 -> t.getTransactionDetails(username, password);
                                        case 4 -> {
                                            t.makeTransaction(username, password);
                                            if (t.isValidTransaction(username, password))
                                                transactionDbAo.sendDataToTable(t);
                                        }
                                        case 5 -> {
                                            l.getLoan(username, password);
                                            if (l.isLoanAlreadyNotExists()) loanDbAo.sendDataToTable(l);
                                            else {
                                                System.out.println("Customer with ID: " + l.getCustomerId()
                                                        + " already have loan!");
                                                System.out.println();
                                            }
                                        }
                                        case 6 -> l.getLoanDetail(username, password);
                                        case 7 -> {
                                            System.out.println("Thank You!. Logged out.");
                                            System.out.println();
                                        }
                                        default -> System.out.println("Invalid option!");
                                    }
                                } while (userSubMenuOption != 7);
                            }
                            case 3 -> System.out.println();
                        }
                    } while (userMenuOption != 3);
                }
                case 2 -> {
                    int adminMenuOption;
                    do {
                        System.out.println();
                        System.out.println("---adminMenu---");
                        adminMenu();
                        System.out.print("Enter the option:");
                        adminMenuOption = sc.nextInt();
                        switch (adminMenuOption) {
                            case 1 -> {
                                Admin ad = new Admin();
                                System.out.println();
                                System.out.println("---Login Panel---");
                                System.out.print("Enter your userName:");
                                String user = sc.next();
                                System.out.print("Enter the password:");
                                String pass = sc.next();
                                System.out.println();
                                if (ad.isExists(user, pass)) {
                                    System.out.println();
                                } else {
                                    while (!ad.isExists(user, pass)) {
                                        System.out.println("Invalid userName or password!");
                                        System.out.print("Enter your userName:");
                                        user = sc.next();
                                        System.out.print("Enter the password:");
                                        pass = sc.next();
                                    }
                                }
                                System.out.println("Welcome Back!");
                                System.out.println();
                                int adminSubMenuOption;
                                do {
                                    System.out.println();
                                    System.out.println("---adminSubMenu---");
                                    adminSubMenu();
                                    System.out.print("Enter the adminSubMenuOption:");
                                    adminSubMenuOption = sc.nextInt();
                                    Admin a = new Admin();
                                    switch (adminSubMenuOption) {
                                        case 1 -> {//Manage loan approvals
                                            Loan l = new Loan();
                                            System.out.println();
                                            l.getAllLoans();
                                            System.out.println("---Manage Approval---");
                                            System.out.print("Enter the LoanId:");
                                            String lid = sc.next();
                                            int loId = Integer.parseInt(lid);
                                            System.out.print("Status update:");
                                            String status = sc.next();
                                            l.updateStatus(loId, status);
                                        }
                                        case 2 ->//View all Customers
                                                a.viewAllCustomer();
                                        case 3 -> a.getAllTransaction();
                                        case 4 -> a.getFinancialReport();
                                        case 5 -> System.out.println("Thank You! logged out.");
                                    }
                                } while (adminSubMenuOption != 5);
                            }
                            case 2 -> {
                                System.out.println("Thank You! Logged out.");
                                System.out.println();
                            }
                            default -> System.out.println("Invalid Option!");
                        }
                    } while (adminMenuOption != 2);
                }
                case 3 -> System.out.println("Thank You for Banking!");
                default -> System.out.println("Invalid Option!");
            }
        }while (Option!=3);
    }
}