
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
 * Login Page
 *
 * Theme:
 * Black + White + Grey
 *
 * Uses existing UserDAO.loginUser()
 */
public class LoginFrame extends JFrame {

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

    private static final Color ERROR =
            new Color(210, 210, 210);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTextField emailField;

    private JPasswordField passwordField;

    private JButton loginButton;

    private JButton backButton;

    private JLabel messageLabel;

    private Timer animationTimer;

    private JPanel card;

    private int animationY = 35;

    // =========================================================
    // DAO
    // =========================================================

    private final UserDAO userDAO =
            new UserDAO();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginFrame() {

        setTitle(
                "EVENTHUB - Login"
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
        // MAIN CENTER
        // =====================================================

        JPanel center =
                new JPanel(
                        new GridBagLayout()
                );

        center.setOpaque(false);

        card =
                createLoginCard();

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
        // ACTIONS
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
    // LOGIN CARD
    // =========================================================

    private JPanel createLoginCard() {

        JPanel panel =
                new JPanel();

        panel.setPreferredSize(
                new Dimension(
                        480,
                        505
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
                                38,
                                48,
                                38,
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
                        "WELCOME TO EVENTHUB"
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
                        12
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "Welcome Back."
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        38
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
                        8
                )
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitle =
                new JLabel(
                        "Login to continue your EVENTHUB experience."
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
                        30
                )
        );

        // =====================================================
        // EMAIL LABEL
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
                        8
                )
        );

        // =====================================================
        // EMAIL FIELD
        // =====================================================

        emailField =
                createTextField();

        panel.add(
                emailField
        );

        panel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // PASSWORD LABEL
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
                        8
                )
        );

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        passwordField =
                createPasswordField();

        panel.add(
                passwordField
        );

        panel.add(
                Box.createVerticalStrut(
                        10
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
                        12
                )
        );

        messageLabel.setForeground(
                ERROR
        );

        messageLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(
                messageLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        10
                )
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        loginButton =
                createLoginButton(
                        "LOGIN"
                );

        panel.add(
                loginButton
        );

        panel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // REGISTER TEXT
        // =====================================================

        JPanel registerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                4,
                                0
                        )
                );

        registerPanel.setOpaque(false);

        JLabel newUser =
                new JLabel(
                        "Don't have an account?"
                );

        newUser.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        newUser.setForeground(
                GREY
        );

        JButton registerButton =
                createLinkButton(
                        "REGISTER"
                );

        registerPanel.add(
                newUser
        );

        registerPanel.add(
                registerButton
        );

        registerPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                registerPanel
        );

        // =====================================================
        // REGISTER ACTION
        // =====================================================

        registerButton.addActionListener(
                e -> {

                    dispose();

                    new RegisterFrame()
                            .setVisible(true);
                }
        );

        // =====================================================
        // LOGIN ACTION
        // =====================================================

        loginButton.addActionListener(
                e -> performLogin()
        );

        // =====================================================
        // ENTER KEY
        // =====================================================

        passwordField.addActionListener(
                e -> performLogin()
        );

        return panel;
    }

    // =========================================================
    // PERFORM LOGIN
    // =========================================================

    private void performLogin() {

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        // =====================================================
        // VALIDATION
        // =====================================================

        if (email.isEmpty()) {

            showMessage(
                    "Please enter your email address."
            );

            emailField.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            showMessage(
                    "Please enter your password."
            );

            passwordField.requestFocus();

            return;
        }

        // =====================================================
        // DISABLE BUTTON
        // =====================================================

        loginButton.setEnabled(
                false
        );

        loginButton.setText(
                "SIGNING IN..."
        );

        messageLabel.setText(
                " "
        );

        // =====================================================
        // DATABASE OPERATION
        // =====================================================

        SwingWorker<User, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected User doInBackground()
                            throws Exception {

                        return userDAO.loginUser(
                                email,
                                password
                        );
                    }

                    @Override
                    protected void done() {

                        try {

                            User user =
                                    get();

                            if (user != null) {

                                dispose();

                                new UserDashboard(
                                        user
                                ).setVisible(
                                        true
                                );

                            } else {

                                showMessage(
                                        "Invalid email or password."
                                );

                                passwordField.setText(
                                        ""
                                );

                                passwordField.requestFocus();

                                loginButton.setEnabled(
                                        true
                                );

                                loginButton.setText(
                                        "LOGIN"
                                );
                            }

                        } catch (Exception ex) {

                            ex.printStackTrace();

                            showMessage(
                                    "Unable to connect to the database."
                            );

                            loginButton.setEnabled(
                                    true
                            );

                            loginButton.setText(
                                    "LOGIN"
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
                        11
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
                        48
                )
        );

        field.setPreferredSize(
                new Dimension(
                        380,
                        48
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
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
                                14,
                                0,
                                14
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
                        48
                )
        );

        field.setPreferredSize(
                new Dimension(
                        380,
                        48
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
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
                                14,
                                0,
                                14
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
                                                14,
                                                0,
                                                14
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
                                                14,
                                                0,
                                                14
                                        )
                                )
                        );
                    }
                }
        );
    }

    // =========================================================
    // LOGIN BUTTON
    // =========================================================

    private JButton createLoginButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        button.setPreferredSize(
                new Dimension(
                        380,
                        50
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
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
    // REGISTER LINK
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
    // SUBTLE ENTRANCE ANIMATION
    // =========================================================

    private void startAnimation() {

        animationTimer =
                new Timer(
                        15,
                        e -> {

                            if (animationY > 0) {

                                animationY -= 1;

                                card.setBorder(
                                        BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(
                                                        BORDER
                                                ),
                                                new EmptyBorder(
                                                        38 + animationY,
                                                        48,
                                                        38,
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
            // BLACK BACKGROUND
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

                    LoginFrame frame =
                            new LoginFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}
