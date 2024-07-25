package com.cogent;

import jakarta.persistence.*;

@Entity
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int value;

    @ManyToOne
    private Account from;
    @ManyToOne
    private Account to;

    private Boolean pending;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Transaction() {
    }

    public Transaction(int value, Account from, Account to) {
        this.value = value;
        this.from = from;
        this.to = to;
        this.pending = true;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value=" + value +
                ", from=" + from +
                ", to=" + to +
                ", pending=" + pending +
                '}';
    }
}
