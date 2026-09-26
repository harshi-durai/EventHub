package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("EventHub - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.setLayout(new BorderLayout());

        // ==============================
        // HEADER
        // ==============================

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(18, 18, 18));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("EVENTHUB");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Event & Ticket Booking System");
        subtitleLabel.setForeground(new Color(170, 170, 170));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalStrut(45));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(35));

        // ==============================
        // FORM PANEL
        // ==============================

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(18, 18, 18));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(350, 45));
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(350, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton loginButton = new JButton("LOGIN");
        loginButton.setMaximumSize(new Dimension(350, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(255, 255, 255));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        JLabel registerLabel = new JLabel("New user? Register here");
        registerLabel.setForeground(new Color(180, 180, 180));
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(emailField);

        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);

        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(registerLabel);

        // ==============================
        // LOGIN ACTION
        // ==============================

        loginButton.addActionListener(e -> loginUser());

        passwordField.addActionListener(e -> loginUser());

        // ==============================
        // ADD PANELS
        // ==============================

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    // ==============================
    // LOGIN METHOD
    // ==============================

    private void loginUser() {

        String email = emailField.getText().trim();

        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        UserDAO userDAO = new UserDAO();

        User user = userDAO.loginUser(email, password);

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Welcome, " + user.getName() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("Logged in user:");
            System.out.println("Name  : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Role  : " + user.getRole());

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==============================
    // MAIN
    // ==============================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}