package gym.management;
import gym.Gym;
import gym.customers.*;
import gym.Exception.*;
import gym.management.Sessions.*;

import java.util.*;

public class Secretary implements Manageable {
    private final Person person;
    private static final List<String> actionPrints = new ArrayList<>();
    private final String secretaryKey = "E|Me@!(@bTx)GST.";

    public Secretary(Person person) {
        this.person = person;
    }

    public Person getPerson(){
        return this.person;
    }

    public List<String> getActionPrints() {
        return actionPrints;
    }

    @Override
    public Client registerClient(Person person) throws InvalidAgeException, DuplicateClientException {
        if (!isCurrentSecretary())
            return null;

        Client newClient = ClientFactory.createClient(person);
        ClientRegistry.getInstance().addClient(newClient);
        actionPrints.add("Registered new client: " + person.getName());

        return newClient;
    }

    @Override
    public void unregisterClient(Client client) throws ClientNotRegisteredException {
        if (!isCurrentSecretary())
            return;

        if (!ClientRegistry.getInstance().isClientRegistered(client))
            throw new ClientNotRegisteredException("Error: Registration is required before attempting to unregister.");

        ClientRegistry.getInstance().removeClient(client);
        unRegisterClientFromLessons(client);
        this.actionPrints.add("Unregistered client: " + client.getPerson().getName());
    }

    @Override
    public Instructor hireInstructor(Person person, int hourSalary, ArrayList<SessionType> sessionList) {
        if (!isCurrentSecretary())
            return null;

        Instructor instructor = InstructorFactory.createInstructor(person, hourSalary, sessionList);
        if (!InstructorRegistry.getInstance().isInstructorRegistered(instructor)) {
            InstructorRegistry.getInstance().addInstructor(instructor);
            actionPrints.add("Hired new instructor: " + person.getName() + " with salary per hour: " + hourSalary);
            return instructor;
        }

        return null;
    }

    @Override
    public Session addSession(SessionType sessionType, String date, ForumType forumType, Instructor instructor) throws InstructorNotQualifiedException {
        if (!isCurrentSecretary())
            return null;

        Session session = SessionFactory.createSession(sessionType, date, forumType, instructor);
        if (!SessionRegistry.getInstance().isSessionRegistered(session)) {
            SessionRegistry.getInstance().addSession(session);
            RegisterClientToSession.getInstance().getClientListMap().put(session, new HashSet<>());
            actionPrints.add("Created new session: " + sessionType + " on " + date + " with instructor: " + instructor.getPerson().getName());
            return session;
        }

        return null;
    }

    @Override
    public void registerClientToLesson(Client c1, Session s1) throws DuplicateClientException, ClientNotRegisteredException, NullPointerException {
        if (!isCurrentSecretary())
            throw new NullPointerException();

    RegisterClientToSession.getInstance().addToMap(s1, c1);
    }

    public void unRegisterClientFromLessons(Client c1) {
        for (Session session : RegisterClientToSession.getInstance().getClientListMap().keySet()) {
            if (RegisterClientToSession.getInstance().getClientListMap().get(session).contains(c1))
                RegisterClientToSession.getInstance().getClientListMap().get(session).remove(c1);
        }
    }

    public void notify(Session s1, String message) {
        for (Client client : RegisterClientToSession.getInstance().getClientListMap().get(s1)) {
            client.getNotifications().add(message);
        }
        this.getActionPrints().add("A message was sent to everyone registered for session " + s1.getSessionType() + " on " + s1.getDate() + " : " + message);
    }

    public void notify(String date, String message) {
        for (Session session : RegisterClientToSession.getInstance().getClientListMap().keySet()) {
            if (session.getDate().substring(0, 10).equals(date)) {
                for (Client client : RegisterClientToSession.getInstance().getClientListMap().get(session)) {
                   client.getNotifications().add(message);
                }
            }
        }
        this.getActionPrints().add("A message was sent to everyone registered for a session on " + date  + " : " + message);
    }

    public void notify(String message) {
        for (Client client :ClientRegistry.getInstance().getAllClients() )
           client.getNotifications().add(message);

        this.getActionPrints().add("A message was sent to all gym clients: " + message);
    }

    public void paySalaries() {
        Gym gym = Gym.getInstance();
        this.person.addToBalance(gym.getSecretarySalary(), this.secretaryKey);
        gym.subtractFromGymBalance(gym.getSecretarySalary());

        for (Instructor instructor : InstructorRegistry.getInstance().getAllInstructors()) {
            instructor.getPerson().addToBalance(instructor.getHourSalary(), this.secretaryKey);
            gym.subtractFromGymBalance(instructor.getHourSalary());
        }

        this.getActionPrints().add("Salaries have been paid to all employees");

    }

    public void printActions() {
        for (String string: this.actionPrints){
            System.out.println(string);
        }
    }

    private boolean isCurrentSecretary() {
        return Gym.getInstance().getSecretary().equals(this);
    }

    public String getKey(){
        return this.secretaryKey;
    }

    @Override
    public String toString() {
        return "Name: " + person.getName() + " | Gender: " + person.getGender() + " | Birthday: " + person.getBirthDate() + " | Age: " + person.getAge() + " | Balance: " + person.getBalance() +
                " | Role: Secretary | Salary per Month: " + Gym.getInstance().getSecretarySalary();
    }
}