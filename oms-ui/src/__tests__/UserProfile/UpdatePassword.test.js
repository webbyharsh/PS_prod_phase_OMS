import { act,cleanup, fireEvent, getByTestId, queryByTestId, render, screen, waitFor } from '@testing-library/react';
import routeData from 'react-router';
import OmsAxios from '../../Utils/OmsAxios';
import UpdatePassword from '../../Components/UserProfile/UpdatePassword';

const history = jest.fn();


const location = {
    pathname: "/update-password",
   
}

describe('Update Password Tests', () => {
    beforeEach(() => {
        let mock = jest.fn(() => {
            return Promise.resolve({
                data: [{
                    clientName: "TestUser",
                    clientId: 0
                }],
                ok: true
            });
        });
        OmsAxios.get = mock;
        OmsAxios.post = mock;
    });

    afterEach(() => {
        cleanup();
        jest.clearAllMocks();
    });
    
    test('renders without error', () => {
        act(() => {
            // render components
            render(<UpdatePassword 
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
            render(<UpdatePassword 
                history={history}
                location={location}
            />);
          });
       
        let element = screen.getByTestId('update-password-form');
        fireEvent.click(screen.getByTestId('section2-btn'));
        expect(screen.getByTestId('update-password-form')).toHaveFormValues({
            oldPassword:"",
            newPassword:"",
            confirmPassword:"",
        });
        console.error.mockClear();
    })

    test('renders validation without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<UpdatePassword 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('update-password-form');
        fireEvent.change(screen.getByTestId('input1'), { target: { value: '' } });
        fireEvent.change(screen.getByTestId('input2'), { target: { value: '' } });
        fireEvent.change(screen.getByTestId('input3'), { target: { value: '' } });

        fireEvent.click(screen.getByTestId('section2-btn'));

        fireEvent.submit(element);
        await waitFor(() => {
            expect(spy).not.toHaveBeenCalled();
            expect(screen.getByText('Please enter old password.')).toBeInTheDocument();
            expect(screen.getByText('New Password should be different than old Password.')).toBeInTheDocument();
            expect(screen.getByText('Please enter confirm password.')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('renders validation for same old and new password', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<UpdatePassword 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('update-password-form');
        fireEvent.change(screen.getByTestId('input1'), { target: { value: 'old' } });
        fireEvent.change(screen.getByTestId('input2'), { target: { value: 'old' } });
        fireEvent.change(screen.getByTestId('input3'), { target: { value: 'old' } });

        fireEvent.click(screen.getByTestId('section2-btn'));

        fireEvent.submit(element);
        await waitFor(() => {
            expect(spy).not.toHaveBeenCalled();
            expect(screen.getByText('New Password should be different than old Password.')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('renders validation for same old and new password', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<UpdatePassword 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('update-password-form');
        fireEvent.change(screen.getByTestId('input1'), { target: { value: 'old' } });
        fireEvent.change(screen.getByTestId('input2'), { target: { value: 'new' } });
        fireEvent.change(screen.getByTestId('input3'), { target: { value: 'new1' } });

        fireEvent.click(screen.getByTestId('section2-btn'));

        fireEvent.submit(element);
        await waitFor(() => {
            expect(spy).not.toHaveBeenCalled();
            expect(screen.getByText('Password does not match.')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('renders button without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        const location3 = {
            pathname: "/update-password",
        }
        act(() => {
        render(<UpdatePassword 
            history={history}
            location={location}
        />);
    }); 
        let element = screen.getByTestId('section2-btn');
        fireEvent.click(element);
        expect(screen.getByTestId('update-password-form')).toHaveFormValues({
            oldPassword:"",
            newPassword:"",
            confirmPassword:"",
              });
        console.error.mockClear();
    })

    test('test on input field', async () => {
       

        act(() => {
            // render components
            render(<UpdatePassword 
                history={history}
                location={location}
            />);
          });
        fireEvent.change(screen.getByTestId('input1'), { target: { value: 'old' } });
        fireEvent.change(screen.getByTestId('input2'), { target: { value: 'new' } });
        fireEvent.change(screen.getByTestId('input3'), { target: { value: 'new' } });
        expect(screen.getByTestId('input1')).toHaveValue("old");
        expect(screen.getByTestId('input2')).toHaveValue("new");
        expect(screen.getByTestId('input3')).toHaveValue("new");
    })

    test('renders onChange without error', async () => {
        const handleChange = jest.fn()
        const {container} = render(<input onChange={handleChange} />)
        const input = container.firstChild
        fireEvent.change(input, {target: {value: 'a'}})
        expect(handleChange).toHaveBeenCalledTimes(1)
    })
    
});
