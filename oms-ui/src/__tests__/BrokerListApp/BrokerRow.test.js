import BrokerRow from "../../Components/Admin/BrokerListApp/BrokerRow";
import { act,cleanup, render,screen,fireEvent,waitFor } from "@testing-library/react";
import { BrowserRouter } from 'react-router-dom';
import OmsAxios from '../../Utils/OmsAxios';
import ReactDom from 'react-dom';



test("Broker List order row without crashing",()=>{
    const div=document.createElement("div");
    act(() => {
      // render components
      ReactDom.render(<BrokerRow/>,div)
    });
});

describe('btn test', () => {
    afterEach(() => {
        cleanup();
        jest.clearAllMocks();
    })
    test("Broker row element render without crashing",()=>{
        act(() => {
              // render components
              render(<BrokerRow key="1"  sno="1" 
              brokerId={1} brokerName="prashant" status={false}/>)
        });
        expect(screen.getByTestId("order-row-tr")).toBeVisible();
        expect(screen.getByTestId("order-row-th")).toBeVisible();
        expect(screen.getByTestId("order-row-td1")).toBeVisible();
        expect(screen.getByTestId("order-row-td2")).toBeVisible();
        expect(screen.getByTestId("order-row-td3")).toBeVisible();
        expect(screen.getByTestId("order-row-btn")).toBeVisible();
    });
});
describe('btn test', () => {
      afterEach(() => {
        cleanup();
        jest.clearAllMocks();
      })
      const Button = ({onClick}) => (
          <BrokerRow key="1"  sno="1" togglefn={onClick}
          brokerId={1} brokerName="prashant" status={false} />
      )
      test("Broker List btn click",async ()=>{
          const onClick = jest.fn();
          act(() => {
            // render components
            const { getByTestId} = render(<Button onClick={onClick} />);
          });
          fireEvent.click(screen.getByTestId("order-row-btn"));
          expect(onClick).toHaveBeenCalled();
        
      });
});


it("renders broker data active", async () => {
  const togglefn = jest.fn();
   // Use the asynchronous version of act to apply resolved promises
   await act( async () => {
   let container = document.createElement("div");
    render( <BrowserRouter>
    <BrokerRow key="1"  sno="1d1" togglefn={togglefn}
              brokerId={1} brokerName="Prashant" status={true}
    /> </BrowserRouter>, container);
  });
  expect(screen.getByTestId("order-row-tr")).toBeVisible();
  expect(screen.getByTestId("order-row-th").textContent).toBe("1");
  expect(screen.getByTestId("order-row-td1").textContent).toBe(" Prashant");
  expect(screen.getByTestId("order-row-td2").textContent).toBe(" Active");

});
it("renders broker data Inactive", async () => {
      const togglefn = jest.fn();
      // Use the asynchronous version of act to apply resolved promises
      await act( async () => {
      let container = document.createElement("div");
      render( <BrowserRouter>   
        <BrokerRow key="1"  sno="1d1" togglefn={togglefn}
                  brokerId={1} brokerName="Prashant" status={false}
        /> </BrowserRouter>, container);
      });
      expect(screen.getByTestId("order-row-tr")).toBeVisible();
      expect(screen.getByTestId("order-row-th").textContent).toBe("1");
      expect(screen.getByTestId("order-row-td1").textContent).toBe(" Prashant");
      expect(screen.getByTestId("order-row-td2").textContent).toBe(" Inactive");
});


/* put button */


describe('toogle button test using mocking put request', () => {
    afterEach(() => {
      cleanup();
      jest.clearAllMocks();
    })
      OmsAxios.put = jest.fn(() => {
            return Promise.resolve(
              {"data": "123", "status":"Batman", "headers":false,"statusText":""}
            );
      });
      const Button = ({onClick}) => (
            <BrokerRow key="1"  sno="1d1" togglefn={onClick}
            brokerId={1} brokerName="Prashant" status={false}
            />
      )
   
      test("toogle button click and check whether it calls in broker toggle",async ()=>{
        const childFunction=jest.fn();
        const onClick = () => childFunction();
        act(() => {
          // render components
                const { getByTestId} = render(<Button  onClick={onClick} />);
        });
        let btn2=screen.getByTestId("order-row-btn");
        await waitFor(() => {
                fireEvent.click(btn2);
        });
      });

});
