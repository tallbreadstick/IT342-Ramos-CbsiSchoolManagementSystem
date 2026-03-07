import React, { useState } from 'react'
import api from '../api'
import { useLocation, useNavigate } from 'react-router-dom'

export default function ChangePassword() {
  const nav = useNavigate()
  const location = useLocation()
  const state: any = location.state || {}
  const [email, setEmail] = useState(state.email || '')
  const [oldPassword, setOldPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [msg, setMsg] = useState<string | null>(null)

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    setMsg(null)
    try {
      const res = await api.post('/api/auth/change-password', { email, oldPassword, newPassword })
      setMsg(res.data.message || 'Password changed')
      // After changing password, navigate to login
      setTimeout(() => nav('/login'), 1500)
    } catch (err: any) {
      setMsg(err?.response?.data?.message || 'Failed to change password')
    }
  }

  return (
    <main className="auth-page container">
      <div className="w-full max-w-md">
        <div className="mb-6">
          <h2 className="text-2xl font-semibold text-slate-800">Change Password</h2>
          <p className="text-sm text-slate-600">You must change your temporary password on first login.</p>
        </div>

        <form onSubmit={submit} className="card space-y-4">
          {msg && <div className="p-3 rounded bg-emerald-50 text-emerald-700">{msg}</div>}

          <div>
            <label className="block text-sm font-medium text-slate-700">Email</label>
            <input className="mt-1 block w-full rounded border px-3 py-2" value={email} onChange={e => setEmail(e.target.value)} type="email" required />
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700">Old password</label>
            <input className="mt-1 block w-full rounded border px-3 py-2" value={oldPassword} onChange={e => setOldPassword(e.target.value)} type="password" required />
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700">New password</label>
            <input className="mt-1 block w-full rounded border px-3 py-2" value={newPassword} onChange={e => setNewPassword(e.target.value)} type="password" required />
          </div>

          <div className="flex justify-end">
            <button type="submit" className="inline-flex items-center px-4 py-2 bg-sky-600 text-white rounded">Change password</button>
          </div>
        </form>
      </div>
    </main>
  )
}
