import { defineFeature, loadFeature } from "jest-cucumber";
import {
  render,
  screen,
  fireEvent,
  userEvent,
  cleanup,
  waitFor,
  getDefaultNormalizer,
} from "@testing-library/react";
import BulkUploadClient from "../../Components/BulkUploadClient/BulkUploadClient";

import axios from "axios";

import testFile from "../../samplefile/testFile.csv";
import testWrong from "../../samplefile/testWrong.csv";
import sample from "../../samplefile/sample.csv";
import OmsAxios from "../../Utils/OmsAxios";

const feature = loadFeature("src/__tests__/features/BulkClientUpload.feature");


defineFeature(feature, (test) => {
  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test("Client data uploaded successfully", ({ given, when, then, and }) => {
    var sampleFile;
    let errorSpy = jest.spyOn(console, "error").mockImplementation((err) => console.log(err));
    given("i have valid client data", () => {
      sampleFile = testFile;

      OmsAxios.post = jest.fn(() => {
        return Promise.resolve({
          data: [
            {
              message: "1 Rows Saved And 0 Failures",
              total: 1,
              successful: 1,
              failures: null,
            }    
          ],
          ok: true, 
        });
      });
   
    });
    and("my bulk upload page has loaded", () => {
      render(<BulkUploadClient />);
    });
    when("i select my file and submit", () => {
      fireEvent.change(screen.getByTestId(/upload_file_input/), {
        target: { file: sampleFile },
      });
      fireEvent.click(screen.getByLabelText(/button_submit/));
    });
    then("the response should be as i expect", async () => {
      await waitFor(() => {
        expect(errorSpy).not.toHaveBeenCalled();
      });
    });
  });




  test("Client invalid data not uploaded successfully", ({ given, when, then, and }) => {
    var sampleFile;
    let errorSpy = jest.spyOn(console, "error").mockImplementation((err) => console.log(err));
    given("i have invalid client data", () => {
      sampleFile = testWrong;

      OmsAxios.post = jest.fn(() => {
      
        return Promise.reject({
         response :{
          data: 
            {
              message: "The Given CSV File Contains Errors",
              total: 1,
              validRowCount: 0,
              errorRowCount: 1,
              errors: {
                csvErrors: [1],
                emailIdErrors: [1],
                emailIdExistsErrors: [1],
                contactNumberErrors: [1],
                csvDuplicateEmailIdErrors: {},
              },
            },
          
         }
        });
     });
   
    });
    and("my bulk upload page has loaded", () => {
      render(<BulkUploadClient />);
    });
    when("i select my file and submit", () => {
      fireEvent.change(screen.getByTestId(/upload_file_input/), {
        target: { file: sampleFile },
      });
      
      fireEvent.click(screen.getByLabelText(/button_submit/));
    });
    then("the response should be as i expect", async () => {
      
      await waitFor(() => {
        
        expect(errorSpy).toHaveBeenCalled();
      });
    });
  });


  test("To download the format of samplefile", ({ given, when, then, and }) => {
    var sampleFile;
    let errorSpy = jest.spyOn(console, "error").mockImplementation((err) => console.log(err));
    given("sample file stored in local storage", () => {
      sampleFile = sample;  
      
      axios.get = jest.fn(() => {
       
        return Promise.reject({
         response :{
          data: 
            {
              file:sampleFile
            }
          
         }
        });
     });


    });
    and("my bulk upload page has loaded", () => {
      render(<BulkUploadClient />);
    });
    when("click the download format button", () => {
      fireEvent.click(screen.getByLabelText(/download_format/));
    });
    then("file get downloaded", async () => {
      await waitFor(() => {
        expect(errorSpy).toHaveBeenCalled();
      });
    });
  });






});
