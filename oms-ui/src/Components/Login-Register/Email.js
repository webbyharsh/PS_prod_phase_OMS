import Properties  from '../../Utils/Properties.js';
import React from 'react';
import { left } from '@popperjs/core';
import OmsAxios from '../../Utils/OmsAxios.js';
import { ToastContainer, toast, Slide } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
  
class Email extends React.Component {
    constructor(props) {
    super(props);
    this.state = {
      input: {
        email: ""
      },
      errors: {}
    };
     
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }
     
  handleChange(event) {
    let input = this.state.input;
    input[event.target.name] = event.target.value;
  
    this.setState({
      input
    });
  }
    
  async handleSubmit(event) {
    event.preventDefault();

    if(this.validate()){
        let object = {
          "email": this.state.input["email"]
        }

        
         OmsAxios.post(`${Properties.USER_SERVER_URL}${Properties.FORGOT_PASSWORD_API}?emailId=${this.state.input["email"]}`,
    
         )
         .then(
            response => {
              toast.success(response.data.message);
              return response;})
          .catch(error => {
            toast.error(error.response?.data?.message || error.message);;
          });
    
        try {
          this.props.history.push("/login");
        } catch (e) {
          toast.error(e.message);
        }
    }
  }
  
  validate(){
      let input = this.state.input;
      let errors = {};
      let isValid = true;
  
  
      if (!input["email"]) {
        isValid = false;
        errors["email"] = "Please enter email.";
      }

      this.setState({
        errors: errors
      });
  
      return isValid;
  }
     
  render() {
    return (
      <div>
        <ToastContainer
          position={toast.POSITION.TOP_RIGHT}
          autoClose={10000}
          transition={Slide}
        />
        <div data-testid="outer-div-section2" style={{
            backgroundColor: "#fff",
            textAlign: 'center'
        }}>
        <div data-testid="inner-div-section2" className="list-group">
            <div className="list-group-item list-group-item-action active" style={{
                textAlign: left
            }}>Forget Password</div>
        <form onSubmit={this.handleSubmit} data-testid="email-form" style={{
            paddingTop: "2%",
            paddingBottom: "2%",
            paddingLeft: "5%",
            paddingRight: "5%"
        }}>
  
  
          <div className="form-group">
            <label htmlFor="email" style={{fontWeight: "bold"}}
            >Email:</label>
            <input
              data-testid="input1" 
              type="email" 
              name="email" 
              value={this.state.input.newPassword}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter Email ID" 
              id="email" 
              required/>
   
              <div data-testid="validation2" className="text-danger">{this.state.errors.email}</div>
          </div>

          <br></br>
          <input data-testid="section2-btn"
          type="submit" value="Send Email"
          className="btn btn-success rounded" id="button_1" />
        </form>
      </div>
      </div>
      </div>
    );
  }
}
  
export default Email;