

function BookDisplayer({jsonList}) {
    const rows = jsonList.map((book, index) => (
        <tr> 
            <td key={book.title}>{book.title}</td>
            <td key={book.author}>{book.author}</td>
        </tr>)
    );
    return jsonList.length != 0 ? (
        <div className="Books">
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                </tr>
            </thead>
            <tbody>
                {rows}
            </tbody>
        </table>
        </div>
    ) : <div className="Books"></div>
}

export default BookDisplayer