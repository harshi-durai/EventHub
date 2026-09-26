
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
  const [route, setRoute] = useState(
    window.location.hash.replace("#/", "") || "home"
  );

  const [selectedEvent, setSelectedEvent] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);

  const [currentUser, setCurrentUser] = useState(() => {
    const saved = localStorage.getItem("eventhubUser");
    return saved ? JSON.parse(saved) : null;
  });

  const [bookings, setBookings] = useState(() => {
    const saved = localStorage.getItem("eventhubBookings");
    return saved ? JSON.parse(saved) : [];
  });

  /* ================= ROUTING ================= */

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

    window.addEventListener("hashchange", changeRoute);

    return () => {
      window.removeEventListener("hashchange", changeRoute);
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

  /* ================= LOGIN ================= */

  const handleLogin = (user) => {
    setCurrentUser(user);

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(user)
    );

    navigate("home");
  };

  /* ================= REGISTER ================= */

  const handleRegister = (user) => {
    setCurrentUser(user);

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(user)
    );

    navigate("home");
  };

  /* ================= LOGOUT ================= */

  const handleLogout = () => {
    localStorage.removeItem("eventhubUser");

    setCurrentUser(null);

    navigate("home");
  };

  /* ================= BOOKING ================= */

  const confirmBooking = (event, seats) => {
    if (!event || !seats || seats.length === 0) {
      return;
    }

    const booking = {
      id: Date.now(),
      event: event,
      seats: seats,
      status: "Confirmed",
      bookedAt: new Date().toISOString(),
      userEmail: currentUser?.email || "guest",
    };

    const updatedBookings = [
      booking,
      ...bookings,
    ];

    setBookings(updatedBookings);

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(updatedBookings)
    );

    setSelectedEvent(event);
    setSelectedSeats(seats);

    navigate("confirmation");
  };

  /* ================= CANCEL BOOKING ================= */

  const cancelBooking = (bookingId) => {
    const updatedBookings = bookings.filter(
      (booking) => booking.id !== bookingId
    );

    setBookings(updatedBookings);

    localStorage.setItem(
      "eventhubBookings",
      JSON.stringify(updatedBookings)
    );
  };

  /* ================= COMMON PROPS ================= */

  const commonProps = {
    onNavigate: navigate,
    currentUser,
    selectedEvent,
    selectedSeats,
    bookings,
    onLogout: handleLogout,
  };

  /* ================= ROUTES ================= */

  switch (route) {
    case "home":
      return <Home {...commonProps} />;

    case "events":
      return <Events {...commonProps} />;

    case "details":
      return <EventDetails {...commonProps} />;

    case "seats":
      return (
        <SeatSelection
          {...commonProps}
          confirmBooking={confirmBooking}
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
          onCancelBooking={cancelBooking}
        />
      );

    case "login":
      return (
        <Login
          onLogin={handleLogin}
          onRegister={() => navigate("register")}
          onBack={() => navigate("home")}
        />
      );

    case "register":
      return (
        <Register
          onRegister={handleRegister}
          onLogin={() => navigate("login")}
          onBack={() => navigate("home")}
        />
      );

    default:
      return <Home {...commonProps} />;
  }
}

export default App;
