package gym.customers;

import gym.Exception.InvalidAgeException;

import java.util.ArrayList;
import java.util.List;

public class Client extends Person {
    private double balance;
    private List<String> notifications = new ArrayList<>();

    public Client(String name, int age, Gender gender, String dateOfBirth, double balance) throws InvalidAgeException {
        super(name, age, gender, dateOfBirth); // This can throw InvalidAgeException
        this.balance = balance;
    }


    public void receiveNotification(String message) {
        notifications.add(message);
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    public void reduceBalance(double amount) {
        balance -= amount;
    }
}
