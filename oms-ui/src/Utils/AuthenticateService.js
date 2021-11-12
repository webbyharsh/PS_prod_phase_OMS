import OmsAxios from "./OmsAxios";
import Properties from "./Properties";
import { getInitialState } from "./PersistState";

async function AuthenticateService() {
  let result = await OmsAxios.get(
    `${Properties.AUTH_SERVER_URL}${Properties.AUTHENTICATE_TOKEN_API}`,
    {
      withCredentials: true,
      headers: {
        Authorization: getInitialState("ACCESS_TOKEN"),
      },
    }
  );
  return result;
}

export default AuthenticateService;
