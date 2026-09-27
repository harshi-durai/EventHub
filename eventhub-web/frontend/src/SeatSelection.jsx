import React, { useEffect, useMemo, useState } from "react";
import "./SeatSelection.css";

const API_BASE_URL = "https://eventhub-xhdu.onrender.com";

const ROWS = ["A", "B", "C", "D", "E", "F", "G", "H"];
const SEATS_PER_ROW = 10;

function SeatSelection({
  selectedEvent,
  selectedSeats = [],
  confirmBooking,
  onNavigate,
}) {
  /*
   * UI selected seats are ALWAYS stored as seat numbers:
   *
   * ["A1", "A2", "A5"]
   *
   * Backend seats are objects:
   *
   * {
   *   id: 31,
   *   eventId: 3,
   *   seatNumber: "A5",
   *   status: "AVAILABLE"
   * }
   */

  const normalizeSelectedSeats = (seats) => {
    if (!Array.isArray(seats)) {
      return [];
    }

    return seats
      .map((seat) => {
        // Already a seat number
        if (typeof seat === "string") {
          return seat;
        }

        // Backend seat object
        if (seat && typeof seat === "object") {
          return (
            seat.seatNumber ??
            seat.seat_number ??
            null
          );
        }

        return null;
      })
      .filter(Boolean);
  };

  const [selected, setSelected] = useState(
    normalizeSelectedSeats(selectedSeats)
  );

  const [backendSeats, setBackendSeats] = useState([]);
  const [loadingSeats, setLoadingSeats] = useState(true);
  const [seatError, setSeatError] = useState("");

  /* =====================================================
     PRICE
  ===================================================== */

  const ticketPrice = Number(
    selectedEvent?.ticketPrice ??
      selectedEvent?.price ??
      150
  );

  /* =====================================================
     EVENT ID
  ===================================================== */

  const eventId =
    selectedEvent?.id ??
    selectedEvent?.eventId ??
    null;

  /* =====================================================
     LOAD SEATS
  ===================================================== */

  useEffect(() => {
    if (!eventId) {
      setBackendSeats([]);
      setLoadingSeats(false);
      return;
    }

    const loadSeats = async () => {
      try {
        setLoadingSeats(true);
        setSeatError("");

        console.log(
          `Loading seats for event ${eventId}...`
        );

        const response = await fetch(
          `${API_BASE_URL}/api/events/${eventId}/seats`
        );

        if (!response.ok) {
          throw new Error(
            `Backend returned ${response.status}`
          );
        }

        const data = await response.json();

        console.log("Raw backend seats:", data);

        /*
         * Backend normally returns an array.
         * Also support:
         *
         * { seats: [...] }
         */

        const rawSeats = Array.isArray(data)
          ? data
          : Array.isArray(data?.seats)
          ? data.seats
          : [];

        const normalizedSeats = rawSeats
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
              null,

            status:
              seat?.status ??
              "AVAILABLE",
          }))
          .filter(
            (seat) =>
              seat.id !== null &&
              seat.seatNumber !== null
          );

        console.log(
          "Normalized backend seats:",
          normalizedSeats
        );

        setBackendSeats(normalizedSeats);

      } catch (error) {
        console.error(
          "Failed to load seats:",
          error
        );

        setSeatError(
          "Unable to load seats from EventHub backend."
        );

        setBackendSeats([]);

      } finally {
        setLoadingSeats(false);
      }
    };

    loadSeats();
  }, [eventId]);

  /* =====================================================
     RESET / NORMALIZE SELECTED SEATS
  ===================================================== */

  useEffect(() => {
    setSelected(
      normalizeSelectedSeats(selectedSeats)
    );
  }, [selectedSeats]);

  /* =====================================================
     BOOKED SEATS
  ===================================================== */

  const bookedSeats = useMemo(() => {
    return backendSeats
      .filter(
        (seat) =>
          String(seat.status).toUpperCase() ===
          "BOOKED"
      )
      .map((seat) =>
        String(seat.seatNumber).toUpperCase()
      );
  }, [backendSeats]);

  /* =====================================================
     AVAILABLE SEATS
  ===================================================== */

  const availableSeats = useMemo(() => {
    return backendSeats
      .filter(
        (seat) =>
          String(seat.status).toUpperCase() ===
          "AVAILABLE"
      )
      .map((seat) =>
        String(seat.seatNumber).toUpperCase()
      );
  }, [backendSeats]);

  /* =====================================================
     FIND BACKEND SEAT
  ===================================================== */

  const findBackendSeat = (seatNumber) => {
    if (!seatNumber) {
      return null;
    }

    const normalizedSeatNumber =
      String(seatNumber).toUpperCase();

    return backendSeats.find(
      (seat) =>
        String(
          seat.seatNumber
        ).toUpperCase() ===
        normalizedSeatNumber
    );
  };

  /* =====================================================
     SEAT TOGGLE
  ===================================================== */

  const toggleSeat = (seatNumber) => {
    const normalizedSeatNumber =
      String(seatNumber).toUpperCase();

    const backendSeat =
      findBackendSeat(normalizedSeatNumber);

    if (!backendSeat) {
      alert(
        `Seat ${normalizedSeatNumber} is not configured in the backend.`
      );
      return;
    }

    const status =
      String(
        backendSeat.status
      ).toUpperCase();

    if (status === "BOOKED") {
      return;
    }

    if (status !== "AVAILABLE") {
      return;
    }

    /* REMOVE */

    if (
      selected.includes(
        normalizedSeatNumber
      )
    ) {
      setSelected(
        selected.filter(
          (seat) =>
            seat !== normalizedSeatNumber
        )
      );

      return;
    }

    /* MAXIMUM 8 */

    if (selected.length >= 8) {
      alert(
        "You can select a maximum of 8 seats."
      );
      return;
    }

    /* ADD */

    setSelected([
      ...selected,
      normalizedSeatNumber,
    ]);
  };

  /* =====================================================
     CONFIRM BOOKING
  ===================================================== */

  const handleBookNow = () => {
    if (!selectedEvent) {
      alert("Please select an event.");
      return;
    }

    if (selected.length === 0) {
      alert(
        "Please select at least one seat."
      );
      return;
    }

    console.log(
      "Selected seat numbers:",
      selected
    );

    /*
     * Convert:
     *
     * ["A3", "A4"]
     *
     * INTO:
     *
     * [
     *   {
     *     id: 23,
     *     eventId: 2,
     *     seatNumber: "A3",
     *     status: "AVAILABLE"
     *   }
     * ]
     */

    const selectedSeatObjects =
      selected
        .map((seatNumber) =>
          findBackendSeat(seatNumber)
        )
        .filter(Boolean);

    console.log(
      "Selected backend seat objects:",
      selectedSeatObjects
    );

    /* CHECK MISSING */

    if (
      selectedSeatObjects.length !==
      selected.length
    ) {
      const missingSeats =
        selected.filter(
          (seatNumber) =>
            !findBackendSeat(seatNumber)
        );

      alert(
        `Unable to identify the selected seats: ${missingSeats.join(
          ", "
        )}`
      );

      console.error(
        "Missing backend seats:",
        missingSeats
      );

      return;
    }

    /* CHECK AVAILABILITY */

    const unavailableSelected =
      selectedSeatObjects.filter(
        (seat) =>
          String(
            seat.status
          ).toUpperCase() !==
          "AVAILABLE"
      );

    if (
      unavailableSelected.length > 0
    ) {
      alert(
        `These seats are no longer available: ${unavailableSelected
          .map(
            (seat) =>
              seat.seatNumber
          )
          .join(", ")}`
      );

      return;
    }

    /*
     * Send COMPLETE backend seat objects.
     *
     * App.jsx can now extract:
     *
     * seat.id
     * seat.seatId
     * seat.seatNumber
     * seat.eventId
     */

    const seatsForBooking =
      selectedSeatObjects.map(
        (seat) => ({
          id: seat.id,

          seatId: seat.id,

          eventId:
            seat.eventId ??
            eventId,

          seatNumber:
            seat.seatNumber,

          status:
            seat.status,
        })
      );

    console.log(
      "Sending seats for booking:",
      seatsForBooking
    );

    if (confirmBooking) {
      confirmBooking(
        selectedEvent,
        seatsForBooking
      );
    }
  };

  /* =====================================================
     TOTAL
  ===================================================== */

  const totalAmount =
    selected.length *
    ticketPrice;

  /* =====================================================
     EVENT DISPLAY
  ===================================================== */

  const eventTitle =
    selectedEvent?.title ||
    selectedEvent?.name ||
    "Select Your Seats";

  const eventDate =
    selectedEvent?.date ||
    selectedEvent?.eventDate ||
    "Event Date";

  const eventLocation =
    selectedEvent?.location ||
    selectedEvent?.venue ||
    "Event Location";

  /* =====================================================
     RENDER
  ===================================================== */

  return (
    <div className="seat-page">

      {/* HEADER */}

      <header className="seat-header">

        <button
          className="seat-brand"
          onClick={() =>
            onNavigate("home")
          }
          type="button"
        >
          EVENT<span>HUB</span>
        </button>

        <div className="seat-header-title">
          SELECT YOUR SEATS
        </div>

        <button
          className="seat-back"
          onClick={() =>
            onNavigate("details", {
              event: selectedEvent,
            })
          }
          type="button"
        >
          ← BACK
        </button>

      </header>

      {/* MAIN */}

      <main className="seat-main">

        {/* EVENT INFORMATION */}

        <section className="seat-event-info">

          <div>

            <span>
              EVENTHUB / BOOKING
            </span>

            <h1>
              {eventTitle}
            </h1>

            <p>
              {eventDate}
              {"  •  "}
              {eventLocation}
            </p>

          </div>

          <div className="seat-price">

            <small>
              PRICE / SEAT
            </small>

            <strong>
              ₹{ticketPrice}
            </strong>

          </div>

        </section>

        {/* ERROR */}

        {seatError && (
          <div className="seat-error">

            {seatError}

            <br />

            <small>
              Please check the EventHub backend connection.
            </small>

          </div>
        )}

        {/* LOADING */}

        {loadingSeats ? (

          <section className="seat-loading">

            <div className="loading-spinner"></div>

            <h3>
              LOADING SEATS
            </h3>

            <p>
              Connecting to EventHub backend...
            </p>

          </section>

        ) : (

          <section className="seat-layout">

            {/* SCREEN */}

            <div className="screen-area">

              <div className="screen">
                SCREEN
              </div>

              <p>
                All eyes this way
              </p>

            </div>

            {/* SEAT MAP */}

            <div className="seat-map">

              {ROWS.map((row) => (

                <div
                  className="seat-row"
                  key={row}
                >

                  <span className="row-label">
                    {row}
                  </span>

                  <div className="seat-row-inner">

                    {Array.from(
                      {
                        length:
                          SEATS_PER_ROW,
                      },
                      (_, index) => {

                        const seatNumber =
                          index + 1;

                        const seatId =
                          `${row}${seatNumber}`;

                        const backendSeat =
                          findBackendSeat(
                            seatId
                          );

                        const isSelected =
                          selected.includes(
                            seatId
                          );

                        const isBooked =
                          backendSeat &&
                          String(
                            backendSeat.status
                          ).toUpperCase() ===
                            "BOOKED";

                        const isAvailable =
                          backendSeat &&
                          String(
                            backendSeat.status
                          ).toUpperCase() ===
                            "AVAILABLE";

                        const isMissing =
                          !backendSeat;

                        return (
                          <React.Fragment
                            key={seatId}
                          >

                            {seatNumber === 6 && (
                              <div className="seat-gap" />
                            )}

                            <button
                              type="button"

                              className={[
                                "seat",

                                isSelected
                                  ? "selected"
                                  : "",

                                isBooked
                                  ? "booked"
                                  : "",

                                isMissing
                                  ? "unavailable"
                                  : "",

                                backendSeat &&
                                !isBooked &&
                                !isAvailable
                                  ? "unavailable"
                                  : "",
                              ]
                                .join(" ")
                                .trim()}

                              disabled={
                                loadingSeats ||
                                isBooked ||
                                isMissing ||
                                !isAvailable
                              }

                              onClick={() =>
                                toggleSeat(
                                  seatId
                                )
                              }

                              title={
                                isBooked
                                  ? `Seat ${seatId} is already booked`
                                  : isAvailable
                                  ? `Seat ${seatId} available`
                                  : isMissing
                                  ? `Seat ${seatId} is not configured`
                                  : `Seat ${seatId} unavailable`
                              }
                            >
                              {seatNumber}
                            </button>

                          </React.Fragment>
                        );
                      }
                    )}

                  </div>

                  <span className="row-label">
                    {row}
                  </span>

                </div>

              ))}

            </div>

            {/* LEGEND */}

            <div className="seat-legend">

              <div>
                <span className="legend-seat available"></span>
                AVAILABLE
              </div>

              <div>
                <span className="legend-seat selected"></span>
                SELECTED
              </div>

              <div>
                <span className="legend-seat booked"></span>
                BOOKED
              </div>

            </div>

          </section>

        )}

        {/* BOOKING SUMMARY */}

        <section className="booking-summary">

          <div className="summary-left">

            <span>
              SELECTED SEATS
            </span>

            <div className="selected-seat-list">

              {selected.length === 0 ? (

                <strong>
                  No seats selected
                </strong>

              ) : (

                [...selected]
                  .sort((a, b) =>
                    a.localeCompare(
                      b,
                      undefined,
                      {
                        numeric: true,
                      }
                    )
                  )
                  .map((seat) => (

                    <span
                      key={seat}
                      className="summary-seat"
                    >
                      {seat}
                    </span>

                  ))

              )}

            </div>

          </div>

          <div className="summary-price">

            <span>
              TOTAL
            </span>

            <strong>
              ₹{totalAmount}
            </strong>

          </div>

          <button
            className="book-now-button"
            onClick={handleBookNow}
            type="button"
            disabled={
              loadingSeats ||
              selected.length === 0
            }
          >
            BOOK NOW
            <span>→</span>
          </button>

        </section>

      </main>

    </div>
  );
}

export default SeatSelection;