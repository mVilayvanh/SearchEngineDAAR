import Button from './Button';
import React, { useState } from "react";
import "./SearchBar.css";

const apiUrl = "http://localhost:8080";;
function SearchBar({onSearch}) {
    const [text, setText] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!text) return;
        onSearch(text); // <-- on envoie le texte à App
        setText("");
    };
    
    return (
        <div className="SearchBar">
            <input 
                type="text" 
                placeholder="Search..." 
                value={text}
                onChange={(e) => setText(e.target.value)}/>
            <Button onClick={handleSubmit} label="Search"/>
        </div>
    );
}

export default SearchBar;