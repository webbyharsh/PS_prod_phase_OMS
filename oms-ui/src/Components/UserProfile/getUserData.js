import Properties  from '../../Utils/Properties';
import OmsAxios from '../../Utils/OmsAxios';
const getUserData = async (user_id) => {     
     return OmsAxios.get(
               `${Properties.USER_SERVER_URL}${Properties.USER_DETAILS_API}`, {
               headers: {                    
               },
               params: {
                    'user_id': user_id
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
export default  getUserData;







