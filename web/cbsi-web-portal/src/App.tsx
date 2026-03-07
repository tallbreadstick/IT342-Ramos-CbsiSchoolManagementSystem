import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import './index.css'
import Header from './components/Header'
import Login from './pages/Login'
import Register from './pages/Register'
import ChangePassword from './pages/ChangePassword'

function Home() {
  return (
    <main className="home">
      <div className="hero card">
        <h2>Welcome to CBSI School Portal</h2>
        <p>Manage students, staff, and school operations.</p>
        <div className="actions">
          <Link to="/login" className="btn">Sign In</Link>
          <Link to="/register" className="btn secondary">Register</Link>
        </div>
      </div>
    </main>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <div className="app-root">
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/change-password" element={<ChangePassword />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}
