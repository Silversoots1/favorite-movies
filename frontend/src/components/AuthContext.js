import React, { createContext, useContext, useState } from 'react'

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext)

export const AuthProvider = ({children}) => {

    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem('isAuthenticated') === 'true'
    })
    const [loginData, setLoginData] = useState(() => {
        return localStorage.getItem('loginData') || ''
    })

    const [username, setUsername] = useState(() => localStorage.getItem('username') || '');

    const [userId, setUserId] = useState(() => localStorage.getItem('userId') || '');

    const login = ({ userId, username }) => {
        setIsAuthenticated(true)
        setUserId(userId);
        setUsername(username)
        localStorage.setItem('isAuthenticated', true);
        localStorage.setItem('userId', userId);
        localStorage.setItem('username', username);
    }

    const logout = () => {
        setIsAuthenticated(false)
        setLoginData('')
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('userId', userId);
        localStorage.removeItem('username', username);
    }
    return (
        <AuthContext.Provider value={{isAuthenticated,  userId, username, login, logout}}>
            {children}
        </AuthContext.Provider>
    )
}