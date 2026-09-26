
import { useEffect, useState } from "react";
import "./App.css";

import Home from "./Home";
import Events from "./Events";
import EventDetails from "./EventDetails";
import SeatSelection from "./SeatSelection";
import BookingConfirmation from "./BookingConfirmation";
import MyBookings from "./MyBookings";
import Login from "./Login";
import Register from "./Register";

const API_BASE_URL = "https://eventhub-xhdu.onrender.com";

function App() {
  const [route, setRoute] = useState(
    window.location.hash.replace("#/", "") || "home"
  );

  const [selectedEvent, setSelectedEvent] = useState(null);

  const [selectedSeats, setSelectedSeats] = useState([]);

  const [currentUser, setCurrentUser] = useState(() => {
    const saved = localStorage.getItem("eventhubUser");

    try {
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });

  const [bookings, setBookings] = useState(() => {
    const saved = localStorage.getItem("eventhubBookings");

    try {
      return saved ? JSON.parse(saved) : [];
    } catch {
      return [];
    }
  });

  /* =====================================================
     ROUTING
  ===================================================== */

  useEffect(() => {
    const changeRoute = () => {
      const newRoute =
        window.location.hash.replace("#/", "") || "home";

      setRoute(newRoute);

      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    };

    window.addEventListener(
      "hashchange",
      changeRoute
    );

    return () => {
      window.removeEventListener(
        "hashchange",
        changeRoute
      );
    };
  }, []);

  const navigate = (page, data = {}) => {
    if (data.event) {
      setSelectedEvent(data.event);
    }

    if (data.seats) {
      setSelectedSeats(data.seats);
    }

    window.location.hash = `/${page}`;

    setRoute(page);

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  /* =====================================================
     LOGIN
  ===================================================== */

  const handleLogin = (user) => {
    setCurrentUser(user);

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(user)
    );

    navigate("home");
  };

  /* =====================================================
     REGISTER
  ===================================================== */

  const handleRegister = (user) => {
    setCurrentUser(user);

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(user)
    );

    navigate("home");
  };

  /* =====================================================
     LOGOUT
  ===================================================== */

  const handleLogout = () => {
    localStorage.removeItem(
      "eventhubUser"
    );

    setCurrentUser(null);

    navigate("home");
  };

  /* =====================================================
     NORMALIZE SEAT
  ===================================================== */

  const normalizeSeat = (seat) => {
    /*
     * New backend seat object:
     *
     * {
     *   id: 35,
     *   seatId: 35,
     *   eventId: 3,
     *   seatNumber: "A5",
     *   status: "AVAILABLE"
     * }
     *
     * Also supports old string seats:
     *
     * "A5"
     */

    if (
      typeof seat === "string"
    ) {
      return {
        id: null,
        seatId: null,
        eventId:
          selectedEvent?.id ??
          selectedEvent?.eventId ??
          null,
        seatNumber: seat,
        status: "AVAILABLE",
      };
    }

    if (!seat) {
      return null;
    }

    return {
      id:
        seat.id ??
        seat.seatId ??
        seat.seat_id ??
        null,

      seatId:
        seat.seatId ??
        seat.id ??
        seat.seat_id ??
        null,

      eventId:
        seat.eventId ??
        seat.event_id ??
        selectedEvent?.id ??
        selectedEvent?.eventId ??
        null,

      seatNumber:
        seat.seatNumber ??
        seat.seat_number ??
        seat.number ??
        null,

      status:
        seat.status ??
        "AVAILABLE",
    };
  };

  /* =====================================================
     BOOKING
  ===================================================== */

  const confirmBooking = async (
    event,
    seats
  ) => {
    if (!event) {
      alert(
        "Unable to identify the selected event."
      );
      return;
    }

    if (
      !Array.isArray(seats) ||
      seats.length === 0
    ) {
      alert(
        "Unable to identify the selected seats."
      );
      return;
    }

    /*
     * Convert every seat into a standard format.
     */
    const normalizedSeats =
      seats
        .map(normalizeSeat)
        .filter(
          (seat) =>
            seat &&
            seat.seatNumber
        );

    if (
      normalizedSeats.length !==
      seats.length
    ) {
      alert(
        "Unable to identify the selected seats."
      );
      return;
    }

    /*
     * Event ID
     */
    const eventId =
      event.id ??
      event.eventId;

    if (!eventId) {
      alert(
        "Unable to identify the selected event."
      );
      return;
    }

    /*
     * Seat IDs are required for backend booking.
     */
    const missingSeatIds =
      normalizedSeats.filter(
        (seat) =>
          seat.id === null ||
          seat.id === undefined
      );

    /*
     * For the moment we still allow the local
     * booking flow if the backend ID is missing.
     *
     * But with the new SeatSelection.jsx,
     * backend IDs should always be present.
     */
    if (
      missingSeatIds.length > 0
    ) {
      console.warn(
        "Some seats do not contain backend IDs:",
        missingSeatIds
      );
    }

    console.log(
      "Booking event:",
      event
    );

    console.log(
      "Booking seats:",
      normalizedSeats
    );

    /*
     * =================================================
     * BACKEND BOOKING
     * =================================================
     *
     * Try to create the real booking in MySQL.
     *
     * The Java backend expects:
     *
     * {
     *   userId,
     *   eventId,
     *   seatIds
     * }
     */

    let backendBooking = null;

    if (
      currentUser &&
      currentUser.id
    ) {
      try {
        const response =
          await fetch(
            `${API_BASE_URL}/api/bookings`,
            {
              method: "POST",

              headers: {
                "Content-Type":
                  "application/json",
              },

              body: JSON.stringify({
                userId:
                  currentUser.id,

                eventId:
                  Number(eventId),

                seatIds:
                  normalizedSeats
                    .map(
                      (seat) =>
                        seat.id
                    )
                    .filter(
                      (id) =>
                        id !== null &&
                        id !== undefined
                    ),
              }),
            }
          );

        const text =
          await response.text();

        console.log(
          "Booking API status:",
          response.status
        );

        console.log(
          "Booking API response:",
          text
        );

        if (response.ok) {
          try {
            backendBooking =
              text
                ? JSON.parse(text)
                : null;
          } catch {
            backendBooking = null;
          }
        } else {
          console.warn(
            "Backend booking request failed:",
            text
          );
        }
      } catch (error) {
        console.error(
          "Backend booking request error:",
          error
        );
      }
    }

    /*
     * =================================================
     * LOCAL BOOKING
     * =================================================
     *
     * Keep localStorage as a frontend backup so
     * My Bookings continues to work.
     */

    const booking = {
      id:
        backendBooking?.id ??
        Date.now(),

      backendBookingId:
        backendBooking?.id ??
        null,

      event: event,

      eventId:
        Number(eventId),

      seats:
        normalizedSeats,

      seatNumbers:
        normalizedSeats.map(
          (seat) =>
            seat.seatNumber
        ),

      status:
        backendBooking?.status ??
        "Confirmed",

      bookedAt:
        backendBooking?.bookingDate ??
        new Date().toISOString(),

      userEmail:
        currentUser?.email ??
        "guest",

      userId:
        currentUser?.id ??
        null,

      totalAmount:
        normalizedSeats.length *
        Number(
          event.ticketPrice ??
            event.price ??
            150
        ),
    };

    /*
     * Add newest booking first.
     */
    const updatedBookings = [
      booking,
      ...bookings,
    ];

    setBookings(
      updatedBookings
    );

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(
        updatedBookings
      )
    );

    /*
     * Keep selected information.
     */
    setSelectedEvent(event);

    setSelectedSeats(
      normalizedSeats
    );

    /*
     * Go to confirmation.
     */
    navigate(
      "confirmation",
      {
        event: event,
        seats: normalizedSeats,
      }
    );
  };

  /* =====================================================
     CANCEL BOOKING
  ===================================================== */

  const cancelBooking = async (
    bookingId
  ) => {
    /*
     * Remove from local bookings.
     */
    const updatedBookings =
      bookings.filter(
        (booking) =>
          booking.id !==
          bookingId
      );

    setBookings(
      updatedBookings
    );

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(
        updatedBookings
      )
    );

    /*
     * If this booking came from backend,
     * try to cancel it there too.
     *
     * This does not block the frontend
     * cancellation if the backend endpoint
     * is not available yet.
     */
    try {
      await fetch(
        `${API_BASE_URL}/api/bookings/${bookingId}`,
        {
          method: "DELETE",
        }
      );
    } catch (error) {
      console.warn(
        "Backend cancellation request failed:",
        error
      );
    }
  };

  /* =====================================================
     COMMON PROPS
  ===================================================== */

  const commonProps = {
    onNavigate: navigate,

    currentUser,

    selectedEvent,

    selectedSeats,

    bookings,

    onLogout:
      handleLogout,
  };

  /* =====================================================
     ROUTES
  ===================================================== */

  switch (route) {

    case "home":
      return (
        <Home
          {...commonProps}
        />
      );

    case "events":
      return (
        <Events
          {...commonProps}
        />
      );

    case "details":
      return (
        <EventDetails
          {...commonProps}
        />
      );

    case "seats":
      return (
        <SeatSelection
          {...commonProps}
          confirmBooking={
            confirmBooking
          }
        />
      );

    case "confirmation":
      return (
        <BookingConfirmation
          {...commonProps}
        />
      );

    case "bookings":
      return (
        <MyBookings
          {...commonProps}
          onCancelBooking={
            cancelBooking
          }
        />
      );

    case "login":
      return (
        <Login
          onLogin={
            handleLogin
          }
          onRegister={() =>
            navigate(
              "register"
            )
          }
          onBack={() =>
            navigate("home")
          }
        />
      );

    case "register":
      return (
        <Register
          onRegister={
            handleRegister
          }
          onLogin={() =>
            navigate("login")
          }
          onBack={() =>
            navigate("home")
          }
        />
      );

    default:
      return (
        <Home
          {...commonProps}
        />
      );
  }
}

export default App;
