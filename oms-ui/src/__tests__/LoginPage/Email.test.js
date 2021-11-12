import { act,cleanup, fireEvent, getByTestId, queryByTestId, render, screen, waitFor } from '@testing-library/react';
import Email from "../../Components/Login-Register/Email";

const history = jest.fn();



const location = {
    pathname: "/forget-password-email",
    
}

describe('send email Tests', () => {
    
    afterEach(() => {
        cleanup();
        jest.clearAllMocks();
    });
    
    test('renders without error', () => {
        act(() => {
            // render components
            render(<Email
                history={history}
                location={location}
            />);
          });
       
        expect(screen.getByTestId('outer-div-section2')).toBeTruthy();
    });

    test('renders form submit without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<Email 
                history={history}
                location={location}
            />);
          });
       
        let element = screen.getByTestId('email-form');
        fireEvent.click(screen.getByTestId('section2-btn'));
        expect(screen.getByTestId('email-form')).toHaveFormValues({
            email:"",
        });
        console.error.mockClear();
    })

    test('renders validation without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<Email 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('email-form');
        fireEvent.change(screen.getByTestId('input1'), { target: { value: '' } });

        fireEvent.click(screen.getByTestId('section2-btn'));

        fireEvent.submit(element);
        await waitFor(() => {
            expect(spy).not.toHaveBeenCalled();
            expect(screen.getByText('Please enter email.')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('test on input field', async () => {
       
        act(() => {
            // render components
            render(<Email
                history={history}
                location={location}
            />);
          });
        fireEvent.change(screen.getByTestId('input1'), { target: { value: 'aaa@gmail.com' } });
       
        expect(screen.getByTestId('input1')).toHaveValue("aaa@gmail.com");
    })

    test('renders onChange without error', async () => {
        const handleChange = jest.fn()
        const {container} = render(<input onChange={handleChange} />)
        const input = container.firstChild
        fireEvent.change(input, {target: {value: 'a'}})
        expect(handleChange).toHaveBeenCalledTimes(1)
    })
    
});