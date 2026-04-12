import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import './index.css'
import Header from './components/Header'
import Login from './pages/Login'
import ChangePassword from './pages/ChangePassword'
import AccountCreation from './pages/AccountCreation'
import UserRecords from './pages/UserRecords'

function Home() {
  return (
    <main className="home">
      <div className="hero card">
        <h2>Welcome to CBSI School Portal</h2>
        <p>Manage students, staff, and school operations.</p>
        <div className="actions">
          <Link to="/login" className="btn">Sign In</Link>
          <Link to="/account-creation" className="btn secondary">Create Account</Link>
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
          <Route path="/account-creation" element={<AccountCreation />} />
          <Route path="/user-records" element={<UserRecords />} />
          <Route path="/change-password" element={<ChangePassword />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}
