package com.cogent;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int balance;

    private Boolean pending;

    public Account(int balance) {
        this.balance = balance;
        this.pending = true;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Account(){}

    public Boolean withdraw (int value){
        if(pending == true){
            System.out.println("Your account must be approved by an employee to complete a transaction");
            return false;
        }
        if(balance >= value){
            balance -= value;
            return true;
        }
        return false;
    }

    public void deposit(int value){
        if(pending == true){
            System.out.println("Your account must be approved by an employee to create a transaction");
            return;
        }
        balance +=value;
    }

}
