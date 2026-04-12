import React, { useEffect, useState } from 'react'
import api from '../api'

type UserRecord = {
  id: number
  schoolId: string
  email: string
  firstName: string
  lastName: string
  middleName?: string
  sex?: string
  dateOfBirth?: string
  permanentAddress?: string
  currentAddress?: string
  dateCreated?: string
  dateUpdated?: string
  accountStatus?: string
  lastLogin?: string
}

export default function UserRecords() {
  const [rows, setRows] = useState<UserRecord[]>([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setLoading(true)
    api.get('/api/admin/users')
      .then(res => setRows(res.data || []))
      .catch(() => setRows([]))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="w-full mx-auto p-6">
      <h2 className="text-2xl font-semibold mb-4">User Records</h2>
      {loading ? (
        <div className="text-sm text-gray-600">Loading...</div>
      ) : (
        <div className="w-full overflow-x-auto bg-white rounded card table-container">
          <table className="w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">School ID</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Email</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">First</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Last</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Middle</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Sex</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">DOB</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Permanent</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Current</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Created</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Updated</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Status</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">Last Login</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {rows.map(r => (
                <tr key={r.id} className="hover:bg-gray-50">
                  <td className="px-4 py-2 text-sm text-gray-700">{r.schoolId}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.email}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.firstName}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.lastName}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.middleName}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.sex}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.dateOfBirth}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.permanentAddress}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.currentAddress}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.dateCreated}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.dateUpdated}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.accountStatus}</td>
                  <td className="px-4 py-2 text-sm text-gray-700">{r.lastLogin}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
