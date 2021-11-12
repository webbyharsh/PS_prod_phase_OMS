import React from 'react'
import Img from './user-icon.png'
import { auto } from '@popperjs/core';
import moment from 'moment';

const ProfileStatus = ({ userId, isActive, createdAt, lastActiveAt }) => {
    return (
        <div data-testid="div1-section1-id" style = {{
            backgroundColor: "#1a1a1a",
            color: '#fff',
            textAlign: 'center',
            paddingBottom: '10%'
        }}>
            <h1 data-testid="h1-section1-id" style={{
                marginTop: "20px",
                marginBottom: "20px",
                color: '#fff'
            }}>User Profile</h1>
            <img data-testid="img-section1-id" src={Img} alt="pic" />
            <br></br>
            <br></br>
            User Id: {userId}
            <br></br>
            Created at {moment(createdAt).utcOffset("+05:30").format('YYYY-MM-DD')}
            <br></br>
            Last seen at {moment(lastActiveAt).utcOffset("+05:30").format('YYYY-MM-DD')}
            <br></br>
            {isActive === true ? 
                <div data-testid="div2-section1-id">
                    <div id="circle-online" style={{
                        margin: auto
                    }}></div>
                    Active
                </div> :
                <div data-testid="div3-section1-id">
                    <div data-testid="div4-section1-id" id="circle-offline" style={{
                        margin: auto
                    }}></div>
                    Inactive
                </div>
            }
        </div> 
    )
}

export default ProfileStatus;
