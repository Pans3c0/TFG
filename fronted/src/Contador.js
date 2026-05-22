import { useEffect, useState } from "react";

function Contador() {
  const [count, setCount] = useState(0);
  useEffect(() => {
    const interval = setInterval(() => {
      setCount((c) => c + 1);
    }, 1000);

    return () => clearInterval(interval);
  });

  return (
    <div>
      <h2>Contador: {count}</h2>
    </div>
  );
}

export default Contador;
