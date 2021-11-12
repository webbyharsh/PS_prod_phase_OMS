import React, {useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { getInitialState } from '../../Utils/PersistState';
import '../Order/OrderListApp/OrderRow.css';
import './UserProfile.css';
import ProfileDetail from './ProfileDetail';
import ProfileStatus from './ProfileStatus';
import getUserData from './getUserData';

const UserProfile =()=> {
    const [finalUsers, setFinalUsers]= useState({});
    const history = useHistory();
    const user_id = getInitialState("USER_ID");
   

    useEffect(() => {
      const fetchData = async () => {
        try{
            let response = await getUserData(user_id);
           
            if(response.status===200){         
              setFinalUsers(response.data);
            
            }
        }catch(err){
         console.log(err);
        }
      }
      fetchData();
    }, []);

  

    return (
        <div data-testid="outer-div-user-profile-id" style={{
          margin: "2% 8% 5%"
        }}>
            <div className="row" data-testid="inner-div-user-profile-id" style={{  
              
            }}> 
              <div className="col-sm-4 pr-1" style={{
              }}>
                <div className="card">
                  <ProfileStatus   
                      userId={user_id}
                      isActive={finalUsers.active}
                      createdAt={finalUsers.createdAt}
                      lastActiveAt={finalUsers.lastActiveAt}
                  /> 
                </div>
              </div>
              <div className="col-sm-8 pl-1" style={{
              }}>
                <div className="card">
                  <ProfileDetail  finalUsers={finalUsers} history={history} />
                </div>
              </div>
            </div> 
        </div>
    );
};
    
export default UserProfile;