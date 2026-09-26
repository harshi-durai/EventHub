
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
   * selected contains seat numbers:
   * ["A1", "A2", "A5"]
   *
   * backendSeats contains complete backend objects:
   * {
   *   id: 31,
   *   eventId: 3,
   *   seatNumber: "A1",
   *   status: "AVAILABLE"
   * }
   */

  const [selected, setSelected] = useState(
    Array.isArray(selectedSeats) ? selectedSeats : []
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

        console.log(
          "Backend seats:",
          data
        );

        /*
         * Normalize backend response.
         *
         * Supports:
         * eventId
         * event_id
         *
         * seatNumber
         * seat_number
         */

        const normalizedSeats = Array.isArray(data)
          ? data.map((seat) => ({
              id:
                seat.id ??
                seat.seatId ??
                seat.seat_id,

              eventId:
                seat.eventId ??
                seat.event_id ??
                eventId,

              seatNumber:
                seat.seatNumber ??
                seat.seat_number,

              status:
                seat.status ??
                "AVAILABLE",
            }))
          : [];

        console.log(
          "Normalized seats:",
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
     BOOKED SEATS
  ===================================================== */

  const bookedSeats = useMemo(() => {
    return backendSeats
      .filter(
        (seat) =>
          String(seat.status).toUpperCase() ===
          "BOOKED"
      )
      .map((seat) => seat.seatNumber);
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
      .map((seat) => seat.seatNumber);
  }, [backendSeats]);

  /* =====================================================
     FIND BACKEND SEAT
  ===================================================== */

  const findBackendSeat = (seatNumber) => {
    return backendSeats.find(
      (seat) =>
        String(seat.seatNumber).toUpperCase() ===
        String(seatNumber).toUpperCase()
    );
  };

  /* =====================================================
     SEAT TOGGLE
  ===================================================== */

  const toggleSeat = (seatNumber) => {
    const backendSeat =
      findBackendSeat(seatNumber);

    /*
     * Seat doesn't exist in backend.
     */
    if (!backendSeat) {
      alert(
        `Seat ${seatNumber} is not available in the backend.`
      );
      return;
    }

    /*
     * Already booked.
     */
    if (
      String(backendSeat.status).toUpperCase() ===
      "BOOKED"
    ) {
      return;
    }

    /*
     * Only AVAILABLE seats can be selected.
     */
    if (
      String(backendSeat.status).toUpperCase() !==
      "AVAILABLE"
    ) {
      return;
    }

    /*
     * Remove selected seat.
     */
    if (selected.includes(seatNumber)) {
      setSelected(
        selected.filter(
          (seat) => seat !== seatNumber
        )
      );

      return;
    }

    /*
     * Maximum 8 seats.
     */
    if (selected.length >= 8) {
      alert(
        "You can select a maximum of 8 seats."
      );
      return;
    }

    /*
     * Add seat number.
     */
    setSelected([
      ...selected,
      seatNumber,
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

    /*
     * Convert selected seat numbers into
     * complete backend seat objects.
     */
    const selectedSeatObjects =
      selected
        .map((seatNumber) =>
          findBackendSeat(seatNumber)
        )
        .filter(Boolean);

    console.log(
      "Selected seat numbers:",
      selected
    );

    console.log(
      "Selected backend seats:",
      selectedSeatObjects
    );

    /*
     * Make sure every selected seat exists.
     */
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

      return;
    }

    /*
     * Final BOOKED check.
     */
    const unavailableSelected =
      selectedSeatObjects.filter(
        (seat) =>
          String(seat.status).toUpperCase() !==
          "AVAILABLE"
      );

    if (
      unavailableSelected.length > 0
    ) {
      alert(
        `These seats are no longer available: ${unavailableSelected
          .map((seat) => seat.seatNumber)
          .join(", ")}`
      );

      return;
    }

    /*
     * IMPORTANT
     *
     * Pass seat numbers to the existing App.jsx
     * booking system, but attach backend seat
     * information as an additional property.
     *
     * This keeps your existing UI compatible.
     */
    const seatsForBooking =
      selectedSeatObjects.map(
        (seat) => ({
          id: seat.id,
          seatId: seat.id,
          eventId:
            seat.eventId ?? eventId,
          seatNumber:
            seat.seatNumber,
          status:
            seat.status,
        })
      );

    /*
     * Store a compatible representation.
     *
     * The first argument remains the event.
     * The second argument is now the complete
     * seat information.
     */
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
    selected.length * ticketPrice;

  /* =====================================================
     EVENT DISPLAY DATA
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

      {/* =================================================
          HEADER
      ================================================= */}

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

      {/* =================================================
          MAIN
      ================================================= */}

      <main className="seat-main">

        {/* =================================================
            EVENT INFORMATION
        ================================================= */}

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

        {/* =================================================
            ERROR
        ================================================= */}

        {seatError && (
          <div className="seat-error">

            {seatError}

            <br />

            <small>
              Please check the EventHub backend connection.
            </small>

          </div>
        )}

        {/* =================================================
            LOADING
        ================================================= */}

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

          /* =================================================
             SEAT AREA
          ================================================= */

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

            {/* =================================================
                SEAT MAP
            ================================================= */}

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

            {/* =================================================
                LEGEND
            ================================================= */}

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

        {/* =================================================
            BOOKING SUMMARY
        ================================================= */}

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
