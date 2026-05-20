import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'

const EMPTY_FORM = {
  name: '', phone: '', address: '', workplace: '', occupationId: '', active: true,
}

export default function Staff() {
  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [occupations, setOccupations] = useState([])

  const [modalOpen, setModalOpen] = useState(false)
  const [editId, setEditId] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [formError, setFormError] = useState('')
  const [saving, setSaving] = useState(false)

  const fetchData = useCallback(async (p = 0) => {
    setLoading(true)
    setError('')
    try {
      const res = await apiClient.get(`/staff?page=${p}&size=20`)
      const data = res.data
      if (Array.isArray(data)) { setRows(data); setTotalPages(1) }
      else { setRows(data.content || data.data || []); setTotalPages(data.totalPages || 1) }
    } catch { setError('Failed to load staff') }
    finally { setLoading(false) }
  }, [])

  useEffect(() => { fetchData(page) }, [page, fetchData])
  useEffect(() => {
    apiClient.get('/occupations').catch(() => ({ data: [] })).then(r => {
      const d = r.data
      setOccupations(Array.isArray(d) ? d : d?.content || d?.data || [])
    })
  }, [])

  const openAdd = () => {
    setEditId(null); setForm(EMPTY_FORM); setFormError(''); setModalOpen(true)
  }
  const openEdit = (row) => {
    setEditId(row.id)
    setForm({
      name: row.name || '',
      phone: row.phone || '',
      address: row.address || '',
      workplace: row.workplace || '',
      occupationId: row.occupationId || row.occupation?.id || '',
      active: row.active !== false,
    })
    setFormError('')
    setModalOpen(true)
  }
  const handleDelete = async (id) => {
    if (!window.confirm('Delete this staff member?')) return
    try { await apiClient.delete(`/staff/${id}`); fetchData(page) }
    catch { alert('Failed to delete staff') }
  }
  const handleSave = async () => {
    if (!form.name.trim()) { setFormError('Name is required'); return }
    setFormError('')
    setSaving(true)
    try {
      if (editId) await apiClient.put(`/staff/${editId}`, form)
      else await apiClient.post('/staff', form)
      setModalOpen(false)
      fetchData(page)
    } catch (err) {
      setFormError(err.response?.data?.message || 'Failed to save')
    } finally { setSaving(false) }
  }
  const setField = (k, v) => setForm(f => ({ ...f, [k]: v }))

  return (
    <div>
      <div className="flex items-center justify-between mb-5">
        <div>
          <h1 className="text-xl font-bold text-gray-800">Staff</h1>
          <p className="text-sm text-gray-500">Manage clinic staff members</p>
        </div>
        <button onClick={openAdd} className="btn-primary">+ Add Staff</button>
      </div>

      {error && <div className="text-red-500 text-sm mb-4">{error}</div>}

      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full">
          <thead>
            <tr>
              <th className="table-header">Name</th>
              <th className="table-header">Phone</th>
              <th className="table-header">Address</th>
              <th className="table-header">Occupation</th>
              <th className="table-header">Status</th>
              <th className="table-header">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={6} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
            ) : rows.length === 0 ? (
              <tr><td colSpan={6} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
            ) : rows.map(row => (
              <tr key={row.id} className="table-row">
                <td className="table-cell font-medium">{row.name}</td>
                <td className="table-cell">{row.phone || '-'}</td>
                <td className="table-cell">{row.address || '-'}</td>
                <td className="table-cell">{row.occupationName || row.occupation?.name || '-'}</td>
                <td className="table-cell">
                  {row.active !== false
                    ? <span className="badge-green">Active</span>
                    : <span className="badge-gray">Inactive</span>}
                </td>
                <td className="table-cell">
                  <div className="flex gap-2">
                    <button onClick={() => openEdit(row)} className="btn-info">Edit</button>
                    <button onClick={() => handleDelete(row.id)} className="btn-danger">Delete</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button onClick={() => setPage(p => Math.max(0, p-1))} disabled={page===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
          <span className="text-sm text-gray-600 py-2">Page {page+1} of {totalPages}</span>
          <button onClick={() => setPage(p => Math.min(totalPages-1, p+1))} disabled={page>=totalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
        </div>
      )}

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editId ? 'Edit Staff' : 'Add Staff'}
        footer={
          <>
            <button onClick={() => setModalOpen(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSave} disabled={saving} className="btn-primary">
              {saving ? 'Saving...' : 'Save'}
            </button>
          </>
        }
      >
        {formError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{formError}</div>}
        <div className="form-group">
          <label className="form-label">Name *</label>
          <input type="text" value={form.name} onChange={e => setField('name', e.target.value)} className="form-input" placeholder="Full name" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Phone</label>
            <input type="text" value={form.phone} onChange={e => setField('phone', e.target.value)} className="form-input" placeholder="Phone" />
          </div>
          <div className="form-group">
            <label className="form-label">Workplace</label>
            <input type="text" value={form.workplace} onChange={e => setField('workplace', e.target.value)} className="form-input" placeholder="Workplace" />
          </div>
        </div>
        <div className="form-group">
          <label className="form-label">Address</label>
          <input type="text" value={form.address} onChange={e => setField('address', e.target.value)} className="form-input" placeholder="Address" />
        </div>
        <div className="form-group">
          <label className="form-label">Occupation</label>
          <select value={form.occupationId} onChange={e => setField('occupationId', e.target.value)} className="form-input">
            <option value="">— Select Occupation —</option>
            {occupations.map(o => <option key={o.id} value={o.id}>{o.name}</option>)}
          </select>
        </div>
        <div className="form-group flex items-center gap-3">
          <label className="form-label mb-0">Active</label>
          <input
            type="checkbox"
            checked={form.active}
            onChange={e => setField('active', e.target.checked)}
            className="w-4 h-4 accent-crimson cursor-pointer"
          />
          <span className="text-sm text-gray-600">{form.active ? 'Active' : 'Inactive'}</span>
        </div>
      </Modal>
    </div>
  )
}
