
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
    try {
      const saved = localStorage.getItem("eventhubUser");
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      console.error("Unable to load saved user:", error);
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
    setSelectedEvent(null);
    setSelectedSeats([]);

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
         BASIC VALIDATION
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
      console.log("Raw selected seats:", seats);
      console.log("=================================");

      /* -------------------------------------------------
         STEP 1
         LOAD SEATS FROM JAVA BACKEND
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
         STEP 2
         NORMALIZE BACKEND SEAT DATA
      ------------------------------------------------- */

      const backendSeats =
        backendSeatsRaw
          .map((seat) => ({
            id:
              seat?.id ??
              seat?.seatId ??
              seat?.seat_id ??
              null,

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
          }))
          .filter(
            (seat) =>
              seat.id !== null &&
              seat.seatNumber
          );

      console.log(
        "Normalized backend seats:",
        backendSeats
      );

      /* -------------------------------------------------
         STEP 3
         NORMALIZE SELECTED SEATS

         Supports BOTH:

         "C5"

         AND

         {
           id: 30,
           seatId: 30,
           seatNumber: "C5",
           status: "AVAILABLE"
         }
      ------------------------------------------------- */

      const normalizedSelectedSeats =
        seats
          .map((seat) => {
            if (typeof seat === "string") {
              return {
                seatNumber: seat
                  .trim()
                  .toUpperCase(),
              };
            }

            if (
              seat &&
              typeof seat === "object"
            ) {
              return {
                id:
                  seat.id ??
                  seat.seatId ??
                  seat.seat_id ??
                  null,

                seatNumber:
                  seat.seatNumber ??
                  seat.seat_number ??
                  "",

                eventId:
                  seat.eventId ??
                  seat.event_id ??
                  eventId,

                status:
                  seat.status ??
                  "AVAILABLE",
              };
            }

            return {
              seatNumber: "",
            };
          })
          .filter(
            (seat) =>
              seat.seatNumber
          );

      console.log(
        "Normalized selected seats:",
        normalizedSelectedSeats
      );

      if (
        normalizedSelectedSeats.length !==
        seats.length
      ) {
        alert(
          "Unable to identify one or more selected seats."
        );

        console.error(
          "Original seats:",
          seats
        );

        console.error(
          "Normalized seats:",
          normalizedSelectedSeats
        );

        return;
      }

      /* -------------------------------------------------
         STEP 4
         FIND EXACT DATABASE SEAT

         IMPORTANT:
         Always match by seatNumber + eventId.

         Example:

         C5 + Event 2
         ->
         ID 30

         NOT B4 / ID 24.
      ------------------------------------------------- */

      const selectedSeatObjects =
        normalizedSelectedSeats.map(
          (selectedSeat) => {

            const selectedNumber =
              String(
                selectedSeat.seatNumber
              )
                .trim()
                .toUpperCase();

            const foundSeat =
              backendSeats.find(
                (backendSeat) => {

                  const backendNumber =
                    String(
                      backendSeat.seatNumber
                    )
                      .trim()
                      .toUpperCase();

                  const sameEvent =
                    Number(
                      backendSeat.eventId
                    ) === Number(eventId);

                  return (
                    sameEvent &&
                    backendNumber ===
                      selectedNumber
                  );
                }
              );

            console.log(
              "SEAT MAPPING:",
              selectedNumber,
              "=>",
              foundSeat
            );

            return foundSeat;
          }
        );

      /* -------------------------------------------------
         STEP 5
         CHECK MISSING SEATS
      ------------------------------------------------- */

      const missingSeats =
        normalizedSelectedSeats.filter(
          (selectedSeat, index) =>
            !selectedSeatObjects[index]
        );

      if (missingSeats.length > 0) {

        const missingNames =
          missingSeats.map(
            (seat) =>
              seat.seatNumber ||
              "Unknown"
          );

        console.error(
          "Missing seats:",
          missingSeats
        );

        console.error(
          "Backend seats:",
          backendSeats
        );

        alert(
          `Unable to identify the selected seats: ${missingNames.join(
            ", "
          )}`
        );

        return;
      }

      /* -------------------------------------------------
         STEP 6
         VERIFY EVENT ID
      ------------------------------------------------- */

      const wrongEventSeats =
        selectedSeatObjects.filter(
          (seat) =>
            Number(seat.eventId) !==
            Number(eventId)
        );

      if (wrongEventSeats.length > 0) {

        console.error(
          "Wrong event seats:",
          wrongEventSeats
        );

        alert(
          "One or more selected seats belong to another event."
        );

        return;
      }

      /* -------------------------------------------------
         STEP 7
         VERIFY AVAILABILITY
      ------------------------------------------------- */

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
          `These seats are no longer available: ${names.join(
            ", "
          )}`
        );

        return;
      }

      /* -------------------------------------------------
         STEP 8
         GET ACTUAL DATABASE SEAT IDS
      ------------------------------------------------- */

      const seatIds =
        selectedSeatObjects.map(
          (seat) =>
            Number(
              seat.id ??
              seat.seatId ??
              0
            )
        );

      /* -------------------------------------------------
         FINAL VALIDATION
      ------------------------------------------------- */

      if (
        seatIds.length !==
        normalizedSelectedSeats.length
      ) {
        alert(
          "Unable to identify the selected seat IDs."
        );

        console.error(
          "Seat objects:",
          selectedSeatObjects
        );

        console.error(
          "Seat IDs:",
          seatIds
        );

        return;
      }

      if (
        seatIds.some(
          (id) =>
            !Number.isInteger(id) ||
            id <= 0
        )
      ) {
        alert(
          "Invalid seat information received from backend."
        );

        console.error(
          "Invalid seat IDs:",
          seatIds
        );

        return;
      }

      /* -------------------------------------------------
         IMPORTANT DEBUG

         Example:

         C5 -> 30
         B4 -> 24
      ------------------------------------------------- */

      console.log("=================================");
      console.log("FINAL SEAT MAPPING");

      selectedSeatObjects.forEach(
        (seat) => {
          console.log(
            `${seat.seatNumber} -> DB ID ${seat.id} -> ${seat.status}`
          );
        }
      );

      console.log(
        "Seat IDs being sent:",
        seatIds
      );

      console.log("=================================");

      /* -------------------------------------------------
         STEP 9
         CALCULATE PRICE
      ------------------------------------------------- */

      const price = Number(
        event?.ticketPrice ??
        event?.price ??
        150
      );

      const totalAmount =
        normalizedSelectedSeats.length *
        price;

      /* -------------------------------------------------
         STEP 10
         BOOKING PAYLOAD
      ------------------------------------------------- */

      const bookingPayload = {
        userId: userId,
        eventId: eventId,
        totalAmount: totalAmount,
        seatIds: seatIds,
      };

      console.log(
        "BOOKING PAYLOAD:",
        bookingPayload
      );

      /* -------------------------------------------------
         STEP 11
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
        "BOOKING RESPONSE:",
        bookingData
      );

      if (!bookingResponse.ok) {

        throw new Error(
          bookingData?.message ||
          `Booking failed (${bookingResponse.status})`
        );
      }

      if (
        bookingData?.status &&
        bookingData.status !==
          "success"
      ) {
        throw new Error(
          bookingData?.message ||
          "Booking was not confirmed."
        );
      }

      /* -------------------------------------------------
         STEP 12
         SUCCESS
      ------------------------------------------------- */

      const confirmedSeatNumbers =
        selectedSeatObjects.map(
          (seat) =>
            seat.seatNumber
        );

      setSelectedEvent(event);

      setSelectedSeats(
        confirmedSeatNumbers
      );

      alert(
        `Booking confirmed successfully!\n\nSeats: ${confirmedSeatNumbers.join(
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

      if (!bookingId) {
        alert("Invalid booking ID.");
        return false;
      }

      console.log(
        "================================="
      );

      console.log(
        "CANCELLING BOOKING"
      );

      console.log(
        "Booking ID:",
        bookingId
      );

      console.log(
        "User ID:",
        userId
      );

      console.log(
        "================================="
      );

      const response =
        await fetch(
          `${API_BASE_URL}/api/bookings/${bookingId}?userId=${userId}`,
          {
            method: "DELETE",
            headers: {
              "Content-Type":
                "application/json",
            },
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

      if (
        data?.status &&
        data.status !== "success"
      ) {
        throw new Error(
          data?.message ||
          "Booking cancellation failed."
        );
      }

      /* -------------------------------------------------
         REMOVE CANCELLED BOOKING FROM LOCAL STATE
      ------------------------------------------------- */

      setBookings((previousBookings) =>
        previousBookings.filter(
          (booking) =>
            Number(
              booking?.id ??
              booking?.bookingId
            ) !== Number(bookingId)
        )
      );

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

