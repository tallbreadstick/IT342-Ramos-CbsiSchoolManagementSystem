import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import logo from '../assets/cbsi-logo.png'

export default function Header() {
  const [open, setOpen] = useState(false)
  return (
    <header className="bg-white border-b sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex items-center gap-3">
            <img src={logo} alt="CBSI logo" className="h-10 w-10 rounded-full object-cover" />
            <div className="hidden sm:block">
              <div className="text-sm font-semibold">CBSI School Portal</div>
              <div className="text-xs text-gray-500">Student & Staff Management</div>
            </div>
          </div>

          <nav className="hidden md:flex items-center space-x-3">
            <Link to="/" className="px-3 py-2 rounded text-sm text-gray-700 hover:bg-gray-100">Home</Link>
            <Link to="/account-creation" className="px-3 py-2 rounded text-sm text-gray-700 hover:bg-gray-100">Account Creation</Link>
            <Link to="/user-records" className="px-3 py-2 rounded text-sm text-gray-700 hover:bg-gray-100">User Records</Link>
            <Link to="/login" className="px-3 py-2 rounded text-sm text-gray-700 hover:bg-gray-100">Login</Link>
          </nav>

          <div className="md:hidden">
            <button onClick={() => setOpen(!open)} className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:bg-gray-100">
              <span className="sr-only">Open main menu</span>
              <svg className="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                {open ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>
        </div>
      </div>

      {open && (
        <div className="md:hidden bg-white border-t">
          <div className="px-4 pt-2 pb-4 space-y-1">
            <Link to="/" className="block text-gray-700 py-2">Home</Link>
            <Link to="/account-creation" className="block text-gray-700 py-2">Account Creation</Link>
            <Link to="/user-records" className="block text-gray-700 py-2">User Records</Link>
            <Link to="/login" className="block text-gray-700 py-2">Login</Link>
          </div>
        </div>
      )}
    </header>
  )
}
