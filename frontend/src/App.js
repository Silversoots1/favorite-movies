import './App.css';
import Navigation from './components/Navigation';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Register from './components/Register';
import Login from './components/Login';
import { AuthProvider } from './components/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import SearchMovies from './components/SearchMovies';
import { Navigate } from 'react-router-dom';
import FavoritesMovies from './components/FavoritesMovies';


function App() {
  return (
    <div className="App">
      <AuthProvider>
        <BrowserRouter>
          <Navigation></Navigation>
          <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path='/Register' element={<Register></Register>} />
            <Route path='/login' element={<Login></Login>} />
            <Route path='/searchMovies' element={<ProtectedRoute><SearchMovies></SearchMovies></ProtectedRoute>} />
            <Route path='/FavoritesMovies' element={<ProtectedRoute><FavoritesMovies></FavoritesMovies></ProtectedRoute>} />
          </Routes>
        </BrowserRouter>       
      </AuthProvider>

    </div>
  );
}

export default App;