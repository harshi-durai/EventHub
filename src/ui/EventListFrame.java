
package ui;

import dao.EventDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Event;
import model.User;

public class EventListFrame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BLACK =
            new Color(5, 5, 7);

    private static final Color DARK =
            new Color(11, 11, 13);

    private static final Color CARD =
            new Color(17, 17, 20);

    private static final Color CARD_HOVER =
            new Color(25, 25, 28);

    private static final Color WHITE =
            new Color(245, 245, 245);

    private static final Color LIGHT =
            new Color(220, 220, 224);

    private static final Color GREY =
            new Color(155, 155, 161);

    private static final Color MUTED =
            new Color(105, 105, 112);

    private static final Color BORDER =
            new Color(45, 45, 49);

    private static final Color HOVER =
            new Color(30, 30, 33);

    // =========================================================
    // USER
    // =========================================================

    private final User loggedInUser;

    // =========================================================
    // PANELS
    // =========================================================

    private JPanel eventsPanel;

    private JPanel contentPanel;

    private JLabel countLabel;

    // =========================================================
    // ANIMATION
    // =========================================================

    private Timer entranceTimer;

    private int animationOffset = 25;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public EventListFrame(User user) {

        this.loggedInUser = user;

        setTitle(
                "EVENTHUB - Discover Events"
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
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        buildUI();

        loadEvents();

        startEntranceAnimation();
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
                createTopBar();

        background.add(
                topBar,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN
        // =====================================================

        contentPanel =
                new JPanel();

        contentPanel.setOpaque(
                false
        );

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        10,
                        55,
                        30,
                        55
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel heading =
                createHeading();

        contentPanel.add(
                heading
        );

        contentPanel.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // EVENTS PANEL
        // =====================================================

        eventsPanel =
                new JPanel();

        eventsPanel.setOpaque(
                false
        );

        eventsPanel.setLayout(
                new BoxLayout(
                        eventsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.add(
                eventsPanel
        );

        contentPanel.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // =====================================================
        // SCROLL
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        contentPanel
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setOpaque(
                false
        );

        scrollPane.getViewport()
                .setOpaque(false);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        scrollPane.getVerticalScrollBar()
                .setPreferredSize(
                        new Dimension(
                                7,
                                0
                        )
                );

        background.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM BAR
        // =====================================================

        JPanel bottom =
                createBottomBar();

        background.add(
                bottom,
                BorderLayout.SOUTH
        );

        setContentPane(
                background
        );
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private JPanel createTopBar() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        22,
                        55,
                        18,
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

        panel.add(
                logo,
                BorderLayout.WEST
        );

        // =====================================================
        // RIGHT
        // =====================================================

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                0
                        )
                );

        right.setOpaque(false);

        JLabel userLabel =
                new JLabel(
                        getUserName()
                );

        userLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        userLabel.setForeground(
                GREY
        );

        JLabel avatar =
                new JLabel(
                        getInitials(),
                        SwingConstants.CENTER
                );

        avatar.setPreferredSize(
                new Dimension(
                        40,
                        40
                )
        );

        avatar.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        14
                )
        );

        avatar.setForeground(
                BLACK
        );

        avatar.setBackground(
                WHITE
        );

        avatar.setOpaque(true);

        right.add(
                userLabel
        );

        right.add(
                avatar
        );

        panel.add(
                right,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // HEADING
    // =========================================================

    private JPanel createHeading() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        105
                )
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel small =
                new JLabel(
                        "DISCOVER WHAT'S HAPPENING"
                );

        small.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        small.setForeground(
                GREY
        );

        JLabel title =
                new JLabel(
                        "Upcoming Events"
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        40
                )
        );

        title.setForeground(
                WHITE
        );

        JLabel subtitle =
                new JLabel(
                        "Find an event. Pick your seats. Make the moment yours."
                );

        subtitle.setFont(
                new Font(
                        "Serif",
                        Font.ITALIC,
                        15
                )
        );

        subtitle.setForeground(
                GREY
        );

        left.add(
                small
        );

        left.add(
                Box.createVerticalStrut(
                        7
                )
        );

        left.add(
                title
        );

        left.add(
                Box.createVerticalStrut(
                        5
                )
        );

        left.add(
                subtitle
        );

        panel.add(
                left,
                BorderLayout.WEST
        );

        // =====================================================
        // RIGHT
        // =====================================================

        JPanel right =
                new JPanel();

        right.setOpaque(false);

        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );

        countLabel =
                new JLabel(
                        " "
                );

        countLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        countLabel.setForeground(
                GREY
        );

        countLabel.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        JButton refresh =
                createRefreshButton(
                        "REFRESH"
                );

        refresh.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        refresh.addActionListener(
                e -> loadEvents()
        );

        right.add(
                countLabel
        );

        right.add(
                Box.createVerticalStrut(
                        10
                )
        );

        right.add(
                refresh
        );

        panel.add(
                right,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // BOTTOM BAR
    // =========================================================

    private JPanel createBottomBar() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        12,
                        55,
                        15,
                        55
                )
        );

        JButton back =
                createTextButton(
                        "←  BACK TO DASHBOARD"
                );

        back.addActionListener(
                e -> {

                    dispose();

                    new UserDashboard(
                            loggedInUser
                    ).setVisible(true);
                }
        );

        JLabel hint =
                new JLabel(
                        "Select an event to view details and book your seats."
                );

        hint.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        hint.setForeground(
                MUTED
        );

        panel.add(
                back,
                BorderLayout.WEST
        );

        panel.add(
                hint,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // LOAD EVENTS
    // =========================================================

    private void loadEvents() {

        eventsPanel.removeAll();

        countLabel.setText(
                "LOADING EVENTS..."
        );

        SwingWorker<List<Event>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Event> doInBackground() {

                        EventDAO eventDAO =
                                new EventDAO();

                        return eventDAO.getAllEvents();
                    }

                    @Override
                    protected void done() {

                        try {

                            List<Event> events =
                                    get();

                            displayEvents(
                                    events
                            );

                        } catch (Exception ex) {

                            ex.printStackTrace();

                            showDatabaseError(
                                    ex
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // DISPLAY EVENTS
    // =========================================================

    private void displayEvents(
            List<Event> events
    ) {

        eventsPanel.removeAll();

        if (
                events == null
                        || events.isEmpty()
        ) {

            countLabel.setText(
                    "NO EVENTS"
            );

            JPanel empty =
                    createEmptyState();

            eventsPanel.add(
                    empty
            );

        } else {

            countLabel.setText(
                    events.size()
                            + " EVENTS AVAILABLE"
            );

            for (
                    int i = 0;
                    i < events.size();
                    i++
            ) {

                Event event =
                        events.get(i);

                JPanel card =
                        createEventCard(
                                event,
                                i
                        );

                eventsPanel.add(
                        card
                );

                eventsPanel.add(
                        Box.createVerticalStrut(
                                14
                        )
                );
            }
        }

        eventsPanel.revalidate();

        eventsPanel.repaint();
    }

    // =========================================================
    // EMPTY STATE
    // =========================================================

    private JPanel createEmptyState() {

        JPanel panel =
                new JPanel();

        panel.setPreferredSize(
                new Dimension(
                        900,
                        230
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        230
                )
        );

        panel.setBackground(
                CARD
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel(
                        "—"
                );

        icon.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        35
                )
        );

        icon.setForeground(
                GREY
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "No upcoming events"
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(
                WHITE
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel text =
                new JLabel(
                        "Check again later for new events."
                );

        text.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        text.setForeground(
                GREY
        );

        text.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                Box.createVerticalGlue()
        );

        panel.add(
                icon
        );

        panel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        panel.add(
                title
        );

        panel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        panel.add(
                text
        );

        panel.add(
                Box.createVerticalGlue()
        );

        return panel;
    }

    // =========================================================
    // EVENT CARD
    // =========================================================

    private JPanel createEventCard(
            Event event,
            int index
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                25,
                                0
                        )
                );

        card.setBackground(
                CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                24,
                                28,
                                24,
                                28
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // =====================================================
        // LEFT NUMBER
        // =====================================================

        JPanel numberPanel =
                new JPanel();

        numberPanel.setOpaque(false);

        numberPanel.setPreferredSize(
                new Dimension(
                        55,
                        100
                )
        );

        numberPanel.setLayout(
                new GridBagLayout()
        );

        JLabel number =
                new JLabel(
                        String.format(
                                "%02d",
                                index + 1
                        )
                );

        number.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        25
                )
        );

        number.setForeground(
                MUTED
        );

        numberPanel.add(
                number
        );

        card.add(
                numberPanel,
                BorderLayout.WEST
        );

        // =====================================================
        // CENTER INFORMATION
        // =====================================================

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel eventName =
                new JLabel(
                        safe(
                                event.getName(),
                                "Untitled Event"
                        )
                );

        eventName.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        25
                )
        );

        eventName.setForeground(
                WHITE
        );

        eventName.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        info.add(
                eventName
        );

        info.add(
                Box.createVerticalStrut(
                        6
                )
        );

        String description =
                safe(
                        event.getDescription(),
                        "No description available."
                );

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + escapeHTML(
                                description
                        )
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

        info.add(
                descriptionLabel
        );

        info.add(
                Box.createVerticalStrut(
                        15
                )
        );

        // =====================================================
        // META
        // =====================================================

        JPanel meta =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        meta.setOpaque(false);

        JLabel date =
                createMetaLabel(
                        "DATE  "
                                + safe(
                                event.getEventDate(),
                                "-"
                        )
                );

        JLabel time =
                createMetaLabel(
                        "TIME  "
                                + safe(
                                event.getEventTime(),
                                "-"
                        )
                );

        JLabel venue =
                createMetaLabel(
                        "VENUE  "
                                + safe(
                                event.getVenue(),
                                "-"
                        )
                );

        meta.add(
                date
        );

        meta.add(
                Box.createHorizontalStrut(
                        18
                )
        );

        meta.add(
                time
        );

        meta.add(
                Box.createHorizontalStrut(
                        18
                )
        );

        meta.add(
                venue
        );

        info.add(
                meta
        );

        card.add(
                info,
                BorderLayout.CENTER
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel right =
                new JPanel();

        right.setOpaque(false);

        right.setPreferredSize(
                new Dimension(
                        175,
                        125
                )
        );

        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel priceCaption =
                new JLabel(
                        "TICKET PRICE"
                );

        priceCaption.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        9
                )
        );

        priceCaption.setForeground(
                MUTED
        );

        priceCaption.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        JLabel price =
                new JLabel(
                        "Rs. "
                                + String.format(
                                "%.2f",
                                event.getTicketPrice()
                        )
                );

        price.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        24
                )
        );

        price.setForeground(
                WHITE
        );

        price.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        JLabel status =
                new JLabel(
                        safe(
                                event.getStatus(),
                                "AVAILABLE"
                        ).toUpperCase()
                );

        status.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        9
                )
        );

        status.setForeground(
                LIGHT
        );

        status.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        JButton details =
                createDetailsButton(
                        "VIEW DETAILS"
                );

        details.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        details.addActionListener(
                e -> showEventDetails(
                        event
                )
        );

        right.add(
                priceCaption
        );

        right.add(
                Box.createVerticalStrut(
                        3
                )
        );

        right.add(
                price
        );

        right.add(
                Box.createVerticalStrut(
                        4
                )
        );

        right.add(
                status
        );

        right.add(
                Box.createVerticalGlue()
        );

        right.add(
                details
        );

        card.add(
                right,
                BorderLayout.EAST
        );

        // =====================================================
        // HOVER
        // =====================================================

        addCardHover(
                card,
                eventName
        );

        return card;
    }

    // =========================================================
    // META LABEL
    // =========================================================

    private JLabel createMetaLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        label.setForeground(
                LIGHT
        );

        return label;
    }

    // =========================================================
    // CARD HOVER
    // =========================================================

    private void addCardHover(
            JPanel card,
            JLabel eventName
    ) {

        card.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                CARD_HOVER
                        );

                        eventName.setForeground(
                                WHITE
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
                                                24,
                                                28,
                                                24,
                                                28
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
                                                24,
                                                28,
                                                24,
                                                28
                                        )
                                )
                        );
                    }
                }
        );
    }

    // =========================================================
    // EVENT DETAILS
    // =========================================================

    private void showEventDetails(
            Event event
    ) {

        String message =
                "EVENT DETAILS\n\n"
                        + "Event: "
                        + safe(
                        event.getName(),
                        "-"
                )
                        + "\n\nDescription: "
                        + safe(
                        event.getDescription(),
                        "-"
                )
                        + "\n\nDate: "
                        + safe(
                        event.getEventDate(),
                        "-"
                )
                        + "\nTime: "
                        + safe(
                        event.getEventTime(),
                        "-"
                )
                        + "\nVenue: "
                        + safe(
                        event.getVenue(),
                        "-"
                )
                        + "\n\nTicket Price: Rs. "
                        + String.format(
                        "%.2f",
                        event.getTicketPrice()
                )
                        + "\nCapacity: "
                        + event.getCapacity()
                        + "\nStatus: "
                        + safe(
                        event.getStatus(),
                        "-"
                );

        Object[] options = {
                "BOOK NOW",
                "CLOSE"
        };

        int choice =
                JOptionPane.showOptionDialog(
                        this,
                        message,
                        "Event Details",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (
                choice == 0
        ) {

            openSeatSelection(
                    event
            );
        }
    }

    // =========================================================
    // SEAT SELECTION
    // =========================================================

    private void openSeatSelection(
            Event event
    ) {

        new SeatSelectionFrame(
                event,
                loggedInUser
        );
    }

    // =========================================================
    // REFRESH BUTTON
    // =========================================================

    private JButton createRefreshButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        105,
                        36
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

        addButtonHover(
                button
        );

        return button;
    }

    // =========================================================
    // DETAILS BUTTON
    // =========================================================

    private JButton createDetailsButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        145,
                        36
                )
        );

        button.setMaximumSize(
                new Dimension(
                        145,
                        36
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
    // BUTTON HOVER
    // =========================================================

    private void addButtonHover(
            JButton button
    ) {

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

                        button.setForeground(
                                WHITE
                        );
                    }
                }
        );
    }

    // =========================================================
    // ENTRANCE ANIMATION
    // =========================================================

    private void startEntranceAnimation() {

        animationOffset = 25;

        entranceTimer =
                new Timer(
                        15,
                        e -> {

                            animationOffset -= 2;

                            if (
                                    animationOffset <= 0
                            ) {

                                animationOffset = 0;

                                entranceTimer.stop();
                            }

                            contentPanel.setBorder(
                                    new EmptyBorder(
                                            10 + animationOffset,
                                            55,
                                            30,
                                            55
                                    )
                            );

                            contentPanel.revalidate();

                            contentPanel.repaint();
                        }
                );

        entranceTimer.setInitialDelay(
                80
        );

        entranceTimer.start();
    }

    // =========================================================
    // DATABASE ERROR
    // =========================================================

    private void showDatabaseError(
            Exception ex
    ) {

        countLabel.setText(
                "UNABLE TO LOAD EVENTS"
        );

        eventsPanel.removeAll();

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        220
                )
        );

        JLabel title =
                new JLabel(
                        "Unable to load events"
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(
                WHITE
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel text =
                new JLabel(
                        "Please check your database connection and try again."
                );

        text.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        text.setForeground(
                GREY
        );

        text.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                Box.createVerticalStrut(
                        55
                )
        );

        panel.add(
                title
        );

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        panel.add(
                text
        );

        eventsPanel.add(
                panel
        );

        eventsPanel.revalidate();

        eventsPanel.repaint();
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            Object value,
            String fallback
    ) {

        if (
                value == null
        ) {

            return fallback;
        }

        String text =
                String.valueOf(
                        value
                ).trim();

        if (
                text.isEmpty()
        ) {

            return fallback;
        }

        return text;
    }

    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHTML(
            String text
    ) {

        return text
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                );
    }

    // =========================================================
    // USER NAME
    // =========================================================

    private String getUserName() {

        try {

            String name =
                    loggedInUser.getName();

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
    // INITIALS
    // =========================================================

    private String getInitials() {

        String name =
                getUserName();

        String[] parts =
                name.split(
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
            // SUBTLE MOVING LIGHT
            // =================================================

            int glowX =
                    (int)
                            (
                                    width * 0.75
                                            + Math.sin(
                                            phase
                                    ) * 100
                            );

            int glowY =
                    (int)
                            (
                                    height * 0.25
                                            + Math.cos(
                                            phase * 0.7
                                    ) * 55
                            );

            RadialGradientPaint glow =
                    new RadialGradientPaint(
                            new Point(
                                    glowX,
                                    glowY
                            ),
                            300f,
                            new float[]{
                                    0f,
                                    0.5f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            255,
                                            255,
                                            255,
                                            8
                                    ),
                                    new Color(
                                            255,
                                            255,
                                            255,
                                            2
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
                    i < 22;
                    i++
            ) {

                int x =
                        (
                                i * 157
                                        + 25
                        )
                                % Math.max(
                                width,
                                1
                        );

                int baseY =
                        (
                                i * 107
                                        + 20
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
                                                        + i * 0.55
                                        ) * 10
                                );

                int size =
                        i % 4 == 0
                                ? 2
                                : 1;

                g2.setColor(
                        new Color(
                                220,
                                220,
                                225,
                                23
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

                    JOptionPane.showMessageDialog(
                            null,
                            "Open EventListFrame from UserDashboard after login."
                    );
                }
        );
    }
}

