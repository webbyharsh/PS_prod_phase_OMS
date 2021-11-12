import {Form,Row,Col} from 'react-bootstrap';
import moment from 'moment';
import { EmailSchema } from './search';
import * as Yup from 'yup';
import 'react-datepicker/dist/react-datepicker.css';
const Filter = (props) => {

    const updateField = e => {  e.preventDefault();props.setFormData({  ...props.formData,  [e.target.name] : e.target.value  }); }
    const handleValidateOnBlur = (e) => {
        Yup.reach(EmailSchema, e.target.name).validate(e.target.value).then((value) => console.log(value)).catch((err) => console.log(err));
    }
  
    return (<> 
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label data-testid="form-email" column sm={2}>
             Client's Email </Form.Label>
        <Col sm={10}>
      
            <Form.Control  data-testid="email-control" type="email" name="clientEmail"  value={ props.formData.clientEmail }  onChange={updateField}     onBlur={handleValidateOnBlur}   placeholder="Enter Email ID" />
        </Col>
    </Form.Group>
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label  data-testid="form-client-name" column sm={2}>
            Client Name
        </Form.Label>
        <Col sm={10}>
            <Form.Control type="text" name="clientName" data-testid="client-name-control" onChange={updateField} placeholder="Enter Client's Name" />
        </Col>
    </Form.Group>
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label  data-testid="form-stock-name" column sm={2}>
            Stock Name
        </Form.Label>
        <Col sm={10}>
            <Form.Control type="text" name="stock" data-testid="stock-name-control" onChange={updateField} placeholder="Enter Stock Name" />
        </Col>
    </Form.Group>
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label  data-testid="form-order-type" column sm={2}>
            Order Type
        </Form.Label>
        <Col sm={10}>
        <select name="type" className="form-select" onChange={updateField} id="side"  data-testid="stock-side-select"  >
      <option data-testid="select-market-target" value="select-type">Market / Limit</option>
        <option data-testid="select-market-price" value="MARKET">Market Price</option>
        <option data-testid="select-target-price" value="LIMIT">Limit Price</option>
        </select>
        </Col>
    </Form.Group>
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label data-testid="start-date" column sm={2}>
            Start Date
        </Form.Label>
        <Col sm={10}><Form.Control type="date" data-testid="start-date-control" value={moment(props.formData.startDate?.toISOString()).format("YYYY-MM-DD")}
         onChange={(e) => { props.setFormData({  ...props.formData,[e.target.name]: e.target.valueAsDate});}} name="startDate"  max={moment().format("YYYY-MM-DD")}   /> </Col>
    </Form.Group>
    <Form.Group as={Row} className="mb-3" controlId="formHorizontal">
        <Form.Label data-testid="end-date" column sm={2}>
            End Date
        </Form.Label>
        <Col sm={10}><Form.Control data-testid="end-date-control" type="date" value={moment(props.formData.endDate?.toISOString()).format("YYYY-MM-DD")}
         onChange={(e) => {  props.setFormData({   ...props.formData, [e.target.name]: e.target.valueAsDate  }); }} name="endDate"  max={moment().format("YYYY-MM-DD")} min={moment(props.formData.startDate?.toISOString()).format("YYYY-MM-DD")}/></Col>
    </Form.Group>
    </>);
}
export default Filter;