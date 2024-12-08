package gym.customers;

import gym.Exception.InvalidAgeException;

public class Person {
    private String name;
    private int age;
    private Gender gender;
    private String dateOfBirth; // Now stored as a string in the specified format

    public Person(String name, int age, Gender gender, String dateOfBirth) throws InvalidAgeException {
        validateAge(age);
        validateFormat(dateOfBirth); // Validate the format of the dateOfBirth string
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // Validation for age
    private void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Person must be at least 18 years old.");
        }
    }

    // Validation for date format
    private void validateFormat(String dateOfBirth) {
        if (!dateOfBirth.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid date format. Use 'dd-MM-yyyy'.");
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
