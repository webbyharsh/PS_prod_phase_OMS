import React from "react";
import axios from "axios";
import sample from "../../samplefile/sample.csv";
import fileDownload from "js-file-download";
import { ToastContainer, toast, Slide } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import "./Bulkuploadstyle.css";
import Properties from "../../Utils/Properties";
import OmsAxios from "../../Utils/OmsAxios";


class BulkUploadClient extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      file: null,
      isModalOpen: false,
    };
    this.onFormSubmit = this.onFormSubmit.bind(this);
    this.onChange = this.onChange.bind(this);
    this.fileUpload = this.fileUpload.bind(this);
    this.handleDownload = this.handleDownload.bind(this);


  }


  onFormSubmit() {
    console.log("file uploading start");
    this.fileUpload(this.state.file)
      .then((response) => {
        console.log("axios get executed successfully");
      
        var message = "";
        if (response.data.total === response.data.successful) {
          message += "data saved successfully";

        }
        
        toast.success(message);
       
      })
      .catch((err) => {

        if(err.message === 'Request failed with status code 415')
        {
          console.log("failed upload file not csv");
          toast.error("The given file is not of CSV Format");
          return;
        }
         console.log("error in uploading file");
        var message = "";
        message += "Upload failed. There are errors in the file. \n";
        if (err.response.data.errors.csvErrors.length !== 0) {
          message += "These rows have missing values:\n";
          err.response.data.errors.csvErrors.map((erre) => {
            return (message += " " + erre + ", ");
          });
        }
        if ( 
          Object.keys(err.response.data.errors.csvDuplicateEmailIdErrors)
            .length !== 0
        ) {
          message += "\n duplicate emailId exists:       \n";
		      message += JSON.stringify(err.response.data.errors.csvDuplicateEmailIdErrors);
        }
        if (err.response.data.errors.emailIdErrors.length !== 0) {
          message += "\n Email format is not right in following rows: \n";
          err.response.data.errors.emailIdErrors.map((erre) => {
            return (message += " " + erre + ", ");
          });
        }
        if (err.response.data.errors.emailIdExistsErrors.length !== 0) {
          message += "\n Clients with the email addresses in these rows already exist: \n ";
          err.response.data.errors.emailIdExistsErrors.map((erre) => {
            return (message += " " + erre + ", ");
          });
        }
        if (err.response.data.errors.contactNumberErrors.length !== 0) {
          message += "Wrong contact number format in rows: \n";
          err.response.data.errors.contactNumberErrors.map((erre) => {
            return (message += " " + erre + ", ");
          });
         
        }
        message += "\nPlease check the file and try again";
       
        console.error("error message"+message);
        toast.error(message);
      
      });
  }
  onChange(e) {
    console.log("onChange called");
    this.setState({ file: e.target.files[0] });
  }
  fileUpload(file) {
    
    const url = `${Properties.USER_SERVER_URL}/client/upload-csv`;
    const formData = new FormData();
    formData.append("file", file);
    const config = {
      headers: {
        "content-type": "multipart/form-data",
        
      }
    };
    console.log("axio request about to get called");
    return OmsAxios.post(url, formData, config);
  }

  handleDownload = (url, filename) => {
    axios
      .get(url, {
        responseType: "blob",
      })
      .then((res) => {
        fileDownload(res.data, filename);
       
      })
      .catch((err) =>{
        console.error(err);
      })
  };

 

  render() {
    return (
      <div>
        <ToastContainer
          position={toast.POSITION.TOP_RIGHT}
          autoClose={-1}
          transition={Slide}
        />



        <div className="row">
          <div className="col-md-6 offset-md-3">
            <br />
            <br />
            <h5 className="text-black">
              Upload Client Details in the specified CSV format.
            </h5>
            <br />

            <div className="bulk-container">

              <div className="form-row">
                <div className="form-group col-md-4">
                  <input
                    type="file"
                    name="upload_file"
                    style={{width:"300px"}}
                    className="form-control"
                    accept=".csv,text/csv"
                    onChange={this.onChange}
                    data-testid = "upload_file_input"
                  />
                </div>
              </div>
              <br></br>


              <div className="form-row">
                <div className="col-md-6">
                  <button
                    type="submit"
                    className="btns"
                    onClick={() => this.onFormSubmit()}
                    data-testid = "button_submit"
                    aria-label="button_submit"
                  >
                    Upload
                  </button>
                </div>
              </div>

            </div>
            <br></br>

            <p>Not sure about the correct file upload format?   </p>

            <div className="form-row">
              <div className="col-md-6">
                <button
                  className="btns"
                  aria-label="download_format"
                  onClick={() => {
                    this.handleDownload(sample, sample);
                  }}
                >
                  Download CSV FILE Template  {" "}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default BulkUploadClient;