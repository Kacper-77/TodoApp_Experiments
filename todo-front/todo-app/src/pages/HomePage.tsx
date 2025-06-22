import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const navigate = useNavigate();

    return (
        <div className="home-container">
            <div className="top-right-buttons">
                <button onClick={() => navigate("/login")}>Sign in</button>
                <button onClick={() => navigate("/register")}>Sign up</button>
            </div>
            <div className="title">
                <h1>Welcome in TodoApp</h1>
            </div>
        </div>
    );
};

export default HomePage;