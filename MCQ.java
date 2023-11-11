import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    private String profile;

    public User(String username, String password, String profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProfile() {
        return profile;
    }

    public void updateProfile(String newProfile) {
        this.profile = newProfile;
        System.out.println("Profile updated successfully!");
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password updated successfully!");
    }
}

class MCQ {
    private String question;
    private String[] options;
    private int correctOption;

    public MCQ(String question, String[] options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(int selectedOption) {
        return selectedOption == correctOption;
    }
}

class Quiz {
    private MCQ[] questions;
    private int timeLimit; 

    public Quiz(MCQ[] questions, int timeLimit) {
        this.questions = questions;
        this.timeLimit = timeLimit;
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        int score = 0;

        System.out.println("Welcome to the Quiz!");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("\nTime's up! Quiz will be auto-submitted.");
                scanner.close();
                timer.cancel();
            }
        };

        timer.schedule(task, timeLimit * 1000);

        for (MCQ question : questions) {
            System.out.println("\n" + question.getQuestion());

            for (int i = 0; i < question.getOptions().length; i++) {
                System.out.println((i + 1) + ". " + question.getOptions()[i]);
            }

            System.out.print("Enter your answer (1-" + question.getOptions().length + "): ");
            int selectedOption = scanner.nextInt();

            if (question.isCorrect(selectedOption)) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect!");
            }
        }

        System.out.println("\nQuiz completed! Your score: " + score + "/" + questions.length);
        timer.cancel();
    }
}

class SessionManager {
    private User currentUser;

    public void loginUser(String username, String password, Map<String, User> users) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    public void logoutUser() {
        currentUser = null;
        System.out.println("Logout successful.");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

public class Main {
    public static void main(String[] args) {
        Map<String, User> users = new HashMap<>();
        users.put("user1", new User("user1", "password1", "Student"));
        users.put("user2", new User("user2", "password2", "Teacher"));

        SessionManager sessionManager = new SessionManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Login\n2. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.next();
                System.out.print("Enter password: ");
                String password = scanner.next();

                sessionManager.loginUser(username, password, users);
                User currentUser = sessionManager.getCurrentUser();

                if (currentUser != null) {
                    System.out.println("\nWelcome, " + currentUser.getUsername() + "!");
                    System.out.println("Profile: " + currentUser.getProfile());

                    if (currentUser.getProfile().equals("Teacher")) {
                        // Teacher can update profiles and passwords
                        System.out.println("\n1. Update Profile\n2. Update Password\n3. Logout");
                        System.out.print("Choose an option: ");
                        int teacherChoice = scanner.nextInt();

                        if (teacherChoice == 1) {
                            System.out.print("Enter new profile: ");
                            String newProfile = scanner.next();
                            currentUser.updateProfile(newProfile);
                        } else if (teacherChoice == 2) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.next();
                            currentUser.updatePassword(newPassword);
                        } else if (teacherChoice == 3) {
                            sessionManager.logoutUser();
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    } else {
                        // Student can take a quiz
                        MCQ[] quizQuestions = {
                            new MCQ("What is the capital of France?", new String[]{"Paris", "Berlin", "London"}, 1),
                            new MCQ("Which planet is known as the Red Planet?", new String[]{"Mars", "Venus", "Jupiter"}, 0),
                            
                        };

                        Quiz quiz = new Quiz(quizQuestions, 60); // 60 seconds time limit
                        quiz.startQuiz();
                        sessionManager.logoutUser();
                    }
                }
            } else if (choice == 2) {
                System.out.println("Quitting...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}