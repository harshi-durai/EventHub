
import React from "react";
import "./MyBookings.css";

function MyBookings({
  bookings = [],
  currentUser,
  onNavigate,
  onCancelBooking,
}) {
  const handleCancel = (bookingId) => {
    const confirmed = window.confirm(
      "Are you sure you want to cancel this booking?"
    );

    if (!confirmed) {
      return;
    }

    if (onCancelBooking) {
      onCancelBooking(bookingId);
    }
  };

  return (
    <div className="bookings-page">

      {/* ================= HEADER ================= */}

      <header className="bookings-header">

        <button
          className="bookings-logo"
          onClick={() => onNavigate("home")}
          type="button"
        >
          EVENT<span>HUB</span>
        </button>

        <div className="bookings-header-title">
          MY BOOKINGS
        </div>

        <button
          className="bookings-back"
          onClick={() => onNavigate("home")}
          type="button"
        >
          ← HOME
        </button>

      </header>


      {/* ================= MAIN ================= */}

      <main className="bookings-main">

        <div className="bookings-heading">

          <span>
            EVENTHUB / YOUR EXPERIENCES
          </span>

          <h1>
            My
            <br />
            Bookings.
          </h1>

          <p>
            {currentUser?.name
              ? `Welcome back, ${currentUser.name}.`
              : "Your confirmed event bookings."}
          </p>

        </div>


        {/* ================= EMPTY ================= */}

        {bookings.length === 0 ? (

          <section className="empty-bookings">

            <div className="empty-number">
              00
            </div>

            <h2>
              No bookings yet.
            </h2>

            <p>
              Discover an event and reserve
              your seats to see your booking here.
            </p>

            <button
              onClick={() => onNavigate("events")}
              type="button"
            >
              EXPLORE EVENTS →
            </button>

          </section>

        ) : (

          <section className="booking-list">

            {bookings.map((booking, index) => {

              const event = booking.event || {};

              return (
                <article
                  className="booking-card"
                  key={booking.id}
                >

                  {/* NUMBER */}

                  <div className="booking-index">
                    0{index + 1}
                  </div>


                  {/* EVENT */}

                  <div className="booking-event">

                    <span>
                      {event.category ||
                        "EVENT"}
                    </span>

                    <h2>
                      {event.title ||
                        "Event"}
                    </h2>

                    <p>
                      {event.date ||
                        "Date"}
                      {"  •  "}
                      {event.location ||
                        "Location"}
                    </p>

                  </div>


                  {/* SEATS */}

                  <div className="booking-seats">

                    <small>
                      SEATS
                    </small>

                    <div>
                      {(booking.seats || []).map(
                        (seat) => (
                          <span key={seat}>
                            {seat}
                          </span>
                        )
                      )}
                    </div>

                  </div>


                  {/* STATUS */}

                  <div className="booking-status">

                    <small>
                      STATUS
                    </small>

                    <strong>
                      {booking.status ||
                        "Confirmed"}
                    </strong>

                  </div>


                  {/* ACTION */}

                  <button
                    className="cancel-booking"
                    onClick={() =>
                      handleCancel(
                        booking.id
                      )
                    }
                    type="button"
                  >
                    CANCEL
                  </button>

                </article>
              );
            })}

          </section>

        )}

      </main>

    </div>
  );
}

export default MyBookings;