import React from 'react';
import './button.css';

const BrokerRow = ({ sno, togglefn, brokerId, brokerName, status }) => {

  const togglefn1 = () => togglefn(brokerId, status)
  return (
    <tr data-testid="order-row-tr" key={sno} className="bg-light text-center">

      <th data-testid="order-row-th" scope="row" >{brokerId}</th>
      <td data-testid="order-row-td1" > {brokerName}</td>
      { status === false ? (<td data-testid="order-row-td2" > Inactive</td>) :
        (<td data-testid="order-row-td2" > Active</td>)}

      <td data-testid="order-row-td3">
        <div class=" btn toggle ">
          <input data-testid="order-row-btn" type="checkbox" onClick={togglefn1} checked={status} />


        </div>
      </td>
    </tr>

  );
}

export default BrokerRow;