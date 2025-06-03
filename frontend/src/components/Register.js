import axios from 'axios'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { API_BASE_URL } from "../apiConfig";

export default function Register() {
    const [formData, setFormData] = useState({
        firstname: '',
        lastname: '',
        username: '',
        password: '',
        confirmPassword: ''
    })
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            return;
        }
        setError('')
        try {
            const response = await axios.post(`${API_BASE_URL}/register`, formData);
            if(response.status === 201) {
                navigate('/login', { state: { registerSuccess: true } })
            } else {
                const errorText = await response.text();
                setError(errorText)
            }
        } catch(err) {
            setError('An error occured during user Register')
        }
    }

    return (
        <div className="register-card">
            <div className="register-card-content">
                <form className="register-form-box" onSubmit={handleSubmit}>
                    <h1 className="register-title">Register</h1>
                    {error && <div className="register-error">{error}</div>}
                    <input
                        type="text"
                        name="firstname"
                        placeholder="First Name"
                        value={formData.firstname}
                        onChange={handleChange}
                        required
                        className="register-textfield"
                    />
                    <input
                        type="text"
                        name="lastname"
                        placeholder="Last Name"
                        value={formData.lastname}
                        onChange={handleChange}
                        required
                        className="register-textfield"
                    />
                    <input
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                        className="register-textfield"
                    />
                    <input
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        className="register-textfield"
                    />
                    <input
                        type="password"
                        name="confirmPassword"
                        placeholder="Confirm Password"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                        className="register-textfield"
                    />
                    <button type="submit" className="register-button">Register</button>
                </form>
            </div>
        </div>
    )
}