import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate
} from "react-router-dom";

import './App.css'
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import { useAuth } from "./hooks/useAuth";


const AuthRoute = ({ element }) => {
  const token = useAuth();

  return token ? element : <Navigate to="/login" />;
};

function App() {
  return (
    <Router>
      <Routes>
        <Route
          exact
          path="/"
          element={<AuthRoute element={<HomePage />} />}
        />
        <Route exact path="/login" element={<LoginPage />} />
      </Routes>
    </Router>
  )
}

export default App