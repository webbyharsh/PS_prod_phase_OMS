import React from 'react'
import { useHistory, useLocation } from 'react-router-dom';
import UpdatePassword from './UpdatePassword';
import './UserProfile.css';

const UpdatePasswordForm = () => {
    const history = useHistory();
    const location = useLocation();

    return (
        <div data-testid="update-password-container" class="center" style={{
            margin: "2% 8% 5%"
        }}>
            <div data-testid="update-password-container-inner" className="col-sm-8 pl-1" style={{
            }}>
                <div className="card">
                    <UpdatePassword
                        history={history}
                        location={location}
                    />
                </div>
            </div>
        </div>
      
    );
}

export default UpdatePasswordForm
