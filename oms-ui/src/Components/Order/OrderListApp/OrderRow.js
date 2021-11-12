import React ,{useState,useEffect}from 'react';
import { Button } from 'react-bootstrap';
import './OrderRow.css';
import { Link } from 'react-router-dom';
import ClientApi from './ClientApi';
const OrderRow = ({ id,stock, count, clientId, details,status,orderDate,side,type }) => {
 
 const [clientName, setClientName] = useState("...");

 useEffect(() => {
  ClientApi(clientId).then((response)=>{
                        if(response.status==200)setClientName(response.data.name);
                         else setClientName("Anonymous")} )
                      .catch((error)=>setClientName("Anonymous")) 

}, [clientId]);
 
    function getDate(date1){
    let year=date1.slice(0, 4);
    let month=date1.slice(5, 7);
    let date=date1.slice(8, 10);
      return date+"-"+month+"-"+year;
    }
   
    return (
      <tr data-testid="order-row-tr" key={id} className="bg-light text-center"> 
                      
            <th data-testid="order-row-th" scope="row" >{id}</th>
              <td data-testid="order-row-td1"> {stock}</td>
              <td data-testid="order-row-td2" className="col-remove"> {count}</td>
              <td data-testid="order-row-td3" className="col-remove">{side} </td>
              <td data-testid="order-row-td4" className="col-remove">{type}</td>
              <td data-testid="order-row-td5" >{ clientName}</td>
              <td data-testid="order-row-td6" className="col-remove"> <nobr>{getDate(orderDate)}</nobr></td>
              <td data-testid="order-row-td7" ><Link to={{pathname: `/order-details/${id}`,clientName:clientName}} style={{ textDecoration: 'none',color:'white' }}><Button   className="rounded " id="button_1" data-testid="order-row-btn">{details}</Button></Link></td>
                          
              <td data-testid="order-row-td8"  > {status} </td>
              </tr>
              
    );
  }

export default OrderRow;