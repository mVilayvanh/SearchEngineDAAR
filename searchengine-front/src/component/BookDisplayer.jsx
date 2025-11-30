
function BookDisplayer({ jsonList }) {

    const openBookInNewTab = (book) => {
        const newWindow = window.open("", "_blank");

        if (newWindow) {
            newWindow.document.write(`
                <pre style="white-space: pre-wrap; font-family: monospace; padding: 20px;">
${book.content.replace(/</g, "&lt;").replace(/>/g, "&gt;")}
                </pre>
            `);
            newWindow.document.title = book.title;
        }
    };

    const rows = jsonList.map((book) => (
        <tr key={book.id}>
            <td>{book.title}</td>
            <td>{book.author}</td>
            <td>
                <button onClick={() => openBookInNewTab(book)}>
                    View
                </button>
            </td>
        </tr>
    ));

    return jsonList.length !== 0 ? (
        <div className="Books">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>View</th>
                    </tr>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        </div>
    ) : (
        <div className="Books"></div>
    );
}

export default BookDisplayer;
