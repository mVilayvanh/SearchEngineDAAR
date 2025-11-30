import Button from './Button';
import { useState } from "react";

const apiUrl = process.env.REACT_APP_SERVER_API_URL;

function SearchBar({ setBooks }) {

    const [text, setText] = useState("");
    const [advanced, setAdvanced] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setBooks([]);

        const endpoint = advanced ? "/api/advanced-search" : "/api/search";

        const res = await fetch(
            `${apiUrl}${endpoint}?query=${encodeURIComponent(text)}`,
            {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            }
        );

        if (res.ok) {
            const data = await res.json();
            setBooks(data);
        }

        setText("");
    };

    return (
        <div className="SearchBar">
            <div className="search-mode">
                <label className="switch">
                    <input
                        type="checkbox"
                        checked={advanced}
                        onChange={() => setAdvanced(!advanced)}
                    />
                    <span className="slider round"></span>
                </label>
                <span style={{ marginLeft: "10px" }}>
                    {advanced ? "Recherche avancée (regex)" : "Recherche simple"}
                </span>
            </div>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder={advanced ? "Regex pattern..." : "Search..."}
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                />
                <Button onClick={handleSubmit} label="Search" />
            </form>

        </div>
    );
}

export default SearchBar;
