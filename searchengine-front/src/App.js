import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SearchBar from "./components/SearchBar";
import BookDetail from "./components/BookDetail";
import "./App.css";

const apiUrl = "http://localhost:8080";

function App() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (query) => {
    setLoading(true);
    try {
      const res = await fetch(`${apiUrl}/api/books?query=${encodeURIComponent(query)}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      });
      if (res.ok) {
        const data = await res.json();
        setBooks(data.books || data);
      } else {
        setBooks([]);
      }
    } catch (err) {
      console.error(err);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <div className="App">
              <header className="App-header">
                <h1>DPG</h1>
              </header>

              <main className="App-main">
                <SearchBar onSearch={handleSearch} />
                {loading && <div className="loader"></div>}

                {books.length === 0 && !loading && <p>Aucun livre trouvé</p>}

                <ul className="book-list">
                  {books.map((book) => (
                    <li key={book.id}>
                      <strong>{book.title}</strong> - {book.author}{" "}
                      <Link to={`/book/${book.id}`}>Voir le livre</Link>
                    </li>
                  ))}
                </ul>
              </main>

              <footer className="App-footer">
                &copy; 2025 Mickaël & Elise - DAAR Project 3
              </footer>
            </div>
          }
        />
        <Route path="/book/:id" element={<BookDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
