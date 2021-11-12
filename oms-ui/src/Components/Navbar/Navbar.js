import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./Navbar.css";
import { faSignOutAlt, faUser } from "@fortawesome/fontawesome-free-solid";
import { AuthContext } from "../../Contexts/AuthContext";
import LogoutService from "../../Utils/LogoutService";

const Navbar = () => {
  const { userName, role, setUserName, setRole } = useContext(AuthContext);

  let contents;
  let adminContents;

  const onLogout = () => {
    setUserName(null);//Changing Context state from here and rest logout formalities from service
    setRole(null);
    LogoutService();
  }

  if (userName && (role === "ROLE_ADMIN" || role === "ROLE_Admin")) {
    adminContents = (
      <ul className="navbar-nav me-auto">
        <li className="nav-item mx-4">
          <Link
            data-testid="view-brokers-button"
            className="nav-link"
            to="/broker-list"
          >
            View Brokers
            <span className="visually-hidden">(current)</span>
          </Link>
        </li>
      </ul>
    );
  }

  if (userName) {
    contents = (
      <ul className="navbar-nav me-auto">
        <li className="nav-item dropdown mx-4">
          <a
            className="nav-link dropdown-toggle"
            data-bs-toggle="dropdown"
            href="#"
            role="button"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Order
          </a>
          <div className="dropdown-menu">
            <Link className="dropdown-item" to="/capture-order">
              Order Capture
            </Link>
            <div className="dropdown-divider"></div>
            <Link className="dropdown-item" to="/order-list">
              Order List
            </Link>
          </div>
        </li>

        {adminContents}

        <li className="nav-item mx-4">
          <Link data-testid="bulk-upload-button" className="nav-link" to="/upload-clients">
            Upload Client Data
            <span className="visually-hidden">(current)</span>
          </Link>
        </li>

        <li className="nav-item dropdown mx-4">
          <a
            className="nav-link dropdown-toggle"
            data-bs-toggle="dropdown"
            href="#"
            role="button"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Account
          </a>
          <div className="dropdown-menu">
            <Link className="dropdown-item" to="/user-profile">
              <FontAwesomeIcon icon={faUser} />
              <span> </span>
              View Profile
            </Link>
            <div className="dropdown-divider"></div>
            <Link className="dropdown-item" onClick={onLogout} to="/login">
              <FontAwesomeIcon icon={faSignOutAlt} />
              <span> </span>
              Log out
            </Link>
          </div>
        </li>
      </ul>
    );
  } else {
    contents = (
      <li className="nav-item mx-4">
        <Link data-testid="login-nav-button" className="nav-link" to="/login">
          Login/Register
          <span className="visually-hidden">(current)</span>
        </Link>
      </li>
    );
  }

  return (
    <>
      <nav
        data-testid="order-nav-id"
        className="navbar navbar-expand-lg navbar-dark bg-primary"
      >
        <div className="container-fluid">
          <Link data-testid="order-nav-title-id" className="navbar-brand" to="/">
            Order Management System
          </Link>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarColor01"
            aria-controls="navbarColor01"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarColor01">
            <ul className="navbar-nav me-auto">
              {contents}

              <li className="nav-item mx-4">
                <Link
                  data-testid="order-nav-link-id"
                  className="nav-link"
                  to="/about"
                >
                  About
                  <span className="visually-hidden">(current)</span>
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
};

export default Navbar;
