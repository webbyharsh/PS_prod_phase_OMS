import OmsAxios from "./OmsAxios";
import Properties from "./Properties";
import { getInitialState } from "./PersistState";

function AuthorizeService() {
  return OmsAxios.get(
    `${Properties.AUTH_SERVER_URL}${Properties.AUTHORIZE_TOKEN_API}`,
    {
      withCredentials: true,
      headers: {
        Authorization: getInitialState("ACCESS_TOKEN"),
      },
    }
  );
}

export default AuthorizeService;
