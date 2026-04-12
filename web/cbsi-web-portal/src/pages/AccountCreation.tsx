import React, { useEffect, useRef, useState } from 'react'
import api from '../api'

type SingleForm = {
  email: string
  firstName: string
  lastName: string
  middleName?: string
  sex: 'Male' | 'Female'
  dateOfBirth: string
  permanentAddress?: string
  currentAddress?: string
  role: 'S' | 'F' | 'A'
  yearFirstEnrolled?: number
}

export default function AccountCreation() {
  const [modeBatch, setModeBatch] = useState(false)
  const [form, setForm] = useState<SingleForm>({
    email: '',
    firstName: '',
    lastName: '',
    middleName: '',
    sex: 'Male',
    dateOfBirth: '',
    permanentAddress: '',
    currentAddress: '',
    role: 'S',
  })
  const [file, setFile] = useState<File | null>(null)
  const [result, setResult] = useState<any>(null)
  const [sameAsPermanent, setSameAsPermanent] = useState(false)

  const [pendingRows, setPendingRows] = useState<any[]>([])
  const [loadingPending, setLoadingPending] = useState(false)

  const fetchPending = async () => {
    setLoadingPending(true)
    try {
      const res = await api.get('/api/admin/users/pending')
      setPendingRows(res.data || [])
    } catch (err) {
      setPendingRows([])
    } finally {
      setLoadingPending(false)
    }
  }

  useEffect(() => { fetchPending() }, [])

  useEffect(() => {
    if (sameAsPermanent) {
      setForm(prev => ({ ...prev, currentAddress: prev.permanentAddress }))
    }
  }, [sameAsPermanent, form.permanentAddress])

  const submitSingle = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const payload = {
        email: form.email,
        firstName: form.firstName,
        lastName: form.lastName,
        middleName: form.middleName,
        sex: form.sex,
        dateOfBirth: form.dateOfBirth,
        permanentAddress: form.permanentAddress,
        currentAddress: form.currentAddress,
        role: form.role,
        yearFirstEnrolled: form.yearFirstEnrolled,
      }
      const res = await api.post('/api/admin/accounts/single', payload)
      setResult(res.data)
      fetchPending()
    } catch (err: any) {
      setResult(err.response?.data || { message: 'Error' })
    }
  }

  const submitBatch = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!file) return setResult({ message: 'No file selected' })
    try {
      const fd = new FormData()
      fd.append('file', file)
      const res = await api.post('/api/admin/accounts/batch', fd, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      setResult(res.data)
      fetchPending()
    } catch (err: any) {
      setResult(err.response?.data || { message: 'Error' })
    }
  }

  const fileInputRef = useRef<HTMLInputElement | null>(null)
  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    const f = e.dataTransfer?.files?.[0]
    if (f) setFile(f)
  }

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const f = e.target.files?.[0]
    if (f) setFile(f)
  }

  return (
    <div className="w-full mx-auto p-6">
      <h2 className="text-2xl font-semibold mb-4">Account Creation</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <div className="mb-4">
            <div className="inline-flex rounded-md bg-white p-1 shadow-sm">
              <button type="button" onClick={() => setModeBatch(false)} className={`px-4 py-2 seg-btn ${!modeBatch ? 'seg-active' : 'text-gray-700'}`}>
                Single
              </button>
              <button type="button" onClick={() => setModeBatch(true)} className={`px-4 py-2 seg-btn ${modeBatch ? 'seg-active' : 'text-gray-700'}`}>
                Batch
              </button>
            </div>
            <p className="text-sm text-gray-500 mt-2">Select single or batch account creation.</p>
          </div>

          {!modeBatch ? (
            <form onSubmit={submitSingle} className="bg-white p-6 rounded-lg card">
              <div className="flex items-center justify-between mb-4">
                <div>
                  <h4 className="text-lg font-medium">Create Single Account</h4>
                  <p className="text-sm text-gray-500">Fill in the user's details below.</p>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium">Email</label>
                  <input placeholder="user@example.com" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
                </div>

                <div>
                  <label className="block text-sm font-medium">Role</label>
                  <select className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.role} onChange={e => setForm({ ...form, role: e.target.value as any })}>
                    <option value="S">Student</option>
                    <option value="F">Faculty</option>
                    <option value="A">Staff</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium">First name</label>
                  <input placeholder="First" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.firstName} onChange={e => setForm({ ...form, firstName: e.target.value })} required />
                </div>

                <div>
                  <label className="block text-sm font-medium">Last name</label>
                  <input placeholder="Last" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.lastName} onChange={e => setForm({ ...form, lastName: e.target.value })} required />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium">Middle name</label>
                  <input placeholder="Middle (optional)" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.middleName} onChange={e => setForm({ ...form, middleName: e.target.value })} />
                </div>

                <div>
                  <label className="block text-sm font-medium">Sex</label>
                  <select className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.sex} onChange={e => setForm({ ...form, sex: e.target.value as any })}>
                    <option>Male</option>
                    <option>Female</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium">Date of birth</label>
                  <input className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" type="date" value={form.dateOfBirth} onChange={e => setForm({ ...form, dateOfBirth: e.target.value })} required />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium">Permanent address</label>
                  <input placeholder="Street, City" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.permanentAddress} onChange={e => setForm({ ...form, permanentAddress: e.target.value })} />
                </div>

                <div className="md:col-span-2 flex items-center gap-2">
                  <input id="sameAs" type="checkbox" checked={sameAsPermanent} onChange={e => setSameAsPermanent(e.target.checked)} className="h-4 w-4" />
                  <label htmlFor="sameAs" className="text-sm text-gray-700">Current address same as permanent</label>
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium">Current address</label>
                  <input placeholder="Street, City" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" value={form.currentAddress} onChange={e => setForm({ ...form, currentAddress: e.target.value })} />
                </div>

                <div>
                  <label className="block text-sm font-medium">Year first enrolled</label>
                  <input placeholder="Optional" className="mt-1 block w-full border rounded px-3 py-2 shadow-sm focus:outline-none focus:ring-2 focus:ring-cbsi-navy" type="number" value={form.yearFirstEnrolled || ''} onChange={e => setForm({ ...form, yearFirstEnrolled: e.target.value ? parseInt(e.target.value) : undefined })} />
                </div>

                <div className="flex items-center justify-end">
                  <button className="btn" type="submit">Create account</button>
                </div>
              </div>
            </form>
          ) : (
            <form onSubmit={submitBatch} className="bg-white p-6 rounded-lg card">
              <label className="block text-sm font-medium text-gray-700 mb-2">Upload CSV or XLSX file</label>
              <p className="text-xs text-gray-500 mb-3">Header: email,firstName,lastName,middleName,sex,dateOfBirth,permanentAddress,currentAddress,role,yearFirstEnrolled</p>
              <div
                onDragOver={(e) => e.preventDefault()}
                onDrop={handleDrop}
                onClick={() => fileInputRef.current?.click()}
                className="mt-2 flex items-center justify-center h-32 border-2 border-dashed rounded cursor-pointer bg-gray-50 hover:bg-gray-100"
              >
                {file ? (
                  <span className="text-sm text-gray-800">{file.name}</span>
                ) : (
                  <span className="text-sm text-gray-500">Drop file here or click to select</span>
                )}
                <input ref={fileInputRef} type="file" className="hidden" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" onChange={handleFileChange} />
              </div>
              <div className="mt-4">
                <button className="btn" type="submit">Upload and Create</button>
              </div>
            </form>
          )}

          
        </div>

        <div>
          <h3 className="text-lg font-semibold mb-3">Pending Accounts</h3>
          {loadingPending ? (
            <div className="text-sm text-gray-600">Loading...</div>
          ) : (
            <div className="overflow-x-auto bg-white rounded card table-container">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">School ID</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Email</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Name</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Created</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {pendingRows.map(r => (
                    <tr key={r.id} className="hover:bg-gray-50">
                      <td className="px-4 py-2 text-sm text-gray-700">{r.schoolId}</td>
                      <td className="px-4 py-2 text-sm text-gray-700">{r.email}</td>
                      <td className="px-4 py-2 text-sm text-gray-700">{r.firstName} {r.lastName}</td>
                      <td className="px-4 py-2 text-sm text-gray-700">{r.dateCreated}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          <div className="mt-3 flex gap-2">
            <button className="btn" type="button" onClick={fetchPending}>Refresh</button>
            <button className="btn secondary" type="button" onClick={() => setPendingRows([])}>Clear</button>
          </div>
        </div>
      </div>
    </div>
  )
}
