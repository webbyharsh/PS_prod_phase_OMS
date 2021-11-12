import React, { Component } from "react";
import getData from "./getData";
import Field from "./Field"
import { withRouter,Link } from 'react-router-dom';
import "./OrderDetails.css";
import SendOrderService from '../../../Utils/SendOrderService';
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import OmsAxios from "../../../Utils/OmsAxios";
import Properties from "../../../Utils/Properties";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowAltCircleLeft } from '@fortawesome/free-solid-svg-icons'
import { left } from '@popperjs/core';
import formate from "../../../Utils/FormatDate";
import map from "./Map";

class OrderDetails extends Component {

    state = {
        details: {

            "orderId": "Order Id",
            "name": "Client Name",
            "stock": "Stock",
            "quantity": "Quantity",
            "stockPrice": "StockPrice",
            "targetPrice": "Target Price",
            "side": "Side",
            "type": "Type",
            "status": "Status",
            "clientId": "Client ID",
            "createdBy": "Created By",
            "modifiedBy": "Modified By",
            "createdAt": "Date Created",
            "modifiedAt": "Date Modified",
            "quantityFilled": "Filled Quantity",
            "executedPrice": "Executed Price",
            "executedAt": "Executed Date",
            "exchangeId": "Exchange ID",
        },
        error: false,
        isAvailableToSend: false,
    }

    sendOrderService = new SendOrderService(null);

    fetchOrder() {
        let orderid = this.props.match.params.id;
        getData(orderid)
            .then((data) => {
                var tempDetails = {}
                tempDetails["name"] = this.props.location.clientName
                for (var key in data.data) {
                    if (key in this.state.details) {
                        if (key == "createdAt" || key == "modifiedAt") {
                            let t = new Date(data.data[key])
                            let date = formate(t)
                            tempDetails[key] = date
                        } else {
                            tempDetails[key] = data.data[key]
                        }
                    }
                }
                if (data.data == undefined) {
                    this.setState({
                        error: true
                    })
                }
                else {
                    this.setState({
                        details: { ...tempDetails },
                        error: false
                    })
                    this.sendOrderService = new SendOrderService(data.data);
                    this.sendOrderService.isAvailableToSend()
                        .then((res) => {
                            this.setState({
                                isAvailableToSend: res
                            });
                        })
                        .catch((err) => {
                            console.error(err);
                        })
                }
            })
            .catch((err) => {
                this.setState({
                    error: true
                })
            })
    }

    componentDidMount() {
        this.fetchOrder()
    }

    built = () => {
        var fields = []
        var i = 0
        var exclude = ["orderId", "createdBy", "modifiedBy"]
        for (const key in this.state.details) {
            if (key != "stock" && this.state.details[key] != null && !(exclude.includes(key))) {
                i++
                let val = map[key]
                console.log(val)
                let temp = (<Field Key={val} data={this.state.details[key]} key={i} />)
                fields.push(temp)
            }
        }
        return fields
    }
    error = () => {
        var temp = ""
        if (this.state.error) {
            temp = (<div class="alert alert-danger" role="alert" style={{ textAlign: "center" }}>
                Error While Fetching The Details
            </div>)
        }
        return temp
    }

    cancelOrder = async () => {

        try {
            let orderid = this.state.details["orderId"];
            let response = await OmsAxios.delete(
                `${Properties.ORDER_SERVER_URL}/order/${orderid}`,
            );
            if (response.created) {
                toast.success("Order Cancelled Successfully");
                this.fetchOrder();
            }
            else {
                toast.warn("Something went wrong!");
            }
        } catch (err) {
            toast.warn("Network Error");
        }
    }

    render() {



        return (
            <div data-testid='details'>
                <div id="custom-container">
                    <div className="orderdetailheader">

                        <div className="list-group">
                            <div className="list-group-item list-group-item-action active" style={{
                                textAlign: left
                            }}><span className="backIcon" onClick={() => this.props.history.push("/order-list")}>
                                    <FontAwesomeIcon icon={faArrowAltCircleLeft} />
                                </span><span>Order Details</span></div></div>
                    </div>
                    <div className="innerheader">
                        <span className="stock">{this.state.details["stock"]}</span>

                    </div>

                    <div className="innercontainer">

                        {this.built()}

                    </div>
                    <div className="buttons" >
                        {
                            (this.state.details["status"] === "CREATED") ?
                            <Link to={{ pathname: `/order-update/${this.props.match.params.id}` }} style={{ textDecoration: 'none', color: 'white' }}>
                                (<button className="btns" style={{ margin: "10px" }} >
                                        Update
                                </button>)
                                </Link>
                                : <button className="btns" style={{ margin: "10px" }} disabled >
                                    Update
                                </button>
                        }
                        {(this.state.details["status"] === "CREATED") ? (<button className="btnscancel" aria-label="cancel order button" onClick={this.cancelOrder.bind(this)}>Cancel</button>) : (<button className="btns danger" aria-label="cancel order button" disabled>
                            Cancel
                        </button>)}
                        {this.state.isAvailableToSend ?
                            (<button className="btns danger" aria-label="send order button" onClick={(e) => {
                                e.preventDefault();
                                this.sendOrderService.send()
                                    .then((result) => {
                                        if (result !== null) {
                                            toast.success('Successfully sent your order');
                                            this.fetchOrder();
                                        } else {
                                            toast.warn(`${this.sendOrderService.error.body}`);
                                        }
                                    })
                                    .catch((err) => {
                                        toast.error(err.response?.data?.message || err.message);
                                    });
                            }}>
                                Send Order
                            </button>) :
                            (<button className="btns danger" aria-label="send order button" disabled>
                                Send Order
                            </button>)
                        }
                    </div>
                </div>
                {this.error()}
            </div>
        )
    }
}



export default withRouter(OrderDetails)