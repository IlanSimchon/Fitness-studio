import entities.Client;
import entities.Person;
import enums.Gender;
import gym.Exception.*;
import gym.customers.*;
import gym.management.*;
import gym.management.Sessions.*;
import management.Gym;
import management.Secretary;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InstructorNotQualifiedException, DuplicateClientException, InvalidAgeException, ClientNotRegisteredException {
        Person p1 = new Person("David", 500, Gender.Male, "20-02-1978");
        Person p2 = new Person("Nofar", 1200, Gender.Female, "03-07-1998");
        Person p3 = new Person("Maayan", 200, Gender.Female, "21-12-2005");
        Person p4 = new Person("Shachar", 300, Gender.Female, "09-04-1958");
        Person p5 = new Person("Yuval", 650, Gender.Female, "23-05-1976");
        Person p6 = new Person("Noam", 70, Gender.Male, "20-12-1984");
        Person p7 = new Person("Neta", 600, Gender.Male, "12-02-1993");
        Person p8 = new Person("Rom", 1600, Gender.Female, "12-12-1999");

        Person p9 = new Person("Dani", 850, Gender.Male, "03-10-2015");

        Gym gym = Gym.getInstance();
        gym.setName("CrossFit");
        gym.setSecretary(p1, 9000);

        Secretary gymSecretary = gym.getSecretary();

        Client c1 = gymSecretary.registerClient(p2);
        Client c2 = gymSecretary.registerClient(p3);
        Client c3 = gymSecretary.registerClient(p4);
        Client c4 = gymSecretary.registerClient(p5);
        Client c5 = gymSecretary.registerClient(p6);
        Client c6 = gymSecretary.registerClient(p7);
        Client c7 = gymSecretary.registerClient(p8);

        try {
            Client c8 = gymSecretary.registerClient(p9);
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }
        try {
            Client c9 = gymSecretary.registerClient(p4);
        } catch (DuplicateClientException e) {
            System.out.println(e.getMessage());
        }

        gymSecretary.unregisterClient(c2);

        try {
            gymSecretary.unregisterClient(c2);
        }
        catch (ClientNotRegisteredException e) {
            System.out.println(e.getMessage());
        }

        c2 = gymSecretary.registerClient(p3);

        Instructor i1 = gymSecretary.hireInstructor(p4, 70, new ArrayList<>(Arrays.asList(SessionType.ThaiBoxing, SessionType.MachinePilates)));
        Instructor i2 = gymSecretary.hireInstructor(p5, 90, new ArrayList<>(Arrays.asList(SessionType.ThaiBoxing, SessionType.Pilates, SessionType.MachinePilates)));
        Instructor i3 = gymSecretary.hireInstructor(p6,50, new ArrayList<>(Arrays.asList(SessionType.Pilates, SessionType.Ninja)));

        Session s1 = gymSecretary.addSession(SessionType.Pilates, "23-01-2025 10:00", ForumType.All, i2);
        Session s2 = gymSecretary.addSession(SessionType.MachinePilates, "23-10-2024 08:00", ForumType.Female, i1);
        Session s3 = gymSecretary.addSession(SessionType.Pilates, "25-01-2025 09:30", ForumType.Seniors, i3);
        Session s4 = gymSecretary.addSession(SessionType.ThaiBoxing, "01-01-2025 14:00", ForumType.All, i2);
        Session s5 = gymSecretary.addSession(SessionType.Ninja, "14-01-2025 20:00", ForumType.All, i3);
        Session s6 = gymSecretary.addSession(SessionType.ThaiBoxing, "14-01-2025 20:00", ForumType.Male, i1);

        try {
            Session s7 = gymSecretary.addSession(SessionType.ThaiBoxing, "10-10-2025 21:00", ForumType.All, i3);
        } catch (InstructorNotQualifiedException e) {
            System.out.println(e.getMessage());
        }

        gymSecretary.registerClientToLesson(c1, s1);
        gymSecretary.registerClientToLesson(c1, s2);
        gymSecretary.registerClientToLesson(c1, s4);
        gymSecretary.registerClientToLesson(c4, s4);
        gymSecretary.registerClientToLesson(c1, s3);
        gymSecretary.registerClientToLesson(c5, s2);
        gymSecretary.registerClientToLesson(c5, s2);

        gymSecretary.registerClientToLesson(c1, s5);
        gymSecretary.registerClientToLesson(c2, s5);
        gymSecretary.registerClientToLesson(c3, s5);
        gymSecretary.registerClientToLesson(c4, s5);
        gymSecretary.registerClientToLesson(c6, s5);
        gymSecretary.registerClientToLesson(c7, s5);


        try{
            gymSecretary.registerClientToLesson(c1,s1);
        } catch (DuplicateClientException e) {
            System.out.println(e.getMessage());
        }

        gymSecretary.unregisterClient(c2);
        try {
            gymSecretary.registerClientToLesson(c2, s3);
        } catch (ClientNotRegisteredException e) {
            System.out.println(e.getMessage());
        }

        gymSecretary.notify(s4, "The instructor will be a few minutes late for the session");
        gymSecretary.notify("01-01-2025", "Heavy traffic reported around the gym today. Plan ahead to avoid missing your session!");
        gymSecretary.notify("Happy New Year to all our valued clients!");

        gymSecretary.paySalaries();

        gym.setSecretary(p3,8000);
        Secretary newGymSecretary = gym.getSecretary();

        try{
            gymSecretary.registerClientToLesson(c1, s1);
        }
        catch (NullPointerException e){
            System.out.println("Error: Former secretaries are not permitted to perform actions");
        }

        System.out.println("\n---Actions history---");
        newGymSecretary.printActions();

        System.out.println("\n" + c1.getName() + " Notifications: " + c1.getNotifications());

        System.out.println("\n---management.Gym information---");

        System.out.print(gym);
    }



}
