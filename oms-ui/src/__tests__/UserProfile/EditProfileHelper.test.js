import { act,cleanup, fireEvent, getByTestId, queryByTestId, render, screen, waitFor } from '@testing-library/react';
import routeData from 'react-router';
import OmsAxios from '../../Utils/OmsAxios';
import EditProfileHelper from '../../Components/UserProfile/EditProfileHelper';

const history = jest.fn();

const finalUsers1 = {
    userId: 2,
    role: {
        roleName: "Admin"
    },
    name: "Riya",
    age: 23,
    contact: "9999999999",
    address: {
        street:"Publicis Sapient",
        city:"Bangalore",
        state:"Karnataka",
        country:"India",
    },
    emailId: "email@publicisgroupe.com",
    lastActiveAt: "29-07-2021",
    createdAt: "12-12-2020",
    isActive: false
}

const finalUsers3 = {
    userId: 2,
    role: {
        roleName: "Broker"
    },
    name: "Prashant",
    age: 22,
    contact: "9999999999",
    address: {
        street:"Publicis Sapient",
        city:"Gurgaon",
        state:"Haryana",
        country:"India",
    },
    emailId: "email@publicisgroupe.com",
    lastActiveAt: "29-07-2021",
    createdAt: "12-12-2020",
    isActive: false
}

const location = {
    pathname: "/edit-user-profile",
    state: {
        finalUsers:finalUsers1}
}

beforeEach(() => {
    jest.spyOn(routeData, 'useLocation').mockReturnValue(location)
});

