import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-toastify/dist/ReactToastify.css";
import "bootswatch/dist/lux/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";
import { AuthContext } from "./Contexts/AuthContext";
import { InactivityTimeout, UnprotectedRoutes } from "./Utils/Constants";
import IdleTimer from "./Utils/IdleTimer";
import { Router, Switch, Route, Redirect } from "react-router-dom";
import history from "./Utils/History";
import { useEffect, useState } from "react";
import { getInitialState } from "./Utils/PersistState";
import OrderList from "./Components/Order/OrderListApp/OrderList";
import AuthenticateService from "./Utils/AuthenticateService";
import OrderDetails from "./Components/Order/OrderDetails/OrderDetails";
import Form from "./Components/Order/OrderCaptureApp/Form";
import Navbar from "./Components/Navbar/Navbar";
import UserProfile from "./Components/UserProfile/UserProfile";
import EditForm from "./Components/UserProfile/EditForm";
import LoginForm from "./Components/Login-Register/LoginForm";
import EmailForm from "./Components/Login-Register/EmailForm";
import UpdatePasswordForm from "./Components/UserProfile/UpdatePasswordForm";
import { ToastContainer, toast, Slide } from "react-toastify";
import LogoutService from "./Utils/LogoutService";
import BulkUploadClient from './Components/BulkUploadClient/BulkUploadClient';
import OrderUpdateForm from './Components/Order/OrderUpdate/OrderUpdateForm';
import BrokerToggle from './Components/Admin/BrokerListApp/BrokerToggle';
import About from './Components/About/About';

function App() {
  document.body.style.backgroundColor = "#E0D5E3";
  const [userName, setUserName] = useState(null);
  const [role, setRole] = useState(null);

  useEffect(() => {
    let checkAuth = async () => {
      if (!UnprotectedRoutes.includes(history.location.pathname)) {
        try {
          let authenticateResponse = await AuthenticateService();
          if (!authenticateResponse.data.isAuthenticated) {
            //hit /refreshToken
            setUserName(null);
            setRole(null);
            LogoutService();
            history.push("/login");
          }
        } catch (err) {
          toast.warn("Something went wrong!");
          setUserName(null);
          setRole(null);
          LogoutService();
          history.push("/login");
        }
      }
    };
    checkAuth();

    setUserName(getInitialState("USER_NAME"));
    setRole(getInitialState("ROLE"));
    
    let onTimeout = () => {
      setUserName(null);
      setRole(null);
      LogoutService();
      history.push("/login");
    }

    let timer;
    if (!UnprotectedRoutes.includes(history.location.pathname)) {
      timer = new IdleTimer({
        timeout: InactivityTimeout, //expire after 3 minutes
        onTimeout: onTimeout,
        onExpired: () => {},
      });
    } else if (typeof timer !== "undefined") {
      timer.cleanUp();
    }

    let unlisten = history.listen((location) => {
      checkAuth();
      if (UnprotectedRoutes.includes(location.pathname) && typeof timer !== "undefined")
        timer.cleanUp();
      else {
        timer = new IdleTimer({
          timeout: InactivityTimeout, //expire after 3 minutes
          onTimeout: onTimeout,
          onExpired: () => {},
        });
      }
    });

    return () => {
      if (typeof timer !== "undefined")
        timer.cleanUp();
      unlisten();
    };
  }, []);

  return (
    <div className="app-home" style={{ width: "100%" }}>
      <AuthContext.Provider value={{ userName, setUserName, role, setRole }}>
        <Router history={history}>
          <Navbar />

          <Switch>
            <Route
              exact
              path="/"
              render={() => {
                return <Redirect to="/login"/>;
              }}
            />

            <Route exact path="/order-list" component={OrderList} />

            <Route exact path="/user-profile" component={UserProfile} />
            <Route excat path="/edit-user-profile" component={EditForm} />

            <Route exact path="/capture-order" component={Form} />
            <Route exact path="/order-details/:id" component={OrderDetails} />
            {/* <Route exact path="/order-details" component={OrderDetails}/> */}
            <Route exact path="/login" component={LoginForm} />
            <Route exact path="/forget-password-email" component={EmailForm} />
            <Route exact path="/update-password" component={UpdatePasswordForm} />
            <Route exact path="/order-update/:id" component={OrderUpdateForm}/>
            <Route exact path="/upload-clients" component={BulkUploadClient}/>
            <Route exact path="/about/"component={About}/>
           { (role === "ROLE_ADMIN" || role === "ROLE_Admin")?
            (<Route exact path="/broker-list/" component={BrokerToggle }/>) : (<></>) }
            
         


          </Switch>
        </Router>
        <ToastContainer
          position={toast.POSITION.TOP_RIGHT}
          autoClose={5000}
          transition={Slide}
        />
      </AuthContext.Provider>
    </div>
  );
}

export default App;
