
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
      /* -------------------------------------------------
         STEP 1: BASIC VALIDATION
      ------------------------------------------------- */

      if (!event) {
        alert("Please select an event.");
        return;
      }

      if (!Array.isArray(seats) || seats.length === 0) {
        alert("Please select at least one seat.");
        return;
      }

      const userId = getUserId();

      if (!userId) {
        alert("Please login before booking.");
        navigate("login");
        return;
      }

      /* -------------------------------------------------
         STEP 2: EVENT ID
      ------------------------------------------------- */

      const eventId = Number(
        event?.id ??
          event?.eventId ??
          event?.event_id ??
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
      console.log("Selected seats received:", seats);
      console.log("=================================");

      /* -------------------------------------------------
         STEP 3:
         LOAD ACTUAL SEATS FROM JAVA BACKEND
      ------------------------------------------------- */

      const seatResponse = await fetch(
        `${API_BASE_URL}/api/events/${eventId}/seats`
      );

      if (!seatResponse.ok) {
        throw new Error(
          `Unable to load seats (${seatResponse.status})`
        );
      }

      const backendSeatsRaw =
        await seatResponse.json();

      console.log(
        "Backend seats:",
        backendSeatsRaw
      );

      if (!Array.isArray(backendSeatsRaw)) {
        throw new Error(
          "Invalid seat data received from backend."
        );
      }

      /* -------------------------------------------------
         STEP 4:
         NORMALIZE BACKEND SEAT DATA
      ------------------------------------------------- */

      const backendSeats =
        backendSeatsRaw.map((seat) => ({
          id:
            seat?.id ??
            seat?.seatId ??
            seat?.seat_id,

          eventId:
            seat?.eventId ??
            seat?.event_id ??
            eventId,

          seatNumber:
            seat?.seatNumber ??
            seat?.seat_number ??
            "",

          status:
            seat?.status ??
            "AVAILABLE",
        }));

      console.log(
        "Normalized backend seats:",
        backendSeats
      );

      /* -------------------------------------------------
         STEP 5:
         NORMALIZE SELECTED SEATS
         
         SeatSelection may send:
         
         "A3"
         
         OR
         
         {
           id: 23,
           seatId: 23,
           seatNumber: "A3",
           status: "AVAILABLE"
         }
      ------------------------------------------------- */

      const selectedSeatNumbers = seats
        .map((seat) => {
          if (typeof seat === "string") {
            return seat.trim().toUpperCase();
          }

          if (
            typeof seat === "object" &&
            seat !== null
          ) {
            return String(
              seat?.seatNumber ??
                seat?.seat_number ??
                ""
            )
              .trim()
              .toUpperCase();
          }

          return "";
        })
        .filter(Boolean);

      console.log(
        "Normalized selected seat numbers:",
        selectedSeatNumbers
      );

      if (selectedSeatNumbers.length !== seats.length) {
        console.error(
          "Could not normalize selected seats:",
          seats
        );

        alert(
          "Unable to identify the selected seats."
        );

        return;
      }

      /* -------------------------------------------------
         STEP 6:
         FIND ACTUAL DATABASE SEAT OBJECTS
      ------------------------------------------------- */

      const selectedSeatObjects =
        selectedSeatNumbers.map(
          (seatNumber) => {
            return backendSeats.find(
              (backendSeat) =>
                String(
                  backendSeat.seatNumber
                )
                  .trim()
                  .toUpperCase() ===
                seatNumber
            );
          }
        );

      console.log(
        "Selected backend seat objects:",
        selectedSeatObjects
      );

      /* -------------------------------------------------
         STEP 7:
         CHECK MISSING SEATS
      ------------------------------------------------- */

      const missingSeatNumbers =
        selectedSeatNumbers.filter(
          (seatNumber) => {
            const exists =
              backendSeats.some(
                (backendSeat) =>
                  String(
                    backendSeat.seatNumber
                  )
                    .trim()
                    .toUpperCase() ===
                  seatNumber
              );

            return !exists;
          }
        );

      if (missingSeatNumbers.length > 0) {
        console.error(
          "Missing seats:",
          missingSeatNumbers
        );

        console.error(
          "Backend seats:",
          backendSeats
        );

        alert(
          `Unable to identify the selected seats: ${missingSeatNumbers.join(
            ", "
          )}`
        );

        return;
      }

      /* -------------------------------------------------
         STEP 8:
         CHECK SEAT IDs
      ------------------------------------------------- */

      const invalidSeatObjects =
        selectedSeatObjects.filter(
          (seat) =>
            !seat ||
            !Number(
              seat.id ??
                seat.seatId ??
                seat.seat_id
            )
        );

      if (invalidSeatObjects.length > 0) {
        console.error(
          "Invalid backend seat objects:",
          invalidSeatObjects
        );

        alert(
          "Unable to identify the database seat IDs."
        );

        return;
      }

      /* -------------------------------------------------
         STEP 9:
         CHECK SEAT AVAILABILITY
      ------------------------------------------------- */

      const unavailableSeats =
        selectedSeatObjects.filter(
          (seat) =>
            String(
              seat.status
            ).toUpperCase() !== "AVAILABLE"
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

      /* -------------------------------------------------
         STEP 10:
         CONVERT TO DATABASE SEAT IDs
      ------------------------------------------------- */

      const seatIds =
        selectedSeatObjects.map(
          (seat) =>
            Number(
              seat.id ??
                seat.seatId ??
                seat.seat_id
            )
        );

      console.log(
        "================================="
      );

      console.log(
        "FINAL DATABASE SEAT IDS:",
        seatIds
      );

      console.log(
        "FINAL SEAT NUMBERS:",
        selectedSeatNumbers
      );

      console.log(
        "================================="
      );

      /* -------------------------------------------------
         STEP 11:
         CALCULATE PRICE
      ------------------------------------------------- */

      const price = Number(
        event?.ticketPrice ??
          event?.price ??
          150
      );

      const totalAmount =
        selectedSeatNumbers.length * price;

      /* -------------------------------------------------
         STEP 12:
         CREATE BOOKING PAYLOAD
      ------------------------------------------------- */

      const bookingPayload = {
        userId: userId,
        eventId: eventId,
        totalAmount: totalAmount,
        seatIds: seatIds,
      };

      console.log(
        "Sending booking payload:",
        bookingPayload
      );

      /* -------------------------------------------------
         STEP 13:
         POST TO JAVA BACKEND
      ------------------------------------------------- */

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

      let bookingData = null;

      try {
        bookingData =
          await bookingResponse.json();
      } catch {
        bookingData = null;
      }

      console.log(
        "Booking response:",
        bookingData
      );

      /* -------------------------------------------------
         STEP 14:
         CHECK BACKEND RESPONSE
      ------------------------------------------------- */

      if (!bookingResponse.ok) {
        throw new Error(
          bookingData?.message ||
            `Booking failed (${bookingResponse.status})`
        );
      }

      if (
        bookingData?.status &&
        bookingData.status !== "success"
      ) {
        throw new Error(
          bookingData?.message ||
            "Booking failed."
        );
      }

      /* -------------------------------------------------
         STEP 15:
         SAVE BOOKING DATA
      ------------------------------------------------- */

      setSelectedEvent(event);

      /*
       * Store seat numbers for confirmation page.
       */
      setSelectedSeats(
        selectedSeatNumbers
      );

      /* -------------------------------------------------
         STEP 16:
         SUCCESS
      ------------------------------------------------- */

      console.log(
        "================================="
      );

      console.log(
        "BOOKING SUCCESSFUL"
      );

      console.log(
        "Booking ID:",
        bookingData?.bookingId
      );

      console.log(
        "Seat IDs:",
        seatIds
      );

      console.log(
        "Seats:",
        selectedSeatNumbers
      );

      console.log(
        "================================="
      );

      alert(
        `Booking confirmed!\nSeats: ${selectedSeatNumbers.join(
          ", "
        )}`
      );

      navigate("confirmation");

    } catch (error) {
      console.error(
        "================================="
      );

      console.error(
        "BOOKING ERROR:",
        error
      );

      console.error(
        "================================="
      );

      alert(
        error?.message ||
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
        alert("Please login again.");
        return false;
      }

      console.log(
        "Cancelling booking:",
        bookingId
      );

      console.log(
        "User ID:",
        userId
      );

      const response =
        await fetch(
          `${API_BASE_URL}/api/bookings/${bookingId}?userId=${userId}`,
          {
            method: "DELETE",
          }
        );

      let data = null;

      try {
        data = await response.json();
      } catch {
        data = null;
      }

      console.log(
        "Cancel response:",
        data
      );

      if (!response.ok) {
        throw new Error(
          data?.message ||
            `Unable to cancel booking (${response.status})`
        );
      }

      alert(
        "Booking cancelled successfully."
      );

      /*
       * Refresh bookings page.
       */
      window.dispatchEvent(
        new Event("bookingUpdated")
      );

      return true;

    } catch (error) {
      console.error(
        "CANCEL ERROR:",
        error
      );

      alert(
        error?.message ||
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
