
package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * EVENTHUB
 * Register Page
 *
 * Theme:
 * Black + White + Grey
 */
public class RegisterFrame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BLACK =
            new Color(5, 5, 7);

    private static final Color DARK =
            new Color(13, 13, 16);

    private static final Color DARKER =
            new Color(9, 9, 11);

    private static final Color WHITE =
            new Color(245, 245, 245);

    private static final Color LIGHT_WHITE =
            new Color(220, 220, 224);

    private static final Color GREY =
            new Color(150, 150, 156);

    private static final Color BORDER =
            new Color(48, 48, 52);

    private static final Color HOVER =
            new Color(28, 28, 31);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTextField nameField;
    private JTextField emailField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton registerButton;
    private JButton backButton;

    private JLabel messageLabel;

    private JPanel card;

    private Timer animationTimer;

    private int animationY = 30;

    // =========================================================
    // DAO
    // =========================================================

    private final UserDAO userDAO =
            new UserDAO();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RegisterFrame() {

        setTitle(
                "EVENTHUB - Register"
        );

        setSize(
                1250,
                760
        );

        setMinimumSize(
                new Dimension(
                        1000,
                        650
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        buildUI();

        startAnimation();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        BackgroundPanel background =
                new BackgroundPanel();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // TOP BAR
        // =====================================================

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );

        topBar.setOpaque(false);

        topBar.setBorder(
                new EmptyBorder(
                        25,
                        55,
                        15,
                        55
                )
        );

        // =====================================================
        // LOGO
        // =====================================================

        JPanel logoPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        logoPanel.setOpaque(false);

        JLabel logoBox =
                new JLabel(
                        "E",
                        SwingConstants.CENTER
                );

        logoBox.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        22
                )
        );

        logoBox.setForeground(
                BLACK
        );

        logoBox.setBackground(
                WHITE
        );

        logoBox.setOpaque(true);

        logoBox.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        JLabel logoText =
                new JLabel(
                        "EVENTHUB"
                );

        logoText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        logoText.setForeground(
                WHITE
        );

        logoPanel.add(
                logoBox
        );

        logoPanel.add(
                logoText
        );

        topBar.add(
                logoPanel,
                BorderLayout.WEST
        );

        // =====================================================
        // BACK TO HOME
        // =====================================================

        backButton =
                createTextButton(
                        "BACK TO HOME"
                );

        topBar.add(
                backButton,
                BorderLayout.EAST
        );

        background.add(
                topBar,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER
        // =====================================================

        JPanel center =
                new JPanel(
                        new GridBagLayout()
                );

        center.setOpaque(false);

        card =
                createRegisterCard();

        center.add(
                card
        );

        background.add(
                center,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );

        // =====================================================
        // BACK ACTION
        // =====================================================

        backButton.addActionListener(
                e -> {

                    dispose();

                    new HomeFrame()
                            .setVisible(true);
                }
        );
    }

    // =========================================================
    // REGISTER CARD
    // =========================================================

    private JPanel createRegisterCard() {

        JPanel panel =
                new JPanel();

        panel.setPreferredSize(
                new Dimension(
                        500,
                        600
                )
        );

        panel.setBackground(
                DARK
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                30,
                                48,
                                30,
                                48
                        )
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // SMALL LABEL
        // =====================================================

        JLabel smallLabel =
                new JLabel(
                        "JOIN EVENTHUB"
                );

        smallLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        smallLabel.setForeground(
                GREY
        );

        smallLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                smallLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "Create Account."
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        36
                )
        );

        title.setForeground(
                WHITE
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                title
        );

        panel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitle =
                new JLabel(
                        "Create your account and start exploring."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                GREY
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                subtitle
        );

        panel.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // NAME
        // =====================================================

        JLabel nameLabel =
                createFieldLabel(
                        "FULL NAME"
                );

        panel.add(
                nameLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        nameField =
                createTextField();

        panel.add(
                nameField
        );

        panel.add(
                Box.createVerticalStrut(
                        13
                )
        );

        // =====================================================
        // EMAIL
        // =====================================================

        JLabel emailLabel =
                createFieldLabel(
                        "EMAIL ADDRESS"
                );

        panel.add(
                emailLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        emailField =
                createTextField();

        panel.add(
                emailField
        );

        panel.add(
                Box.createVerticalStrut(
                        13
                )
        );

        // =====================================================
        // PASSWORD
        // =====================================================

        JLabel passwordLabel =
                createFieldLabel(
                        "PASSWORD"
                );

        panel.add(
                passwordLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        passwordField =
                createPasswordField();

        panel.add(
                passwordField
        );

        panel.add(
                Box.createVerticalStrut(
                        13
                )
        );

        // =====================================================
        // CONFIRM PASSWORD
        // =====================================================

        JLabel confirmLabel =
                createFieldLabel(
                        "CONFIRM PASSWORD"
                );

        panel.add(
                confirmLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        confirmPasswordField =
                createPasswordField();

        panel.add(
                confirmPasswordField
        );

        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // MESSAGE
        // =====================================================

        messageLabel =
                new JLabel(
                        " "
                );

        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        messageLabel.setForeground(
                LIGHT_WHITE
        );

        messageLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(
                messageLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // REGISTER BUTTON
        // =====================================================

        registerButton =
                createRegisterButton(
                        "CREATE ACCOUNT"
                );

        panel.add(
                registerButton
        );

        panel.add(
                Box.createVerticalStrut(
                        13
                )
        );

        // =====================================================
        // LOGIN LINK
        // =====================================================

        JPanel loginPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                4,
                                0
                        )
                );

        loginPanel.setOpaque(false);

        JLabel existingUser =
                new JLabel(
                        "Already have an account?"
                );

        existingUser.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        existingUser.setForeground(
                GREY
        );

        JButton loginLink =
                createLinkButton(
                        "LOGIN"
                );

        loginPanel.add(
                existingUser
        );

        loginPanel.add(
                loginLink
        );

        loginPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                loginPanel
        );

        // =====================================================
        // LOGIN ACTION
        // =====================================================

        loginLink.addActionListener(
                e -> {

                    dispose();

                    new LoginFrame()
                            .setVisible(true);
                }
        );

        // =====================================================
        // REGISTER ACTION
        // =====================================================

        registerButton.addActionListener(
                e -> registerUser()
        );

        // =====================================================
        // ENTER KEY
        // =====================================================

        confirmPasswordField.addActionListener(
                e -> registerUser()
        );

        return panel;
    }

    // =========================================================
    // REGISTER USER
    // =========================================================

    private void registerUser() {

        String name =
                nameField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        // =====================================================
        // VALIDATION
        // =====================================================

        if (name.isEmpty()) {

            showMessage(
                    "Please enter your full name."
            );

            nameField.requestFocus();

            return;
        }

        if (email.isEmpty()) {

            showMessage(
                    "Please enter your email address."
            );

            emailField.requestFocus();

            return;
        }

        if (!email.contains("@")
                || !email.contains(".")) {

            showMessage(
                    "Please enter a valid email address."
            );

            emailField.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            showMessage(
                    "Please enter a password."
            );

            passwordField.requestFocus();

            return;
        }

        if (password.length() < 6) {

            showMessage(
                    "Password must contain at least 6 characters."
            );

            passwordField.requestFocus();

            return;
        }

        if (!password.equals(
                confirmPassword
        )) {

            showMessage(
                    "Passwords do not match."
            );

            confirmPasswordField.requestFocus();

            return;
        }

        // =====================================================
        // DISABLE BUTTON
        // =====================================================

        registerButton.setEnabled(
                false
        );

        registerButton.setText(
                "CREATING ACCOUNT..."
        );

        messageLabel.setText(
                " "
        );

        // =====================================================
        // USER OBJECT
        // =====================================================

        User user =
                new User(
                        name,
                        email,
                        password,
                        "USER"
                );

        // =====================================================
        // DATABASE OPERATION
        // =====================================================

        SwingWorker<Boolean, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected Boolean doInBackground()
                            throws Exception {

                        return userDAO.registerUser(
                                user
                        );
                    }

                    @Override
                    protected void done() {

                        try {

                            boolean success =
                                    get();

                            if (success) {

                                JOptionPane.showMessageDialog(
                                        RegisterFrame.this,
                                        "Account created successfully!",
                                        "EVENTHUB",
                                        JOptionPane.INFORMATION_MESSAGE
                                );

                                dispose();

                                new LoginFrame()
                                        .setVisible(true);

                            } else {

                                showMessage(
                                        "Registration failed. Email may already exist."
                                );

                                registerButton.setEnabled(
                                        true
                                );

                                registerButton.setText(
                                        "CREATE ACCOUNT"
                                );
                            }

                        } catch (Exception ex) {

                            ex.printStackTrace();

                            showMessage(
                                    "Unable to connect to the database."
                            );

                            registerButton.setEnabled(
                                    true
                            );

                            registerButton.setText(
                                    "CREATE ACCOUNT"
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // MESSAGE
    // =========================================================

    private void showMessage(
            String message
    ) {

        messageLabel.setText(
                message
        );
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        label.setForeground(
                LIGHT_WHITE
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setPreferredSize(
                new Dimension(
                        390,
                        42
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(
                WHITE
        );

        field.setBackground(
                DARKER
        );

        field.setCaretColor(
                WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                13,
                                0,
                                13
                        )
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addFieldHoverEffect(
                field
        );

        return field;
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setPreferredSize(
                new Dimension(
                        390,
                        42
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(
                WHITE
        );

        field.setBackground(
                DARKER
        );

        field.setCaretColor(
                WHITE
        );

        field.setEchoChar(
                '•'
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                13,
                                0,
                                13
                        )
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addFieldHoverEffect(
                field
        );

        return field;
    }

    // =========================================================
    // FIELD HOVER
    // =========================================================

    private void addFieldHoverEffect(
            JComponent component
    ) {

        component.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        component.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(
                                                        90,
                                                        90,
                                                        94
                                                )
                                        ),
                                        new EmptyBorder(
                                                0,
                                                13,
                                                0,
                                                13
                                        )
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        component.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                BORDER
                                        ),
                                        new EmptyBorder(
                                                0,
                                                13,
                                                0,
                                                13
                                        )
                                )
                        );
                    }
                }
        );
    }

    // =========================================================
    // REGISTER BUTTON
    // =========================================================

    private JButton createRegisterButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        button.setPreferredSize(
                new Dimension(
                        390,
                        46
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                BLACK
        );

        button.setBackground(
                WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder()
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (button.isEnabled()) {

                            button.setBackground(
                                    LIGHT_WHITE
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (button.isEnabled()) {

                            button.setBackground(
                                    WHITE
                            );
                        }
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TEXT BUTTON
    // =========================================================

    private JButton createTextButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                GREY
        );

        button.setContentAreaFilled(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setForeground(
                                WHITE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setForeground(
                                GREY
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // LINK BUTTON
    // =========================================================

    private JButton createLinkButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                WHITE
        );

        button.setContentAreaFilled(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setForeground(
                                LIGHT_WHITE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setForeground(
                                WHITE
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // ENTRANCE ANIMATION
    // =========================================================

    private void startAnimation() {

        animationTimer =
                new Timer(
                        18,
                        e -> {

                            if (animationY > 0) {

                                animationY--;

                                card.setBorder(
                                        BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(
                                                        BORDER
                                                ),
                                                new EmptyBorder(
                                                        30 + animationY,
                                                        48,
                                                        30,
                                                        48
                                                )
                                        )
                                );

                                card.revalidate();

                                card.repaint();

                            } else {

                                animationTimer.stop();
                            }
                        }
                );

        animationTimer.setInitialDelay(
                100
        );

        animationTimer.start();
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    private static class BackgroundPanel
            extends JPanel {

        private double movement =
                0;

        private final Timer timer;

        BackgroundPanel() {

            setBackground(
                    BLACK
            );

            timer =
                    new Timer(
                            45,
                            e -> {

                                movement +=
                                        0.025;

                                repaint();
                            }
                    );

            timer.start();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(
                    g
            );

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            // =================================================
            // BLACK
            // =================================================

            g2.setColor(
                    BLACK
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            // =================================================
            // SUBTLE GREY PARTICLES
            // =================================================

            for (
                    int i = 0;
                    i < 14;
                    i++
            ) {

                int x =
                        (i * 197)
                                % Math.max(
                                width,
                                1
                        );

                int baseY =
                        (i * 131)
                                % Math.max(
                                height,
                                1
                        );

                int y =
                        (int)
                                (
                                        baseY
                                                + Math.sin(
                                                movement + i
                                                ) * 7
                                );

                int size =
                        i % 3 == 0
                                ? 2
                                : 1;

                g2.setColor(
                        new Color(
                                130,
                                130,
                                135,
                                30
                        )
                );

                g2.fillOval(
                        x,
                        y,
                        size,
                        size
                );
            }

            g2.dispose();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    RegisterFrame frame =
                            new RegisterFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}

