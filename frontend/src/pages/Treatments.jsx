import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'
import { formatRupiah } from '../utils/format.js'

function TabButton({ active, onClick, children }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 rounded-t-lg font-medium text-sm transition-colors ${active ? 'text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
      style={active ? { backgroundColor: '#B0003A' } : {}}
    >
      {children}
    </button>
  )
}

// ---- Package Detail Modal ----
function PackageDetailModal({ pkg, onClose }) {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!pkg) return
    setLoading(true)
    apiClient.get(`/packages/${pkg.id}`)
      .then(r => {
        const d = r.data
        setItems(d.treatments || d.items || d.details || [d].filter(Boolean))
      })
      .catch(() => setItems([]))
      .finally(() => setLoading(false))
  }, [pkg])

  if (!pkg) return null

  return (
    <Modal
      isOpen={!!pkg}
      onClose={onClose}
      title={`Package: ${pkg.name}`}
      footer={<button onClick={onClose} className="btn-secondary">Close</button>}
    >
      {loading ? (
        <p className="text-center text-gray-400 py-4">Loading...</p>
      ) : items.length === 0 ? (
        <p className="text-center text-gray-400 py-4">No treatments in this package</p>
      ) : (
        <table className="w-full text-sm">
          <thead>
            <tr>
              <th className="table-header">Treatment</th>
              <th className="table-header">Price</th>
            </tr>
          </thead>
          <tbody>
            {items.map((t, i) => (
              <tr key={t.id || i} className="table-row">
                <td className="table-cell">{t.name || t.treatmentName || '-'}</td>
                <td className="table-cell">{t.price != null ? formatRupiah(t.price) : '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </Modal>
  )
}

export default function Treatments() {
  const [tab, setTab] = useState('treatments')

  // Treatments
  const [treatments, setTreatments] = useState([])
  const [tLoading, setTLoading] = useState(true)
  const [tError, setTError] = useState('')
  const [tPage, setTPage] = useState(0)
  const [tTotalPages, setTTotalPages] = useState(1)
  const [tModal, setTModal] = useState(false)
  const [editTId, setEditTId] = useState(null)
  const [tForm, setTForm] = useState({ name: '', price: '' })
  const [tFormError, setTFormError] = useState('')
  const [tSaving, setTSaving] = useState(false)

  // Packages
  const [packages, setPackages] = useState([])
  const [pLoading, setPLoading] = useState(true)
  const [pError, setPError] = useState('')
  const [pPage, setPPage] = useState(0)
  const [pTotalPages, setPTotalPages] = useState(1)
  const [pModal, setPModal] = useState(false)
  const [editPId, setEditPId] = useState(null)
  const [pForm, setPForm] = useState({ name: '', price: '' })
  const [pFormError, setPFormError] = useState('')
  const [pSaving, setPSaving] = useState(false)
  const [detailPkg, setDetailPkg] = useState(null)

  const fetchTreatments = useCallback(async (p = 0) => {
    setTLoading(true); setTError('')
    try {
      const res = await apiClient.get(`/treatments?page=${p}&size=20`)
      const d = res.data
      if (Array.isArray(d)) { setTreatments(d); setTTotalPages(1) }
      else { setTreatments(d.content || d.data || []); setTTotalPages(d.totalPages || 1) }
    } catch { setTError('Failed to load treatments') }
    finally { setTLoading(false) }
  }, [])

  const fetchPackages = useCallback(async (p = 0) => {
    setPLoading(true); setPError('')
    try {
      const res = await apiClient.get(`/packages?page=${p}&size=20`)
      const d = res.data
      if (Array.isArray(d)) { setPackages(d); setPTotalPages(1) }
      else { setPackages(d.content || d.data || []); setPTotalPages(d.totalPages || 1) }
    } catch { setPError('Failed to load packages') }
    finally { setPLoading(false) }
  }, [])

  useEffect(() => { fetchTreatments(tPage) }, [tPage, fetchTreatments])
  useEffect(() => { fetchPackages(pPage) }, [pPage, fetchPackages])

  // Treatments CRUD
  const openAddT = () => { setEditTId(null); setTForm({ name: '', price: '' }); setTFormError(''); setTModal(true) }
  const openEditT = (row) => { setEditTId(row.id); setTForm({ name: row.name || '', price: row.price || '' }); setTFormError(''); setTModal(true) }
  const handleDeleteT = async (id) => {
    if (!window.confirm('Delete this treatment?')) return
    try { await apiClient.delete(`/treatments/${id}`); fetchTreatments(tPage) }
    catch { alert('Failed to delete') }
  }
  const handleSaveT = async () => {
    if (!tForm.name.trim()) { setTFormError('Name is required'); return }
    setTFormError(''); setTSaving(true)
    try {
      if (editTId) await apiClient.put(`/treatments/${editTId}`, tForm)
      else await apiClient.post('/treatments', tForm)
      setTModal(false); fetchTreatments(tPage)
    } catch (err) { setTFormError(err.response?.data?.message || 'Failed to save') }
    finally { setTSaving(false) }
  }

  // Packages CRUD
  const openAddP = () => { setEditPId(null); setPForm({ name: '', price: '' }); setPFormError(''); setPModal(true) }
  const openEditP = (row) => { setEditPId(row.id); setPForm({ name: row.name || '', price: row.price || '' }); setPFormError(''); setPModal(true) }
  const handleDeleteP = async (id) => {
    if (!window.confirm('Delete this package?')) return
    try { await apiClient.delete(`/packages/${id}`); fetchPackages(pPage) }
    catch { alert('Failed to delete') }
  }
  const handleSaveP = async () => {
    if (!pForm.name.trim()) { setPFormError('Name is required'); return }
    setPFormError(''); setPSaving(true)
    try {
      if (editPId) await apiClient.put(`/packages/${editPId}`, pForm)
      else await apiClient.post('/packages', pForm)
      setPModal(false); fetchPackages(pPage)
    } catch (err) { setPFormError(err.response?.data?.message || 'Failed to save') }
    finally { setPSaving(false) }
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <div>
          <h1 className="text-xl font-bold text-gray-800">Treatments</h1>
          <p className="text-sm text-gray-500">Manage treatments and packages</p>
        </div>
        <button
          onClick={tab === 'treatments' ? openAddT : openAddP}
          className="btn-primary"
        >
          {tab === 'treatments' ? '+ Add Treatment' : '+ Add Package'}
        </button>
      </div>

      <div className="flex gap-1 mb-0">
        <TabButton active={tab === 'treatments'} onClick={() => setTab('treatments')}>Treatments</TabButton>
        <TabButton active={tab === 'packages'} onClick={() => setTab('packages')}>Packages</TabButton>
      </div>

      {/* Treatments Tab */}
      {tab === 'treatments' && (
        <div className="bg-white rounded-b-xl rounded-tr-xl shadow-sm overflow-hidden">
          {tError && <div className="text-red-500 text-sm p-4">{tError}</div>}
          <table className="w-full">
            <thead>
              <tr>
                <th className="table-header">Treatment Name</th>
                <th className="table-header">Price</th>
                <th className="table-header">Actions</th>
              </tr>
            </thead>
            <tbody>
              {tLoading ? (
                <tr><td colSpan={3} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
              ) : treatments.length === 0 ? (
                <tr><td colSpan={3} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
              ) : treatments.map(row => (
                <tr key={row.id} className="table-row">
                  <td className="table-cell font-medium">{row.name}</td>
                  <td className="table-cell">{row.price != null ? formatRupiah(row.price) : '-'}</td>
                  <td className="table-cell">
                    <div className="flex gap-2">
                      <button onClick={() => openEditT(row)} className="btn-info">Edit</button>
                      <button onClick={() => handleDeleteT(row.id)} className="btn-danger">Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {tTotalPages > 1 && (
            <div className="flex justify-center gap-2 py-4">
              <button onClick={() => setTPage(p => Math.max(0, p-1))} disabled={tPage===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
              <span className="text-sm text-gray-600 py-2">Page {tPage+1} of {tTotalPages}</span>
              <button onClick={() => setTPage(p => Math.min(tTotalPages-1, p+1))} disabled={tPage>=tTotalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
            </div>
          )}
        </div>
      )}

      {/* Packages Tab */}
      {tab === 'packages' && (
        <div className="bg-white rounded-b-xl rounded-tr-xl shadow-sm overflow-hidden">
          {pError && <div className="text-red-500 text-sm p-4">{pError}</div>}
          <table className="w-full">
            <thead>
              <tr>
                <th className="table-header">Package Name</th>
                <th className="table-header">Price</th>
                <th className="table-header">Actions</th>
              </tr>
            </thead>
            <tbody>
              {pLoading ? (
                <tr><td colSpan={3} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
              ) : packages.length === 0 ? (
                <tr><td colSpan={3} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
              ) : packages.map(row => (
                <tr key={row.id} className="table-row">
                  <td className="table-cell font-medium">{row.name}</td>
                  <td className="table-cell">{row.price != null ? formatRupiah(row.price) : '-'}</td>
                  <td className="table-cell">
                    <div className="flex gap-2">
                      <button onClick={() => setDetailPkg(row)} className="btn-info">View Details</button>
                      <button onClick={() => openEditP(row)} className="btn-info" style={{ backgroundColor: '#9C27B0' }}
                        onMouseOver={e => e.currentTarget.style.backgroundColor='#7B1FA2'}
                        onMouseOut={e => e.currentTarget.style.backgroundColor='#9C27B0'}>Edit</button>
                      <button onClick={() => handleDeleteP(row.id)} className="btn-danger">Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {pTotalPages > 1 && (
            <div className="flex justify-center gap-2 py-4">
              <button onClick={() => setPPage(p => Math.max(0, p-1))} disabled={pPage===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
              <span className="text-sm text-gray-600 py-2">Page {pPage+1} of {pTotalPages}</span>
              <button onClick={() => setPPage(p => Math.min(pTotalPages-1, p+1))} disabled={pPage>=pTotalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
            </div>
          )}
        </div>
      )}

      {/* Treatment Modal */}
      <Modal
        isOpen={tModal}
        onClose={() => setTModal(false)}
        title={editTId ? 'Edit Treatment' : 'Add Treatment'}
        footer={
          <>
            <button onClick={() => setTModal(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSaveT} disabled={tSaving} className="btn-primary">{tSaving ? 'Saving...' : 'Save'}</button>
          </>
        }
      >
        {tFormError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{tFormError}</div>}
        <div className="form-group">
          <label className="form-label">Name *</label>
          <input type="text" value={tForm.name} onChange={e => setTForm(f => ({...f, name: e.target.value}))} className="form-input" placeholder="Treatment name" />
        </div>
        <div className="form-group">
          <label className="form-label">Price</label>
          <input type="number" value={tForm.price} onChange={e => setTForm(f => ({...f, price: e.target.value}))} className="form-input" placeholder="0" />
        </div>
      </Modal>

      {/* Package Modal */}
      <Modal
        isOpen={pModal}
        onClose={() => setPModal(false)}
        title={editPId ? 'Edit Package' : 'Add Package'}
        footer={
          <>
            <button onClick={() => setPModal(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSaveP} disabled={pSaving} className="btn-primary">{pSaving ? 'Saving...' : 'Save'}</button>
          </>
        }
      >
        {pFormError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{pFormError}</div>}
        <div className="form-group">
          <label className="form-label">Package Name *</label>
          <input type="text" value={pForm.name} onChange={e => setPForm(f => ({...f, name: e.target.value}))} className="form-input" placeholder="Package name" />
        </div>
        <div className="form-group">
          <label className="form-label">Price</label>
          <input type="number" value={pForm.price} onChange={e => setPForm(f => ({...f, price: e.target.value}))} className="form-input" placeholder="0" />
        </div>
      </Modal>

      {/* Package Detail Modal */}
      <PackageDetailModal pkg={detailPkg} onClose={() => setDetailPkg(null)} />
    </div>
  )
}
