package com.cogent;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

public class App {

    public static void main( String[] args ) {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Scanner scan = new Scanner(System.in);
        Boolean flag = true;
        int entry = -1;

        String userName = "";
        String password = "";
        int loginID = 0;


        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        int terminate = 0;
        while(terminate == 0) {

            System.out.print("Press 0 to login or create an account. Select another value to terminate program: ");
            try{
                terminate = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e){
                terminate = -1;
            }

            if(terminate != 0){
                session.close();
                return;
            } else {
                loginID = 0;
            }

            while (loginID == 0) {

                System.out.println("***************************");
                System.out.println("Please login. Press 1 for customer. Press 2 for employee. Press 3 to create a customer's account. Press any other key to terminate program. ");
                System.out.println("***************************");
                System.out.print("Account type: ");
                try {
                    entry = Integer.parseInt(scan.nextLine());
                } catch (Exception e){
                    System.out.println("invalid entry, please try again");
                    entry = -1;
                }
                if (entry == 1 || entry == 2 || entry == 3) {
                    System.out.print("Enter username: ");
                    userName = scan.nextLine();
                    System.out.print("Enter password: ");
                    password = scan.nextLine();
                    String hql = "";
                    if (entry == 1) {
                        hql = "SELECT id FROM Customer WHERE name = :userName AND password = :password";
                        Query query = session.createQuery(hql);
                        query.setParameter("userName", userName);
                        query.setParameter("password", password);
                        List results = query.getResultList();
                        try {
                            loginID = (int) results.get(0);
                        } catch (Exception e) {
                            System.out.println("Try again. Please enter valid credentials");
                            loginID = 0;
                        }
                    } else if (entry == 2) {
                        hql = "SELECT id FROM Employee WHERE name = :userName AND password = :password";
                        Query query = session.createQuery(hql);
                        query.setParameter("userName", userName);
                        query.setParameter("password", password);
                        List results = query.getResultList();
                        try {
                            loginID = (int) results.get(0);
                        } catch (Exception e) {
                            System.out.println("Try again. Please enter valid credentials");
                            loginID = 0;
                        }
                    } else if (entry == 3) {
                        Customer customer = new Customer(userName, password);
                        try {
                            session.save(customer);
                        } catch (Exception e) {
                            System.out.println("There was an error while creating your account");
                        }
                        transaction.commit();
                        System.out.println("Account created successfully");
                    }

                } else {
                    System.out.println("Terminating...");
                    session.close();
                    return;
                }
            }


            //Customer Options
            while (entry == 1) {

                if (!transaction.isActive()) {
                    transaction = session.beginTransaction();
                }

                /* As a customer, I can apply for a new bank account with a starting balance.
                 * As a customer, I can view the balance of a specific account.
                 * As a customer, I can make a withdrawal or deposit to a specific account. */

                Customer customer = session.get(Customer.class, loginID);
                List<Account> myAccounts = customer.getAccounts();
                int accountID;
                int value;
                Account myAccount;

                System.out.println("***************************");
                System.out.println("Please select an option to proceed");
                System.out.println("Select 1 to apply for an account");
                System.out.println("Select 2 to view your balance");
                System.out.println("Select 3 to make a withdrawal or deposit");
                System.out.println("Select 4 to transfer money to another account");
                System.out.println("Select 5 to view and approve your pending transactions");
                System.out.println("Select any other number to logout");
                System.out.println("***************************");
                System.out.print("Option: ");


                int option = -1;
                try {
                    option = Integer.parseInt(scan.nextLine());
                }catch(Exception e){
                    System.out.println("invalid entry, please try again");
                }

                switch (option) {
                    case 1:
                        System.out.println("Please enter the value you would like to open your account with");
                        System.out.print("Value: ");
                        value = Integer.parseInt(scan.nextLine());
                        Account account = new Account(value);
                        session.save(account);
                        System.out.println("your new account id is: " + account.getId());
                        customer.addAccount(account);
                        session.update(customer);
                        transaction.commit();
                        System.out.println("Your account is pending approval");
                        break;
                    case 2:
                        System.out.println("Select an account to view");
                        for (int i = 0; i < myAccounts.size(); i++) {
                            System.out.println("" + myAccounts.get(i).getId() + " ");
                        }
                        System.out.println();
                        System.out.print("Account number: ");
                        accountID = -1;
                        try{
                            accountID = Integer.parseInt(scan.nextLine());
                        } catch (Exception e){
                            System.out.println("invalid entry, please try again");
                        }
                        myAccount = session.get(Account.class, accountID);
                        try {
                            System.out.println("Value: " + myAccount.getBalance());
                        } catch (Exception e) {
                            System.out.println("Please try again and enter a valid id");
                        }
                        break;
                    case 3:
                        System.out.println("Select an account to view");
                        for (int i = 0; i < myAccounts.size(); i++) {
                            System.out.println("" + myAccounts.get(i).getId() + " ");
                        }
                        System.out.println();
                        System.out.print("Account number: ");
                        accountID = -1;
                        try {
                            accountID = Integer.parseInt(scan.nextLine());
                        } catch (Exception e){
                            System.out.println("invalid entry, please try again");
                        }
                        myAccount = session.get(Account.class, accountID);
                        System.out.println("your current balance is: " + myAccount.getBalance());
                        System.out.print("Press 1 to deposit, press 2 to withdraw: ");
                        int dw = -1;
                        try{
                            Integer.parseInt(scan.nextLine());
                        } catch (Exception e){
                            System.out.println("invalid entry, please try again");
                        }
                        System.out.print("Enter a value: ");
                        value = 0;
                        try{
                            Integer.parseInt(scan.nextLine());
                        } catch (Exception e){
                            System.out.println("invalid entry please try again");
                        }
                        if (value <= 0) {
                            System.out.println("Please enter a valid value");
                            break;
                        }
                        if (dw == 1) {
                            myAccount.deposit(value);
                            System.out.println("Deposit successful");
                        } else if (dw == 2) {
                            Boolean d = myAccount.withdraw(value);
                            if (d) {
                                System.out.println("Withdrawal successful");
                            } else {
                                System.out.println("Insufficient funds");
                            }
                        }
                        session.update(myAccount);
                        transaction.commit();
                        break;
                    case 4:
                        try {
                            CriteriaBuilder cb = session.getCriteriaBuilder();
                            CriteriaQuery<Customer> criteriaQuery = cb.createQuery(Customer.class);
                            Root<Customer> root = criteriaQuery.from(Customer.class);
                            CriteriaQuery<Customer> all = criteriaQuery.select(root);
                            TypedQuery<Customer> allEmps = session.createQuery(all);
                            List<Customer> customers = allEmps.getResultList();
                            for (Customer c : customers) {
                                System.out.println("id: " + c.getId() + " name: " + c.getName());
                            }
                            System.out.println("Select a customer to transfer money to");
                            System.out.print("Customer ID: ");
                            int customerID = Integer.parseInt(scan.nextLine());
                            Customer destinationCustomer = session.get(Customer.class, customerID);
                            System.out.println("Select an account to transfer money to");
                            List<Account> destinationAccounts = destinationCustomer.getAccounts();
                            for (Account a : destinationAccounts) {
                                System.out.println("id: " + a.getId());
                            }
                            System.out.print("Account ID: ");
                            int destinationAccountID = Integer.parseInt(scan.nextLine());
                            Account destinationAccount = session.get(Account.class, destinationAccountID);
                            System.out.println("Enter an account to transfer money from");
                            destinationAccounts = customer.getAccounts();
                            for (Account a : destinationAccounts) {
                                System.out.println("id: " + a.getId() + " balance: " + a.getBalance());
                            }
                            System.out.print("Account ID: ");
                            int sourceAccountID = Integer.parseInt(scan.nextLine());
                            Account sourceAccount = session.get(Account.class, sourceAccountID);
                            System.out.print("Enter a value to be transfered: ");
                            int transferValue = Integer.parseInt(scan.nextLine());
                            Boolean sufficientFunds = sourceAccount.withdraw(transferValue);
                            if (!sufficientFunds) {
                                System.out.println("Insufficient funds, please try again");
                                break;
                            }
                            com.cogent.Transaction transaction1 = new com.cogent.Transaction(transferValue, sourceAccount, destinationAccount);
                            session.save(transaction1);
                            transaction.commit();
                            System.out.println("Your transaction is pending acceptance");
                        } catch (NumberFormatException e){
                            System.out.println("invalid entry please try again");
                        }
                        break;
                    case 5:
                        try {
                            CriteriaBuilder cb2 = session.getCriteriaBuilder();
                            CriteriaQuery<com.cogent.Transaction> criteriaQuery2 = cb2.createQuery(com.cogent.Transaction.class);
                            Root<com.cogent.Transaction> root2 = criteriaQuery2.from(com.cogent.Transaction.class);
                            CriteriaQuery<com.cogent.Transaction> all2 = criteriaQuery2.select(root2);
                            TypedQuery<com.cogent.Transaction> allEmps2 = session.createQuery(all2);
                            List<com.cogent.Transaction> transactions = allEmps2.getResultList();
                            for (com.cogent.Transaction t : transactions) {
                                System.out.println("transaction id: " + t.getId() + " value: " + t.getValue() + " from account: " + t.getFrom().getId() + " to account: " + t.getTo().getId() + " is pending: " + t.getPending());
                            }
                            int tid;
                            int ad;
                            com.cogent.Transaction myTransaction;
                            try {
                                System.out.print("select a transaction: ");
                                tid = Integer.parseInt(scan.nextLine());
                                System.out.print("select 1 to approve select 2 to deny: ");
                                ad = Integer.parseInt(scan.nextLine());
                                myTransaction = session.get(com.cogent.Transaction.class, tid);
                            } catch (Exception e) {
                                System.out.println("invalid input");
                                break;
                            }
                            if (!myTransaction.getPending()) {
                                System.out.println("Transaction has already been processed");
                                break;
                            }
                            if (ad == 1) {
                                myTransaction.getTo().deposit(myTransaction.getValue());
                                session.save(myTransaction.getTo());
                                System.out.println("depositing value...");
                            } else if (ad == 2) {
                                myTransaction.getFrom().deposit(myTransaction.getValue());
                                session.save(myTransaction.getFrom());
                                System.out.println("returning value...");
                            }
                            myTransaction.setPending(false);
                            session.save(myTransaction);
                            System.out.println("transaction processed");
                            transaction.commit();
                        } catch (NumberFormatException e){
                            System.out.println("invalid entry please try again");
                        }
                        break;
                    default:
                        entry = 0;
                        break;
                }
            }

            //employee option
            while (entry == 2) {

                if (!transaction.isActive()) {
                    transaction = session.beginTransaction();
                }

                String hql = "";
                Query query;
                List results;

                System.out.println("***************************");
                System.out.println("Please select an option to continue");
                System.out.println("Press 1 to approve or reject a pending account");
                System.out.println("Press 2 to view a customer's bank accounts");
                System.out.println("Press 3 to view all transactions");
                System.out.println("Enter another value to logout");
                System.out.println("***************************");
                System.out.print("Option: ");

                int option = -1;

                try {
                    option = Integer.parseInt(scan.nextLine());
                } catch (Exception e){
                    System.out.println("invalid entry please try again. ");
                }

                // As an employee, I can approve or reject an account.
                // As an employee, I can view a customer's bank accounts.
                // A an employee, I can view a log of all transactions.

                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Customer> criteriaQuery = cb.createQuery(Customer.class);
                Root<Customer> root = criteriaQuery.from(Customer.class);
                CriteriaQuery<Customer> all = criteriaQuery.select(root);
                TypedQuery<Customer> allEmps = session.createQuery(all);
                List<Customer> customers = allEmps.getResultList();

                switch (option) {
                    case 1:
                        try {
                            for (Customer c : customers) {
                                System.out.println("id: " + c.getId() + " name: " + c.getName());
                            }
                            System.out.print("Select a customer id: ");
                            int customerID = Integer.parseInt(scan.nextLine());
                            List<Account> accounts = session.get(Customer.class, customerID).getAccounts();
                            for (Account a : accounts) {
                                if (a.getPending()) {
                                    System.out.println("id: " + a.getId() + " balance: " + a.getBalance());
                                }
                            }
                            System.out.print("Select an account id to proceed: ");
                            int accountID = Integer.parseInt(scan.nextLine());
                            System.out.print("Select 1 to approve account, select 2 to deny account: ");
                            int ad = Integer.parseInt(scan.nextLine());
                            Account account = session.get(Account.class, accountID);
                            if (ad == 1) {
                                account.setPending(false);
                                session.update(account);
                                transaction.commit();
                            } else if (ad == 2) {
                                session.delete(account);
                                transaction.commit();
                            } else {
                                System.out.println("please enter a valid option");
                            }
                        } catch (NumberFormatException e){
                            System.out.println("invalid entry please try again");
                        }
                        break;
                    case 2:
                        for (Customer c : customers) {
                            System.out.println("customer id: " + c.getId() + " name: " + c.getName());
                        }
                        System.out.print("Select a customer ID to view their accounts: ");
                        int acid = -1;
                        try {
                            acid = Integer.parseInt(scan.nextLine());
                        } catch (Exception e){
                            System.out.println("invalid entry please try again");
                        }
                        Customer customer = session.get(Customer.class, acid);
                        List<Account> ac = customer.getAccounts();
                        for(Account a : ac){
                            System.out.println("account id: "+a.getId()+" balance: "+a.getBalance());
                        }
                        break;
                    case 3:
                        CriteriaBuilder cb2 = session.getCriteriaBuilder();
                        CriteriaQuery<com.cogent.Transaction> criteriaQuery2 = cb2.createQuery(com.cogent.Transaction.class);
                        Root<com.cogent.Transaction> root2 = criteriaQuery2.from(com.cogent.Transaction.class);
                        CriteriaQuery<com.cogent.Transaction> all2 = criteriaQuery2.select(root2);
                        TypedQuery<com.cogent.Transaction> allTrans = session.createQuery(all2);
                        List<com.cogent.Transaction> transactions = allTrans.getResultList();
                        for (com.cogent.Transaction t : transactions) {
                            System.out.println("id: " + t.getId() + " from account: " + t.getFrom() + " to account: " + t.getTo() + " value: " + t.getValue() + " pending: " + t.getPending());
                        }
                        break;
                    default:
                        entry = 0;
                        break;
                }
            }
        }

        session.close();


    }
}
