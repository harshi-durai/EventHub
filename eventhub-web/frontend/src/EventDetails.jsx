
import React from "react";
import "./EventDetails.css";

function EventDetails({
  selectedEvent,
  onNavigate,
}) {
  if (!selectedEvent) {
    return (
      <div className="details-empty">

        <h2>No event selected.</h2>

        <button
          onClick={() => onNavigate("events")}
        >
          BACK TO EVENTS
        </button>

      </div>
    );
  }

  const handleBookNow = () => {
    onNavigate("seats", {
      event: selectedEvent,
    });
  };

  return (
    <div className="details-page">

      <header className="details-header">

        <button
          onClick={() => onNavigate("events")}
        >
          ← BACK TO EVENTS
        </button>

        <div className="details-logo">
          EVENT<span>HUB</span>
        </div>

        <span>01 / 03</span>

      </header>

      <main className="details-main">

        <div className="details-image">

          <img
            src={selectedEvent.image}
            alt={selectedEvent.title}
          />

          <div className="details-image-overlay"></div>

          <span>
            EVENTHUB / FEATURED
          </span>

        </div>

        <div className="details-content">

          <small>
            {selectedEvent.category}
          </small>

          <h1>
            {selectedEvent.title}
          </h1>

          <p className="details-description">
            Experience an unforgettable event with
            live entertainment, amazing people and
            memorable moments.
          </p>

          <div className="details-meta">

            <div>
              <span>DATE</span>
              <strong>
                {selectedEvent.date}
              </strong>
            </div>

            <div>
              <span>LOCATION</span>
              <strong>
                {selectedEvent.location}
              </strong>
            </div>

            <div>
              <span>TICKET</span>
              <strong>
                ₹{selectedEvent.price}
              </strong>
            </div>

          </div>

          <button
            type="button"
            className="book-now-button"
            onClick={handleBookNow}
          >
            BOOK NOW
            <span>→</span>
          </button>

        </div>

      </main>

    </div>
  );
}

export default EventDetails;
