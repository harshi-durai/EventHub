
package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;

/**
 * EVENTHUB
 * User Dashboard
 *
 * Theme:
 * Black + White + Grey
 *
 * Features:
 * - Animated background
 * - Smooth card entrance
 * - Hover animations
 * - Event discovery
 * - My bookings
 * - Profile section
 * - Logout
 */
public class UserDashboard extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BLACK =
            new Color(5, 5, 7);

    private static final Color DARK =
            new Color(12, 12, 15);

    private static final Color CARD =
            new Color(16, 16, 19);

    private static final Color CARD_HOVER =
            new Color(24, 24, 27);

    private static final Color WHITE =
            new Color(245, 245, 245);

    private static final Color LIGHT =
            new Color(220, 220, 224);

    private static final Color GREY =
            new Color(150, 150, 156);

    private static final Color MUTED =
            new Color(105, 105, 112);

    private static final Color BORDER =
            new Color(45, 45, 49);
    private static final Color HOVER =
        new Color(28, 28, 31);

    // =========================================================
    // USER
    // =========================================================

    private final User user;

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPanel mainContent;

    private JPanel heroPanel;

    private JLabel welcomeName;

    private Timer animationTimer;

    private double animationProgress = 0.0;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UserDashboard(User user) {

        this.user = user;

        setTitle(
                "EVENTHUB - Dashboard"
        );

        setSize(
                1250,
                760
        );

        setMinimumSize(
                new Dimension(
                        1050,
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
        // TOP NAVIGATION
        // =====================================================

        JPanel navbar =
                createNavbar();

        background.add(
                navbar,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContent =
                new JPanel(
                        new BorderLayout()
                );

        mainContent.setOpaque(
                false
        );

        mainContent.setBorder(
                new EmptyBorder(
                        5,
                        55,
                        30,
                        55
                )
        );

        // =====================================================
        // SCROLL CONTENT
        // =====================================================

        JPanel content =
                new JPanel();

        content.setOpaque(
                false
        );

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // HERO
        // =====================================================

        heroPanel =
                createHeroSection();

        content.add(
                heroPanel
        );

        content.add(
                Box.createVerticalStrut(
                        24
                )
        );

        // =====================================================
        // SECTION TITLE
        // =====================================================

        JPanel sectionHeader =
                createSectionHeader();

        content.add(
                sectionHeader
        );

        content.add(
                Box.createVerticalStrut(
                        14
                )
        );

        // =====================================================
        // ACTION CARDS
        // =====================================================

        JPanel cards =
                createActionCards();

        content.add(
                cards
        );

        content.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // QUICK INFO
        // =====================================================

        JPanel info =
                createInfoSection();

        content.add(
                info
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setOpaque(
                false
        );

        scrollPane.getViewport()
                .setOpaque(false);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        scrollPane.getVerticalScrollBar()
                .setPreferredSize(
                        new Dimension(
                                0,
                                0
                        )
                );

        mainContent.add(
                scrollPane,
                BorderLayout.CENTER
        );

        background.add(
                mainContent,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );
    }

    // =========================================================
    // NAVBAR
    // =========================================================

    private JPanel createNavbar() {

        JPanel navbar =
                new JPanel(
                        new BorderLayout()
                );

        navbar.setOpaque(false);

        navbar.setBorder(
                new EmptyBorder(
                        22,
                        55,
                        20,
                        55
                )
        );

        // =====================================================
        // LOGO
        // =====================================================

        JPanel logo =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        logo.setOpaque(false);

        JLabel logoBox =
                new JLabel(
                        "E",
                        SwingConstants.CENTER
                );

        logoBox.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
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

        JLabel logoText =
                new JLabel(
                        "EVENTHUB"
                );

        logoText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        logoText.setForeground(
                WHITE
        );

        logo.add(
                logoBox
        );

        logo.add(
                logoText
        );

        navbar.add(
                logo,
                BorderLayout.WEST
        );

        // =====================================================
        // RIGHT USER AREA
        // =====================================================

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                15,
                                0
                        )
                );

        right.setOpaque(false);

        JLabel userText =
                new JLabel(
                        getUserName()
                );

        userText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        userText.setForeground(
                LIGHT
        );

        JLabel avatar =
                new JLabel(
                        getInitials(),
                        SwingConstants.CENTER
                );

        avatar.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        avatar.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        15
                )
        );

        avatar.setForeground(
                BLACK
        );

        avatar.setBackground(
                WHITE
        );

        avatar.setOpaque(true);

        JButton logout =
                createNavButton(
                        "LOGOUT"
                );

        right.add(
                userText
        );

        right.add(
                avatar
        );

        right.add(
                logout
        );

        navbar.add(
                right,
                BorderLayout.EAST
        );

        logout.addActionListener(
                e -> logout()
        );

        return navbar;
    }

    // =========================================================
    // HERO SECTION
    // =========================================================

    private JPanel createHeroSection() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout()
                );

        wrapper.setOpaque(false);

        wrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        245
                )
        );

        JPanel hero =
                new JPanel();

        hero.setBackground(
                CARD
        );

        hero.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                32,
                                35,
                                32,
                                35
                        )
                )
        );

        hero.setLayout(
                new BoxLayout(
                        hero,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // LABEL
        // =====================================================

        JLabel label =
                new JLabel(
                        "YOUR EVENTHUB SPACE"
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                GREY
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        hero.add(
                label
        );

        hero.add(
                Box.createVerticalStrut(
                        10
                )
        );

        // =====================================================
        // WELCOME
        // =====================================================

        welcomeName =
                new JLabel(
                        "Welcome, "
                                + getUserName()
                                + "."
                );

        welcomeName.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        42
                )
        );

        welcomeName.setForeground(
                WHITE
        );

        welcomeName.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        hero.add(
                welcomeName
        );

        hero.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel description =
                new JLabel(
                        "Everything you need to discover events, "
                                + "choose seats and manage your bookings."
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        description.setForeground(
                GREY
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        hero.add(
                description
        );

        hero.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // CTA
        // =====================================================

        JButton discover =
                createPrimaryButton(
                        "DISCOVER EVENTS"
                );

        discover.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        hero.add(
                discover
        );

        discover.addActionListener(
                e -> openEvents()
        );

        wrapper.add(
                hero,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    // =========================================================
    // SECTION HEADER
    // =========================================================

    private JPanel createSectionHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        JLabel title =
                new JLabel(
                        "QUICK ACCESS"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        title.setForeground(
                GREY
        );

        JLabel line =
                new JLabel(
                        "Choose what you want to do next."
                );

        line.setFont(
                new Font(
                        "Serif",
                        Font.ITALIC,
                        15
                )
        );

        line.setForeground(
                LIGHT
        );

        panel.add(
                title,
                BorderLayout.WEST
        );

        panel.add(
                line,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // ACTION CARDS
    // =========================================================

    private JPanel createActionCards() {

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                16,
                                0
                        )
                );

        cards.setOpaque(false);

        cards.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        175
                )
        );

        cards.add(
                createActionCard(
                        "01",
                        "DISCOVER",
                        "Explore upcoming events and "
                                + "find something worth attending.",
                        "BROWSE EVENTS",
                        this::openEvents
                )
        );

        cards.add(
                createActionCard(
                        "02",
                        "MY BOOKINGS",
                        "View your confirmed bookings "
                                + "and ticket details.",
                        "VIEW BOOKINGS",
                        this::openBookings
                )
        );

        cards.add(
                createActionCard(
                        "03",
                        "MY PROFILE",
                        "View your account information "
                                + "and personal details.",
                        "VIEW PROFILE",
                        this::showProfile
                )
        );

        return cards;
    }

    // =========================================================
    // ACTION CARD
    // =========================================================

    private JPanel createActionCard(
            String number,
            String title,
            String description,
            String buttonText,
            Runnable action
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(
                CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // NUMBER
        // =====================================================

        JLabel numberLabel =
                new JLabel(
                        number
                );

        numberLabel.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        13
                )
        );

        numberLabel.setForeground(
                GREY
        );

        numberLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(
                numberLabel
        );

        card.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        22
                )
        );

        titleLabel.setForeground(
                WHITE
        );

        titleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(
                titleLabel
        );

        card.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        descriptionLabel.setForeground(
                GREY
        );

        descriptionLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(
                descriptionLabel
        );

        card.add(
                Box.createVerticalGlue()
        );

        // =====================================================
        // BUTTON
        // =====================================================

        JButton button =
                createSmallButton(
                        buttonText
                );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(
                button
        );

        button.addActionListener(
                e -> action.run()
        );

        // =====================================================
        // HOVER
        // =====================================================

        card.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                CARD_HOVER
                        );

                        card.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(
                                                        90,
                                                        90,
                                                        94
                                                )
                                        ),
                                        new EmptyBorder(
                                                18,
                                                20,
                                                18,
                                                20
                                        )
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                CARD
                        );

                        card.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                BORDER
                                        ),
                                        new EmptyBorder(
                                                18,
                                                20,
                                                18,
                                                20
                                        )
                                )
                        );
                    }
                }
        );

        return card;
    }

    // =========================================================
    // INFO SECTION
    // =========================================================

    private JPanel createInfoSection() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                16,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        115
                )
        );

        panel.add(
                createInfoCard(
                        "EVENTS",
                        "Discover",
                        "Find upcoming college events "
                                + "and exciting experiences."
                )
        );

        panel.add(
                createInfoCard(
                        "TICKETS",
                        "Book",
                        "Choose your preferred seats "
                                + "and keep your bookings organized."
                )
        );

        return panel;
    }

    // =========================================================
    // INFO CARD
    // =========================================================

    private JPanel createInfoCard(
            String label,
            String title,
            String description
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
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
                                16,
                                20,
                                16,
                                20
                        )
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel small =
                new JLabel(
                        label
                );

        small.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        small.setForeground(
                MUTED
        );

        JLabel heading =
                new JLabel(
                        title
                );

        heading.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        20
                )
        );

        heading.setForeground(
                WHITE
        );

        JLabel desc =
                new JLabel(
                        description
                );

        desc.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        desc.setForeground(
                GREY
        );

        text.add(
                small
        );

        text.add(
                Box.createVerticalStrut(
                        4
                )
        );

        text.add(
                heading
        );

        text.add(
                Box.createVerticalStrut(
                        4
                )
        );

        text.add(
                desc
        );

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        175,
                        44
                )
        );

        button.setMaximumSize(
                new Dimension(
                        175,
                        44
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

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                LIGHT
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                WHITE
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // SMALL BUTTON
    // =========================================================

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        135,
                        34
                )
        );

        button.setMaximumSize(
                new Dimension(
                        135,
                        34
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                DARK
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
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

                        button.setBackground(
                                HOVER
                        );

                        button.setForeground(
                                WHITE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                DARK
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // NAV BUTTON
    // =========================================================

    private JButton createNavButton(
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
                        11
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
    // OPEN EVENTS
    // =========================================================

    private void openEvents() {

        try {

            EventListFrame frame =
                    new EventListFrame(
                            user
                    );

            frame.setVisible(
                    true
            );

            dispose();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open events.",
                    "EVENTHUB",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // OPEN BOOKINGS
    // =========================================================

    private void openBookings() {

        try {

            MyBookingsFrame frame =
                    new MyBookingsFrame(
                            user
                    );

            frame.setVisible(
                    true
            );

            dispose();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open your bookings.",
                    "EVENTHUB",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // PROFILE
    // =========================================================

    private void showProfile() {

        String name =
                getUserName();

        String email =
                getUserEmail();

        JOptionPane.showMessageDialog(
                this,
                "Name: "
                        + name
                        + "\n\n"
                        + "Email: "
                        + email,
                "MY PROFILE",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "EVENTHUB",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        == JOptionPane.YES_OPTION
        ) {

            dispose();

            new LoginFrame()
                    .setVisible(true);
        }
    }

    // =========================================================
    // USER NAME
    // =========================================================

    private String getUserName() {

        try {

            String name =
                    user.getName();

            if (
                    name != null
                            && !name.trim().isEmpty()
            ) {

                return name.trim();
            }

        } catch (Exception ignored) {
        }

        return "User";
    }

    // =========================================================
    // USER EMAIL
    // =========================================================

    private String getUserEmail() {

        try {

            String email =
                    user.getEmail();

            if (
                    email != null
                            && !email.trim().isEmpty()
            ) {

                return email.trim();
            }

        } catch (Exception ignored) {
        }

        return "Not available";
    }

    // =========================================================
    // INITIALS
    // =========================================================

    private String getInitials() {

        String name =
                getUserName();

        String[] parts =
                name.trim().split(
                        "\\s+"
                );

        if (
                parts.length == 1
        ) {

            return parts[0]
                    .substring(
                            0,
                            1
                    )
                    .toUpperCase();
        }

        return (
                parts[0]
                        .substring(
                                0,
                                1
                        )
                        + parts[
                        parts.length - 1
                ].substring(
                        0,
                        1
                )
        ).toUpperCase();
    }

    // =========================================================
    // ANIMATION
    // =========================================================

    private void startAnimation() {

        animationTimer =
                new Timer(
                        16,
                        e -> {

                            animationProgress +=
                                    0.035;

                            if (
                                    animationProgress
                                            >= 1.0
                            ) {

                                animationProgress =
                                        1.0;

                                animationTimer.stop();
                            }

                            float alpha =
                                    (float)
                                            animationProgress;

                            setOpacitySafely(
                                    alpha
                            );

                            if (
                                    heroPanel != null
                            ) {

                                heroPanel.repaint();
                            }
                        }
                );

        animationTimer.setInitialDelay(
                80
        );

        animationTimer.start();
    }

    // =========================================================
    // SAFE WINDOW FADE
    // =========================================================

    private void setOpacitySafely(
            float value
    ) {

        try {

            if (
                    isDisplayable()
                            && isUndecorated()
            ) {

                setOpacity(
                        Math.max(
                                0.0f,
                                Math.min(
                                        1.0f,
                                        value
                                )
                        )
                );
            }

        } catch (Exception ignored) {
        }
    }

    // =========================================================
    // ANIMATED BACKGROUND
    // =========================================================

    private static class BackgroundPanel
            extends JPanel {

        private double phase =
                0.0;

        private final Timer timer;

        BackgroundPanel() {

            setBackground(
                    BLACK
            );

            timer =
                    new Timer(
                            35,
                            e -> {

                                phase +=
                                        0.018;

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
            // PURE BLACK
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
            // VERY SUBTLE MOVING HIGHLIGHT
            // =================================================

            int glowX =
                    (int)
                            (
                                    width * 0.78
                                            + Math.sin(
                                            phase
                                            ) * 100
                            );

            int glowY =
                    (int)
                            (
                                    height * 0.25
                                            + Math.cos(
                                            phase * 0.8
                                            ) * 60
                            );

            RadialGradientPaint glow =
                    new RadialGradientPaint(
                            new Point(
                                    glowX,
                                    glowY
                            ),
                            280f,
                            new float[]{
                                    0f,
                                    0.45f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            255,
                                            255,
                                            255,
                                            9
                                    ),
                                    new Color(
                                            255,
                                            255,
                                            255,
                                            3
                                    ),
                                    new Color(
                                            0,
                                            0,
                                            0,
                                            0
                                    )
                            }
                    );

            g2.setPaint(
                    glow
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            // =================================================
            // FLOATING DOTS
            // =================================================

            for (
                    int i = 0;
                    i < 18;
                    i++
            ) {

                int x =
                        (
                                i * 173
                                        + 40
                        )
                                % Math.max(
                                width,
                                1
                        );

                int baseY =
                        (
                                i * 113
                                        + 30
                        )
                                % Math.max(
                                height,
                                1
                        );

                int y =
                        (int)
                                (
                                        baseY
                                                + Math.sin(
                                                phase
                                                        + i * 0.6
                                        ) * 12
                                );

                int size =
                        i % 4 == 0
                                ? 2
                                : 1;

                g2.setColor(
                        new Color(
                                210,
                                210,
                                215,
                                25
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

                    // Demo user for testing
                    User demoUser =
                            new User(
                                    1,
                                    "Harshini",
                                    "harshini@eventhub.com",
                                    "",
                                    "USER"
                            );

                    UserDashboard frame =
                            new UserDashboard(
                                    demoUser
                            );

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}

