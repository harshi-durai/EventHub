
import React, { useEffect, useState } from "react";
import "./Events.css";

const eventImages = {
  1: "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1200&q=85",
  2: "https://images.unsplash.com/photo-1506157786151-b8491531f063?auto=format&fit=crop&w=1200&q=85",
  3: "https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1200&q=85",
  4: "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?auto=format&fit=crop&w=1200&q=85",
  5: "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1200&q=85",
  6: "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?auto=format&fit=crop&w=1200&q=85",
};

const eventCategories = {
  1: "MUSIC / ENTERTAINMENT",
  2: "CULTURE / PERFORMANCE",
  3: "TECHNOLOGY / INNOVATION",
  4: "MUSIC / LIVE",
  5: "DESIGN / CREATIVITY",
  6: "CULTURE / CELEBRATION",
};

function Events({ onNavigate }) {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("https://eventhub-xhdu.onrender.com/api/events")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Unable to load events");
        }

        return response.json();
      })
      .then((data) => {
        setEvents(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError("Unable to connect to EventHub backend.");
        setLoading(false);
      });
  }, []);

  const openEvent = (event) => {
    onNavigate("details", {
      event: {
        ...event,

        title: event.name,
        category:
          eventCategories[event.id] || "EVENT / EXPERIENCE",

        date: event.eventDate
          ? new Date(event.eventDate).toLocaleDateString(
              "en-GB",
              {
                day: "2-digit",
                month: "short",
                year: "numeric",
              }
            ).toUpperCase()
          : "",

        location: event.venue,

        price: event.ticketPrice,

        image:
          eventImages[event.id] ||
          eventImages[1],
      },
    });
  };

  return (
    <div className="events-page">

      {/* ================= HEADER ================= */}

      <header className="events-header">

        <button
          className="events-logo"
          onClick={() => onNavigate("home")}
          type="button"
        >
          EVENT<span>HUB</span>
        </button>

        <div className="events-header-title">
          DISCOVER EVENTS
        </div>

        <button
          className="events-home"
          onClick={() => onNavigate("home")}
          type="button"
        >
          ← HOME
        </button>

      </header>


      {/* ================= MAIN ================= */}

      <main className="events-main">

        <div className="events-heading">

          <span>
            EVENTHUB / 2026
          </span>

          <h1>
            Find Your
            <br />
            Event.
          </h1>

          <p>
            Discover concerts, cultural experiences,
            technology events and more.
          </p>

        </div>


        {/* ================= LOADING ================= */}

        {loading && (
          <div className="events-loading">
            LOADING EVENTS...
          </div>
        )}


        {/* ================= ERROR ================= */}

        {error && (
          <div className="events-error">
            {error}
            <br />
            <small>
              Make sure the Java backend is running on port 8080.
            </small>
          </div>
        )}


        {/* ================= EVENT GRID ================= */}

        {!loading && !error && (

          <div className="events-grid">

            {events.map((event, index) => {

              const displayEvent = {
                ...event,

                title: event.name,

                category:
                  eventCategories[event.id] ||
                  "EVENT / EXPERIENCE",

                date: event.eventDate
                  ? new Date(event.eventDate)
                      .toLocaleDateString(
                        "en-GB",
                        {
                          day: "2-digit",
                          month: "short",
                          year: "numeric",
                        }
                      )
                      .toUpperCase()
                  : "",

                location: event.venue,

                price: event.ticketPrice,

                image:
                  eventImages[event.id] ||
                  eventImages[1],
              };

              return (

                <article
                  className="events-card"
                  key={event.id}
                  onClick={() => openEvent(event)}
                >

                  <div className="events-card-image">

                    <img
                      src={displayEvent.image}
                      alt={displayEvent.title}
                    />

                    <div className="events-card-overlay"></div>

                    <span className="events-card-number">
                      {String(index + 1).padStart(2, "0")}
                    </span>

                    <button
                      className="events-card-arrow"
                      type="button"
                      onClick={(e) => {
                        e.stopPropagation();
                        openEvent(event);
                      }}
                    >
                      ↗
                    </button>

                  </div>


                  <div className="events-card-info">

                    <span>
                      {displayEvent.category}
                    </span>

                    <h2>
                      {displayEvent.title}
                    </h2>

                    <div className="events-card-meta">

                      <span>
                        {displayEvent.date}
                      </span>

                      <span>
                        {displayEvent.location}
                      </span>

                    </div>

                    <div className="events-card-price">

                      ₹{displayEvent.price}

                      <small>
                        / SEAT
                      </small>

                    </div>

                  </div>

                </article>

              );
            })}

          </div>

        )}

      </main>

    </div>
  );
}

export default Events;