describe('EditProfileHelper Tests', () => {
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
            render(<EditProfileHelper 
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
            render(<EditProfileHelper 
                history={history}
                location={location}
            />);
          });
       
        let element = screen.getByTestId('edit-profile-form');
        fireEvent.click(screen.getByTestId('section2-btn'));
        expect(screen.getByTestId('edit-profile-form')).toHaveFormValues({
            name: "Riya",
            age: 23,
            contact: "9999999999",
            street:"Publicis Sapient",
            city:"Bangalore",
            state:"Karnataka",
            country:"India",
        });
        console.error.mockClear();
    })

    test('renders validation without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<EditProfileHelper 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('edit-profile-form');
        fireEvent.change(screen.getByLabelText(/name/i), { target: { value: '' } });
        fireEvent.change(screen.getByLabelText(/age/i), { target: { value: 0 } });
        fireEvent.change(screen.getByLabelText(/contact/i), { target: { value: '' } });
        fireEvent.change(screen.getByLabelText(/street/i), { target: { value: '' } });
        fireEvent.change(screen.getByLabelText(/city/i), { target: { value: '' } });
        fireEvent.change(screen.getByLabelText(/state/i), { target: { value: '' } });
        fireEvent.change(screen.getByLabelText(/country/i), { target: { value: '' } });

        fireEvent.click(screen.getByTestId('section2-btn'));

        fireEvent.submit(element);
        await waitFor(() => {
            expect(spy).not.toHaveBeenCalled();
            expect(screen.getByText('Please enter your name.')).toBeInTheDocument();
            expect(screen.getByText('Please enter valid age input')).toBeInTheDocument();
            expect(screen.getByText('Please enter your contact.')).toBeInTheDocument();
            expect(screen.getByText('Please enter street')).toBeInTheDocument();
            expect(screen.getByText('Please enter city')).toBeInTheDocument();
            expect(screen.getByText('Please enter state')).toBeInTheDocument();
            expect(screen.getByText('Please enter country')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('renders validation 2 without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        act(() => {
            // render components
            render(<EditProfileHelper 
                history={history}
                location={location}
            />);
          });
       

        let element = screen.getByTestId('edit-profile-form');
        fireEvent.change(screen.getByLabelText(/name/i), { target: { value: 'nmjKCPv0DlT'+
        '3Et8UMuOcNAURLP0AHhKXKCkw3ufG8w1OrTfGqTsDmMMyFXUQhZv9sCzYYVnFsT7dWEqUxF7joYdwank'+
        'yUHy96npDRKoMejOMKqoVAmWO8TKnpKURVaisIS5aDABgFu0GT7UGZE2TOY12'} });
        fireEvent.change(screen.getByLabelText(/age/i), { target: { value: 200 } });
        fireEvent.change(screen.getByLabelText(/contact/i), { target: { value: '21324fgh' } });
        fireEvent.change(screen.getByLabelText(/street/i), { target: { value: 'nmjKCPv0DlT'+
        '3Et8UMuOcNAURLP0AHhKXKCkw3ufG8w1OrTfGqTsDmMMyFXUQhZv9sCzYYVnFsT7dWEqUxF7joYdwank'+
        'yUHy96npDRKoMejOMKqoVAmWO8TKnpKURVaisIS5aDABgFu0GT7UGZE2TOY12'} });
        fireEvent.change(screen.getByLabelText(/city/i), { target: { value: '9QHm2ClTfO'+
        'cra3CBmWp76Xqtdv7p2beOZQXIpuzIlLLbPRqpmH12' } });
        fireEvent.change(screen.getByLabelText(/state/i), { target: { value: '9QHm2ClTfO'+
        'cra3CBmWp76Xqtdv7p2beOZQXIpuzIlLLbPRqpmH12' } });
        fireEvent.change(screen.getByLabelText(/country/i), { target: { value: '9QHm2ClTfO'+
        'cra3CBmWp76Xqtdv7p2beOZQXIpuzIlLLbPRqpmH12' } });

        fireEvent.click(screen.getByTestId('section2-btn'));
        await waitFor(() => {
            expect(screen.getByText('Name length should be less than 150.')).toBeInTheDocument();
            expect(screen.getByText('Please enter valid age input')).toBeInTheDocument();
            expect(screen.getByText('Please enter valid contact.')).toBeInTheDocument();
            expect(screen.getByText('Street length should be less than 150.')).toBeInTheDocument();
            expect(screen.getByText('City length should be less than 50.')).toBeInTheDocument();
            expect(screen.getByText('State length should be less than 50.')).toBeInTheDocument();
            expect(screen.getByText('Country length should be less than 50.')).toBeInTheDocument();
        });
        spy.mockRestore();
    })

    test('renders button without error', async () => {
        let spy = jest.spyOn(console, "error").mockImplementation();
        const location3 = {
            pathname: "/edit-user-profile",
            state: {
                finalUsers:finalUsers3}
        }
        render(<EditProfileHelper 
            history={history}
            location={location3}
        />);
        let element = screen.getByTestId('section2-btn');
        fireEvent.click(element);
        expect(screen.getByTestId('edit-profile-form')).toHaveFormValues({
            name: "Prashant",
            age: 22,
            contact: "9999999999",
            street:"Publicis Sapient",
            city:"Gurgaon",
            state:"Haryana",
            country:"India"
              });
        console.error.mockClear();
    })

    test('test on name input field', async () => {
        act(() => {
            // render components
            render(<EditProfileHelper 
                history={history}
                location={location}
            />);
          });
       
        expect(screen.getByLabelText(/name/i)).toHaveValue("Riya");
        expect(screen.getByLabelText(/age/i)).toHaveValue(23);
        expect(screen.getByLabelText(/contact/i)).toHaveValue("9999999999");
        expect(screen.getByLabelText(/street/i)).toHaveValue("Publicis Sapient");
        expect(screen.getByLabelText(/city/i)).toHaveValue("Bangalore");
        expect(screen.getByLabelText(/state/i)).toHaveValue("Karnataka");
        expect(screen.getByLabelText(/country/i)).toHaveValue("India");
    })

    test('renders onChange without error', async () => {
        const handleChange = jest.fn()
        const {container} = render(<input onChange={handleChange} />)
        const input = container.firstChild
        fireEvent.change(input, {target: {value: 'a'}})
        expect(handleChange).toHaveBeenCalledTimes(1)
    })
    
});
