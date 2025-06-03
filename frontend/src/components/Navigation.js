import { NavLink } from "react-router-dom";
import { useAuth } from "./AuthContext";
import "../App.css";

function Navigation() {
  const { isAuthenticated, logout } = useAuth();

  return (
    <nav className="navigation">
      {isAuthenticated ? (
        <>
          <NavLink
            to="/searchMovies"
            className={({ isActive }) =>
              isActive ? "navigation-link navigation-link-active" : "navigation-link"
            }
          >
            Search Movies
          </NavLink>
          <NavLink
            to="/FavoritesMovies"
            className={({ isActive }) =>
              isActive ? "navigation-link navigation-link-active" : "navigation-link"
            }
          >
            Favorites
          </NavLink>
          <NavLink
            onClick={logout}
            to="/"
            className={({ isActive }) =>
              isActive ? "navigation-link navigation-link-active" : "navigation-link"
            }
          >
            Logout
          </NavLink>
        </>
      ) : (
        <>
          <NavLink
            to="/login"
            className={({ isActive }) =>
              isActive ? "navigation-link navigation-link-active" : "navigation-link"
            }
          >
            Login
          </NavLink>
          <NavLink
            to="/register"
            className={({ isActive }) =>
              isActive ? "navigation-link navigation-link-active" : "navigation-link"
            }
          >
            Register
          </NavLink>
        </>
      )}
    </nav>
  );
}

export default Navigation;