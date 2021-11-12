import Properties  from '../../Utils/Properties.js';
import React from 'react';
import { left } from '@popperjs/core';
import OmsAxios from '../../Utils/OmsAxios.js';
import {getInitialState} from '../../Utils/PersistState.js';
import { ToastContainer, toast, Slide } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import './UserProfile.css';
import {faEye,faEyeSlash} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
var eyeclass =[faEye,faEyeSlash]
 

class UpdatePassword extends React.Component {

  
    constructor(props) {
    super(props);
    this.state = {
      input: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      errors: {},
      eye1:1,
      eye2:1,
      eye3:1
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
          "oldPassword": this.state.input["oldPassword"],
          "newPassword": this.state.input["newPassword"],
          "confirmPassword": this.state.input["confirmPassword"]
        }

          let user_id = getInitialState("USER_ID");
          OmsAxios.post(`${Properties.USER_SERVER_URL}${Properties.UPDATE_PASSWORD_API}`, object, {
          headers: {
            'userId' : user_id
          }
        }).then(
            response => {
              toast.success(response.data.message);
              try {
                this.props.history.push("/user-profile");
              } catch (e) {
                toast.error(e.message);
              }
              return response;})
          .catch(error => {
            toast.error(error.response?.data?.message || error.message);
          });
    
        
    }
  }
  
  validate(){
      let input = this.state.input;
      let errors = {};
      let isValid = true;
  
      if (!input["oldPassword"]) {
        isValid = false;
        errors["oldPassword"] = "Please enter old password.";
      }
  
      if (!input["newPassword"]) {
        isValid = false;
        errors["newPassword"] = "Please enter new password.";
      }

      if (input["oldPassword"]==input["newPassword"]) {
        isValid = false;
        errors["newPassword"] = "New Password should be different than old Password.";
      }

      if (!input["confirmPassword"]) {
        isValid = false;
        errors["confirmPassword"] = "Please enter confirm password.";
      }
  

      if (input["confirmPassword"]!=input["newPassword"]) {
        isValid = false;
        errors["confirmPassword"] = "Password does not match.";
      }
  
      this.setState({
        errors: errors
      });
  
      return isValid;
  }

  eye = (id)=>{
    let p = document.getElementById(id)
    if(id == "oldPassword"){
    this.setState({
      eye1:this.state.eye1^1
    })
    if(this.state.eye1==1){
     
      p.type='text'
     }
     else{
      p.type='password'
     }
  }
  if(id == "newPassword"){
    this.setState({
      eye2:this.state.eye2^1
    })
    if(this.state.eye2==1){
     
      p.type='text'
     }
     else{
      p.type='password'
     }
  }
  if(id == "confirmPassword"){
    this.setState({
      eye3:this.state.eye3^1
    })
    if(this.state.eye3==1){
     
      p.type='text'
     }
     else{
      p.type='password'
     }
  }
    
     
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
            }}>Update Password</div>
        <form onSubmit={this.handleSubmit} data-testid="update-password-form" style={{
            paddingTop: "2%",
            paddingBottom: "2%",
            paddingLeft: "5%",
            paddingRight: "5%"
        }}>

          <div class="center">
          <div class="col-md-4" class="left">
            
            <input 
              data-testid="input1"
              type="password" 
              name="oldPassword" 
              value={this.state.input.oldPassword}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter Old Password" 
              id="oldPassword" 
              required/>
              <FontAwesomeIcon icon={eyeclass[this.state.eye1]} style={{marginLeft: -30, marginTop: 15}} onClick={()=>{this.eye("oldPassword")}}  />
  
              <div data-testid="validation1"className="text-danger">{this.state.errors.oldPassword}</div>
          </div>
          <br/>
          <div class="col-md-4" class="left">
            
            <input 
              data-testid="input2"
              type="password" 
              name="newPassword" 
              value={this.state.input.newPassword}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter New Password" 
              id="newPassword"
              required />
              <FontAwesomeIcon icon={eyeclass[this.state.eye2]} style={{marginLeft: -30, marginTop: 15}} onClick={()=>{this.eye("newPassword")}}  />
   
              <div data-testid="validation2" className="text-danger">{this.state.errors.newPassword}</div>
          </div>
         
          <br/>
          <div class="col-md-4" class="center">
            
            <input 
              data-testid="input3"
              type="password" 
              name="confirmPassword" 
              value={this.state.input.confirmPassword}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Confirm Password" 
              id="confirmPassword" 
              required/>
              <FontAwesomeIcon icon={eyeclass[this.state.eye3]} style={{marginLeft: -30, marginTop: 15}} onClick={()=>{this.eye("confirmPassword")}}  />
   
              <div data-testid="validation3" className="text-danger">{this.state.errors.confirmPassword}</div>
          </div>
          </div>
          
          <br/>
            <p class="center-red">
              *Password must contain 8 - 16 characters<br/>
              *Password must contain atleast 1 numeric character<br/>
              *Password must contain atleast 1 special character<br/>
              *Password must contain atleast 1 uppercase<br/>
              *Password must contain atleast 1 lowercase<br/>
            </p>
         
          <input data-testid="section2-btn"
          type="submit" value="Update Password"
          className="btn btn-success rounded" id="button_1" />
        </form>
      </div>
      </div>
      </div>
    );
  }
}
  
export default UpdatePassword;