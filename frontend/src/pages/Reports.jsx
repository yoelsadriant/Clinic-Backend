import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import { formatRupiah, formatDate } from '../utils/format.js'

export default function Reports() {
  const [totalRevenue, setTotalRevenue] = useState(null)
  const [totalLoading, setTotalLoading] = useState(true)

  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)

  const [staffList, setStaffList] = useState([])
  const [filters, setFilters] = useState({ from: '', to: '', staffId: '' })
  const [activeFilters, setActiveFilters] = useState({ from: '', to: '', staffId: '' })

  const fetchTotal = useCallback(() => {
    setTotalLoading(true)
    apiClient.get('/reports/revenue/total')
      .then(r => setTotalRevenue(r.data?.total ?? r.data?.totalRevenue ?? r.data))
      .catch(() => setTotalRevenue(null))
      .finally(() => setTotalLoading(false))
  }, [])

  const fetchRevenue = useCallback(async (af, p = 0) => {
    setLoading(true); setError('')
    try {
      const params = new URLSearchParams({ page: p, size: 20 })
      if (af.from) params.append('from', af.from)
      if (af.to) params.append('to', af.to)
      if (af.staffId) params.append('staffId', af.staffId)
      const res = await apiClient.get(`/reports/revenue?${params}`)
      const d = res.data
      if (Array.isArray(d)) { setRows(d); setTotalPages(1) }
      else { setRows(d.content || d.data || []); setTotalPages(d.totalPages || 1) }
    } catch { setError('Failed to load revenue report') }
    finally { setLoading(false) }
  }, [])

  useEffect(() => { fetchTotal() }, [fetchTotal])
  useEffect(() => { fetchRevenue(activeFilters, page) }, [activeFilters, page, fetchRevenue])
  useEffect(() => {
    apiClient.get('/staff').catch(() => ({ data: [] })).then(r => {
      const d = r.data
      setStaffList(Array.isArray(d) ? d : d?.content || d?.data || [])
    })
  }, [])

  const applyFilters = () => { setPage(0); setActiveFilters({ ...filters }) }
  const clearFilters = () => {
    const empty = { from: '', to: '', staffId: '' }
    setFilters(empty); setActiveFilters(empty); setPage(0)
  }

  const setFilter = (k, v) => setFilters(f => ({ ...f, [k]: v }))

  return (
    <div>
      <div className="mb-5">
        <h1 className="text-xl font-bold text-gray-800">Reports</h1>
        <p className="text-sm text-gray-500">Revenue analytics and financial reports</p>
      </div>

      {/* Total Revenue card */}
      <div className="mb-6">
        <div
          className="inline-block rounded-2xl px-8 py-6 shadow-md text-white"
          style={{ background: 'linear-gradient(135deg, #B0003A, #8B002E)' }}
        >
          <div className="text-sm font-medium opacity-80 mb-1">Total Revenue (All Time)</div>
          <div className="text-3xl font-bold">
            {totalLoading ? '…' : totalRevenue != null ? formatRupiah(totalRevenue) : 'N/A'}
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl shadow-sm px-5 py-4 mb-5">
        <h2 className="text-sm font-semibold text-gray-700 mb-3">Filter Revenue</h2>
        <div className="flex flex-wrap gap-3 items-end">
          <div>
            <label className="form-label">From Date</label>
            <input
              type="date"
              value={filters.from}
              onChange={e => setFilter('from', e.target.value)}
              className="form-input"
            />
          </div>
          <div>
            <label className="form-label">To Date</label>
            <input
              type="date"
              value={filters.to}
              onChange={e => setFilter('to', e.target.value)}
              className="form-input"
            />
          </div>
          <div>
            <label className="form-label">Staff</label>
            <select value={filters.staffId} onChange={e => setFilter('staffId', e.target.value)} className="form-input">
              <option value="">All Staff</option>
              {staffList.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
          <div className="flex gap-2">
            <button onClick={applyFilters} className="btn-primary">Apply</button>
            <button onClick={clearFilters} className="btn-secondary">Clear</button>
          </div>
        </div>
      </div>

      {/* Revenue Table */}
      {error && <div className="text-red-500 text-sm mb-3">{error}</div>}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full">
          <thead>
            <tr>
              <th className="table-header">Date</th>
              <th className="table-header">Client</th>
              <th className="table-header">Staff</th>
              <th className="table-header">Item</th>
              <th className="table-header">Amount</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
            ) : rows.length === 0 ? (
              <tr><td colSpan={5} className="table-cell text-center text-gray-400 py-8">No revenue data found</td></tr>
            ) : rows.map((row, i) => (
              <tr key={row.id || i} className="table-row">
                <td className="table-cell">{formatDate(row.date || row.saleDate || row.createdAt)}</td>
                <td className="table-cell">{row.clientName || row.client?.name || '-'}</td>
                <td className="table-cell">{row.staffName || row.staff?.name || '-'}</td>
                <td className="table-cell">{row.itemName || row.treatmentName || row.packageName || row.productName || '-'}</td>
                <td className="table-cell font-semibold">{row.amount != null ? formatRupiah(row.amount) : '-'}</td>
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
    </div>
  )
}
