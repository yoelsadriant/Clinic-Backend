import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'
import { formatDate, formatTime } from '../utils/format.js'

const EMPTY_FORM = {
  clientId: '',
  packageId: '',
  treatmentId: '',
  date: '',
  time: '',
  statusClientId: '',
}

function StatusBadge({ status }) {
  const s = (status || '').toLowerCase()
  if (s === 'done') return <span className="badge-green">Done</span>
  if (s === 'cancelled' || s === 'canceled') return <span className="badge-red">Cancelled</span>
  return <span className="badge-blue">{status || 'Booked'}</span>
}

export default function Schedule() {
  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)

  // Dropdown data
  const [clients, setClients] = useState([])
  const [packages, setPackages] = useState([])
  const [treatments, setTreatments] = useState([])
  const [statuses, setStatuses] = useState([])

  // Modal state
  const [modalOpen, setModalOpen] = useState(false)
  const [editId, setEditId] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [formError, setFormError] = useState('')
  const [saving, setSaving] = useState(false)

  const fetchData = useCallback(async (p = 0) => {
    setLoading(true)
    setError('')
    try {
      const res = await apiClient.get(`/schedules?page=${p}&size=20`)
      const data = res.data
      if (Array.isArray(data)) {
        setRows(data)
        setTotalPages(1)
      } else {
        setRows(data.content || data.data || [])
        setTotalPages(data.totalPages || 1)
      }
    } catch {
      setError('Failed to load schedules')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchData(page) }, [page, fetchData])

  useEffect(() => {
    Promise.all([
      apiClient.get('/clients').catch(() => ({ data: [] })),
      apiClient.get('/packages').catch(() => ({ data: [] })),
      apiClient.get('/treatments').catch(() => ({ data: [] })),
      apiClient.get('/status-clients').catch(() => ({ data: [] })),
    ]).then(([c, p, t, s]) => {
      setClients(Array.isArray(c.data) ? c.data : c.data?.content || c.data?.data || [])
      setPackages(Array.isArray(p.data) ? p.data : p.data?.content || p.data?.data || [])
      setTreatments(Array.isArray(t.data) ? t.data : t.data?.content || t.data?.data || [])
      setStatuses(Array.isArray(s.data) ? s.data : s.data?.content || s.data?.data || [])
    })
  }, [])

  const openAdd = () => {
    setEditId(null)
    setForm(EMPTY_FORM)
    setFormError('')
    setModalOpen(true)
  }

  const openEdit = (row) => {
    setEditId(row.id)
    setForm({
      clientId: row.clientId || row.client?.id || '',
      packageId: row.packageId || row.package?.id || '',
      treatmentId: row.treatmentId || row.treatment?.id || '',
      date: row.date || '',
      time: row.time || '',
      statusClientId: row.statusClientId || row.statusClient?.id || '',
    })
    setFormError('')
    setModalOpen(true)
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this schedule?')) return
    try {
      await apiClient.delete(`/schedules/${id}`)
      fetchData(page)
    } catch {
      alert('Failed to delete schedule')
    }
  }

  const handleSave = async () => {
    if (!form.clientId) { setFormError('Client is required'); return }
    if (!form.date) { setFormError('Date is required'); return }
    if (!form.time) { setFormError('Time is required'); return }
    setFormError('')
    setSaving(true)
    try {
      if (editId) {
        await apiClient.put(`/schedules/${editId}`, form)
      } else {
        await apiClient.post('/schedules', form)
      }
      setModalOpen(false)
      fetchData(page)
    } catch (err) {
      setFormError(err.response?.data?.message || 'Failed to save')
    } finally {
      setSaving(false)
    }
  }

  const setField = (k, v) => setForm(f => ({ ...f, [k]: v }))

  return (
    <div>
      {/* Page header */}
      <div className="flex items-center justify-between mb-5">
        <div>
          <h1 className="text-xl font-bold text-gray-800">Schedule</h1>
          <p className="text-sm text-gray-500">Manage appointments and bookings</p>
        </div>
        <button onClick={openAdd} className="btn-primary">+ Add Schedule</button>
      </div>

      {/* Error */}
      {error && <div className="text-red-500 text-sm mb-4">{error}</div>}

      {/* Table */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full">
          <thead>
            <tr>
              <th className="table-header">Date</th>
              <th className="table-header">Time</th>
              <th className="table-header">Client Name</th>
              <th className="table-header">Treatment / Package</th>
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
                <td className="table-cell">{formatDate(row.date)}</td>
                <td className="table-cell">{formatTime(row.time)}</td>
                <td className="table-cell font-medium">
                  {row.clientName || row.client?.name || '-'}
                </td>
                <td className="table-cell">
                  {row.treatmentName || row.treatment?.name || row.packageName || row.package?.name || '-'}
                </td>
                <td className="table-cell">
                  <StatusBadge status={row.statusName || row.statusClient?.name || row.status} />
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

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button
            onClick={() => setPage(p => Math.max(0, p - 1))}
            disabled={page === 0}
            className="btn-secondary text-sm disabled:opacity-40"
          >
            ← Prev
          </button>
          <span className="text-sm text-gray-600 py-2">Page {page + 1} of {totalPages}</span>
          <button
            onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
            disabled={page >= totalPages - 1}
            className="btn-secondary text-sm disabled:opacity-40"
          >
            Next →
          </button>
        </div>
      )}

      {/* Add/Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editId ? 'Edit Schedule' : 'Add Schedule'}
        footer={
          <>
            <button onClick={() => setModalOpen(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSave} disabled={saving} className="btn-primary">
              {saving ? 'Saving...' : 'Save'}
            </button>
          </>
        }
      >
        {formError && (
          <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{formError}</div>
        )}
        <div className="form-group">
          <label className="form-label">Client *</label>
          <select value={form.clientId} onChange={e => setField('clientId', e.target.value)} className="form-input">
            <option value="">— Select Client —</option>
            {clients.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Package</label>
          <select value={form.packageId} onChange={e => setField('packageId', e.target.value)} className="form-input">
            <option value="">— Select Package —</option>
            {packages.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Treatment</label>
          <select value={form.treatmentId} onChange={e => setField('treatmentId', e.target.value)} className="form-input">
            <option value="">— Select Treatment —</option>
            {treatments.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
          </select>
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Date *</label>
            <input type="date" value={form.date} onChange={e => setField('date', e.target.value)} className="form-input" />
          </div>
          <div className="form-group">
            <label className="form-label">Time *</label>
            <input type="time" value={form.time} onChange={e => setField('time', e.target.value)} className="form-input" />
          </div>
        </div>
        <div className="form-group">
          <label className="form-label">Status</label>
          <select value={form.statusClientId} onChange={e => setField('statusClientId', e.target.value)} className="form-input">
            <option value="">— Select Status —</option>
            {statuses.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>
      </Modal>
    </div>
  )
}
