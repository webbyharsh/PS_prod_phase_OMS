import Properties  from '../../../Utils/Properties';
import OmsAxios from '../../../Utils/OmsAxios';
const ApiCall = async (values,pageNumber) => {     
  
  return OmsAxios.put(`${Properties.ORDER_SERVER_URL}${Properties.ORDER_SEARCH_API}`, 
        values, {
          headers: {
            "pageIndex":pageNumber,      
            "pageSize":8
          }
        })
        .then(response => 
          {     
               return response; 
          })
    .catch(error => {
        console.log(error);
      return "ERROR";
      });
    }     

export default  ApiCall;
