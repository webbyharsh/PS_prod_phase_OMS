import './About.css';

const About =()=> {
   return (
        <div className="container mt-5"id="aboudContainer">
        <div class="row position-relative overflow-hidden text-center aboutRow">
        <div class="col-md-5 p-lg-5 mx-auto my-5 text">
          <h1 class="display-4 font-weight-normal">About</h1>
          <p class="lead font-weight-normal">An Order Management Web Application to aid a broker in the whole
            journey of making trade orders, from capturing the order to
            receiving the fulfillment information from the exchange, for clients
            associated with the organisation.
          </p>
          <a class="btn btn-outline-secondary" >Developed by ASDE Batch 3</a>
        </div>
        <div class="circle-device box-shadow d-none d-md-block"></div>
        <div class="circle-device circle-device-3 box-shadow d-none d-md-block"></div>
        <div class="circle-device circle-device-2 box-shadow d-none d-md-block"></div>
        <div class="circle-device circle-device-4 box-shadow d-none d-md-block"></div>
      </div>
      </div>
   

    );
};
    
export default About;