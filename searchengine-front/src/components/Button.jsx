import React from "react";

function Button({ label, onClick }) {
  return (
    <button className="Button" onClick={onClick}>
      {label}
    </button>
  );
}

export default Button;