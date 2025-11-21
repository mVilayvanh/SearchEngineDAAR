import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import "./BookDetail.css";

const apiUrl = "http://localhost:8080";

function BookDetail() {
  const { id } = useParams();
  const [book, setBook] = useState(null);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const res = await fetch(`${apiUrl}/api/books/${id}`, {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        });
        if (res.ok) {
          const data = await res.json();
          setBook(data);
        }
      } catch (err) {
        console.error(err);
      }
    };

    fetchBook();
  }, [id]);

  if (!book) return <p>Chargement...</p>;

  return (
    <div className="App">
        <header className="App-header">
            <h1>DPG</h1>
        </header>

        <main className="App-main">
            <div className="BookDetail">
                <h2>{book.title}</h2>
                <p><strong>Auteur :</strong> {book.author}</p>
                <p><strong>Date de publication :</strong> {book.created_at}</p>
                <p><strong>Nombre de recherche :</strong> {book.clicks}</p>
                <p><strong>URL de téléchargement:</strong> {book.url}</p>
                <p><strong>Contenu :</strong> {book.content}</p>
                <Link to="/">Retour à la recherche</Link>
            </div>
        </main>

        <footer className="App-footer">
            &copy; 2025 Mickaël & Elise - DAAR Project 3
        </footer>
    </div>
  );
}

export default BookDetail;
