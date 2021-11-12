import Properties  from '../../../Utils/Properties';
import OmsAxios from '../../../Utils/OmsAxios';
const ClientApi = async (clientId) => {     

  
return OmsAxios.get(`${Properties.USER_SERVER_URL}${Properties.GET_CLIENT_BY_ID}${clientId}`,
     {
      headers: {
      }
    }).then((response)=>{
        return response;
    })
    .catch((error)=>{
        return "NULL"
    }
    )
   
}
export default  ClientApi;



