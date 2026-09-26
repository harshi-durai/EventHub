
import React from "react";
import "./MyBookings.css";

function MyBookings({
  bookings = [],
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

  /*
   * Convert any seat format into a safe display string.
   *
   * Supports:
   * "A5"
   *
   * OR
   * {
   *   id: 35,
   *   seatId: 35,
   *   eventId: 3,
   *   seatNumber: "A5",
   *   status: "BOOKED"
   * }
   */
  const getSeatNumber = (seat) => {
    if (typeof seat === "string") {
      return seat;
    }

    if (typeof seat === "number") {
      return String(seat);
    }

    if (seat && typeof seat === "object") {
      return (
        seat.seatNumber ||
        seat.seat ||
        seat.name ||
        String(seat.seatId || seat.id || "")
      );
    }

    return "";
  };

  const getEventName = (booking) => {
    return (
      booking?.event?.name ||
      booking?.event?.title ||
      booking?.eventName ||
      "Event"
    );
  };

  const getEventDate = (booking) => {
    return (
      booking?.event?.eventDate ||
      booking?.event?.date ||
      "Date not available"
    );
  };

  const getEventTime = (booking) => {
    return (
      booking?.event?.eventTime ||
      booking?.event?.time ||
      ""
    );
  };

  const getVenue = (booking) => {
    return (
      booking?.event?.venue ||
      booking?.event?.location ||
      "Venue not available"
    );
  };

  const getPrice = (booking) => {
    return Number(
      booking?.event?.ticketPrice ??
      booking?.event?.price ??
      0
    );
  };

  const formatDate = (date) => {
    if (!date) {
      return "Date not available";
    }

    try {
      const parsed = new Date(date);

      if (Number.isNaN(parsed.getTime())) {
        return String(date);
      }

      return parsed.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric",
      });
    } catch {
      return String(date);
    }
  };

  return (
    <div className="bookings-page">

      {/* ================= HEADER ================= */}

      <header className="bookings-header">

        <button
          className="bookings-logo"
          type="button"
          onClick={() => onNavigate("home")}
        >
          EVENT<span>HUB</span>
        </button>

        <div className="bookings-header-title">
          MY BOOKINGS
        </div>

        <button
          className="bookings-back"
          type="button"
          onClick={() => onNavigate("home")}
        >
          ← HOME
        </button>

      </header>

      {/* ================= MAIN ================= */}

      <main className="bookings-main">

        <section className="bookings-heading">

          <div>
            <span>EVENTHUB / ACCOUNT</span>

            <h1>
              MY BOOKINGS<span>.</span>
            </h1>
          </div>

          <p>
            {bookings.length === 0
              ? "Your confirmed event bookings will appear here."
              : "Your confirmed event bookings."}
          </p>

        </section>

        {/* ================= EMPTY ================= */}

        {bookings.length === 0 ? (

          <section className="empty-bookings">

            <div className="empty-number">
              00
            </div>

            <h2>
              NO BOOKINGS YET
            </h2>

            <p>
              You have not booked any events yet.
              Choose an event and reserve your seats.
            </p>

            <button
              type="button"
              onClick={() => onNavigate("events")}
            >
              EXPLORE EVENTS →
            </button>

          </section>

        ) : (

          /* ================= BOOKING LIST ================= */

          <section className="booking-list">

            {bookings.map((booking, index) => {

              const event = booking?.event || {};

              /*
               * IMPORTANT:
               * Convert backend seat objects into seat numbers.
               */
              const rawSeats = Array.isArray(
                booking?.seats
              )
                ? booking.seats
                : [];

              const seatNumbers = rawSeats
                .map(getSeatNumber)
                .filter(Boolean);

              const price = getPrice(booking);

              const total =
                seatNumbers.length * price;

              return (
                <article
                  className="booking-card"
                  key={
                    booking?.id ??
                    `${event?.id ?? "event"}-${index}`
                  }
                >

                  {/* ================= INDEX ================= */}

                  <div className="booking-index">
                    {String(index + 1).padStart(2, "0")}
                  </div>

                  {/* ================= EVENT ================= */}

                  <div className="booking-event">

                    <span className="booking-label">
                      EVENT
                    </span>

                    <h2>
                      {getEventName(booking)}
                    </h2>

                    <p>
                      {formatDate(
                        getEventDate(booking)
                      )}

                      {getEventTime(booking) && (
                        <>
                          {"  •  "}
                          {getEventTime(booking)}
                        </>
                      )}
                    </p>

                    <p>
                      {getVenue(booking)}
                    </p>

                  </div>

                  {/* ================= SEATS ================= */}

                  <div className="booking-seats">

                    <span className="booking-label">
                      SEATS
                    </span>

                    <div className="booking-seat-list">

                      {seatNumbers.length > 0 ? (

                        seatNumbers.map(
                          (seat, seatIndex) => (
                            <span
                              className="summary-seat"
                              key={`${seat}-${seatIndex}`}
                            >
                              {seat}
                            </span>
                          )
                        )

                      ) : (

                        <span>
                          No seat information
                        </span>

                      )}

                    </div>

                  </div>

                  {/* ================= TOTAL ================= */}

                  <div className="booking-total">

                    <span className="booking-label">
                      TOTAL
                    </span>

                    <strong>
                      ₹{total}
                    </strong>

                  </div>

                  {/* ================= STATUS ================= */}

                  <div className="booking-status">

                    <span className="booking-label">
                      STATUS
                    </span>

                    <strong>
                      {booking?.status || "Confirmed"}
                    </strong>

                  </div>

                  {/* ================= CANCEL ================= */}

                  <button
                    className="cancel-booking"
                    type="button"
                    onClick={() =>
                      handleCancel(booking?.id)
                    }
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
