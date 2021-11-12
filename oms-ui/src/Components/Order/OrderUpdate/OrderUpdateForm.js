import React from "react";
//import "./OrderUpdateForm.css";
import "../OrderCaptureApp/Form.css"
import SearchBox from "../OrderCaptureApp/SearchBox";
import SendOrderService from '../../../Utils/SendOrderService';
import * as yup from "yup";
import OmsAxios from "../../../Utils/OmsAxios";
import axios from "axios";
import Properties from "../../../Utils/Properties";
import { toast } from "react-toastify";
import getData from "../OrderDetails/getData";
// import { Router } from "react-router-dom";
import { withRouter } from "react-router";
import { Link } from 'react-router-dom'
import StockList from "../../../Utils/StockList";

const orderSchema = yup.object().shape({
  stockName: yup.string().required("Required"),
  stockQuantity: yup
    .number()
    .integer("Must be an integer")
    .min(1, "Quantity must be between [1,1000]")
    .max(1000, "Quantity must be between [1,1000]")
    .required("Required"),
  side: yup
    .string()
    .oneOf(["buy", "sell", "Buy", "Sell", "BUY", "SELL"], "Invalid side")
    .required("Required"),
  type: yup
    .string()
    .oneOf(["market", "limit", "Market", "Limit", "MARKET", "LIMIT"], "Invalid type")
    .required("Required"),
  exchange: yup
    .string()
    .oneOf(["bse", "nse", "NSE", "BSE", "Nse", "Bse"], "Invalid exchange")
    .required("Required"),
  clientID: yup.string().not(["default"], "Required").required("Required"),
  price: yup.number().optional(),
});

class OrderUpDateForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      stockName: "",
      stockQuantity: 0,
      side: "",
      type: "",
      exchange: "nse",
      clientID: "",
      targetprice: 0,
      stockPrice: 1,
      price: 0,
      status: "",
      clients: [],
      stocks: [],
      stockNameError: null,
      stockQuantityError: null,
      sideError: null,
      typeError: null,
      exchangeError: null,
      clientIDError: null,
      priceError: null,
      error: false,
      isAvailableToSend: false,
      isActive: true,
    }
    this.change = this.change.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleValidateOnBlur = this.handleValidateOnBlur.bind(this);
  }

  sendOrderService = new SendOrderService(null);

  componentDidMount() {
    let orderid = this.props.match.params.id;
    getData(orderid)
      .then((data) => {
        //  console.log(data.data);
        this.setState({
          stockName: data.data.stock,
          stockQuantity: data.data.quantity,
          side: data.data.side,
          type: data.data.type,
          targetPrice: data.data.targetPrice,
          // added this one for price and targetprice naming errors.
          price: data.data.targetPrice,
          stockPrice: data.data.stockPrice,
          status: data.data.status,
          isActive: data.data.isActive,
          clientID: data.data.clientId,
        })
      });

  }

  change = (e) => {
    if (e.target.name === "type" && e.target.value === "market")
      this.setState({
        price: undefined,
        priceError: null,
      });
    this.setState({
      [e.target.name]: e.target.value,
      [`${e.target.name}Error`]: null,
    });
  };

  /** Utility function to check for status of validation errors
   * Depends on name of error variables to end with `Error`
   */
  noErrors = () => {
    let result = true;
    let regExp = /Error$/;
    for (let key in this.state) {
      if (regExp.test(key) && this.state[key] !== null) result = false;
    }
    return result;
  };

  onSubmit = async (e) => {
    e.preventDefault();
    orderSchema
      .validate(
        {

          stockName: this.state.stockName,
          stockQuantity: this.state.stockQuantity,
          side: this.state.side,
          type: this.state.type,
          exchange: this.state.exchange,
          clientID: this.state.clientID,
          price: this.state.price,
        },
        { abortEarly: false }
      )
      .catch((errors) => {
        errors.inner.forEach((err) => {
          console.error(`${err.name}: ${err.errors} ${err.type} ${err.path}`);
          if (
            err.type &&
            err.path &&
            err.type === "typeError" &&
            ["stockQuantity", "price"].includes(err.path)
          ) {
            this.setState({
              [`${err.path}Error`]: "Required",
            });
          } else {
            this.setState({
              [`${err.path}Error`]:
                err.errors.length > 0 ? err.errors[0] : null,
            });
          }
        });
      })
      .finally(async () => {
        let noErrors = this.noErrors();
        if (noErrors === true) {
          const body = {
            clientId: this.state.clientID,
            quantity: this.state.stockQuantity,
            stock: this.state.stockName,
            stockPrice: this.state.stockPrice,
            //naming errors, edited this, target price and price.
            targetPrice: this.state.price,
            isActive: this.state.isActive,
            status: this.state.status.toUpperCase(),
            side: this.state.side.toUpperCase(),
            type: this.state.type.toUpperCase(),

          };
          if (body.type === "market" || body.type === "MARKET") {
            body.targetPrice = null;
          }
          try {
            //   console.log(body);

            let response = await OmsAxios.put(
              `${Properties.ORDER_SERVER_URL}/order/${this.props.match.params.id}`,
              body,
            );

            if (response.created) {
              // alert("Your order has been submitted successfully");
              this.props.history.push(
                "/order-details/" + response.data.orderId
              );
              toast.success("Order Updated Successfully");
            } else {
              toast.warn("Something went wrong!");
            }
          } catch (err) {
            toast.warn("Network Error");
          }
        }
      });
  };

  handleValidateOnBlur = (e) => {
    let schemaSubset = yup.reach(orderSchema, e.target.name);
    schemaSubset.validate(this.state[e.target.name]).catch((err) => {
      console.error(`${err.name}: ${err.errors} ${err.type}`);
      if (this.state.type !== "market" || e.target.name !== "price") {
        if (
          err.type &&
          err.type === "typeError" &&
          ["stockQuantity", "price"].includes(e.target.name)
        ) {
          this.setState({
            [`${e.target.name}Error`]: "Required",
          });
        } else {
          this.setState({
            [`${e.target.name}Error`]:
              err.errors.length > 0 ? err.errors[0] : null,
          });
        }
      }
    });
  };
  clientSearch = async (e) => {
    const search = e.target.value;

    // Send search request for clients only when keyword length > 2 
    if (search.length > 2) {
      //cancel previous requests
      let cancelToken;

      if (typeof cancelToken != typeof undefined) {
        cancelToken.cancel("Cancelling the previous request");
      }

      cancelToken = axios.CancelToken.source();

      const response = await OmsAxios.get(
        `${Properties.USER_SERVER_URL}${Properties.FETCH_CLIENT_API}?keyword=` +
          search,
        { cancelToken: cancelToken.token }
      );
      console.table(response.data);

      //based on response fill clientTable
      let clientsData;
      if (response.ok) {
        clientsData = response.data;
        const clientTable = [];
        clientsData?.map((client) =>
          clientTable.push({
            value: client.id,
            label: "" + client.name + " - " + client.id,
          })
        );
        this.setState({ clients: clientTable });
      }
    }
  };
  stockSearch = (e) => {
    // searching through StockList
    const search = e.target.value;
    if (search.length > 2) {
      let stockTable = [];
      StockList.map((stock) =>
        stockTable.push({ value: stock, label: stock })
      );
      this.setState({ stocks: stockTable });
    } else {
      this.setState({ stocks: [] });
    }
  };

  render() {
    return (
      <div
        style={{ marginTop: "50px", padding: "10px" }}
      >
        <div
          className="card text-center mobile "
          style={{
            marginLeft: "auto",
            marginRight: "auto",
            borderRadius: "10px",
          }}
        >
          <div className="card-header" id="header">
            <legend>UPDATE ORDER</legend>
          </div>
          <div className="card-body">
            <form data-testid="order-update-form" className="order-update-form">
              <br />

              <div className="row justify-content-center mt-3 ">
                <div className="form-group p-2 bd-highlight col-md-10 col-12">
                  <select
                    name="exchange"
                    value={this.state.exchange}
                    onChange={(e) => this.change(e)}
                    className="form-select"
                    id="Stockexchange"
                    data-testid="stock-exchange-select"
                    onBlur={this.handleValidateOnBlur}
                  >
                    <option value="nse">NSE</option>
                  </select>
                  {this.state.exchangeError ? (
                    <p
                      style={{ color: "red" }}
                      aria-label="exchange name error"
                    >
                      {this.state.exchangeError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>
              </div>
              <br />

              <div className="row justify-content-center ">
                <div className="form-group p-2 bd-highlight col-md-5 col-12">
                  <SearchBox
                    name="stockName"
                    placeholder="Search Stocks (Options appear after 2 characters)"
                    inputChange={this.stockSearch}
                    change={this.change}
                    onBlur={this.handleValidateOnBlur}
                    options={this.state.stocks}
                    aria-label="stock-name-select"
                  />
                  {this.state.stockNameError ? (
                    <p style={{ color: "red" }} aria-label="stock name error">
                      {this.state.stockNameError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="form-group p-2 bd-highlight form-floating col-md-5 col-12">
                  <input
                    id="floatingInput"
                    name="stockQuantity"
                    value={this.state.stockQuantity}
                    onChange={(e) => {
                      this.change(e);
                      this.handleValidateOnBlur(e);
                    }}
                    onBlur={this.handleValidateOnBlur}
                    className="form-control "
                    type="number"
                    placeholder="0"
                    min="0"
                    max="1000"
                    data-testid="stock-quantity-input"
                  />
                  <label htmlFor="floatingInput">Quantity</label>
                  {this.state.stockQuantityError ? (
                    <p
                      style={{ color: "red" }}
                      aria-label="stock quantity error"
                    >
                      {this.state.stockQuantityError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>
              </div>

              <div className="row justify-content-center ">
                <div className="form-group p-2 bd-highlight col-md-10 col-12">
                  <select
                    name="side"
                    //keeping prefilled this one , edited this.
                    value={(this.state.side === "BUY") ? "Buy" : "Sell"}
                    onChange={(e) => this.change(e)}
                    className="form-select"
                    id="side"
                    data-testid="stock-side-select"
                    onBlur={this.handleValidateOnBlur}
                  >

                    <option value="Buy">Buy</option>
                    <option value="Sell">Sell</option>
                  </select>
                  {this.state.sideError ? (
                    <p style={{ color: "red" }} aria-label="side error">
                      {this.state.sideError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>
              </div>
              <br />

              <div className="row justify-content-center ">
                <div className="form-group p-2 bd-highlight col-md-5 col-12">
                  <select
                    name="type"
                    value={this.state.type}
                    onChange={(e) => this.change(e)}
                    onBlur={this.handleValidateOnBlur}
                    className="form-select"
                    id="type"
                    data-testid="stock-type-select"
                  >
                    <option value="default">--Select Type--</option>
                    <option value="market">Market</option>
                    <option value="limit">Limit</option>


                  </select>
                  {this.state.typeError ? (
                    <p style={{ color: "red" }} aria-label="type error">
                      {this.state.typeError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="form-group p-2 bd-highlight form-floating col-md-5 col-12">
                  {(() => {
                    if (this.state.type === "LIMIT" || this.state.type === "Limit" || this.state.type === "limit") {
                      return (
                        <>
                          <input
                            name="price"
                            value={this.state.price}
                            onChange={(e) => this.change(e)}
                            className="form-control"
                            id="disabledInput floatingInput"
                            type="number"
                            placeholder="Enter target price"
                            aria-label="stock-price-input"
                          />
                          <label htmlFor="floatingInput">
                            Enter Target Price
                          </label>
                          {this.state.priceError ? (
                            <p
                              style={{ color: "red" }}
                              aria-label="price error"
                            >
                              {this.state.priceError}
                            </p>
                          ) : (
                            <></>
                          )}
                        </>
                      );
                    } else {
                      return (
                        <>
                          <div className="form-group">
                            <fieldset>
                              <input
                                className="form-control"
                                id="readOnlyInput"
                                type="text"
                                placeholder="Price: $$"
                                readOnly
                                aria-label="stock-price-input"
                              ></input>
                            </fieldset>
                          </div>
                        </>
                      );
                    }
                  })()}
                </div>
              </div>
              <br />

              <div className="row justify-content-center ">
                <div className="form-group p-2 bd-highlight col-md-10 col-12">
                  <SearchBox
                    name="clientID"
                    placeholder="Search Clients (Options appear after 2 characters)"
                    inputChange={this.clientSearch}
                    change={this.change}
                    onBlur={this.handleValidateOnBlur}
                    options={this.state.clients}
                    aria-label="stock-client-select"
                  />
                  {this.state.clientIDError ? (
                    <p style={{ color: "red" }} aria-label="client id error">
                      {this.state.clientIDError}
                    </p>
                  ) : (
                    <></>
                  )}
                </div>
              </div>
              <br />
              <br />
              <Link to={{ pathname: `/order-details/${this.props.match.params.id}` }} style={{ textDecoration: 'none', color: 'white' }}>
              <button
                className="btns btn btn-primary"
                aria-label="back button"
              >
                  Back
                
              </button>
              </Link>
              &nbsp;&nbsp;&nbsp;
              
              <button
                value="submit"
                onClick={(e) => this.onSubmit(e)}
                className="btns btn btn-primary"
                aria-label="proceed button"
              >
                Proceed
              </button>
            </form>
          </div>
        </div>
        <br />
      </div>
    );
  }
}

export default withRouter(OrderUpDateForm);
