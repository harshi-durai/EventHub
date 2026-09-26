package ui;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class HomeFrame extends JFrame {
    private static final Color BLACK =
            new Color(5, 5, 7);

    private static final Color DARK =
            new Color(13, 13, 16);

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

    private AnimatedBackground background;
    private JPanel heroPanel;
    private Timer animationTimer;

    private int animationStep = 24;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public HomeFrame() {

        setTitle(
                "EVENTHUB - Event & Ticket Booking System"
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

        background =
                new AnimatedBackground();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // NAVBAR
        // =====================================================

        JPanel navbar =
                new JPanel(
                        new BorderLayout()
                );

        navbar.setOpaque(false);

        navbar.setBorder(
                new EmptyBorder(
                        25,
                        55,
                        20,
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

        JLabel logoLetter =
                new JLabel(
                        "E",
                        SwingConstants.CENTER
                );

        logoLetter.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        22
                )
        );

        logoLetter.setForeground(
                BLACK
        );

        logoLetter.setBackground(
                WHITE
        );

        logoLetter.setOpaque(true);

        logoLetter.setPreferredSize(
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
                logoLetter
        );

        logoPanel.add(
                logoText
        );

        navbar.add(
                logoPanel,
                BorderLayout.WEST
        );

        // =====================================================
        // ONLY LOGIN BUTTON
        // =====================================================

        JPanel navigation =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        navigation.setOpaque(false);

        JButton loginButton =
                createNavButton(
                        "LOGIN",
                        90
                );

        navigation.add(
                loginButton
        );

        navbar.add(
                navigation,
                BorderLayout.EAST
        );

        background.add(
                navbar,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel centerContainer =
                new JPanel(
                        new BorderLayout()
                );

        centerContainer.setOpaque(false);

        // =====================================================
        // HERO
        // =====================================================

        heroPanel =
                createHeroPanel();

        centerContainer.add(
                heroPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // FEATURES
        // =====================================================

        JPanel features =
                createFeatureSection();

        centerContainer.add(
                features,
                BorderLayout.SOUTH
        );

        background.add(
                centerContainer,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );

        // =====================================================
        // LOGIN ACTION
        // =====================================================

        loginButton.addActionListener(
                e -> {

                    dispose();

                    new LoginFrame()
                            .setVisible(true);
                }
        );
    }

    // =========================================================
    // HERO SECTION
    // =========================================================

    private JPanel createHeroPanel() {

        JPanel outer =
                new JPanel(
                        new GridBagLayout()
                );

        outer.setOpaque(false);

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // SMALL TEXT
        // =====================================================

        JLabel smallLabel =
                new JLabel(
                        "EVENTS  •  EXPERIENCES  •  MEMORIES"
                );

        smallLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        smallLabel.setForeground(
                LIGHT_WHITE
        );

        smallLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(
                smallLabel
        );

        content.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // MAIN HEADING
        // =====================================================

        JLabel heading =
                new JLabel(
                        "Experience Events."
                );

        heading.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        50
                )
        );

        heading.setForeground(
                WHITE
        );

        heading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(
                heading
        );

        // =====================================================
        // SECOND HEADING
        // =====================================================

        JLabel headingTwo =
                new JLabel(
                        "Create Memories."
                );

        headingTwo.setFont(
                new Font(
                        "Serif",
                        Font.ITALIC | Font.BOLD,
                        48
                )
        );

        headingTwo.setForeground(
                LIGHT_WHITE
        );

        headingTwo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(
                headingTwo
        );

        content.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel description =
                new JLabel(
                        "<html>"
                                + "<div style='text-align:center;'>"
                                + "Discover exciting events, choose your seats,<br>"
                                + "and book your tickets in just a few clicks."
                                + "</div>"
                                + "</html>"
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        description.setForeground(
                GREY
        );

        description.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(
                description
        );

        content.add(
                Box.createVerticalStrut(
                        30
                )
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                12,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton exploreButton =
                createMainButton(
                        "EXPLORE EVENTS"
                );

        JButton howButton =
                createSecondaryButton(
                        "HOW IT WORKS"
                );

        buttonPanel.add(
                exploreButton
        );

        buttonPanel.add(
                howButton
        );

        content.add(
                buttonPanel
        );

        // =====================================================
        // EXPLORE ACTION
        // =====================================================

        exploreButton.addActionListener(
                e -> {

                    dispose();

                    new LoginFrame()
                            .setVisible(true);
                }
        );

        // =====================================================
        // HOW IT WORKS ACTION
        // =====================================================

        howButton.addActionListener(
                e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "1. Create your EVENTHUB account.\n"
                                    + "2. Login to your account.\n"
                                    + "3. Discover upcoming events.\n"
                                    + "4. Select your preferred seats.\n"
                                    + "5. Confirm your ticket booking.",
                            "How EVENTHUB Works",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );

        outer.add(
                content
        );

        return outer;
    }

    // =========================================================
    // FEATURE SECTION
    // =========================================================

    private JPanel createFeatureSection() {

        JPanel section =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                18,
                                15
                        )
                );

        section.setOpaque(false);

        section.setBorder(
                new EmptyBorder(
                        0,
                        45,
                        25,
                        45
                )
        );

        section.add(
                createFeatureCard(
                        "01",
                        "DISCOVER",
                        "Explore upcoming events."
                )
        );

        section.add(
                createFeatureCard(
                        "02",
                        "CHOOSE",
                        "Select your preferred seats."
                )
        );

        section.add(
                createFeatureCard(
                        "03",
                        "BOOK",
                        "Confirm your tickets easily."
                )
        );

        return section;
    }

    // =========================================================
    // FEATURE CARD
    // =========================================================

    private JPanel createFeatureCard(
            String number,
            String title,
            String description
    ) {

        JPanel card =
                new JPanel();

        card.setPreferredSize(
                new Dimension(
                        270,
                        105
                )
        );

        card.setBackground(
                DARK
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                14,
                                18,
                                14,
                                18
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
                        "SansSerif",
                        Font.BOLD,
                        11
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
                        6
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
                        18
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
                        4
                )
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel descriptionLabel =
                new JLabel(
                        description
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

        // =====================================================
        // HOVER ANIMATION
        // =====================================================

        card.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                HOVER
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
                                                14,
                                                18,
                                                14,
                                                18
                                        )
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                DARK
                        );

                        card.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                BORDER
                                        ),
                                        new EmptyBorder(
                                                14,
                                                18,
                                                14,
                                                18
                                        )
                                )
                        );
                    }
                }
        );

        return card;
    }

    // =========================================================
    // LOGIN NAV BUTTON
    // =========================================================

    private JButton createNavButton(
            String text,
            int width
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        width,
                        42
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
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

        button.setOpaque(false);

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
    // MAIN WHITE BUTTON
    // =========================================================

    private JButton createMainButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        180,
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

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                LIGHT_WHITE
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
    // SECONDARY BUTTON
    // =========================================================

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        155,
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
                WHITE
        );

        button.setBackground(
                DARK
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
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

                        button.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(
                                                        85,
                                                        85,
                                                        90
                                                )
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                10,
                                                15,
                                                10,
                                                15
                                        )
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                DARK
                        );

                        button.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                BORDER
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                10,
                                                15,
                                                10,
                                                15
                                        )
                                )
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
                        25,
                        e -> {

                            if (animationStep > 0) {

                                animationStep--;

                                heroPanel.setBorder(
                                        new EmptyBorder(
                                                animationStep,
                                                0,
                                                0,
                                                0
                                        )
                                );

                                heroPanel.revalidate();

                                heroPanel.repaint();

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
    // BLACK BACKGROUND WITH SUBTLE MOVING DOTS
    // =========================================================

    private static class AnimatedBackground
            extends JPanel {

        private double movement = 0;

        private final Timer timer;

        AnimatedBackground() {

            setBackground(
                    BLACK
            );

            timer =
                    new Timer(
                            45,
                            e -> {

                                movement += 0.025;

                                repaint();
                            }
                    );

            timer.start();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

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
            // PURE BLACK BACKGROUND
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
            // VERY SUBTLE GREY PARTICLES
            // =================================================

            for (
                    int i = 0;
                    i < 12;
                    i++
            ) {

                int x =
                        (i * 211)
                                % Math.max(
                                width,
                                1
                        );

                int baseY =
                        (i * 127)
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
                                                ) * 8
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
                                35
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

                    HomeFrame frame =
                            new HomeFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}

