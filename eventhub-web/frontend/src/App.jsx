
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

function App() {
  /* =====================================================
     ROUTING
  ===================================================== */

  const [route, setRoute] = useState(
    window.location.hash.replace("#/", "") || "home"
  );

  /* =====================================================
     SELECTED EVENT / SEATS
  ===================================================== */

  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);

  /* =====================================================
     CURRENT USER
  ===================================================== */

  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const saved = localStorage.getItem("eventhubUser");
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      console.error("Failed to load user:", error);
      return null;
    }
  });

  /* =====================================================
     BOOKINGS
  ===================================================== */

  const [bookings, setBookings] = useState(() => {
    try {
      const saved = localStorage.getItem("eventhubBookings");

      if (!saved) {
        return [];
      }

      const parsed = JSON.parse(saved);

      /*
       * Normalize old booking data also.
       * This prevents React error #31 if an old booking
       * contains seat objects instead of seat strings.
       */
      return parsed.map((booking) => ({
        ...booking,

        seats: Array.isArray(booking.seats)
          ? booking.seats
              .map((seat) => {
                if (typeof seat === "string") {
                  return seat;
                }

                if (seat && typeof seat === "object") {
                  return (
                    seat.seatNumber ||
                    seat.seatId ||
                    seat.id ||
                    ""
                  );
                }

                return "";
              })
              .filter(Boolean)
          : [],
      }));
    } catch (error) {
      console.error(
        "Failed to load bookings:",
        error
      );

      return [];
    }
  });

  /* =====================================================
     ROUTE LISTENER
  ===================================================== */

  useEffect(() => {
    const changeRoute = () => {
      const newRoute =
        window.location.hash.replace("#/", "") ||
        "home";

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

  /* =====================================================
     NAVIGATION
  ===================================================== */

  const navigate = (page, data = {}) => {
    if (data.event) {
      setSelectedEvent(data.event);
    }

    if (data.seats) {
      /*
       * IMPORTANT:
       * SeatSelection may send:
       *
       * ["A5", "A6"]
       *
       * OR
       *
       * [
       *   { id: 35, seatNumber: "A5", ... }
       * ]
       *
       * Convert everything into seat numbers.
       */

      const normalizedSeats = Array.isArray(data.seats)
        ? data.seats
            .map((seat) => {
              if (typeof seat === "string") {
                return seat;
              }

              if (seat && typeof seat === "object") {
                return (
                  seat.seatNumber ||
                  seat.seatId ||
                  seat.id ||
                  ""
                );
              }

              return "";
            })
            .filter(Boolean)
        : [];

      setSelectedSeats(normalizedSeats);
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
    localStorage.removeItem("eventhubUser");

    setCurrentUser(null);

    navigate("home");
  };

  /* =====================================================
     BOOKING
  ===================================================== */

  const confirmBooking = (event, seats) => {
    if (!event) {
      alert("Please select an event.");
      return;
    }

    if (
      !Array.isArray(seats) ||
      seats.length === 0
    ) {
      alert("Please select at least one seat.");
      return;
    }

    /*
     * VERY IMPORTANT
     *
     * Convert backend seat objects into simple
     * seat-number strings before saving.
     */

    const normalizedSeats = seats
      .map((seat) => {
        if (typeof seat === "string") {
          return seat;
        }

        if (seat && typeof seat === "object") {
          return (
            seat.seatNumber ||
            seat.seatId ||
            seat.id ||
            ""
          );
        }

        return "";
      })
      .filter(Boolean);

    if (normalizedSeats.length === 0) {
      alert(
        "Unable to identify the selected seats."
      );
      return;
    }

    console.log(
      "Booking event:",
      event
    );

    console.log(
      "Original seats:",
      seats
    );

    console.log(
      "Normalized seats:",
      normalizedSeats
    );

    /* =================================================
       CREATE BOOKING
    ================================================= */

    const booking = {
      id: Date.now(),

      event: event,

      /*
       * Store ONLY:
       * ["A5", "A6"]
       */
      seats: normalizedSeats,

      status: "Confirmed",

      bookedAt:
        new Date().toISOString(),

      userEmail:
        currentUser?.email || "guest",
    };

    /* =================================================
       UPDATE BOOKINGS
    ================================================= */

    const updatedBookings = [
      booking,
      ...bookings,
    ];

    setBookings(updatedBookings);

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(
        updatedBookings
      )
    );

    /* =================================================
       UPDATE CURRENT SELECTION
    ================================================= */

    setSelectedEvent(event);

    setSelectedSeats(
      normalizedSeats
    );

    /* =================================================
       GO TO CONFIRMATION
    ================================================= */

    navigate("confirmation");
  };

  /* =====================================================
     CANCEL BOOKING
  ===================================================== */

  const cancelBooking = (bookingId) => {
    const updatedBookings =
      bookings.filter(
        (booking) =>
          booking.id !== bookingId
      );

    setBookings(updatedBookings);

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(
        updatedBookings
      )
    );
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

    onLogout: handleLogout,
  };

  /* =====================================================
     ROUTES
  ===================================================== */

  switch (route) {
    /* ================= HOME ================= */

    case "home":
      return (
        <Home
          {...commonProps}
        />
      );

    /* ================= EVENTS ================= */

    case "events":
      return (
        <Events
          {...commonProps}
        />
      );

    /* ================= EVENT DETAILS ================= */

    case "details":
      return (
        <EventDetails
          {...commonProps}
        />
      );

    /* ================= SEAT SELECTION ================= */

    case "seats":
      return (
        <SeatSelection
          {...commonProps}
          confirmBooking={
            confirmBooking
          }
        />
      );

    /* ================= CONFIRMATION ================= */

    case "confirmation":
      return (
        <BookingConfirmation
          {...commonProps}
        />
      );

    /* ================= MY BOOKINGS ================= */

    case "bookings":
      return (
        <MyBookings
          {...commonProps}
          onCancelBooking={
            cancelBooking
          }
        />
      );

    /* ================= LOGIN ================= */

    case "login":
      return (
        <Login
          onLogin={handleLogin}
          onRegister={() =>
            navigate("register")
          }
          onBack={() =>
            navigate("home")
          }
        />
      );

    /* ================= REGISTER ================= */

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

    /* ================= DEFAULT ================= */

    default:
      return (
        <Home
          {...commonProps}
        />
      );
  }
}

export default App;