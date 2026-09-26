
import React, { useState } from "react";
import "./Login.css";

function Login({ onLogin, onRegister, onBack }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = (e) => {
    e.preventDefault();

    setError("");

    const cleanEmail = email.trim().toLowerCase();

    if (!cleanEmail || !password) {
      setError("Please enter your email and password.");
      return;
    }

    const savedUser = localStorage.getItem(
      "eventhubRegisteredUser"
    );

    if (!savedUser) {
      setError("Account not found. Please create an account first.");
      return;
    }

    const user = JSON.parse(savedUser);

    if (
      user.email !== cleanEmail ||
      user.password !== password
    ) {
      setError("Incorrect email or password.");
      return;
    }

    const loggedUser = {
      name: user.name,
      email: user.email,
    };

    localStorage.setItem(
      "eventhubUser",
      JSON.stringify(loggedUser)
    );

    if (onLogin) {
      onLogin(loggedUser);
    }
  };

  const handleCreateAccount = () => {
    console.log("CREATE ACCOUNT BUTTON CLICKED");

    if (onRegister) {
      onRegister();
    } else {
      window.location.hash = "/register";
    }
  };

  return (
    <div className="login-page">

      <div className="login-art">

        <div className="art-grid"></div>

        <div className="art-circle circle-one"></div>

        <div className="art-circle circle-two"></div>

        <div className="art-ring"></div>

        <div className="art-text">
          EH
        </div>

        <div className="art-label">
          EVENT
          <br />
          HUB
        </div>

      </div>


      <div className="login-container">

        <button
          type="button"
          className="back-button"
          onClick={onBack}
        >
          ← BACK
        </button>


        <div className="login-brand">
          EVENT<span>HUB</span>
        </div>


        <div className="login-content">

          <div className="login-kicker">
            01 / WELCOME BACK
          </div>


          <h1>
            Sign
            <br />
            <span>In.</span>
          </h1>


          <p>
            Continue your EventHub
            experience.
          </p>


          <form onSubmit={handleLogin}>

            <div className="login-field">

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


            <div className="login-field">

              <label>
                PASSWORD
              </label>

              <input
                type="password"
                value={password}
                placeholder="Enter your password"
                onChange={(e) =>
                  setPassword(e.target.value)
                }
              />

            </div>


            {error && (
              <div className="login-error">
                {error}
              </div>
            )}


            <button
              type="submit"
              className="login-submit"
            >
              <span>
                SIGN IN
              </span>

              <strong>
                →
              </strong>
            </button>

          </form>


          <div className="login-divider">
            <span></span>
            NEW TO EVENTHUB?
            <span></span>
          </div>


          <button
            type="button"
            className="create-account-button"
            onClick={handleCreateAccount}
          >
            <span>
              CREATE ACCOUNT
            </span>

            <strong>
              ↗
            </strong>
          </button>

        </div>


        <div className="login-footer">

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

export default Login;
