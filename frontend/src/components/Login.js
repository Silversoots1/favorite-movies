import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './AuthContext';
import { API_BASE_URL } from "../apiConfig";


export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    // Check for register success message
    const registerSuccess = location.state && location.state.registerSuccess;

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        const loginData = { username, password };
        try {
            const response = await axios.post(`${API_BASE_URL}/login`, loginData, { withCredentials: true });
            if (response.status === 200) {
                const { userId, username } = response.data;
                login({ userId, username });
                navigate('/searchMovies');
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Login failed for user. Please retry!');
            }
        } catch (error) {
            setError('And error occurred. please retry');
        }
    };

    return (
        <div className="login-card">
            <div className="login-card-content">
                <form className="login-form-box" onSubmit={handleLogin}>
                    <h1 className="login-title">Login</h1>
                    {registerSuccess && (
                        <div className="login-success">Registration successful! Please log in.</div>
                    )}
                    {error && <div className="login-error">{error}</div>}
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        required
                        className="login-textfield"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                        className="login-textfield"
                    />
                    <button type="submit" className="login-button">Login</button>
                </form>
            </div>
        </div>
    );
}