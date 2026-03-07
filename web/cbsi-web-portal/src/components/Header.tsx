import React from 'react'
import { Link } from 'react-router-dom'

export default function Header() {
  return (
    <header className="site-header">
      <div className="container flex items-center justify-between">
        <div className="brand flex flex-col">
          <h1 className="text-lg font-semibold">CBSI School Portal</h1>
          <span className="text-sm text-gray-600">Student & Staff Management</span>
        </div>
        <nav className="space-x-4">
          <Link to="/" className="text-sm text-slate-700 hover:text-slate-900">Home</Link>
          <Link to="/login" className="text-sm text-slate-700 hover:text-slate-900">Login</Link>
          <Link to="/register" className="text-sm text-slate-700 hover:text-slate-900">Register</Link>
        </nav>
      </div>
    </header>
  )
}
