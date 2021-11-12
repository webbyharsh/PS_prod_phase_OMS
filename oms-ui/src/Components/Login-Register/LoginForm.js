import "./LoginForm.css";
import React from "react";
import { withRouter,Link } from "react-router-dom";
import * as yup from "yup";
import OmsAxios from "../../Utils/OmsAxios";
import Properties from "../../Utils/Properties";
import { AuthContext } from "../../Contexts/AuthContext";
import { persistState } from "../../Utils/PersistState";
import AuthenticateService from "../../Utils/AuthenticateService";
import AuthorizeService from "../../Utils/AuthorizeService";
import { toast } from "react-toastify";
import { faEye,faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
var eyeclass = [faEyeSlash, faEye];

//Keep loginSchema and registerSchema separate for separate validations
const loginSchema = yup.object().shape({
  email: yup.string().email("Invalid Email Format").required("Required"),
  password: yup.string().required("Required"),
 
});


const signinSchema = yup.object().shape({
  name: yup.string().required("Required"),
  email: yup.string().email("Invalid Email Format").required("Required"),
  password: yup.string()
  .required('Required') 
  .min(8, 'Password is too short - should contains 8 chars minimum.')
  .matches(
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/,
  "Must contain One Uppercase, One Lowercase, One Number and One Special Case Character"
),
 age: yup.number().required().positive().integer().typeError("must be number"),
  contactNumber:  yup.string().required("required").matches(/^[0-9]\d{9}$/,"Enter valid phone number")
});

const initialState = {
  name: "",
  email: "",
  age: "",
  password: "",
  contactNumber: "",
  address: {
    street: "",
    city: "",
    state: "",
    country: "",
  }
};

class LoginForm extends React.Component {
  static contextType = AuthContext;

  constructor(props) {
    super(props);
    this.state = {
      name: "",
      email: "",
      age: "",
      password: "",
      contactNumber: "",
      address: {
        street: "",
        city: "",
        state: "",
        country: "",
      },
      eye1: 0,
      contactNumberError: null,
      emailError: null,
      passwordError: null,
      ageError: null,
      nameError: null,
      message: "",
      successful: false,
      isRegistering: false
    };



    this.handleValidateBlurLogin = this.handleValidateBlurLogin.bind(this);
    this.handleValidateBlurSignin = this.handleValidateBlurSignin.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onSubmitLogin = this.onSubmitLogin.bind(this);
    this.onSubmitRegister = this.onSubmitRegister.bind(this);
  }




  handleValidateBlurLogin = (e) => {
    let schemaSubset = yup.reach(loginSchema, e.target.name);
    schemaSubset.validate(e.target.value)
      .then((value) => {
        this.setState({
          [`${e.target.name}Error`]: null
        })
      })
      .catch((err) => {
        console.error(`${err.name}: ${err.errors} ${err.type}`);

        this.setState({
          [`${e.target.name}Error`]: err.errors[0],
        });
      });
  };

  onSubmitLogin = (e) => {
    e.preventDefault();
    loginSchema
      .validate(
        { email: this.state.email, password: this.state.password },
        { abortEarly: false }
      ).then(async (validValues) => {
        try {
          let response = await OmsAxios.post(
            `${Properties.AUTH_SERVER_URL}${Properties.GENERATE_TOKEN_API}`,
            {
              username: this.state.email,
              password: this.state.password,
            },
            {
              withCredentials: true,
            }
          );
          persistState("ACCESS_TOKEN", response.data.jwtAccessToken);
          try {
            let authenticateResponse = await AuthenticateService();
            this.context.setUserName(authenticateResponse.data.username); //Fetch name from database using userId and change
            persistState("USER_NAME", authenticateResponse.data.username);
            persistState("USER_ID", authenticateResponse.data.userId);
          } catch (error) {
            toast.warn("Tokens obtained but Authentication Failed");
          }
          try {
            let authorizeResponse = await AuthorizeService();
            this.context.setRole(authorizeResponse.data.listRoles[0]); //Obtained from /authorize end point
            persistState("ROLE", authorizeResponse.data.listRoles[0]);
          } catch (error) {
            toast.warn("Authenticated but Authorization Failed");
          }
          this.props.history.push("/capture-order");
        } catch(error) {
          const resMessage = error.response?.data?.message || error.toString();
              toast.warn(resMessage)
              this.setState({
                message: resMessage,
              });
        }
      })
      .catch((allErrors) => {
        allErrors.inner.forEach((err) => {
          console.error(`${err.name}: ${err.errors} ${err.type}`);
          this.setState({
            [`${err.path}Error`]: err.errors.length > 0 ? err.errors[0] : null,
          });
        });
      })
  };

  handleValidateBlurSignin = (e) => {
    let schemaSubset = yup.reach(signinSchema, e.target.name);
    schemaSubset.validate(e.target.value).catch((err) => {
      console.error(`${err.name}: ${err.errors} ${err.type}`);

      this.setState({
        [`${e.target.name}Error`]: err.errors[0],
      });
    });
  };

  onChange = (e) => {
    this.setState({
      [e.target.name]: e.target.value,
      [`${e.target.name}Error`]: null,
    });
  };
  onAddressChange = (e) => {
    if (e.target.name === "street") {
      this.setState({
        address: {
          ...this.state.address,
          street: e.target.value,
        },
      });
    } else if (e.target.name === "city") {
      this.setState({
        address: {
          ...this.state.address,
          city: e.target.value,
        },
      });
    } else if (e.target.name === "state") {
      this.setState({
        address: {
          ...this.state.address,
          state: e.target.value,
        },
      });
    } else if (e.target.name === "country") {
      this.setState({
        address: {
          ...this.state.address,
          country: e.target.value,
        },
      });
    }
  };

  onSubmitRegister = (e) => {
    e.preventDefault();

    signinSchema
      .validate(
        {
          name: this.state.name,
          password: this.state.password,
          email: this.state.email,
          age: this.state.age,
          contactNumber: this.state.contactNumber,
        },
        { abortEarly: false }
      ).then(async (validValues) => {
        try {
          let response = await OmsAxios.post(`${Properties.USER_SERVER_URL}${Properties.REGISTER_API}`, {
            name: this.state.name,
            emailId: this.state.email,
            password: this.state.password,
            age: this.state.age,
            contactNumber: this.state.contactNumber,
            address: this.state.address,
          });
          console.log("successfull mail sent==", response.data?.message || response.message);
          if (response)
            this.setState({
              message: response.data?.message || response.message,
              successful: true,
            });
          if (response.status === 202) {
            toast.success(response.data?.message || response.message);
            this.setState({ 
              ...initialState,
              isRegistering: false
            });
          }
          else
            toast.error(response.data?.message || response.message);
        } catch (error) {
          const resMessage = error.response?.data?.message || error.toString();
          this.setState({
            message: resMessage,
            successful: false,
          });
          toast.error(this.state.message);
        }
      })
      .catch((allErrors) => {
        allErrors.inner.forEach((err) => {
          console.error(`${err.name}: ${err.errors} ${err.type}`);
          this.setState({
            [`${err.path}Error`]: err.errors.length > 0 ? err.errors[0] : null,
          });
        });
      });
  };


  togglepassword = () => {
    this.setState((oldstate) => {

      return {
        ...oldstate,
        eye1: 1 ^ oldstate.eye1,
      }
    })
  }


  render() {

    return (
      <div className="loginCustomBody">
        <div className={this.state.isRegistering ?
          "login-container right-panel-active" :
          "login-container"} id="container" data-testid="login-container">
          <div className="form-container sign-up-container">
            <form
              id="signup-form-css"
              className={this.state.successful ? "is-hidden" : ""}
              onSubmit={this.onSubmitRegister}
              data-testid="signup-form"
            >
              <h3 style={{ width: "200%" }}>Create Account</h3>
              <div className="input-container">
                  <input
                    id="login-input"
                    className="form-control"
                    name="name"
                    type="text"
                    placeholder="Name"
                    value={this.state.name}
                    onBlur={this.handleValidateBlurSignin}
                    onChange={this.onChange}
                    required
                    data-testid="signin-name"

                  />
                  {this.state.nameError ? (
                    <span style={{ color: "red" }} aria-label="name_error">
                      {this.state.nameError}
                    </span>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="input-container">
                  <input
                    id="login-input"
                    className="form-control"
                    name="email"
                    type="email"
                    placeholder="Email"
                    value={this.state.email}
                    onBlur={this.handleValidateBlurSignin}
                    onChange={this.onChange}
                    required
                    data-testid="signin-email"
                  />
                  {this.state.emailError ? (
                    <span style={{ color: "red" }} aria-label="email_error">
                      {this.state.emailError}
                    </span>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="input-container">
                  <input
                    id="login-input"
                    className="form-control"
                    name="password"
                    type={this.state.eye1 === 1 ? "text" : "password"}
                    placeholder="Password"
                    value={this.state.password}
                    onBlur={this.handleValidateBlurSignin}
                    onChange={this.onChange}
                    required
                    data-testid="signin-password"
                  />

                  <span className="eye-icon">
                    <FontAwesomeIcon icon={eyeclass[this.state.eye1]} onClick={() => { this.togglepassword() }} />
                  </span>

                  {this.state.passwordError ? (
                    <div style={{ color: "red"}} aria-label="password_error">
                      {this.state.passwordError}
                    </div>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="input-container">
                  <input
                    id="login-input"
                    className="form-control"
                    name="contactNumber"
                    type="text"
                    placeholder="Contact Number"
                    value={this.state.contactNumber}
                    onBlur={this.handleValidateBlurSignin}
                    onChange={this.onChange}
                    required
                    data-testid="signin-contactNumber"
                  />
                  {this.state.contactNumberError ? (
                    <span style={{ color: "red" }} aria-label="contactNumber_error">
                      {this.state.contactNumberError}
                    </span>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="input-container">
                  <input
                    id="login-input"
                    type="number"
                    className="form-control"
                    placeholder="Age"
                    name="age"
                    value={this.state.age}
                    onBlur={this.handleValidateBlurSignin}
                    onChange={this.onChange}
                    required
                    data-testid="signin-age"
                  />
                  {this.state.ageError ? (
                    <span style={{ color: "red" }} aria-label="age_error">
                      {this.state.ageError}
                    </span>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="input-container-2">
                  <input
                    id="login-input-address"
                    className="form-control"
                    name="street"
                    type="text"
                    placeholder="Street"
                    value={this.state.address.street}
                    onChange={this.onAddressChange}
                    required
                    data-testid="signin-street"
                  />

                  <input
                    id="login-input-address"
                    className="form-control"
                    name="city"
                    type="text"
                    placeholder="city"
                    value={this.state.address.city}
                    onChange={this.onAddressChange}
                    required
                    data-testid="signin-city"
                  />
                </div>

                <div className="input-container-2">
                  <input
                    id="login-input-address"
                    className="form-control"
                    name="state"
                    type="text"
                    placeholder="State"
                    value={this.state.address.state}
                    onChange={this.onAddressChange}
                    required
                    data-testid="signin-state"
                  />

                  <input
                    id="login-input-address"
                    className="form-control"
                    name="country"
                    type="country"
                    placeholder="Country"
                    value={this.state.address.country}
                    onChange={this.onAddressChange}
                    required
                    data-testid="signin-country"
                  />
                </div>

              <button className="btn btns btn-primary" aria-label="signup_button" data-testid="signup-button">Sign Up</button>
            </form>
          </div>

          <div className="form-container sign-in-container">
            <form
              onSubmit={this.onSubmitLogin}
              data-testid="login-form"
              id="login-form-css"
            >

              <h3>Log in</h3>

              <input
                id="login-input"
                type="email"
                name="email"
                className="form-control"
                placeholder="Email"
                value={this.state.email}
                onChange={this.onChange}
                onBlur={this.handleValidateBlurLogin}
                data-testid="email-input"
              />
              {this.state.emailError ? (
                <p style={{ color: "red" }} aria-label="email error">
                  {this.state.emailError}
                </p>
              ) : (
                <></>
              )}
              <input
                id="login-input"
                type="password"
                name="password"
                className="form-control"
                placeholder="Password"
                value={this.state.password}
                onChange={this.onChange}
                onBlur={this.handleValidateBlurLogin}
                data-testid="password-input"
              />
              {this.state.passwordError ? (
                <span style={{ color: "red" }} aria-label="password error">
                  {this.state.passwordError}
                </span>
              ) : (
                <></>
              )}
              <Link to="/forget-password-email">Forgot your password?</Link>
              <button
                className="btn btns btn-primary"
                aria-label="sign-in button"
              >
                Log In
              </button>
            </form>
          </div>
          <div className="overlay-container">
            <div className="overlay">
              <div className="overlay-panel overlay-left">
                <h1>Buy and Sell Stocks!</h1>
                <button
                  className="ghost btn btn-primary"
                  id="signIn"
                  aria-label="login_button"
                  onClick={(e) => this.setState(oldValue => {
                    e.preventDefault();
                    e.stopPropagation();
                    return { isRegistering: !oldValue.isRegistering }
                  })}
                >
                  Log In
                </button>
              </div>
              <div className="overlay-panel overlay-right">
                <div>
                  <h1>Buy and Sell Stocks!</h1>
                </div>

                <button
                  className="ghost btn btn-primary"
                  id="signUp"
                  aria-label="sign up button"
                  onClick={(e) => this.setState(oldValue => {
                    e.preventDefault();
                    e.stopPropagation();
                    return { isRegistering: !oldValue.isRegistering }
                  })}
                >
                  Sign Up
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(LoginForm);