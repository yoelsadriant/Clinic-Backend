import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'
import { formatDate } from '../utils/format.js'

const EMPTY_CLIENT = {
  name: '', address: '', phone: '', email: '', sex: '', staffId: '',
}
const EMPTY_INSTALLMENT = {
  clientId: '', dueDate: '', totalAmount: '', amountPaid: '',
}

function TabButton({ active, onClick, children }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 rounded-t-lg font-medium text-sm transition-colors ${
        active
          ? 'text-white'
          : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
      }`}
      style={active ? { backgroundColor: '#B0003A' } : {}}
    >
      {children}
    </button>
  )
}

export default function Clients() {
  const [tab, setTab] = useState('clients')

  // --- Clients state ---
  const [clients, setClients] = useState([])
  const [clientsLoading, setClientsLoading] = useState(true)
  const [clientsError, setClientsError] = useState('')
  const [clientPage, setClientPage] = useState(0)
  const [clientTotalPages, setClientTotalPages] = useState(1)
  const [staffList, setStaffList] = useState([])

  // --- Installments state ---
  const [installments, setInstallments] = useState([])
  const [instLoading, setInstLoading] = useState(true)
  const [instError, setInstError] = useState('')
  const [instPage, setInstPage] = useState(0)
  const [instTotalPages, setInstTotalPages] = useState(1)

  // --- Client Modal ---
  const [clientModal, setClientModal] = useState(false)
  const [editClientId, setEditClientId] = useState(null)
  const [clientForm, setClientForm] = useState(EMPTY_CLIENT)
  const [clientFormError, setClientFormError] = useState('')
  const [clientSaving, setClientSaving] = useState(false)

  // --- History Modal ---
  const [historyModal, setHistoryModal] = useState(false)
  const [historyClient, setHistoryClient] = useState(null)
  const [historyRows, setHistoryRows] = useState([])
  const [historyLoading, setHistoryLoading] = useState(false)

  // --- Installment Modal ---
  const [instModal, setInstModal] = useState(false)
  const [editInstId, setEditInstId] = useState(null)
  const [instForm, setInstForm] = useState(EMPTY_INSTALLMENT)
  const [instFormError, setInstFormError] = useState('')
  const [instSaving, setInstSaving] = useState(false)

  const fetchClients = useCallback(async (p = 0) => {
    setClientsLoading(true)
    setClientsError('')
    try {
      const res = await apiClient.get(`/clients?page=${p}&size=20`)
      const data = res.data
      if (Array.isArray(data)) { setClients(data); setClientTotalPages(1) }
      else { setClients(data.content || data.data || []); setClientTotalPages(data.totalPages || 1) }
    } catch { setClientsError('Failed to load clients') }
    finally { setClientsLoading(false) }
  }, [])

  const fetchInstallments = useCallback(async (p = 0) => {
    setInstLoading(true)
    setInstError('')
    try {
      const res = await apiClient.get(`/installments?page=${p}&size=20`)
      const data = res.data
      if (Array.isArray(data)) { setInstallments(data); setInstTotalPages(1) }
      else { setInstallments(data.content || data.data || []); setInstTotalPages(data.totalPages || 1) }
    } catch { setInstError('Failed to load installments') }
    finally { setInstLoading(false) }
  }, [])

  useEffect(() => { fetchClients(clientPage) }, [clientPage, fetchClients])
  useEffect(() => { fetchInstallments(instPage) }, [instPage, fetchInstallments])
  useEffect(() => {
    apiClient.get('/staff').catch(() => ({ data: [] })).then(r => {
      const d = r.data
      setStaffList(Array.isArray(d) ? d : d?.content || d?.data || [])
    })
  }, [])

  // Client CRUD
  const openAddClient = () => {
    setEditClientId(null)
    setClientForm(EMPTY_CLIENT)
    setClientFormError('')
    setClientModal(true)
  }
  const openEditClient = (row) => {
    setEditClientId(row.id)
    setClientForm({
      name: row.name || '',
      address: row.address || '',
      phone: row.phone || '',
      email: row.email || '',
      sex: row.sex || '',
      staffId: row.staffId || row.staff?.id || '',
    })
    setClientFormError('')
    setClientModal(true)
  }
  const handleDeleteClient = async (id) => {
    if (!window.confirm('Delete this client?')) return
    try { await apiClient.delete(`/clients/${id}`); fetchClients(clientPage) }
    catch { alert('Failed to delete client') }
  }
  const handleSaveClient = async () => {
    if (!clientForm.name.trim()) { setClientFormError('Name is required'); return }
    setClientFormError('')
    setClientSaving(true)
    try {
      if (editClientId) await apiClient.put(`/clients/${editClientId}`, clientForm)
      else await apiClient.post('/clients', clientForm)
      setClientModal(false)
      fetchClients(clientPage)
    } catch (err) {
      setClientFormError(err.response?.data?.message || 'Failed to save')
    } finally { setClientSaving(false) }
  }

  // History
  const openHistory = async (client) => {
    setHistoryClient(client)
    setHistoryRows([])
    setHistoryLoading(true)
    setHistoryModal(true)
    try {
      const res = await apiClient.get(`/histories?clientId=${client.id}`)
      const d = res.data
      setHistoryRows(Array.isArray(d) ? d : d?.content || d?.data || [])
    } catch { setHistoryRows([]) }
    finally { setHistoryLoading(false) }
  }

  // Installment CRUD
  const openAddInst = () => {
    setEditInstId(null)
    setInstForm(EMPTY_INSTALLMENT)
    setInstFormError('')
    setInstModal(true)
  }
  const openEditInst = (row) => {
    setEditInstId(row.id)
    setInstForm({
      clientId: row.clientId || row.client?.id || '',
      dueDate: row.dueDate || '',
      totalAmount: row.totalAmount || '',
      amountPaid: row.amountPaid || '',
    })
    setInstFormError('')
    setInstModal(true)
  }
  const handleDeleteInst = async (id) => {
    if (!window.confirm('Delete this installment?')) return
    try { await apiClient.delete(`/installments/${id}`); fetchInstallments(instPage) }
    catch { alert('Failed to delete installment') }
  }
  const handleSaveInst = async () => {
    if (!instForm.clientId) { setInstFormError('Client is required'); return }
    if (!instForm.dueDate) { setInstFormError('Due date is required'); return }
    if (!instForm.totalAmount) { setInstFormError('Total amount is required'); return }
    setInstFormError('')
    setInstSaving(true)
    try {
      if (editInstId) await apiClient.put(`/installments/${editInstId}`, instForm)
      else await apiClient.post('/installments', instForm)
      setInstModal(false)
      fetchInstallments(instPage)
    } catch (err) {
      setInstFormError(err.response?.data?.message || 'Failed to save')
    } finally { setInstSaving(false) }
  }

  const setClientField = (k, v) => setClientForm(f => ({ ...f, [k]: v }))
  const setInstField = (k, v) => setInstForm(f => ({ ...f, [k]: v }))

  const formatRupiah = (v) => v != null ? 'Rp. ' + Number(v).toLocaleString('id-ID') : '-'

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <div>
          <h1 className="text-xl font-bold text-gray-800">Clients</h1>
          <p className="text-sm text-gray-500">Client records and installments</p>
        </div>
        <button
          onClick={tab === 'clients' ? openAddClient : openAddInst}
          className="btn-primary"
        >
          {tab === 'clients' ? '+ Add Client' : '+ Add Installment'}
        </button>
      </div>

      {/* Tabs */}
      <div className="flex gap-1 mb-0">
        <TabButton active={tab === 'clients'} onClick={() => setTab('clients')}>Clients</TabButton>
        <TabButton active={tab === 'installments'} onClick={() => setTab('installments')}>Installments</TabButton>
      </div>

      {/* Clients Tab */}
      {tab === 'clients' && (
        <div className="bg-white rounded-b-xl rounded-tr-xl shadow-sm overflow-hidden">
          {clientsError && <div className="text-red-500 text-sm p-4">{clientsError}</div>}
          <table className="w-full">
            <thead>
              <tr>
                <th className="table-header">Name</th>
                <th className="table-header">Sex</th>
                <th className="table-header">Phone</th>
                <th className="table-header">Address</th>
                <th className="table-header">Actions</th>
              </tr>
            </thead>
            <tbody>
              {clientsLoading ? (
                <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
              ) : clients.length === 0 ? (
                <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
              ) : clients.map(row => (
                <tr key={row.id} className="table-row">
                  <td className="table-cell font-medium">{row.name}</td>
                  <td className="table-cell">{row.sex || '-'}</td>
                  <td className="table-cell">{row.phone || '-'}</td>
                  <td className="table-cell">{row.address || '-'}</td>
                  <td className="table-cell">
                    <div className="flex gap-2 flex-wrap">
                      <button onClick={() => openEditClient(row)} className="btn-info">Edit</button>
                      <button onClick={() => openHistory(row)} className="btn-crimson">History</button>
                      <button onClick={() => handleDeleteClient(row.id)} className="btn-danger">Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {clientTotalPages > 1 && (
            <div className="flex justify-center gap-2 py-4">
              <button onClick={() => setClientPage(p => Math.max(0, p-1))} disabled={clientPage===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
              <span className="text-sm text-gray-600 py-2">Page {clientPage+1} of {clientTotalPages}</span>
              <button onClick={() => setClientPage(p => Math.min(clientTotalPages-1, p+1))} disabled={clientPage>=clientTotalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
            </div>
          )}
        </div>
      )}

      {/* Installments Tab */}
      {tab === 'installments' && (
        <div className="bg-white rounded-b-xl rounded-tr-xl shadow-sm overflow-hidden">
          {instError && <div className="text-red-500 text-sm p-4">{instError}</div>}
          <table className="w-full">
            <thead>
              <tr>
                <th className="table-header">Client Name</th>
                <th className="table-header">Due Date</th>
                <th className="table-header">Total</th>
                <th className="table-header">Remaining</th>
                <th className="table-header">Actions</th>
              </tr>
            </thead>
            <tbody>
              {instLoading ? (
                <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
              ) : installments.length === 0 ? (
                <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
              ) : installments.map(row => (
                <tr key={row.id} className="table-row">
                  <td className="table-cell font-medium">{row.clientName || row.client?.name || '-'}</td>
                  <td className="table-cell">{formatDate(row.dueDate)}</td>
                  <td className="table-cell">{formatRupiah(row.totalAmount)}</td>
                  <td className="table-cell">
                    {row.remaining != null
                      ? formatRupiah(row.remaining)
                      : row.totalAmount != null && row.amountPaid != null
                        ? formatRupiah(Number(row.totalAmount) - Number(row.amountPaid))
                        : '-'}
                  </td>
                  <td className="table-cell">
                    <div className="flex gap-2">
                      <button onClick={() => openEditInst(row)} className="btn-info">Edit</button>
                      <button onClick={() => handleDeleteInst(row.id)} className="btn-danger">Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {instTotalPages > 1 && (
            <div className="flex justify-center gap-2 py-4">
              <button onClick={() => setInstPage(p => Math.max(0, p-1))} disabled={instPage===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
              <span className="text-sm text-gray-600 py-2">Page {instPage+1} of {instTotalPages}</span>
              <button onClick={() => setInstPage(p => Math.min(instTotalPages-1, p+1))} disabled={instPage>=instTotalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
            </div>
          )}
        </div>
      )}

      {/* Client Add/Edit Modal */}
      <Modal
        isOpen={clientModal}
        onClose={() => setClientModal(false)}
        title={editClientId ? 'Edit Client' : 'Add Client'}
        footer={
          <>
            <button onClick={() => setClientModal(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSaveClient} disabled={clientSaving} className="btn-primary">
              {clientSaving ? 'Saving...' : 'Save'}
            </button>
          </>
        }
      >
        {clientFormError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{clientFormError}</div>}
        <div className="form-group">
          <label className="form-label">Name *</label>
          <input type="text" value={clientForm.name} onChange={e => setClientField('name', e.target.value)} className="form-input" placeholder="Full name" />
        </div>
        <div className="form-group">
          <label className="form-label">Address</label>
          <input type="text" value={clientForm.address} onChange={e => setClientField('address', e.target.value)} className="form-input" placeholder="Address" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Phone</label>
            <input type="text" value={clientForm.phone} onChange={e => setClientField('phone', e.target.value)} className="form-input" placeholder="Phone number" />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input type="email" value={clientForm.email} onChange={e => setClientField('email', e.target.value)} className="form-input" placeholder="Email" />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Sex</label>
            <select value={clientForm.sex} onChange={e => setClientField('sex', e.target.value)} className="form-input">
              <option value="">— Select —</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Staff</label>
            <select value={clientForm.staffId} onChange={e => setClientField('staffId', e.target.value)} className="form-input">
              <option value="">— Select Staff —</option>
              {staffList.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
        </div>
      </Modal>

      {/* History Modal */}
      <Modal
        isOpen={historyModal}
        onClose={() => setHistoryModal(false)}
        title={`History — ${historyClient?.name || ''}`}
        footer={<button onClick={() => setHistoryModal(false)} className="btn-secondary">Close</button>}
      >
        {historyLoading ? (
          <p className="text-center text-gray-400 py-4">Loading history...</p>
        ) : historyRows.length === 0 ? (
          <p className="text-center text-gray-400 py-4">No history found</p>
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr>
                <th className="table-header">Date</th>
                <th className="table-header">Treatment</th>
                <th className="table-header">Notes</th>
              </tr>
            </thead>
            <tbody>
              {historyRows.map((h, i) => (
                <tr key={h.id || i} className="table-row">
                  <td className="table-cell">{formatDate(h.date)}</td>
                  <td className="table-cell">{h.treatmentName || h.treatment?.name || '-'}</td>
                  <td className="table-cell">{h.notes || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Modal>

      {/* Installment Add/Edit Modal */}
      <Modal
        isOpen={instModal}
        onClose={() => setInstModal(false)}
        title={editInstId ? 'Edit Installment' : 'Add Installment'}
        footer={
          <>
            <button onClick={() => setInstModal(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleSaveInst} disabled={instSaving} className="btn-primary">
              {instSaving ? 'Saving...' : 'Save'}
            </button>
          </>
        }
      >
        {instFormError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{instFormError}</div>}
        <div className="form-group">
          <label className="form-label">Client *</label>
          <select value={instForm.clientId} onChange={e => setInstField('clientId', e.target.value)} className="form-input">
            <option value="">— Select Client —</option>
            {clients.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Due Date *</label>
          <input type="date" value={instForm.dueDate} onChange={e => setInstField('dueDate', e.target.value)} className="form-input" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div className="form-group">
            <label className="form-label">Total Amount *</label>
            <input type="number" value={instForm.totalAmount} onChange={e => setInstField('totalAmount', e.target.value)} className="form-input" placeholder="0" />
          </div>
          <div className="form-group">
            <label className="form-label">Amount Paid</label>
            <input type="number" value={instForm.amountPaid} onChange={e => setInstField('amountPaid', e.target.value)} className="form-input" placeholder="0" />
          </div>
        </div>
      </Modal>
    </div>
  )
}
