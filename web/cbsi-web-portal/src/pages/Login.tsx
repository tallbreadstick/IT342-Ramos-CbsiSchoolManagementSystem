import React, { useState } from 'react'
import api from '../api'
import logo from '../assets/cbsi-logo.png'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [schoolId, setSchoolId] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const navigate = useNavigate()

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    try {
      const res = await api.post('/api/auth/login', { schoolId, password })
      const data = res.data
      if (data.mustChangePassword) {
        // send user to change-password flow
        navigate('/change-password', { state: { schoolId } })
        return
      }
      // store token and go to a placeholder dashboard
      if (data.token) {
        localStorage.setItem('cbsi_token', data.token)
      }
      navigate('/')
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Login failed')
    }
  }

  return (
    <main className="auth-page container">
      <div className="w-full max-w-md mx-auto">
        <div className="mb-6 text-center">
          <img src={logo} alt="CBSI" className="mx-auto h-16 w-16 rounded-full mb-3" />
          <h2 className="text-2xl font-semibold text-slate-800">Sign in to your account</h2>
          <p className="text-sm text-slate-600">Enter your School ID and password.</p>
        </div>

        <form onSubmit={submit} className="card space-y-4">
          {error && <div className="p-3 rounded bg-red-50 text-red-700">{error}</div>}

          <div>
            <label className="block text-sm font-medium text-slate-700">School ID</label>
            <input className="mt-1 block w-full rounded border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={schoolId} onChange={e => setSchoolId(e.target.value)} type="text" placeholder="e.g. CBSI2026-001" required />
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700">Password</label>
            <input className="mt-1 block w-full rounded border px-3 py-2" value={password} onChange={e => setPassword(e.target.value)} type="password" required />
          </div>

          <div className="flex justify-end">
            <button type="submit" className="btn">Sign in</button>
          </div>
        </form>
      </div>
    </main>
  )
}
