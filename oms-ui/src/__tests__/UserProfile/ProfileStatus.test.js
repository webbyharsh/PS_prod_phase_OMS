import { act,render, screen,cleanup } from '@testing-library/react';
import ProfileStatus from '../../Components/UserProfile/ProfileStatus';

test("Section1  renders order row without crashing",()=>{
  act(() => {
    // render components
    render(<ProfileStatus/>)
  });
 
    expect(screen.getByTestId("h1-section1-id")).toBeVisible();
    expect(screen.getByTestId("div1-section1-id")).toBeVisible();
    expect(screen.getByTestId("div4-section1-id")).toBeVisible();
    expect(screen.getByTestId("img-section1-id")).toBeVisible();
    expect(screen.getByText(/user profile/i)).toBeInTheDocument;
    expect(screen.getByText(/user id/i)).toBeInTheDocument;
    expect(screen.getByText(/created at/i)).toBeInTheDocument;
 })