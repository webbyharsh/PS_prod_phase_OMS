import OmsAxios from '../../../Utils/OmsAxios';
import Properties  from '../../../Utils/Properties';
const getBrokerList = async () => {     
     return  OmsAxios.get(`${Properties.USER_SERVER_URL}${Properties.GET_BROKER_LIST}`, {
        headers: {
       
      }})
     .then(response => 
          {     
               return response; 
          })
    .catch(error => {
        console.log(error);});
}
export default  getBrokerList;