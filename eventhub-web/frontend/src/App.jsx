
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
    return saved ? JSON.parse(saved) : null;
  });

  const [bookings, setBookings] = useState([]);

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

    window.addEventListener("hashchange", changeRoute);

    return () => {
      window.removeEventListener("hashchange", changeRoute);
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
    localStorage.removeItem("eventhubUser");

    setCurrentUser(null);
    setBookings([]);

    navigate("home");
  };

  /* =====================================================
     GET USER ID
  ===================================================== */

  const getUserId = () => {
    return Number(
      currentUser?.id ??
      currentUser?.userId ??
      currentUser?.user_id ??
      0
    );
  };

  /* =====================================================
     CREATE BOOKING
  ===================================================== */

  const confirmBooking = async (event, seats) => {
    try {
      if (!event) {
        alert("Please select an event.");
        return;
      }

      if (!seats || seats.length === 0) {
        alert("Please select at least one seat.");
        return;
      }

      const userId = getUserId();

      if (!userId) {
        alert("Please login before booking.");
        navigate("login");
        return;
      }

      const eventId = Number(
        event?.id ??
        event?.eventId ??
        0
      );

      if (!eventId) {
        alert("Unable to identify the selected event.");
        return;
      }

      console.log("=================================");
      console.log("BOOKING STARTED");
      console.log("User ID:", userId);
      console.log("Event ID:", eventId);
      console.log("Selected seats:", seats);
      console.log("=================================");

      /* -----------------------------------------------
         STEP 1:
         Get actual seat IDs from Java backend
      ------------------------------------------------ */

      const seatResponse = await fetch(
        `${API_BASE_URL}/api/events/${eventId}/seats`
      );

      if (!seatResponse.ok) {
        throw new Error(
          `Unable to load seats (${seatResponse.status})`
        );
      }

      const backendSeats = await seatResponse.json();

      console.log(
        "Backend seats:",
        backendSeats
      );

      /* -----------------------------------------------
         STEP 2:
         Convert A3, A5 etc. → database seat IDs
      ------------------------------------------------ */

      const selectedSeatObjects = seats.map(
        (seatNumber) => {

          const foundSeat =
            backendSeats.find(
              (seat) =>
                String(
                  seat.seatNumber
                ).toUpperCase() ===
                String(
                  seatNumber
                ).toUpperCase()
            );

          return foundSeat;
        }
      );

      const missingSeats =
        selectedSeatObjects.filter(
          (seat) => !seat
        );

      if (missingSeats.length > 0) {
        alert(
          "Unable to identify the selected seats."
        );

        console.error(
          "Selected seats:",
          seats
        );

        console.error(
          "Backend seats:",
          backendSeats
        );

        return;
      }

      const unavailableSeats =
        selectedSeatObjects.filter(
          (seat) =>
            String(
              seat.status
            ).toUpperCase() !==
            "AVAILABLE"
        );

      if (unavailableSeats.length > 0) {

        const names =
          unavailableSeats.map(
            (seat) =>
              seat.seatNumber
          );

        alert(
          `These seats are not available: ${names.join(
            ", "
          )}`
        );

        return;
      }

      const seatIds =
        selectedSeatObjects.map(
          (seat) =>
            Number(
              seat.id ??
              seat.seatId
            )
        );

      console.log(
        "Converted seat IDs:",
        seatIds
      );

      /* -----------------------------------------------
         STEP 3:
         Calculate price
      ------------------------------------------------ */

      const price = Number(
        event?.ticketPrice ??
        event?.price ??
        150
      );

      const totalAmount =
        seats.length * price;

      /* -----------------------------------------------
         STEP 4:
         POST booking to Java backend
      ------------------------------------------------ */

      const bookingPayload = {
        userId: userId,
        eventId: eventId,
        totalAmount: totalAmount,
        seatIds: seatIds,
      };

      console.log(
        "Sending booking:",
        bookingPayload
      );

      const bookingResponse =
        await fetch(
          `${API_BASE_URL}/api/bookings`,
          {
            method: "POST",

            headers: {
              "Content-Type":
                "application/json",
            },

            body: JSON.stringify(
              bookingPayload
            ),
          }
        );

      const bookingData =
        await bookingResponse.json();

      console.log(
        "Booking response:",
        bookingData
      );

      if (!bookingResponse.ok) {
        throw new Error(
          bookingData?.message ||
          "Booking failed"
        );
      }

      /* -----------------------------------------------
         STEP 5:
         Booking successful
      ------------------------------------------------ */

      setSelectedEvent(event);
      setSelectedSeats(seats);

      alert(
        `Booking confirmed!\nSeats: ${seats.join(
          ", "
        )}`
      );

      navigate("confirmation");

    } catch (error) {

      console.error(
        "BOOKING ERROR:",
        error
      );

      alert(
        error.message ||
        "Unable to complete booking."
      );
    }
  };

  /* =====================================================
     CANCEL BOOKING
  ===================================================== */

  const cancelBooking = async (bookingId) => {

    try {

      const userId = getUserId();

      if (!userId) {
        alert(
          "Please login again."
        );
        return false;
      }

      console.log(
        "Cancelling booking:",
        bookingId
      );

      const response =
        await fetch(
          `${API_BASE_URL}/api/bookings/${bookingId}?userId=${userId}`,
          {
            method: "DELETE",
          }
        );

      const data =
        await response.json();

      console.log(
        "Cancel response:",
        data
      );

      if (!response.ok) {
        throw new Error(
          data?.message ||
          "Unable to cancel booking."
        );
      }

      alert(
        "Booking cancelled successfully."
      );

      return true;

    } catch (error) {

      console.error(
        "CANCEL ERROR:",
        error
      );

      alert(
        error.message ||
        "Unable to cancel booking."
      );

      return false;
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
    onLogout: handleLogout,
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
          onLogin={handleLogin}
          onRegister={() =>
            navigate("register")
          }
          onBack={() =>
            navigate("home")
          }
        />
      );

    case "register":
      return (
        <Register
          onRegister={handleRegister}
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