import { act,render, screen,cleanup ,fireEvent, waitFor} from '@testing-library/react';
import React from 'react';
import ReactDOM from 'react-dom';
import Pagination from '../../Components/Pagination/Pagination';

test("Pagination renders order row without crashing",()=>{
    const div=document.createElement("div");
    ReactDOM.render(<Pagination/>,div)
   
});
test("Pagination div renders order row without crashing",()=>{
    render(<Pagination/>)
    expect(screen.getByTestId("pagination-test-id")).toBeVisible();
    expect(screen.getByTestId("pagination-btn3-id")).toBeVisible();  
});
describe('paginate btn test', () => {
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  })
  const Button = ({onClick}) => (
    <Pagination
        
    data={100}
    pageCount={20}
    pageLimit={5}
    dataLimit={5}
    paginate={onClick}
    currentPage={1}
    />
  )
     
  test("go to previous page btn click",async ()=>{
      const onClick = jest.fn();
      act(() => {
        // render components
         const { getByTestId} = render(<Button onClick={onClick} />);
      });
      fireEvent.click(screen.getByTestId("pagination-btn1-id"));
      expect(onClick).toHaveBeenCalled(); 
  });

  test("go to next page btn click",async ()=>{
      const onClick = jest.fn();
      const { getByTestId} = render(<Button onClick={onClick} />);
      fireEvent.click(screen.getByTestId("pagination-btn3-id"));
      expect(onClick).toHaveBeenCalled();
 
  });
});

