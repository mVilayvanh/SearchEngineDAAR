import Button from './Button';
import { useState } from "react";

const apiUrl = process.env.REACT_APP_SERVER_API_URL;

function SearchBar({setBooks}) {
    const [text, setText] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setBooks([]);
        const res = await fetch(`${apiUrl}/api/books?query="` + text + '"', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        });
        if (res.ok) {
            const data = await res.json();
            console.log(JSON.stringify(data, null, 2));
            setBooks(data);
        }
        setText("");
    };
    return (
        <div className="SearchBar">
            <form onSubmit={handleSubmit}>
                <input 
                    type="text" 
                    placeholder="Search..." 
                    value={text}
                    onChange={(e) => setText(e.target.value)}/>
                <Button onClick={handleSubmit} label="Search"/>
            </form>
        </div>
    );
}

export default SearchBar;