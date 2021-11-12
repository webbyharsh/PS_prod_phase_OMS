import React, { useState, useEffect } from 'react';

import BrokerRow from './BrokerRow';
import Pagination from '../../Pagination/Pagination.js';
import getBrokerList from './getBrokerList';
import UpdateBroker from './UpdateBroker';

const BrokerToggle = () => {
  const [finalBrokers, setFinalBrokers] = useState(['']);
  const [currentPage, setCurrentPage] = useState(1);
  const [brokersPerPage] = useState(5);
  const fetchData = async () => {
    try {
      let response = await getBrokerList();
      if (response.status == 200) setFinalBrokers(response.data);
      else throw new Error(`HTTP error! status: ${response.status}`);
    } catch (err) { console.log(err) }
  }

  /* Making Api call to retrive data */
  useEffect(() => {
    fetchData();
  }, []);

  // Get Broker List 
  const indexOfLastBroker = currentPage * brokersPerPage;
  const indexOfFirstBroker = indexOfLastBroker - brokersPerPage;
  let finalBrokers1 = finalBrokers.sort((a, b) => (parseInt(a.userId) > parseInt(b.userId)) ? 1 : -1)
  let currentBrokers = finalBrokers1.slice(indexOfFirstBroker, indexOfLastBroker);

  //Change Page
  const paginate = pageNumber => {
    setCurrentPage(pageNumber);
  }
  const togglefn = async (brokerId, status1) => {
    await UpdateBroker(brokerId, status1)
    fetchData();
  };
  return (

    <div data-testid="order-list-id" className="container main-content " style={{ marginTop: "5%" }} >

      <div className="table-responsive-sm">
        <table data-testid="table-order-list-id" className="table table-hover text-center">
          <thead data-testid="thead-test-id" className="table-dark">
            <tr>

              <th data-testid="broker-id" scope="col" >Broker Id</th>
              <th data-testid="broker-name" scope="col">Broker Name</th>
              <th data-testid="current-status" scope="col" >Current Status</th>
              <th data-testid="change-status" scope="col" >Change status</th>
            </tr>

          </thead>
          <tbody data-testid="tbody-test-id">


            {currentBrokers.map((broker, index) => {

              return (<BrokerRow key={index} sno={index} togglefn={togglefn}
                brokerId={broker.userId} brokerName={broker.name} status={broker.active}
              />)
            })
            }
          </tbody>
        </table>


      </div>

      <Pagination data-testid="pagination-test-id"

        data={finalBrokers.length}
        pageCount={Math.ceil(finalBrokers.length / brokersPerPage)}
        pageLimit={5}
        dataLimit={brokersPerPage}
        paginate={paginate}
        currentPage={currentPage}
      />

    </div>
  );
};



export default BrokerToggle;