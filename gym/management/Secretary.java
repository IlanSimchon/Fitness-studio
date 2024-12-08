package gym.management;

import gym.customers.*;
import gym.Exception.*;
import gym.management.Sessions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Secretary extends Person {
    private double salary;
    private List<Client> clients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actions = new ArrayList<>();
    private boolean accessBlocked = false;


    public Secretary(String name, int age, Gender gender, String dateOfBirth, double salary) throws InvalidAgeException {
        super(name, age, gender, dateOfBirth);
        this.salary = salary;
        System.out.printf("A new secretary has started working at the gym: " + name);
    }

    public void blockAccess() {
        this.accessBlocked = true;
    }
    private void ensureAccess() {
        if (accessBlocked) {
            throw new SecurityException("Former secretaries are not permitted to perform actions");
        }
    }


    public Client registerClient(Person person) throws InvalidAgeException, DuplicateClientException {
        ensureAccess();
        if (person.getAge() < 18) {
            throw new InvalidAgeException("Client must be at least 18 years old to register");
        }
        if (clients.contains(person)) {
            throw new DuplicateClientException("The client is already registered");
        }
        Client client = new Client(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), 1000.0);
        clients.add(client);
        actions.add("Registered new client: " + client.getName());
        return client;
    }


    public void unregisterClient(Client client) throws ClientNotRegisteredException {
        ensureAccess();
        if (!clients.contains(client)) {
            throw new ClientNotRegisteredException("Registration is required before attempting to unregister");
        }
        clients.remove(client);
        actions.add("Unregistered client: " + client.getName());
    }


    public Instructor hireInstructor(Person person, double hourlyWage, List<SessionType> qualifications) throws InvalidAgeException {
        ensureAccess();
        Instructor instructor = new Instructor(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), hourlyWage, qualifications);
        instructors.add(instructor);
        actions.add("Hired new instructor: " + instructor.getName());
        return instructor;
    }

    public Session addSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) throws InstructorNotQualifiedException {
        ensureAccess();
        if (!instructor.isQualifiedFor(type)) {
            throw new InstructorNotQualifiedException("Instructor is not qualified to conduct this session type.");
        }
        Session session = new Session(type, dateTime, forum, instructor);
        sessions.add(session);
        actions.add("Created new session: " + type.getName() + " on " + dateTime + "with instructor: " + instructor.getName());
        return session;
    }

    public void registerClientToLesson(Client client, Session session) throws DuplicateClientException,ClientNotRegisteredException  {
        ensureAccess();
        if (!clients.contains(client))
        {
            throw new ClientNotRegisteredException("The client is not registered with the gym and cannot enroll in lessons");
        }
        List<Client> participants = session.getParticipants();
        if(participants.contains(client))
        {
            throw new DuplicateClientException("The client is already registered for this lesson");
        }
        if (session.isExpired())
        {
            throw new IllegalArgumentException("Session has already passed.");
        }
        if (!session.isForumCompatible(client))
        {
            if(session.getForum() == ForumType.Seniors)
            {
                actions.add("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
            }
            else if(session.getForum() == ForumType.Male || session.getForum() == ForumType.Female)
            {
                actions.add("Failed registration: Client's gender doesn't match the session's gender requirements (" + session.getForum() + ")");
            }
            throw new IllegalArgumentException("Client is not compatible with the session forum.");
        }
        if (!session.hasSpace()) {
            actions.add("Failed registration: No available spots for session");
            throw new IllegalArgumentException("No space left in the session.");
        }
        if (client.getBalance() < session.getPrice())
        {
            actions.add("Failed registration: Client doesn't have enough balance");
            throw new InsufficientFundsException("Client does not have enough balance.");
        }
        if (session.addParticipant(client)) {
            // Reduce client balance
            client.setBalance(client.getBalance() - session.getPrice());
            // Update Gym balance
            Gym.getInstance().setGymBalance(Gym.getInstance().getGymBalance() + session.getPrice());
            actions.add("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime() + " for price: " + session.getPrice() );

        } else {
            actions.add("Failed registration: Session is not in the future");
            throw new DuplicateClientException("Client already registered for session: " + session.getType());
        }
    }

    // Notify clients that related to the specific session
    public void notify(Session session, String message) {
        for (Client client : session.getParticipants()) {
            client.receiveNotification(message);
        }
        actions.add("A message was sent to everyone registered for session " + session.getType() + " on " + session.getDateTime() + " : " + message);
    }

    // Notify all clients that are sighed to session on a specific date
    public void notify(String date, String message) {
        for(Session session : sessions)
        {
            if(extractDate(session.getDateTime()).equals(extractDate(date))) // only by format of dd-MM-yyyy
            {
                for (Client client : session.getParticipants())
                {
                    client.receiveNotification(message);
                }
                actions.add("A message was sent to everyone registered for a session on " + session.getDateTime() + " : " + message);
            }
        }
    }

    // Notify all clients
    public void notify(String message) {
        for(Client client : clients) {
            client.receiveNotification(message);
        }
        actions.add("A message was sent to all gym clients: " + message);
    }

    public void paySalaries() {
        actions.add("Salaries have been paid to all employees");
    }

    public void printActions() {
        for (String action : actions) {
            System.out.println(action);
        }
    }
    private static String extractDate(String dateTimeString) {
        // Parse the input string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        // Format the LocalDateTime to only extract the date part
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
