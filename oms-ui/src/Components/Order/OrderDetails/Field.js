import "./Field.css"

export default function Field(props){
 
    return (
        <div className="field navbar-dark bg-primary">
            <p> {props.Key} : {props.data}</p>
        </div>
    )
}