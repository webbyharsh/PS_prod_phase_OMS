import Properties  from '../../Utils/Properties.js';
import React from 'react';
import { left } from '@popperjs/core';
import OmsAxios from '../../Utils/OmsAxios.js';
  
class EditProfileHelper extends React.Component {
    constructor(props) {
    super(props);
    this.state = {
      input: {
        name: this.props.location.state.finalUsers.name,
        age: this.props.location.state.finalUsers.age,
        contact: this.props.location.state.finalUsers.contact,
        street: this.props.location.state.finalUsers.address.street,
        city: this.props.location.state.finalUsers.address.city,
        state: this.props.location.state.finalUsers.address.state,
        country: this.props.location.state.finalUsers.address.country
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
          "userId": this.props.location.state.finalUsers.userId,
          "name": this.state.input["name"],
          "address": {
            "street": this.state.input["street"],
            "city": this.state.input["city"],
            "state": this.state.input["state"],
            "country": this.state.input["country"],
          },
          "contact": this.state.input["contact"],
          "emailId": this.props.location.state.finalUsers.emailId,
          "age": this.state.input["age"]
        }
        await OmsAxios.put(`${Properties.USER_SERVER_URL}${Properties.USER_DETAILS_UPDATE_API}`, object, {
          headers: {
          }
        }).then(
            response => {  alert("Sucessfully Updated!"); console.log(response); return response;})
          .catch(error => { console.log(object); console.log(error); alert(`User profile update failed! ${error.message}`);   });
        try {  this.props.history.push("/user-profile");} catch (e) {alert(`User profile update failed! ${e.message}`);  }
    }
  }
  
  validate(){
      let input = this.state.input;
      let errors = {};
      let isValid = true;

      if(input["name"].length > 150){
        isValid = false;
        errors["name"] = "Name length should be less than 150.";
      }
  
      if (!input["name"]) {
        isValid = false;
        errors["name"] = "Please enter your name.";
      }
  
      if (!input["age"] || input["age"]<18 || input["age"]>120) {
        isValid = false;
        errors["age"] = "Please enter valid age input";
      }

      var phoneno = /^\d{10}$/ ;
      if (!input["contact"].match(phoneno)) {
        isValid = false;
        errors["contact"] = "Please enter valid contact.";
      }

      if (!input["contact"]) {
        isValid = false;
        errors["contact"] = "Please enter your contact.";
      }
  
      if(input["street"].length > 150){
        isValid = false;
        errors["street"] = "Street length should be less than 150.";
      }

      if (!input["street"]) {
        isValid = false;
        errors["street"] = "Please enter street";
      }

      if(input["city"].length > 50){
        isValid = false;
        errors["city"] = "City length should be less than 50.";
      }

      if (!input["city"]) {
        isValid = false;
        errors["city"] = "Please enter city";
      }

      if(input["state"].length > 50){
        isValid = false;
        errors["state"] = "State length should be less than 50.";
      }

      if (!input["state"]) {
        isValid = false;
        errors["state"] = "Please enter state";
      }

      if(input["country"].length > 50){
        isValid = false;
        errors["country"] = "Country length should be less than 50.";
      }

      if (!input["country"]) {
        isValid = false;
        errors["country"] = "Please enter country";
      }
  
      this.setState({
        errors: errors
      });
  
      return isValid;
  }
     
  render() {
    return (
        <div data-testid="outer-div-section2" style={{
            backgroundColor: "#fff",
            textAlign: 'center'
        }}>
        <div data-testid="inner-div-section2" className="list-group">
            <div className="list-group-item list-group-item-action active" style={{
                textAlign: left
            }}>Edit User Details</div>
        <form onSubmit={this.handleSubmit} data-testid="edit-profile-form" style={{
            paddingTop: "2%",
            paddingBottom: "2%",
            paddingLeft: "6%",
            paddingRight: "6%"
        }}>
  
          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="name" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >Name:</label>
            <input 
              type="text" 
              name="name" 
              value={this.state.input.name}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter name" 
              id="name" />
  
              <div data-testid="validation1"className="text-danger">{this.state.errors.name}</div>
          </div>
  
          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="age" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >Age:</label>
            <input 
              type="number" 
              name="age" 
              value={this.state.input.age}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter Age" 
              id="age" />
   
              <div data-testid="validation2" className="text-danger">{this.state.errors.age}</div>
          </div>

          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="contact" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >Contact:</label>
            <input 
              type="text" 
              name="contact" 
              value={this.state.input.contact}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter Contact" 
              id="contact" />
   
              <div data-testid="validation3" className="text-danger">{this.state.errors.contact}</div>
          </div>

          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="street" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >Street:</label>
            <input 
              type="text" 
              name="street" 
              value={this.state.input.street}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter street" 
              id="street" />
              
              <div data-testid="validation4" className="text-danger">{this.state.errors.street}</div>
          </div>

          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="city" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >City:</label>
            <input 
              type="text" 
              name="city" 
              value={this.state.input.city}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter city" 
              id="city" />
   
              <div data-testid="validation5" className="text-danger">{this.state.errors.city}</div>
          </div>

          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="state" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >State:</label>
            <input 
              type="text" 
              name="state" 
              value={this.state.input.state}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter state" 
              id="state" />
   
              <div data-testid="validation6" className="text-danger">{this.state.errors.state}</div>
          </div>

          <div className="col-md-12 form-group form-inline" id="form-group-1">
            <label htmlFor="country" 
              class="col-sm-2 col-form-label" 
              style={{fontWeight: "bold"}}
            >Country:</label>
            <input 
              type="text" 
              name="country" 
              value={this.state.input.country}
              onChange={this.handleChange}
              className="form-control" 
              placeholder="Enter country" 
              id="country" />
   
              <div data-testid="validation7" className="text-danger">{this.state.errors.country}</div>
          </div>

          <br></br>
          <input data-testid="section2-btn"
          type="submit" value="Submit"
          className="btn btn-success rounded" id="button_1" />
        </form>
      </div>
      </div>
    );
  }
}
  
export default EditProfileHelper;