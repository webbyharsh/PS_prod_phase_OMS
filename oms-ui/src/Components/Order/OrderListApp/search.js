import React, { useState } from 'react';
import { Modal,Button } from 'react-bootstrap';
import Filter from './filter';
import './search.css';
import * as Yup from 'yup';
import moment from 'moment'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from '@fortawesome/free-solid-svg-icons';

export const EmailSchema = Yup.object().shape({
    clientEmail: Yup.string().nullable(true).email('Enter a valid email'),
});
const Search = (props) => {

    const [show, setShow] = useState(false);
    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);
    const [form, setForm] = useState({ clientEmail: null, clientName: null, stock: null, type: null, startDate: null, endDate: null });
    const printForm = async () => {
        handleClose();

        EmailSchema.validate(form, { abortEarly: false })
            .then(async (values) => {
              
                if (values.startDate === null)
                    values.startDate = moment();
                if (values.endDate === null)
                    values.endDate = moment().add(1, 'days');
                else
                    values.endDate = moment(values.endDate).add(1, 'days');
              
                await props.searchOrder(values, 0);
                props.setAllOrderCall(false)
                setForm({ clientEmail: null, clientName: null, stock: null, type: null, startDate: null, endDate: null })
                await props.setFormValue(values);
            })
            .catch((err) => {});
    }
    const cancelSearch = async () => {
        props.fetchData(0)
        props.setAllOrderCall(false)
    }
    return (<>

        <Button class="btn btn-outline-primary" data-testid="cancel-search" variant="primary" onClick={cancelSearch}>
            All Orders
    </Button>
        <Button class="btn btn-outline-primary" style={{ float: 'right' }} data-testid="search-order" variant="primary" onClick={handleShow}>
            Search Order
        <span> <FontAwesomeIcon icon={faSearch} /></span>
        </Button>


        <Modal data-testid="modal-visible" show={show} onHide={handleClose} centered backdrop="static">
            <Modal.Header closeButton>
                <Modal.Title data-testid="modal-title"> Search Order </Modal.Title>
            </Modal.Header>

            <Modal.Body data-testid="modal-body" >
                <Filter formData={form} setFormData={setForm} />
            </Modal.Body>

            <Modal.Footer data-testid="modal-footer" >
                <Button data-testid="close-btn" variant="secondary" onClick={handleClose}>Close</Button>
                <Button data-testid="search-btn" variant="primary" onClick={printForm}>Search</Button>
            </Modal.Footer>
        </Modal>

    </>);
}

export default Search;