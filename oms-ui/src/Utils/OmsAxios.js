import axios from "axios";
import { getInitialState, persistState } from "./PersistState";
import Properties from "./Properties";

const OmsAxios = axios.create({
  timeout: Properties.REQUEST_TIMEOUT,
  headers: {
    "Content-Type": "application/json",
    cache: "no-cache",
  },
});

const requestHandler = (request) => {
  if (
    request.url !==
      `${Properties.AUTH_SERVER_URL}${Properties.GENERATE_TOKEN_API}` &&
    request.url !==
      `${Properties.AUTH_SERVER_URL}${Properties.REFRESH_TOKEN_API}`
  ) {
    request.headers.userId = getInitialState("USER_ID");
    request.headers.Authorization = getInitialState("ACCESS_TOKEN");
  }

  if (request.url.includes(`${Properties.AUTH_SERVER_URL}`))
    request.withCredentials = true;
  return request;
};

const status = [200, 201];

const responseHandler = (response) => {
  if (response.statusText === "CREATED" || status.includes(response.status))
    response["created"] = true;

  if(response.statusText === "OK" || status.includes(response.status))
    response["ok"] = true;
  
  return response;
};

const errorHandler = async (error) => {
  if (error.message === "Request failed with status code 401") {
    let { url, method, headers, withCredentials } = error.config;
    try {
      let result = await axios.get(
      `${Properties.AUTH_SERVER_URL}${Properties.REFRESH_TOKEN_API}`, 
      { withCredentials: true }
      )
      if ((result.status === 200 || 
        result.statusText === 'OK') &&
        result.data.jwtAccessToken) {
        persistState("ACCESS_TOKEN", result.data.jwtAccessToken);
        try {
          let refreshedResult = await axios[method](url, {
            headers: {
              ...headers, 
              Authorization: result.data.jwtAccessToken
            },
            withCredentials: withCredentials
          });
          return refreshedResult;
        } catch (err) { throw error }
      }
    }
    catch(err)  {
      console.error(err);
      throw error;
    }
  } 
  else throw error;
}

OmsAxios.interceptors.request.use(
  (request) => requestHandler(request),
  (error) => errorHandler(error)
);

OmsAxios.interceptors.response.use(
  (response) => responseHandler(response),
  (error) => errorHandler(error)
);

export default OmsAxios;

