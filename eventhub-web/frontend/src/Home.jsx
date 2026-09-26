
import React from "react";
import "./Home.css";

/* =========================================================
   EVENT DATA
========================================================= */

const events = [
  {
    id: 1,
    title: "College Fest 2026",
    category: "MUSIC / ENTERTAINMENT",
    date: "25 SEP 2026",
    location: "College Auditorium",
    image:
      "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1400&q=90",
  },

  {
    id: 2,
    title: "Cultural Night",
    category: "CULTURE / PERFORMANCE",
    date: "30 SEP 2026",
    location: "Main Hall",
    image:
      "https://images.unsplash.com/photo-1506157786151-b8491531f063?auto=format&fit=crop&w=1400&q=90",
  },

  {
    id: 3,
    title: "Tech Symposium",
    category: "TECHNOLOGY / INNOVATION",
    date: "05 OCT 2026",
    location: "Seminar Hall",
    image:
      "https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1400&q=90",
  },
];

/* =========================================================
   HOME COMPONENT
========================================================= */

function Home({
  currentUser,
  user,
  onNavigate,
  onLogout,
}) {
  /*
    Supports both currentUser and user.
    Your App.jsx currently sends currentUser.
  */
  const loggedUser = currentUser || user || null;

  /* =======================================================
     EVENT SELECT
  ======================================================= */

  const openEvent = (event) => {
    if (onNavigate) {
      onNavigate("details", {
        event: event,
      });
    }
  };

  /* =======================================================
     NAVIGATION
  ======================================================= */

  const goHome = () => {
    if (onNavigate) {
      onNavigate("home");
    }
  };

  const goEvents = () => {
    if (onNavigate) {
      onNavigate("events");
    }
  };

  const goBookings = () => {
    if (onNavigate) {
      onNavigate("bookings");
    }
  };

  const goLogin = () => {
    if (onNavigate) {
      onNavigate("login");
    }
  };

  const goRegister = () => {
    if (onNavigate) {
      onNavigate("register");
    }
  };

  /* =======================================================
     JSX
  ======================================================= */

  return (
    <div className="home-page">

      {/* ===================================================
          HEADER
      =================================================== */}

      <header className="home-header">

        {/* LOGO */}

        <button
          className="brand"
          onClick={goHome}
          type="button"
        >
          EVENT<span>HUB</span>
        </button>


        {/* NAVIGATION */}

        <nav className="main-nav">

          <button
            className="nav-link active"
            onClick={goHome}
            type="button"
          >
            HOME
          </button>

          <button
            className="nav-link"
            onClick={goEvents}
            type="button"
          >
            EVENTS
          </button>

          <button
            className="nav-link"
            onClick={goBookings}
            type="button"
          >
            MY BOOKINGS
          </button>

          <button
            className="nav-link"
            onClick={() => {
              document
                .getElementById("about")
                ?.scrollIntoView({
                  behavior: "smooth",
                });
            }}
            type="button"
          >
            ABOUT
          </button>

        </nav>


        {/* LOGIN / LOGOUT */}

        {loggedUser ? (

          <button
            className="login-button"
            onClick={onLogout}
            type="button"
          >
            LOGOUT
            <span>↗</span>
          </button>

        ) : (

          <button
            className="login-button"
            onClick={goLogin}
            type="button"
          >
            LOGIN
            <span>↗</span>
          </button>

        )}

      </header>


      {/* ===================================================
          MAIN
      =================================================== */}

      <main>


        {/* =================================================
            HERO SECTION
        ================================================= */}

        <section className="hero-section">

          {/* LEFT CONTENT */}

          <div className="hero-content">

            <div className="hero-eyebrow">

              <span></span>

              EVENT & TICKET EXPERIENCE

            </div>


            <h1 className="hero-title">

              Find Your

              <br />

              <strong>Next Event.</strong>

            </h1>


            <p className="hero-description">

              Discover unforgettable events, choose your
              preferred seats, and book your experience —
              all in one place.

            </p>


            {/* HERO BUTTONS */}

            <div className="hero-buttons">

              <button
                className="primary-button"
                onClick={goEvents}
                type="button"
              >

                EXPLORE EVENTS

                <span>→</span>

              </button>


              {!loggedUser && (

                <button
                  className="secondary-button"
                  onClick={goRegister}
                  type="button"
                >

                  CREATE ACCOUNT

                </button>

              )}

            </div>


            {/* SCROLL INDICATOR */}

            <div className="scroll-indicator">

              <span>
                SCROLL TO EXPLORE
              </span>

              <div className="scroll-line"></div>

              <div className="scroll-circle">
                ↓
              </div>

            </div>

          </div>


          {/* =================================================
              HERO IMAGE
          ================================================= */}

          <div className="hero-image-wrapper">

            <div className="hero-image-card">

              <img
                src="https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1600&q=90"
                alt="Aesthetic live cultural event"
              />


              <div className="hero-image-overlay"></div>


              {/* TOP INFO */}

              <div className="hero-image-top">

                <span>
                  EVENTHUB / 01
                </span>

                <span>
                  LIVE
                </span>

              </div>


              {/* BOTTOM INFO */}

              <div className="hero-image-bottom">

                <div>

                  <small>
                    FEATURED EVENT
                  </small>

                  <h2>

                    College

                    <br />

                    Fest

                  </h2>

                </div>


                <button
                  onClick={() => openEvent(events[0])}
                  type="button"
                  aria-label="Open College Fest"
                >
                  ↗
                </button>

              </div>

            </div>

          </div>


          {/* =================================================
              SIDE NUMBER
          ================================================= */}

          <div className="hero-side-number">

            <strong>
              01
            </strong>

            <span>
              /03
            </span>


            <div className="vertical-line"></div>


            <p>

              DISCOVER

              <br />

              SELECT

              <br />

              BOOK

            </p>

          </div>

        </section>


        {/* =================================================
            EVENTS SECTION
        ================================================= */}

        <section
          className="events-section"
          id="events"
        >

          <div className="section-heading">

            <div>

              <span className="section-number">
                01 / DISCOVER
              </span>

              <h2>

                What's

                <br />

                Happening.

              </h2>

            </div>


            <div className="section-description">

              <p>

                Explore upcoming experiences
                and find something worth
                remembering.

              </p>


              <button
                onClick={goEvents}
                type="button"
              >
                VIEW ALL EVENTS →
              </button>

            </div>

          </div>


          {/* EVENT GRID */}

          <div className="event-grid">

            {events.map((event, index) => (

              <article
                className="event-card"
                key={event.id}
                onClick={() => openEvent(event)}
              >

                {/* IMAGE */}

                <div className="event-image">

                  <img
                    src={event.image}
                    alt={event.title}
                  />


                  <div className="event-image-number">
                    0{index + 1}
                  </div>


                  <div className="event-image-overlay"></div>


                  <button
                    className="event-arrow"
                    onClick={(e) => {
                      e.stopPropagation();
                      openEvent(event);
                    }}
                    type="button"
                    aria-label={`Open ${event.title}`}
                  >
                    ↗
                  </button>

                </div>


                {/* EVENT INFORMATION */}

                <div className="event-info">

                  <span className="event-category">
                    {event.category}
                  </span>


                  <h3>
                    {event.title}
                  </h3>


                  <div className="event-meta">

                    <span>
                      ◷ {event.date}
                    </span>

                    <span>
                      ⌖ {event.location}
                    </span>

                  </div>

                </div>

              </article>

            ))}

          </div>

        </section>


        {/* =================================================
            PROCESS SECTION
        ================================================= */}

        <section className="process-section">

          <div className="process-title">

            <span>
              02 / SIMPLE PROCESS
            </span>

            <h2>

              From Discovery

              <br />

              to Experience.

            </h2>

          </div>


          <div className="process-list">


            {/* PROCESS 01 */}

            <div className="process-item">

              <div className="process-number">
                01 / Discover
              </div>

              <h3>
                Discover
              </h3>

              <p>
                Browse upcoming events and find
                the experience you're looking for.
              </p>

            </div>


            {/* PROCESS 02 */}

            <div className="process-item">

              <div className="process-number">
                02 / Select
              </div>

              <h3>
                Select
              </h3>

              <p>
                Choose your preferred event
                and select the exact seats.
              </p>

            </div>


            {/* PROCESS 03 */}

            <div className="process-item">

              <div className="process-number">
                03 / Book
              </div>

              <h3>
                Book
              </h3>

              <p>
                Confirm your booking and get
                your digital ticket instantly.
              </p>

            </div>


            {/* PROCESS 04 */}

            <div className="process-item">

              <div className="process-number">
                04 / Experience
              </div>

              <h3>
                Experience
              </h3>

              <p>
                Enjoy your event with everything
                organized in one place.
              </p>

            </div>

          </div>

        </section>


        {/* =================================================
            ABOUT SECTION
        ================================================= */}

        <section
          className="about-section"
          id="about"
        >

          {/* ABOUT VISUAL */}

          <div className="about-image">

            <img
              src="https://images.unsplash.com/photo-1540039155733-5bb30b53aa14?auto=format&fit=crop&w=1400&q=90"
              alt="Event lights and stage"
            />


            <div className="about-image-label">
              EVENTHUB / EXPERIENCE
            </div>

          </div>


          {/* ABOUT CONTENT */}

          <div className="about-content">

            <span className="section-number">
              03 / EVENTHUB
            </span>


            <h2>

              One Place.

              <br />

              Every Experience.

            </h2>


            <p>

              EventHub brings events, seat selection
              and ticket booking together in one
              simple experience. Discover, choose,
              book and enjoy without unnecessary
              steps.

            </p>


            <button
              className="primary-button"
              onClick={goEvents}
              type="button"
            >

              START EXPLORING

              <span>
                →
              </span>

            </button>


            {/* STATS */}

            <div className="stats">

              <div>

                <strong>
                  100+
                </strong>

                <span>
                  EVENTS HOSTED
                </span>

              </div>


              <div>

                <strong>
                  50K+
                </strong>

                <span>
                  HAPPY USERS
                </span>

              </div>


              <div>

                <strong>
                  4.8
                </strong>

                <span>
                  USER RATING
                </span>

              </div>

            </div>

          </div>

        </section>


        {/* =================================================
            FINAL CTA
        ================================================= */}

        <section className="final-section">

          <div className="final-content">

            <span>
              YOUR NEXT EXPERIENCE
            </span>

            <h2>

              Ready to Find

              <br />

              Your Event?

            </h2>

          </div>


          <button
            className="primary-button"
            onClick={goEvents}
            type="button"
          >

            EXPLORE EVENTS

            <span>
              →
            </span>

          </button>

        </section>

      </main>


      {/* ===================================================
          FOOTER
      =================================================== */}

      <footer className="home-footer">

        <div>
          EVENT<span>HUB</span>
        </div>


        <span>
          EVENTS MADE SIMPLE.
        </span>


        <span>
          © 2026 EVENTHUB
        </span>

      </footer>

    </div>
  );
}

export default Home;
