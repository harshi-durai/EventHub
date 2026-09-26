
import React from "react";
import "./BookingConfirmation.css";

function BookingConfirmation({
  selectedEvent,
  selectedSeats,
  onNavigate,
}) {
  return (
    <div className="confirmation-page">

      <header className="confirmation-header">

        <div className="confirmation-logo">
          EVENT<span>HUB</span>
        </div>

        <span>
          03 / 03
        </span>

      </header>

      <main className="confirmation-main">

        <div className="confirmation-check">
          ✓
        </div>

        <small>
          EVENTHUB / BOOKING CONFIRMED
        </small>

        <h1>
          You're
          <br />
          <em>All Set.</em>
        </h1>

        {selectedEvent && (
          <div className="ticket">

            <div className="ticket-image">

              <img
                src={selectedEvent.image}
                alt={selectedEvent.title}
              />

            </div>

            <div className="ticket-info">

              <small>
                EVENT
              </small>

              <h2>
                {selectedEvent.title}
              </h2>

              <p>
                {selectedEvent.date}
                <br />
                {selectedEvent.location}
              </p>

              <div className="ticket-seats">

                <span>
                  SEATS
                </span>

                <strong>
                  {selectedSeats.join(", ")}
                </strong>

              </div>

            </div>

          </div>
        )}

        <div className="confirmation-actions">

          <button
            onClick={() => onNavigate("bookings")}
          >
            VIEW MY BOOKINGS →
          </button>

          <button
            onClick={() => onNavigate("events")}
          >
            EXPLORE MORE EVENTS
          </button>

        </div>

      </main>

    </div>
  );
}

export default BookingConfirmation;