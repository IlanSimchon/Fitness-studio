package entities;
import enums.Gender;

public class Person {
    private String name;
    private int balance;
    private Gender gender;
    private String dateOfBirth;

    public Person(String name, int balance, Gender gender, String dateOfBirth) {
        this.name = name;
        this.balance = balance;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

}