import Properties  from '../../../Utils/Properties';
import OmsAxios from '../../../Utils/OmsAxios';
const getData = async (pageNumber) => {     
 
    
return OmsAxios.get(`${Properties.ORDER_SERVER_URL}${Properties.CREATE_ORDER_API}`, {
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
export default  getData;



