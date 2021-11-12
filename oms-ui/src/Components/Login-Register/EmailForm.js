import React from 'react'
import { useHistory, useLocation } from 'react-router-dom';
import Email from './Email';
import '../UserProfile//UserProfile.css';

const EmailForm = () => {
    const history = useHistory();
    const location = useLocation();

    return (
        <div data-testid="email-container" class="center" style={{
            margin: "2% 8% 5%"
        }}>
            <div data-testid="email-container-inner" className="col-sm-8 pl-1" style={{
            }}>
                <div className="card">
                    <Email
                        history={history}
                        location={location}
                    />
                </div>
            </div>
        </div>
      
    );
}

export default EmailForm