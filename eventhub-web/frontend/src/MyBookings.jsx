
import React, {
  useEffect,
  useState,
} from "react";

import "./MyBookings.css";

const API_BASE_URL =
  "https://eventhub-xhdu.onrender.com";

function MyBookings({
  currentUser,
  onNavigate,
  onLogout,
}) {

  const [bookings, setBookings] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");


  /* =====================================================
     USER ID
  ===================================================== */

  const userId = Number(
    currentUser?.id ??
    currentUser?.userId ??
    currentUser?.user_id ??
    0
  );


  /* =====================================================
     LOAD BOOKINGS
  ===================================================== */

  const loadBookings = async () => {

    if (!userId) {

      setBookings([]);
      setLoading(false);

      return;
    }


    try {

      setLoading(true);
      setError("");


      console.log(
        "Loading bookings for user:",
        userId
      );


      const response =
        await fetch(
          `${API_BASE_URL}/api/bookings?userId=${userId}`
        );


      const data =
        await response.json();


      console.log(
        "Bookings response:",
        data
      );


      if (!response.ok) {

        throw new Error(
          data?.message ||
          "Unable to load bookings."
        );
      }


      setBookings(
        Array.isArray(data?.bookings)
          ? data.bookings
          : []
      );


    } catch (err) {

      console.error(
        "LOAD BOOKINGS ERROR:",
        err
      );

      setError(
        err.message ||
        "Unable to load bookings."
      );

      setBookings([]);

    } finally {

      setLoading(false);
    }
  };


  /* =====================================================
     LOAD ON PAGE OPEN
  ===================================================== */

  useEffect(() => {

    loadBookings();

  }, [userId]);


  /* =====================================================
     CANCEL BOOKING
  ===================================================== */

  const handleCancel = async (
    bookingId
  ) => {

    const confirmed =
      window.confirm(
        "Are you sure you want to cancel this booking?"
      );


    if (!confirmed) {
      return;
    }


    try {

      console.log(
        "Cancelling booking:",
        bookingId
      );


      const response =
        await fetch(
          `${API_BASE_URL}/api/bookings/${bookingId}?userId=${userId}`,
          {
            method: "DELETE",
          }
        );


      const data =
        await response.json();


      console.log(
        "Cancel response:",
        data
      );


      if (!response.ok) {

        throw new Error(
          data?.message ||
          "Unable to cancel booking."
        );
      }


      alert(
        "Booking cancelled successfully."
      );


      /* Refresh from MySQL */
      await loadBookings();


    } catch (err) {

      console.error(
        "CANCEL ERROR:",
        err
      );


      alert(
        err.message ||
        "Unable to cancel booking."
      );
    }
  };


  /* =====================================================
     NOT LOGGED IN
  ===================================================== */

  if (!currentUser) {

    return (
      <div className="my-bookings-page">

        <div className="my-bookings-header">

          <button
            className="my-bookings-brand"
            onClick={() =>
              onNavigate("home")
            }
          >
            EVENT<span>HUB</span>
          </button>

        </div>


        <main className="my-bookings-content">

          <div className="empty-bookings">

            <h1>
              MY BOOKINGS
            </h1>

            <p>
              Please login to view your bookings.
            </p>

            <button
              onClick={() =>
                onNavigate("login")
              }
            >
              LOGIN
            </button>

          </div>

        </main>

      </div>
    );
  }


  /* =====================================================
     PAGE
  ===================================================== */

  return (

    <div className="my-bookings-page">

      {/* HEADER */}

      <header className="my-bookings-header">

        <button
          className="my-bookings-brand"
          onClick={() =>
            onNavigate("home")
          }
          type="button"
        >
          EVENT<span>HUB</span>
        </button>


        <div className="my-bookings-title">
          MY BOOKINGS
        </div>


        <button
          className="my-bookings-back"
          onClick={() =>
            onNavigate("home")
          }
          type="button"
        >
          ← BACK
        </button>

      </header>


      {/* MAIN */}

      <main className="my-bookings-content">

        <div className="bookings-heading">

          <div>

            <span>
              EVENTHUB / ACCOUNT
            </span>

            <h1>
              MY BOOKINGS
            </h1>

          </div>

          <button
            className="browse-events-button"
            onClick={() =>
              onNavigate("events")
            }
            type="button"
          >
            BROWSE EVENTS →
          </button>

        </div>


        {/* LOADING */}

        {loading && (

          <div className="bookings-loading">

            <div className="loading-spinner"></div>

            <h3>
              LOADING BOOKINGS
            </h3>

            <p>
              Fetching your bookings from EventHub...
            </p>

          </div>
        )}


        {/* ERROR */}

        {!loading && error && (

          <div className="bookings-error">

            <h3>
              UNABLE TO LOAD BOOKINGS
            </h3>

            <p>
              {error}
            </p>

            <button
              onClick={loadBookings}
              type="button"
            >
              TRY AGAIN
            </button>

          </div>
        )}


        {/* EMPTY */}

        {!loading &&
          !error &&
          bookings.length === 0 && (

            <div className="empty-bookings">

              <div className="empty-number">
                00
              </div>

              <h2>
                NO BOOKINGS YET
              </h2>

              <p>
                You haven't booked any events yet.
              </p>

              <button
                onClick={() =>
                  onNavigate("events")
                }
                type="button"
              >
                DISCOVER EVENTS →
              </button>

            </div>
          )}


        {/* BOOKINGS */}

        {!loading &&
          !error &&
          bookings.length > 0 && (

            <div className="bookings-list">

              {bookings.map(
                (booking, index) => {

                  const status =
                    String(
                      booking.status || ""
                    ).toUpperCase();


                  const isCancelled =
                    status ===
                    "CANCELLED";


                  return (

                    <article
                      className={`booking-card ${
                        isCancelled
                          ? "cancelled"
                          : ""
                      }`}
                      key={
                        booking.id ??
                        index
                      }
                    >

                      {/* NUMBER */}

                      <div className="booking-index">

                        {String(
                          index + 1
                        ).padStart(
                          2,
                          "0"
                        )}

                      </div>


                      {/* DETAILS */}

                      <div className="booking-details">

                        <span className="booking-label">
                          EVENT
                        </span>

                        <h2>
                          {
                            booking.eventName ||
                            booking.event ||
                            "Event"
                          }
                        </h2>


                        <div className="booking-meta">

                          <div>

                            <span>
                              DATE
                            </span>

                            <strong>
                              {
                                booking.eventDate ||
                                "N/A"
                              }
                            </strong>

                          </div>


                          <div>

                            <span>
                              VENUE
                            </span>

                            <strong>
                              {
                                booking.venue ||
                                "N/A"
                              }
                            </strong>

                          </div>


                          <div>

                            <span>
                              SEATS
                            </span>

                            <strong>
                              {
                                booking.seats ||
                                "N/A"
                              }
                            </strong>

                          </div>

                        </div>

                      </div>


                      {/* RIGHT */}

                      <div className="booking-right">

                        <span className="booking-label">
                          BOOKING ID
                        </span>

                        <strong>
                          #
                          {
                            booking.id
                          }
                        </strong>


                        <span className="booking-label">
                          TICKET
                        </span>

                        <strong>
                          {
                            booking.ticketCode ||
                            "N/A"
                          }
                        </strong>


                        <span
                          className={`booking-status ${
                            isCancelled
                              ? "cancelled"
                              : "confirmed"
                          }`}
                        >
                          {
                            booking.status ||
                            "UNKNOWN"
                          }
                        </span>


                        <strong className="booking-total">
                          ₹
                          {
                            Number(
                              booking.totalAmount ||
                              0
                            ).toFixed(2)
                          }
                        </strong>


                        {!isCancelled && (

                          <button
                            className="cancel-booking-button"
                            onClick={() =>
                              handleCancel(
                                booking.id
                              )
                            }
                            type="button"
                          >
                            CANCEL BOOKING
                          </button>

                        )}

                      </div>

                    </article>
                  );
                }
              )}

            </div>
          )}

      </main>

    </div>
  );
}

export default MyBookings;