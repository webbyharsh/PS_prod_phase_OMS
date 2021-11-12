import React, { useState, useEffect } from 'react';
import getData from './getData';
import OrderRow from './OrderRow';
import './OrderRow.css';
import Search from './search';
import './search.css';
import Pagination from '../../Pagination/Pagination.js';
import ApiCall from './ApiCall';

const OrderList =()=> {
  const [finalOrders, setFinalOrders]= useState([]);
  const [formValue, setFormValue] = useState({});
  const [allOrderCall, setAllOrderCall] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const [totalOrders, setTotalOrders] = useState(1);
  const [ordersPerPage,setOrdersPerPage] = useState(8);
  const setState = async (response) => {
    setFinalOrders(response.data.orders);
    setTotalPage(response.data.totalPages)
    setTotalOrders(response.data.totalOrders)
    setCurrentPage(response.data.pageNumber+1)
    setOrdersPerPage(8)
  }
  const searchOrder = async (formValue1,pageNumber) => {
    
    try{
      let response = await ApiCall(formValue1,pageNumber);
        if(response.status==200)  await setState(response)
        else  throw new Error(`Order Not found`);
    }catch(err){ console.log(err); setFinalOrders([]); setTotalPage(1); setTotalOrders(0); }
  }



  const fetchData = async (pageNumber) => {
    try{
        let response = await getData(pageNumber);
        if(response.status==200){
             await setState(response) 
            
        } 
        else throw new Error(`Order Not found`); 
    }catch(err){ console.log(err)}
  }
  useEffect(() => {
    fetchData(0);
   
  }, []);

  //Change Page
  const paginate = async pageNumber => {
    if (allOrderCall) { fetchData(pageNumber - 1); 
      setCurrentPage(pageNumber); }
    else { searchOrder(formValue, pageNumber - 1); 
      setCurrentPage(pageNumber); }
  }

  return (
    <div data-testid="order-list-id" className="container main-content " style={{ marginTop: "5%" }} >
      <Search searchOrder={searchOrder} fetchData={fetchData} setAllOrderCall={setAllOrderCall} setFormValue={setFormValue} filteredOrdersHandler={setFinalOrders} />
      <br />
      <br />

      <div className="table-responsive-sm">
        <table data-testid="table-order-list-id" className="table table-hover text-center">
          <thead className="table-dark">
                <tr>
                  <th scope="col">Order No.</th>
                  <th scope="col">Stock Name</th>
                  <th scope="col" className="col-remove">Stocks Count</th>
                  <th scope="col" className="col-remove">Side</th>
                  <th scope="col" className="col-remove">Type</th>
                  <th scope="col" >Client Name</th>
                  <th scope="col" className="col-remove">Date Created</th>
                  <th scope="col">View Details</th>
                  <th scope="col">Order Status</th>
                </tr>
            
          </thead>
          { finalOrders &&
          finalOrders.length !== 0 ?
          <tbody> 
          { finalOrders.map((order,index) => {
          return (<OrderRow key={index}  
            id={order.orderId}
            count={order.quantity} 
            
            clientId={order.clientId} 
            
            orderDate={order.createdAt} 
            details="view" 
            {...order}
          />)
          
          
             
            })
          }
          </tbody>
           : <p>No orders found</p> }
        </table>
        
        <Pagination 
           data={totalOrders}
           pageCount={totalPage}
           pageLimit={8}
           dataLimit={ordersPerPage}
           paginate={paginate}
           currentPage={currentPage}
         />
    </div>
       
    </div>
  );
};



export default OrderList;