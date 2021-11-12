import React,{useEffect} from 'react';

function Pagination({ data,pageCount,pageLimit, dataLimit,paginate, currentPage }) {
  function goToNextPage() {paginate(currentPage+1);}
  function goToPreviousPage() { paginate(currentPage-1);}
  function changePage(event) {
    const pageNumber = Number(event.target.textContent);
    paginate(pageNumber);
  }

  //Settign pages
  const getPaginationGroup = () => {
    const pageNumbers = [];
   
    if(pageCount-currentPage+1<5)
    {   
        for (let i = 1; i <=Math.min(currentPage+pageLimit-1,pageCount); i++) 
        {
            pageNumbers.push(i);   
        }
    }
   else{
        

        for(let i=currentPage;i<=Math.min(pageCount,currentPage+pageLimit-1);i++)
        {
            pageNumbers.push(i);
        }
   }
    return pageNumbers;
  };

  useEffect(() => {
    window.scrollTo({ behavior: 'smooth', top: '0px' });
  }, [currentPage]);

  return (
    <div>
      <div data-testid= "pagination-test-id" className="row justify-content-center">
        <div className="col-4 ">
            <ul className="pagination justify-content-center">
              {/* show previous page */}
              <li key="0"  className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
              <button data-testid="pagination-btn1-id" onClick={goToPreviousPage}  className="page-link ">&laquo; </button>
              </li>
              
              {/* show page numbers */}
              {getPaginationGroup().map((item, index) => (
                <li  data-testid={`pagination-btn2-id${item}`} key={index} className={`page-item  ${currentPage === item ? 'active' : null}`}>
                <button  onClick={changePage} className="page-link"
                >
                  <span>{item}</span>
                </button>
                </li>
              ))}
               
               {/* show next page */}
               <li  key="-1"className={`page-item ${currentPage === pageCount ? 'disabled' : ''}`}>
                <button data-testid="pagination-btn3-id"  onClick={goToNextPage} className="page-link ">&raquo;</button>
              </li>
            </ul>
          </div>
        </div>
    </div>
  );

}
export default Pagination;
