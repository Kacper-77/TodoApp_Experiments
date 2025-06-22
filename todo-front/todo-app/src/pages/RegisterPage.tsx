import { useState } from "react";
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [age, setAge] = useState<string>("");
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const [error, setError] = useState<string>("");
  const [success, setSuccess] = useState<boolean>(false);

  const handleRegistration = async () => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/auth/register-page`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username,
            email,
            password,
            age: Number(age), // konwersja stringa na liczbę
            phoneNumber,
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Please pass valid data.");
      }

      setSuccess(true);
      setError("");

      navigate("/dashboard")
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Registration error.";
      setError(errorMessage);
      setSuccess(false);
    }
  };

  return (
    <div className="app-container">
      <div className="todo-app-title">
        <h2 onClick={() => navigate("/")}>TodoApp</h2>
      </div>

      <h2 className="animated-text">Register</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      /><br />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      /><br />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      /><br />
      <input
        type="number"
        placeholder="Age"
        value={age}
        onChange={(e) => setAge(e.target.value)}
      /><br />
      <input
        type="tel"
        placeholder="Phone Number"
        value={phoneNumber}
        onChange={(e) => setPhoneNumber(e.target.value)}
      /><br />
      <button onClick={handleRegistration}>Sign up</button>
      {success && <p style={{ color: "lime" }}>Signed up!</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default RegisterPage;
