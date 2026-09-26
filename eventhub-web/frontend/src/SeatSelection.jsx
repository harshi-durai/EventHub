
import React, { useMemo, useState } from "react";
import "./SeatSelection.css";

const ROWS = ["A", "B", "C", "D", "E", "F", "G", "H"];
const SEATS_PER_ROW = 10;

function SeatSelection({
  selectedEvent,
  selectedSeats = [],
  confirmBooking,
  onNavigate,
}) {
  const [selected, setSelected] = useState(
    selectedSeats || []
  );

  /* ================= PRICE ================= */

  const ticketPrice = Number(
    selectedEvent?.price || 150
  );

  /* ================= BOOKED SEATS ================= */

  const bookedSeats = useMemo(() => {
    const stored =
      JSON.parse(
        localStorage.getItem("eventhubBookings")
      ) || [];

    const booked = [];

    stored.forEach((booking) => {
      if (
        selectedEvent &&
        booking.event?.id === selectedEvent.id
      ) {
        booked.push(...(booking.seats || []));
      }
    });

    return booked;
  }, [selectedEvent]);

  /* ================= SEAT TOGGLE ================= */

  const toggleSeat = (seatId) => {
    if (bookedSeats.includes(seatId)) {
      return;
    }

    if (selected.includes(seatId)) {
      setSelected(
        selected.filter((seat) => seat !== seatId)
      );
    } else {
      if (selected.length >= 8) {
        return;
      }

      setSelected([
        ...selected,
        seatId,
      ]);
    }
  };

  /* ================= CONFIRM ================= */

  const handleBookNow = () => {
    if (!selectedEvent) {
      return;
    }

    if (selected.length === 0) {
      alert("Please select at least one seat.");
      return;
    }

    if (confirmBooking) {
      confirmBooking(
        selectedEvent,
        selected
      );
    }
  };

  /* ================= TOTAL ================= */

  const totalAmount =
    selected.length * ticketPrice;

  return (
    <div className="seat-page">

      {/* ================= HEADER ================= */}

      <header className="seat-header">

        <button
          className="seat-brand"
          onClick={() => onNavigate("home")}
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


      {/* ================= MAIN ================= */}

      <main className="seat-main">

        {/* EVENT INFO */}

        <section className="seat-event-info">

          <div>

            <span>
              EVENTHUB / BOOKING
            </span>

            <h1>
              {selectedEvent?.title ||
                "Select Your Seats"}
            </h1>

            <p>
              {selectedEvent?.date ||
                "Event Date"}
              {"  •  "}
              {selectedEvent?.location ||
                "Event Location"}
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


        {/* ================= SEAT AREA ================= */}

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
                      length: SEATS_PER_ROW,
                    },
                    (_, index) => {

                      const seatNumber =
                        index + 1;

                      const seatId =
                        `${row}${seatNumber}`;

                      const isSelected =
                        selected.includes(
                          seatId
                        );

                      const isBooked =
                        bookedSeats.includes(
                          seatId
                        );

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
                            ].join(" ")}
                            disabled={isBooked}
                            onClick={() =>
                              toggleSeat(
                                seatId
                              )
                            }
                            title={
                              isBooked
                                ? "Already booked"
                                : `Seat ${seatId}`
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


        {/* ================= SUMMARY ================= */}

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

                selected
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
