import OmsAxios from "./OmsAxios";
import Properties from "./Properties";
import Constants from "./Constants";

class SendOrderService {
  STATUS_INVALID_ORDER = 1;
  STATUS_UNAUTHORIZED_USER = 2;
  STATUS_SERVER_UNREACHABLE = 3;

  constructor(order) {
    this.orderDetails = order;
    this.error = {
      status: -1,
      message: "",
      body: "",
    };
    this.response = null;
  }

  get isOrderValid() {
    let actualKeys = Object.keys(this.orderDetails);
    let errors = [];
    // are all required keys present in order object
    for (let key of Constants.ListOfOrderKeys) {
      if (!actualKeys.includes(key) && !Constants.OptionalKeys.includes(key)) {
        errors.push(`${key} is a required attribute`);
      }
    }

    // is quantity within limits
    if (this.orderDetails["quantity"] !== null) {
      let actual = this.orderDetails["quantity"];
      let num = undefined;
      if (typeof actual === "number") num = actual;
      else {
        try {
          num = parseInt(actual);
        } catch (err) {
          errors.push("Quantity must be a number");
        }
      }
      if (num !== undefined && (num < 1 || num > 1000))
        errors.push("Quantity must be within [1,1000]");
    }

    // are other values non-null and non-empty strings
    for (let key of actualKeys) {
      if (
        !Constants.NonStringKeys.includes(key) &&
        !Constants.MandatorilyNullKeysWhenCreated.includes(key)
      ) {
        let actual = this.orderDetails[key];
        if (typeof actual !== "string" || actual === "")
          errors.push(`${key} must be a non-null and non-empty string`);
      }
    }

    // status must be `CREATED`
    if (this.orderDetails["status"] !== Constants.CREATED)
      errors.push("Order cannot be sent");

    if (errors.length > 0) {
      this.setError(
        this.STATUS_INVALID_ORDER,
        "Invalid order",
        errors.join(".\n")
      );
      return false;
    }

    return true;
  }

  isAvailableToSend = async () => {
    if (this.orderDetails !== null && this.isOrderValid) {
      try {
        let result = await OmsAxios.get(
          `${Properties.AUTH_SERVER_URL}${Properties.AUTHORIZE_TOKEN_API}`
        );
        if (
          result.data.listRoles?.includes("ROLE_ADMIN") ||
          result.data.listRoles?.includes("ROLE_Admin") ||
          result.data.listRoles?.includes("ROLE_User") ||
          result.data.listRoles?.includes("ROLE_USER")
        ) {
          return true;
        } else {
          this.setError(
            this.STATUS_UNAUTHORIZED_USER,
            "Unauthorized action",
            "You are not allowed to perform this action"
          );
          return false;
        }
      } catch (err) {
        this.setError(
          this.STATUS_SERVER_UNREACHABLE,
          "Unable to authorize user",
          "Couldn't reach servers, please try again"
        );
        return false;
      }
    } else return false;
  };

  setError = (
    status = null,
    message = "Unknown error occurred",
    body = "Please contact the administrator to get help"
  ) => {
    this.error = {
      status: status,
      message: message,
      body: body,
    };
  };

  send = async () => {
    let isAvailable = await this.isAvailableToSend();
    if (isAvailable) {
      try {
        let response = await OmsAxios.put(
          `${Properties.ORDER_SERVER_URL}${Properties.SEND_ORDER_API}/${this.orderDetails["orderId"]}`
        );
        if (response.status === 200) this.response = response.data;
        else
          this.setError(
            response.status,
            "Order rejected",
            JSON.stringify(response.data)
          );
        return response.data;
      } catch (err) {
        this.setError();
        throw err;
      }
    } else {
      return null;
    }
  };
}

export default SendOrderService;
