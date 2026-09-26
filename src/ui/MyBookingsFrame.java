
package ui;

import dao.BookingDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.Booking;
import model.User;

public class MyBookingsFrame extends JFrame {

    private User loggedInUser;
    private JPanel bookingsPanel;

    public MyBookingsFrame(User user) {

        this.loggedInUser = user;

        setTitle("EventHub - My Bookings");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(18, 18, 18)
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(
                new Color(25, 25, 25)
        );

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setBackground(
                new Color(25, 25, 25)
        );

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel("MY BOOKINGS");

        titleLabel.setForeground(
                Color.WHITE
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "View your event booking history"
                );

        subtitleLabel.setForeground(
                new Color(170, 170, 170)
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        titlePanel.add(titleLabel);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitleLabel);

        JButton refreshButton =
                new JButton("REFRESH");

        styleButton(refreshButton);

        refreshButton.setPreferredSize(
                new Dimension(
                        110,
                        35
                )
        );

        refreshButton.addActionListener(
                e -> loadBookings()
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        headerPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        // =====================================================
        // BOOKINGS PANEL
        // =====================================================

        bookingsPanel =
                new JPanel();

        bookingsPanel.setBackground(
                new Color(18, 18, 18)
        );

        bookingsPanel.setLayout(
                new BoxLayout(
                        bookingsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        bookingsPanel
                );

        scrollPane.setBorder(null);

        scrollPane.setBackground(
                new Color(18, 18, 18)
        );

        scrollPane.getViewport()
                .setBackground(
                        new Color(18, 18, 18)
                );

        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        bottomPanel.setBackground(
                new Color(25, 25, 25)
        );

        JButton backButton =
                new JButton("BACK");

        styleButton(backButton);

        backButton.setPreferredSize(
                new Dimension(
                        100,
                        35
                )
        );

        backButton.addActionListener(
                e -> dispose()
        );

        bottomPanel.add(backButton);

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        loadBookings();

        setVisible(true);
    }

    // =========================================================
    // LOAD BOOKINGS
    // =========================================================

    private void loadBookings() {

        bookingsPanel.removeAll();

        BookingDAO bookingDAO =
                new BookingDAO();

        List<Booking> bookings =
                bookingDAO.getBookingsByUser(
                        loggedInUser.getId()
                );

        if (bookings.isEmpty()) {

            JLabel noBookingsLabel =
                    new JLabel(
                            "You have no bookings yet."
                    );

            noBookingsLabel.setForeground(
                    new Color(
                            180,
                            180,
                            180
                    )
            );

            noBookingsLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            16
                    )
            );

            noBookingsLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            bookingsPanel.add(
                    Box.createVerticalStrut(
                            60
                    )
            );

            bookingsPanel.add(
                    noBookingsLabel
            );

        } else {

            for (Booking booking : bookings) {

                bookingsPanel.add(
                        createBookingCard(
                                booking
                        )
                );

                bookingsPanel.add(
                        Box.createVerticalStrut(
                                15
                        )
                );
            }
        }

        bookingsPanel.revalidate();

        bookingsPanel.repaint();
    }

    // =========================================================
    // CREATE BOOKING CARD
    // =========================================================

