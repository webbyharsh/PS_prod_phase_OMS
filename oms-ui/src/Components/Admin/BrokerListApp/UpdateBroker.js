import OmsAxios from '../../../Utils/OmsAxios';
import Properties from '../../../Utils/Properties';

const UpdateBroker = async (brokerId, status1) => {
  let status2;
  if (status1 === true) status2 = "false";
  else status2 = "true";
  let object = {
    "isActive": status2,
    "userId": brokerId.toString()
  }

  return  OmsAxios.put(
    `${Properties.USER_SERVER_URL}${Properties.UPDATE_BROKER_LIST}`, 
    object
  )
  .then(response => 
    {     
         return response; 
    })
.catch(error => {
  console.log(error);
return error;
});
  
}
export default UpdateBroker;