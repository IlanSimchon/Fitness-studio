package gym.management;

import gym.Exception.InvalidAgeException;
import gym.customers.Person;

public class Gym {
    private static final Gym INSTANCE = new Gym();
    private String name;
    private Secretary secretary;
    private double gymBalance;

    private Gym() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Cannot instantiate singleton class using reflection");
        }
    }
    public static Gym getInstance() {
        return INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecretary(Person person, double salary) throws InvalidAgeException {
        if (secretary != null) {
            secretary.blockAccess(); // Block old secretary
        }
        this.secretary = new Secretary(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth().toString(), salary);
    }

    public void setGymBalance(double gymBalance) {
        this.gymBalance = gymBalance;
    }

    public double getGymBalance() {
        return gymBalance;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    @Override
    public String toString() {
        return "Gym Name: " + name + "\nSecretary: " + secretary.getName();
    }
}
