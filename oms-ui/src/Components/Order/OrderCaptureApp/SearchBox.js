import React from 'react';
import Select from 'react-select';

function SearchBox(props) {

  // Pass to parent form
  const handleInputChange = (item) => {
    props.inputChange({
      target: {
        name: props.name,
        value: item,
      },
    });
  };

  const handleOnChange = (item) => {
    props.change({
      target: {
        name: props.name,
        value: item.value,
      },
    });
    props.onBlur({
      target: {
        name: props.name,
        value: item.value,
      }
    });
  };

  return (
    <div className="SearchBox">
      <label htmlFor={props['name']} hidden>{props['aria-label']}</label>
      <Select
        name={props['name']}
        inputId={props['name']}
        placeholder = {props['placeholder']}
        styles={{
          control: styles => ({ 
            ...styles, 
            borderRadius: '10px',
            borderColor: 'white',
            backgroundColor: '#f7f7f9',
            padding: '0.4rem 0rem 0.4rem 0rem' 
          }),
        }}
        options={props['options']}
        onInputChange={handleInputChange}
        onChange={handleOnChange}
      />
    </div>
  );
}

export default SearchBox;
