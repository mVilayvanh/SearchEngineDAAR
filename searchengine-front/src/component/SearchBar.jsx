import Button from './Button';
import React, { useState } from "react";
import REACT_APP_SERVER_API_URL from '../.env';

const apiUrl = "http://localhost:8080";
function SearchBar() {
    const [text, setText] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
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