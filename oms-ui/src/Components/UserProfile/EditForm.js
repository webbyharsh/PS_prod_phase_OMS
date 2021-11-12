import React from 'react'
import { useHistory, useLocation } from 'react-router-dom';
import './UserProfile.css';
import EditProfileHelper from './EditProfileHelper';

const EditForm = () => {
    const history = useHistory();
    const location = useLocation();

    return (
        <div data-testid="edit-profile-container" style={{
            margin: "2% 20% 5%"
        }}>
            <div data-testid="edit-profile-container-outer"  className="row">
              <div data-testid="edit-profile-container-inner2" style={{
              }}>
                <div className="card">
                    <EditProfileHelper
                        history={history}
                        location={location}
                    /> 
                </div>
              </div>   
            </div>
        </div>
    );
}

export default EditForm
