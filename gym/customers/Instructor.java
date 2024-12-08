package gym.customers;

import gym.Exception.InvalidAgeException;
import gym.management.Sessions.SessionType;

import java.util.List;

public class Instructor extends Person {
    private double hourlyWage;
    private List<SessionType> qualifications;

    public Instructor(String name, int age, Gender gender, String dateOfBirth, double hourlyWage, List<SessionType> qualifications) throws InvalidAgeException {
        super(name, age, gender, dateOfBirth);
        this.hourlyWage = hourlyWage;
        this.qualifications = qualifications;
    }

    public boolean isQualifiedFor(SessionType type) {
        return qualifications.contains(type);
    }
}