    private JPanel createBookingCard(
            Booking booking) {

        BookingDAO bookingDAO =
                new BookingDAO();

        String eventName =
                bookingDAO.getEventName(
                        booking.getEventId()
                );

        String eventDate =
                bookingDAO.getEventDate(
                        booking.getEventId()
                );

        String venue =
                bookingDAO.getEventVenue(
                        booking.getEventId()
                );

        String seats =
                bookingDAO.getBookedSeats(
                        booking.getId()
                );

        String ticketCode =
                bookingDAO.getTicketCode(
                        booking.getId()
                );

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                new Color(28, 28, 28)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        55,
                                        55,
                                        55
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        900,
                        250
                )
        );

        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel infoPanel =
                new JPanel();

        infoPanel.setBackground(
                new Color(28, 28, 28)
        );

        infoPanel.setLayout(
                new BoxLayout(
                        infoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel eventLabel =
                new JLabel(eventName);

        eventLabel.setForeground(
                Color.WHITE
        );

        eventLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        JLabel bookingIdLabel =
                new JLabel(
                        "Booking ID: "
                                + booking.getId()
                );

        JLabel dateLabel =
                new JLabel(
                        "Date: "
                                + eventDate
                );

        JLabel venueLabel =
                new JLabel(
                        "Venue: "
                                + venue
                );

        JLabel seatsLabel =
                new JLabel(
                        "Seats: "
                                + seats
                );

        styleInfoLabel(
                bookingIdLabel
        );

        styleInfoLabel(
                dateLabel
        );

        styleInfoLabel(
                venueLabel
        );

        styleInfoLabel(
                seatsLabel
        );

        infoPanel.add(eventLabel);

        infoPanel.add(
                Box.createVerticalStrut(8)
        );

        infoPanel.add(
                bookingIdLabel
        );

        infoPanel.add(
                Box.createVerticalStrut(4)
        );

        infoPanel.add(
                dateLabel
        );

        infoPanel.add(
                Box.createVerticalStrut(4)
        );

        infoPanel.add(
                venueLabel
        );

        infoPanel.add(
                Box.createVerticalStrut(4)
        );

        infoPanel.add(
                seatsLabel
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel rightPanel =
                new JPanel();

        rightPanel.setBackground(
                new Color(28, 28, 28)
        );

        rightPanel.setLayout(
                new BoxLayout(
                        rightPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel amountLabel =
                new JLabel(
                        "Rs. "
                                + String.format(
                                "%.2f",
                                booking.getTotalAmount()
                        )
                );

        amountLabel.setForeground(
                Color.WHITE
        );

        amountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        amountLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // =====================================================
        // STATUS
        // =====================================================

        JLabel statusLabel =
                new JLabel(
                        booking.getStatus()
                );

        statusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        if (
                "CONFIRMED".equalsIgnoreCase(
                        booking.getStatus()
                )
        ) {

            statusLabel.setForeground(
                    new Color(
                            100,
                            210,
                            140
                    )
            );

        } else {

            statusLabel.setForeground(
                    new Color(
                            220,
                            100,
                            100
                    )
            );
        }

        // =====================================================
        // TICKET
        // =====================================================

        JLabel ticketLabel =
                new JLabel(
                        "Ticket: "
                                + ticketCode
                );

        ticketLabel.setForeground(
                new Color(
                        170,
                        170,
                        170
                )
        );

        ticketLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        ticketLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // =====================================================
        // VIEW TICKET BUTTON
        // =====================================================

        JButton ticketButton =
                new JButton(
                        "VIEW TICKET"
                );

        styleButton(ticketButton);

        ticketButton.setPreferredSize(
                new Dimension(
                        150,
                        38
                )
        );

        ticketButton.setMaximumSize(
                new Dimension(
                        150,
                        38
                )
        );

        ticketButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        ticketButton.addActionListener(
                e -> showTicket(
                        booking,
                        eventName,
                        eventDate,
                        venue,
                        seats,
                        ticketCode
                )
        );

        // =====================================================
        // CANCEL BUTTON
        // =====================================================

        JButton cancelButton =
                new JButton(
                        "CANCEL BOOKING"
                );

        styleCancelButton(
                cancelButton
        );

        cancelButton.setPreferredSize(
                new Dimension(
                        150,
                        38
                )
        );

        cancelButton.setMaximumSize(
                new Dimension(
                        150,
                        38
                )
        );

        cancelButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cancelButton.addActionListener(
                e -> cancelBooking(
                        booking
                )
        );

        // =====================================================
        // ADD RIGHT SIDE COMPONENTS
        // =====================================================

        rightPanel.add(
                amountLabel
        );

        rightPanel.add(
                Box.createVerticalStrut(5)
        );

        rightPanel.add(
                statusLabel
        );

        rightPanel.add(
                Box.createVerticalStrut(8)
        );

        rightPanel.add(
                ticketLabel
        );

        rightPanel.add(
                Box.createVerticalStrut(12)
        );

        rightPanel.add(
                ticketButton
        );

        // =====================================================
        // ONLY SHOW CANCEL FOR CONFIRMED BOOKINGS
        // =====================================================

        if (
                "CONFIRMED".equalsIgnoreCase(
                        booking.getStatus()
                )
        ) {

            rightPanel.add(
                    Box.createVerticalStrut(
                            8
                    )
            );

            rightPanel.add(
                    cancelButton
            );
        }

        // =====================================================
        // ADD PANELS
        // =====================================================

        card.add(
                infoPanel,
                BorderLayout.CENTER
        );

        card.add(
                rightPanel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    private void cancelBooking(
            Booking booking) {

        int result =
                JOptionPane.showConfirmDialog(
                        this,

                        "Are you sure you want to cancel this booking?\n\n"
                                + "Booking ID: "
                                + booking.getId()
                                + "\n\n"
                                + "The selected seats will be released.",

                        "Cancel Booking",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );

        if (
                result
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        BookingDAO bookingDAO =
                new BookingDAO();

        boolean cancelled =
                bookingDAO.cancelBooking(
                        booking.getId(),
                        loggedInUser.getId()
                );

        if (cancelled) {

            JOptionPane.showMessageDialog(
                    this,

                    "Booking cancelled successfully!\n\n"
                            + "Booking ID: "
                            + booking.getId()
                            + "\n\n"
                            + "The seats are now available again.",

                    "Cancellation Successful",

                    JOptionPane.INFORMATION_MESSAGE
            );

            loadBookings();

        } else {

            JOptionPane.showMessageDialog(
                    this,

                    "Unable to cancel this booking.\n\n"
                            + "It may already be cancelled "
                            + "or the booking could not be found.",

                    "Cancellation Failed",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SHOW TICKET
    // =========================================================

    private void showTicket(
            Booking booking,
            String eventName,
            String eventDate,
            String venue,
            String seats,
            String ticketCode) {

        String message =
                "================================\n"
                        + "          EVENTHUB TICKET\n"
                        + "================================\n\n"

                        + "Event       : "
                        + eventName

                        + "\nDate        : "
                        + eventDate

                        + "\nVenue       : "
                        + venue

                        + "\nSeats       : "
                        + seats

                        + "\nAmount      : Rs. "
                        + String.format(
                        "%.2f",
                        booking.getTotalAmount()
                )

                        + "\nBooking ID  : "
                        + booking.getId()

                        + "\nTicket Code : "
                        + ticketCode

                        + "\nStatus      : "
                        + booking.getStatus()

                        + "\n\n================================";

        JOptionPane.showMessageDialog(
                this,
                message,
                "Digital Ticket",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // LABEL STYLE
    // =========================================================

    private void styleInfoLabel(
            JLabel label) {

        label.setForeground(
                new Color(
                        190,
                        190,
                        190
                )
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );
    }

    // =========================================================
    // NORMAL BUTTON STYLE
    // =========================================================

    private void styleButton(
            JButton button) {

        button.setBackground(
                Color.WHITE
        );

        button.setForeground(
                Color.BLACK
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // CANCEL BUTTON STYLE
    // =========================================================

    private void styleCancelButton(
            JButton button) {

        button.setBackground(
                new Color(
                        45,
                        45,
                        45
                )
        );

        button.setForeground(
                new Color(
                        235,
                        235,
                        235
                )
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                90,
                                90,
                                90
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // TEST
    // =========================================================

    public static void main(
            String[] args) {

        User testUser =
                new User(
                        2,
                        "Harshini",
                        "harshini@eventhub.com",
                        "user123",
                        "USER"
                );

        SwingUtilities.invokeLater(
                () -> new MyBookingsFrame(
                        testUser
                )
        );
    }
}

