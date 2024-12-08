package gym.management.Sessions;

import gym.customers.Client;
import gym.customers.Gender;
import gym.customers.Instructor;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class Session {
    private final SessionType type;
    private final String dateTime; // Stored as "dd-MM-yyyy HH:mm"
    private final ForumType forum;
    private final Instructor instructor;
    private final List<Client> participants = new ArrayList<>();

    public Session(SessionType type, String dateTime, ForumType forum, Instructor instructor) {
        this.type = type;
        validateFormat(dateTime);
        this.dateTime = dateTime;
        this.forum = forum;
        this.instructor = instructor;
    }

    // Validation for date format
    private void validateFormat(String dateOfBirth) {
        if (!dateOfBirth.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid date format. Use 'dd-MM-yyyy HH:mm'.");
        }
    }


    public boolean isExpired() {
        // Parse session date-time
        LocalDateTime sessionDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        // Get current time formatted to the same pattern
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Compare session date-time with the current date-time
        return !sessionDateTime.isAfter(currentDateTime); // Returns true if expired
    }

    public boolean isForumCompatible(Client client) {
        switch (forum) {
            case Male:
                return client.getGender() == Gender.Male;
            case Female:
                return client.getGender() == Gender.Female;
            case Seniors:
                return client.getAge() >= 65;
            case All:
                return true;
            default:
                return false;
        }
    }

    public boolean hasSpace() {
        return participants.size() < type.getMaxParticipants();
    }

    public boolean addParticipant(Client client) {
        if (!hasSpace()) {
            return false; // No space left
        }
        if (participants.contains(client)) {
            return false; // Client already registered
        }
        participants.add(client);
        return true;
    }


    public String getType() {
        return type.getName();
    }


    public ForumType getForum() {
        return forum;
    }
    public double getPrice() {
        return type.getPrice();
    }

    public List<Client> getParticipants() {
        return participants;
    }

    public String getDateTime() {
        return dateTime;
    }
}
