import React, { useState } from 'react'
import api from '../api'

export default function Register() {
  const [form, setForm] = useState({
    email: '',
    firstName: '',
    lastName: '',
    middleName: '',
    sex: 'MALE',
    dateOfBirth: '',
    permanentAddress: '',
    currentAddress: '',
  })
  const [message, setMessage] = useState<string | null>(null)
  const [generated, setGenerated] = useState<string | null>(null)
  const [schoolId, setSchoolId] = useState<string | null>(null)

  const update = (k: string, v: any) => setForm(prev => ({ ...prev, [k]: v }))

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    setMessage(null)
    setGenerated(null)
    try {
      const res = await api.post('/api/auth/register', form)
      setMessage(res.data.message || 'Registered')
      setGenerated(res.data.generatedPassword)
      setSchoolId(res.data.schoolId || null)
    } catch (err: any) {
      setMessage(err?.response?.data?.message || 'Registration failed')
    }
  }

  return (
    <main className="auth-page container">
      <div className="w-full max-w-2xl">
        <div className="mb-6">
          <h2 className="text-2xl font-semibold text-slate-800">Create an Account</h2>
          <p className="text-sm text-slate-600">Create a new CBSI account. A temporary password will be shown after registration.</p>
        </div>

        <form onSubmit={submit} className="card space-y-4">
          {message && <div className="p-3 rounded bg-emerald-50 text-emerald-700">{message}</div>}
          {generated && <div className="p-3 rounded bg-yellow-50 text-yellow-800">Temporary password: <strong>{generated}</strong></div>}
          {schoolId && <div className="p-3 rounded bg-sky-50 text-sky-800">Assigned School ID: <strong>{schoolId}</strong></div>}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700">Email</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" type="email" value={form.email} onChange={e => update('email', e.target.value)} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">First name</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" value={form.firstName} onChange={e => update('firstName', e.target.value)} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Last name</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" value={form.lastName} onChange={e => update('lastName', e.target.value)} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Middle name</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" value={form.middleName} onChange={e => update('middleName', e.target.value)} />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Sex</label>
              <select className="mt-1 block w-full rounded border px-3 py-2" value={form.sex} onChange={e => update('sex', e.target.value)}>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Date of birth</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" type="date" value={form.dateOfBirth} onChange={e => update('dateOfBirth', e.target.value)} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Permanent address</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" value={form.permanentAddress} onChange={e => update('permanentAddress', e.target.value)} />
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700">Current address</label>
              <input className="mt-1 block w-full rounded border px-3 py-2" value={form.currentAddress} onChange={e => update('currentAddress', e.target.value)} />
            </div>
          </div>

          <div className="flex justify-end">
            <button type="submit" className="inline-flex items-center px-4 py-2 bg-sky-600 text-white rounded">Register</button>
          </div>
        </form>
      </div>
    </main>
  )
}
