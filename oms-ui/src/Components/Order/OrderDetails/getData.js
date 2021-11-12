import Properties  from '../../../Utils/Properties';
import OmsAxios from '../../../Utils/OmsAxios';


const getData = async (orderid) => {     
  
    return OmsAxios.get(`${Properties.ORDER_SERVER_URL}/order/${orderid}`)
    .then(response => 
      {     
           return response; 
      })
.catch(error => {
    console.log(error);
  return "ERROR";
  });
 
  } 

export default  getData;
