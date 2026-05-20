import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'
import { formatRupiah } from '../utils/format.js'

const EMPTY_FORM = { name: '', netWeight: '', price: '', stock: '' }

export default function Products() {
  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)

  const [modalOpen, setModalOpen] = useState(false)
  const [editId, setEditId] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [formError, setFormError] = useState('')
  const [saving, setSaving] = useState(false)

  const fetchData = useCallback(async (p = 0) => {
    setLoading(true); setError('')
    try {
      const res = await apiClient.get(`/products?page=${p}&size=20`)
      const data = res.data
      if (Array.isArray(data)) { setRows(data); setTotalPages(1) }
      else { setRows(data.content || data.data || []); setTotalPages(data.totalPages || 1) }
    } catch { setError('Failed to load products') }
    finally { setLoading(false) }
  }, [])

  useEffect(() => { fetchData(page) }, [page, fetchData])

  const openAdd = () => { setEditId(null); setForm(EMPTY_FORM); setFormError(''); setModalOpen(true) }
  const openEdit = (row) => {
    setEditId(row.id)
    setForm({ name: row.name || '', netWeight: row.netWeight || '', price: row.price || '', stock: row.stock ?? '' })
    setFormError('')
    setModalOpen(true)
  }
  const handleDelete = async (id) => {
    if (!window.confirm('Delete this product?')) return
    try { await apiClient.delete(`/products/${id}`); fetchData(page) }
    catch { alert('Failed to delete product') }
  }
  const handleSave = async () => {
    if (!form.name.trim()) { setFormError('Name is required'); return }
    setFormError(''); setSaving(true)
    try {
      if (editId) await apiClient.put(`/products/${editId}`, form)
      else await apiClient.post('/products', form)
      setModalOpen(false); fetchData(page)
    } catch (err) { setFormError(err.response?.data?.message || 'Failed to save') }
    finally { setSaving(false) }
  }
  const setField = (k, v) => setForm(f => ({ ...f, [k]: v }))

  return (
    <div>
      <div className="flex items-center justify-between mb-5">
        <div>
          <h1 className="text-xl font-bold text-gray-800">Products</h1>
          <p className="text-sm text-gray-500">Manage clinic products and inventory</p>
        </div>
        <button onClick={openAdd} className="btn-primary">+ Add Product</button>
      </div>

      {error && <div className="text-red-500 text-sm mb-4">{error}</div>}

      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full">
          <thead>
            <tr>
              <th className="table-header">Product Name</th>
              <th className="table-header">Net Weight</th>
              <th className="table-header">Stock</th>
              <th className="table-header">Price</th>
              <th className="table-header">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
            ) : rows.length === 0 ? (
              <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
            ) : rows.map(row => (
              <tr key={row.id} className="table-row">
                <td className="table-cell font-medium">{row.name}</td>
                <td className="table-cell">{row.netWeight || '-'}</td>
                <td className="table-cell">
                  <span className={`font-medium ${row.stock <= 0 ? 'text-red-500' : row.stock <= 5 ? 'text-orange-500' : 'text-gray-800'}`}>
                    {row.stock ?? '-'}
                  </span>
                </td>
                <td className="table-cell">{row.price != null ? formatRupiah(row.price) : '-'}</td>
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
        title={editId ? 'Edit Product' : 'Add Product'}
        footer={
          <>
            <button onClick={() => setModalOpen(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSave} disabled={saving} className="btn-primary">{saving ? 'Saving...' : 'Save'}</button>
          </>
        }
      >
        {formError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{formError}</div>}
        <div className="form-group">
          <label className="form-label">Product Name *</label>
          <input type="text" value={form.name} onChange={e => setField('name', e.target.value)} className="form-input" placeholder="Product name" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Net Weight</label>
            <input type="text" value={form.netWeight} onChange={e => setField('netWeight', e.target.value)} className="form-input" placeholder="e.g. 250ml" />
          </div>
          <div className="form-group">
            <label className="form-label">Stock</label>
            <input type="number" value={form.stock} onChange={e => setField('stock', e.target.value)} className="form-input" placeholder="0" />
          </div>
        </div>
        <div className="form-group">
          <label className="form-label">Price</label>
          <input type="number" value={form.price} onChange={e => setField('price', e.target.value)} className="form-input" placeholder="0" />
        </div>
      </Modal>
    </div>
  )
}
