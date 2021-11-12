import { getInitialState, persistState } from "./PersistState";
import OmsAxios from "./OmsAxios";
import Properties from "./Properties";

function LogoutService() {
  OmsAxios.post(
    `${Properties.AUTH_SERVER_URL}${Properties.INVALIDATE_TOKEN_API}`,
    {},
    {
      withCredentials: true,
      headers: {
        Authorization: getInitialState("ACCESS_TOKEN"),
      },
    }
  ).finally(() => {
    persistState("ACCESS_TOKEN", null);
    persistState("USER_NAME", null);
    persistState("ROLE", null);
    persistState("USER_ID", null);
  });
}

export default LogoutService;
