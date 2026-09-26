package ui;

import dao.BookingDAO;
import dao.SeatDAO;
import model.Booking;
import model.Event;
import model.Seat;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFrame extends JFrame {

    private Event event;
    private User user;

    private JPanel seatsPanel;
    private JLabel selectedLabel;
    private JLabel totalLabel;

    private List<Seat> selectedSeats = new ArrayList<>();

    public SeatSelectionFrame(Event event, User user) {

        this.event = event;
        this.user = user;

        setTitle("EventHub - Select Seats");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBackground(new Color(25, 25, 25));

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        JLabel titleLabel =
                new JLabel(event.getName());

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        JLabel priceLabel =
                new JLabel(
                        "Rs. " +
                        String.format(
                                "%.2f",
                                event.getTicketPrice()
                        ) +
                        " / seat"
                );

        priceLabel.setForeground(
                new Color(190, 190, 190)
        );

        priceLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        headerPanel.add(
                priceLabel,
                BorderLayout.EAST
        );

        // =========================
        // CENTER
        // =========================

        JPanel centerPanel =
                new JPanel();

        centerPanel.setBackground(
                new Color(18, 18, 18)
        );

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel screenLabel =
                new JLabel("SCREEN");

        screenLabel.setForeground(
                new Color(180, 180, 180)
        );

        screenLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        screenLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                Box.createVerticalStrut(25)
        );

        centerPanel.add(screenLabel);

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // =========================
        // SEATS
        // =========================

        seatsPanel = new JPanel();

        seatsPanel.setBackground(
                new Color(18, 18, 18)
        );

        seatsPanel.setLayout(
                new GridLayout(
                        3,
                        5,
                        15,
                        15
                )
        );

        loadSeats();

        centerPanel.add(seatsPanel);

        // =========================
        // SELECTION INFO
        // =========================

        selectedLabel =
                new JLabel("Selected Seats: None");

        selectedLabel.setForeground(Color.WHITE);

        selectedLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        selectedLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        totalLabel =
                new JLabel("Total: Rs. 0.00");

        totalLabel.setForeground(
                new Color(200, 200, 200)
        );

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        totalLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                Box.createVerticalStrut(25)
        );

        centerPanel.add(selectedLabel);

        centerPanel.add(
                Box.createVerticalStrut(8)
        );

        centerPanel.add(totalLabel);

        // =========================
        // BOTTOM
        // =========================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                15
                        )
                );

        bottomPanel.setBackground(
                new Color(25, 25, 25)
        );

        JButton backButton =
                new JButton("BACK");

        styleButton(backButton);

        backButton.addActionListener(
                e -> dispose()
        );

        JButton confirmButton =
                new JButton("CONFIRM BOOKING");

        styleButton(confirmButton);

        confirmButton.addActionListener(
                e -> confirmBooking()
        );

        bottomPanel.add(backButton);
        bottomPanel.add(confirmButton);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        setVisible(true);
    }

    // =========================================
    // LOAD SEATS
    // =========================================

    private void loadSeats() {

        SeatDAO seatDAO = new SeatDAO();

        List<Seat> seats =
                seatDAO.getSeatsByEvent(
                        event.getId()
                );

        for (Seat seat : seats) {

            JButton seatButton =
                    new JButton(
                            seat.getSeatNumber()
                    );

            seatButton.setPreferredSize(
                    new Dimension(80, 50)
            );

            seatButton.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            seatButton.setFocusPainted(false);

            if ("BOOKED".equals(
                    seat.getStatus()
            )) {

                seatButton.setEnabled(false);

                seatButton.setBackground(
                        new Color(120, 50, 50)
                );

                seatButton.setForeground(
                        Color.WHITE
                );

            } else {

                seatButton.setBackground(
                        new Color(55, 150, 90)
                );

                seatButton.setForeground(
                        Color.WHITE
                );

                seatButton.addActionListener(
                        e -> toggleSeat(
                                seat,
                                seatButton
                        )
                );
            }

            seatsPanel.add(seatButton);
        }

        seatsPanel.revalidate();
        seatsPanel.repaint();
    }

    // =========================================
    // SELECT / DESELECT
    // =========================================

    private void toggleSeat(
            Seat seat,
            JButton seatButton) {

        if (selectedSeats.contains(seat)) {

            selectedSeats.remove(seat);

            seatButton.setBackground(
                    new Color(55, 150, 90)
            );

        } else {

            selectedSeats.add(seat);

            seatButton.setBackground(
                    new Color(70, 110, 200)
            );
        }

        updateSelectionInfo();
    }

    // =========================================
    // UPDATE TOTAL
    // =========================================

    private void updateSelectionInfo() {

        if (selectedSeats.isEmpty()) {

            selectedLabel.setText(
                    "Selected Seats: None"
            );

        } else {

            StringBuilder seatsText =
                    new StringBuilder();

            for (int i = 0;
                 i < selectedSeats.size();
                 i++) {

                if (i > 0) {
                    seatsText.append(", ");
                }

                seatsText.append(
                        selectedSeats
                                .get(i)
                                .getSeatNumber()
                );
            }

            selectedLabel.setText(
                    "Selected Seats: " +
                    seatsText
            );
        }

        double total =
                selectedSeats.size() *
                event.getTicketPrice();

        totalLabel.setText(
                "Total: Rs. " +
                String.format(
                        "%.2f",
                        total
                )
        );
    }

    // =========================================
    // CONFIRM BOOKING
    // =========================================

    private void confirmBooking() {

        if (selectedSeats.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one seat.",
                    "Seat Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double total =
                selectedSeats.size() *
                event.getTicketPrice();

        StringBuilder seatsText =
                new StringBuilder();

        for (Seat seat : selectedSeats) {

            if (seatsText.length() > 0) {
                seatsText.append(", ");
            }

            seatsText.append(
                    seat.getSeatNumber()
            );
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,

                        "Event: " +
                        event.getName() +

                        "\nSeats: " +
                        seatsText +

                        "\nTotal: Rs. " +
                        String.format(
                                "%.2f",
                                total
                        ) +

                        "\n\nConfirm your booking?",

                        "Confirm Booking",

                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // =====================================
        // CREATE BOOKING OBJECT
        // =====================================

        Booking booking =
                new Booking(
                        0,
                        user.getId(),
                        event.getId(),
                        total,
                        "CONFIRMED"
                );

        // =====================================
        // SAVE TO DATABASE
        // =====================================

        BookingDAO bookingDAO =
                new BookingDAO();

        boolean success =
                bookingDAO.createBooking(
                        booking,
                        selectedSeats
                );

        if (success) {

            showBookingSuccess(
                    booking,
                    seatsText.toString(),
                    total
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,

                    "Booking failed.\n" +
                    "Please try again.",

                    "Booking Failed",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================
    // SUCCESS MESSAGE
    // =========================================

    private void showBookingSuccess(
            Booking booking,
            String seats,
            double total) {

        String message =
                "BOOKING CONFIRMED!\n\n" +

                "Event: " +
                event.getName() +

                "\nDate: " +
                event.getEventDate() +

                "\nVenue: " +
                event.getVenue() +

                "\n\nSeats: " +
                seats +

                "\nTotal: Rs. " +
                String.format(
                        "%.2f",
                        total
                ) +

                "\n\nBooking ID: " +
                booking.getId();

        JOptionPane.showMessageDialog(
                this,
                message,
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    // =========================================
    // BUTTON STYLE
    // =========================================

    private void styleButton(
            JButton button) {

        button.setBackground(Color.WHITE);

        button.setForeground(Color.BLACK);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setPreferredSize(
                new Dimension(180, 40)
        );
    }
}