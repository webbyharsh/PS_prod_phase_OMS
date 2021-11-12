import React from 'react';
import { Button } from 'react-bootstrap';
import { left } from '@popperjs/core';

const ProfileDetail = ({finalUsers, history}) => {
    function buttonFunction() {
        history.push({
            pathname: "/edit-user-profile",
            state: { finalUsers }
        });
    }
    function updateFunction() {
        history.push({
            pathname: "/update-password",
            state: { finalUsers }
        });
    }
    return (
        <div data-testid="outer-div1-section3" style={{
            backgroundColor: "#fff",
            textAlign: 'center'
        }}>
            <div data-testid="outer-div2-section3" className="list-group">
                <div className="list-group-item list-group-item-action active" style={{
                    textAlign: left
                }}>User Details</div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="inner-div1-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div style={{
                            fontWeight: "bold"
                        }}>Name:</div>
                        <div class="capitalize"
                            data-testid="inner-div2-section3" style={{
                            textAlign: left
                        }}>{finalUsers.name}</div>
                    </div>
                </div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="outer-div3-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div data-testid="inner-div3-section3" style={{
                            fontWeight: "bold"
                        }}>Age:</div>
                        <div data-testid="inner-div4-section3" style={{
                            textAlign: left
                        }}>{finalUsers.age}</div>
                    </div>
                </div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="outer-div4-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div data-testid="inner-div5-section3" style={{
                            fontWeight: "bold"
                        }}>Role:</div>
                        <div data-testid="inner-div6-section3" style={{
                            textAlign: left
                        }}>{finalUsers.roles?.map((role) => role.name)}</div>
                    </div>
                </div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="outer-div6-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div data-testid="inner-div9-section3"  style={{
                            fontWeight: "bold"
                        }}>Mobile no.:</div>
                        <div data-testid="inner-div10-section3" style={{
                            textAlign: left
                        }}>{finalUsers.contact}</div>
                    </div>
                </div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="outer-div9-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div data-testid="inner-div13-section3" style={{
                            fontWeight: "bold"
                        }}>Email:</div>
                        <div data-testid="inner-div14-section3" style={{
                            textAlign: left
                        }}>{finalUsers.emailId}</div>
                    </div>
                </div>
                <div className="list-group-item list-group-item-action">
                    <div data-testid="outer-div7-section3" style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)" }}>
                        <div data-testid="inner-div11-section3" style={{
                            fontWeight: "bold"
                        }}>Address:</div>
                        <div class="capitalize" 
                            data-testid="inner-div12-section3" style={{
                            textAlign: left
                        }}>{finalUsers.address?.street}, {finalUsers.address?.city}
                        , {finalUsers.address?.state}, {finalUsers.address?.country}</div>
                    </div>
                </div>
                
                <a className="list-group-item list-group-item-action">
                    <Button className="rounded" id="button_1" data-testid="order-row-btn1" onClick={buttonFunction}
                    >Edit Details</Button>
                </a>
            </div>    
            <a className="list-group-item list-group-item-action">
                <Button className="rounded" id="button_1" data-testid="order-row-btn2" onClick={updateFunction}
                >Change Password</Button>
            </a>    
        </div> 
    )
}

export default ProfileDetail;
