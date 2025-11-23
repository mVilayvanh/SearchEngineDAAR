
import './App.css';
import { useState } from 'react';
import SearchBar from './component/SearchBar';
import BookDisplayer from './component/BookDisplayer';

function App() {
  const [books, setBooks] = useState([]);
  return (
    <div className="App">
      <SearchBar setBooks={setBooks} />
      <BookDisplayer jsonList={books} />
    </div>
  );
}

export default App;
