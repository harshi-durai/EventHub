
import React, { useState } from "react";
import "./Register.css";

function Register({
  onRegister,
  onLogin,
  onBack,
}) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] =
    useState("");

  const [error, setError] = useState("");

  const handleRegister = (e) => {
    e.preventDefault();

    setError("");

    const cleanName = name.trim();
    const cleanEmail = email.trim().toLowerCase();

    if (
      !cleanName ||
      !cleanEmail ||
      !password ||
      !confirmPassword
    ) {
      setError("Please fill in all fields.");
      return;
    }

    if (!cleanEmail.includes("@")) {
      setError("Please enter a valid email address.");
      return;
    }

    if (password.length < 6) {
      setError(
        "Password must contain at least 6 characters."
      );
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    const account = {
      name: cleanName,
      email: cleanEmail,
      password: password,
    };

    /*
      SAVE REGISTERED ACCOUNT
    */

    localStorage.setItem(
      "eventhubRegisteredUser",
      JSON.stringify(account)
    );

    /*
      LOGIN SESSION
    */

    const loggedUser = {
      name: cleanName,
      email: cleanEmail,
    };

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(loggedUser)
    );

    console.log(
      "ACCOUNT CREATED:",
      loggedUser
    );

    /*
      RETURN TO APP
    */

    if (onRegister) {
      onRegister(loggedUser);
    } else {
      window.location.hash = "/home";
    }
  };


  const handleLogin = () => {

    if (onLogin) {
      onLogin();
    } else {
      window.location.hash = "/login";
    }

  };


  return (
    <div className="register-page">

      <div className="register-art">

        <div className="register-glow"></div>

        <div className="register-orbit orbit-one"></div>

        <div className="register-orbit orbit-two"></div>

        <div className="register-orbit orbit-three"></div>

        <div className="register-art-title">
          CREATE
          <br />
          <span>YOUR</span>
          <br />
          STORY.
        </div>

        <div className="register-art-number">
          02
        </div>

        <div className="register-art-label">
          EVENTHUB
          <br />
          EXPERIENCE
        </div>

      </div>


      <div className="register-container">

        <button
          type="button"
          className="register-back"
          onClick={onBack}
        >
          ← BACK
        </button>


        <div className="register-brand">
          EVENT<span>HUB</span>
        </div>


        <div className="register-content">

          <div className="register-kicker">
            02 / CREATE ACCOUNT
          </div>


          <h1>
            Join
            <br />
            <span>Us.</span>
          </h1>


          <p>
            Create your account and start
            discovering experiences.
          </p>


          <form onSubmit={handleRegister}>

            <div className="register-field">

              <label>
                FULL NAME
              </label>

              <input
                type="text"
                value={name}
                placeholder="Your name"
                onChange={(e) =>
                  setName(e.target.value)
                }
              />

            </div>


            <div className="register-field">

              <label>
                EMAIL ADDRESS
              </label>

              <input
                type="email"
                value={email}
                placeholder="you@example.com"
                onChange={(e) =>
                  setEmail(e.target.value)
                }
              />

            </div>


            <div className="register-two-fields">

              <div className="register-field">

                <label>
                  PASSWORD
                </label>

                <input
                  type="password"
                  value={password}
                  placeholder="Min. 6 characters"
                  onChange={(e) =>
                    setPassword(e.target.value)
                  }
                />

              </div>


              <div className="register-field">

                <label>
                  CONFIRM
                </label>

                <input
                  type="password"
                  value={confirmPassword}
                  placeholder="Repeat"
                  onChange={(e) =>
                    setConfirmPassword(
                      e.target.value
                    )
                  }
                />

              </div>

            </div>


            {error && (
              <div className="register-error">
                {error}
              </div>
            )}


            <button
              type="submit"
              className="register-submit"
            >
              <span>
                CREATE ACCOUNT
              </span>

              <strong>
                →
              </strong>
            </button>

          </form>


          <div className="register-divider">
            <span></span>
            ALREADY A MEMBER?
            <span></span>
          </div>


          <button
            type="button"
            className="login-button"
            onClick={handleLogin}
          >
            <span>
              SIGN IN TO EVENTHUB
            </span>

            <strong>
              ↗
            </strong>
          </button>

        </div>


        <div className="register-footer">

          <span>
            EVENT & TICKET EXPERIENCE
          </span>

          <span>
            © 2026 EVENTHUB
          </span>

        </div>

      </div>

    </div>
  );
}

export default Register;
