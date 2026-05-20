import React, { useEffect, useState, useCallback } from 'react'
import apiClient from '../api/client.js'
import Modal from '../components/Modal.jsx'
import { formatRupiah, formatDate } from '../utils/format.js'

const SALE_TABS = [
  { key: 'products',     label: 'Products',     endpoint: '/products',     itemLabel: 'Product' },
  { key: 'treatments',   label: 'Treatments',   endpoint: '/treatments',   itemLabel: 'Treatment' },
  { key: 'packages',     label: 'Packages',     endpoint: '/packages',     itemLabel: 'Package' },
  { key: 'installments', label: 'Installments', endpoint: '/installments', itemLabel: 'Installment' },
]

function SaleTabButton({ active, onClick, children }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 text-sm font-medium border-b-2 transition-colors ${
        active ? 'border-crimson text-crimson' : 'border-transparent text-gray-500 hover:text-gray-700'
      }`}
      style={active ? { borderColor: '#B0003A', color: '#B0003A' } : {}}
    >
      {children}
    </button>
  )
}

export default function Sales() {
  const [activeTab, setActiveTab] = useState('products')
  const [tabItems, setTabItems] = useState([])
  const [tabLoading, setTabLoading] = useState(false)
  const [tabError, setTabError] = useState('')
  const [tabPage, setTabPage] = useState(0)
  const [tabTotalPages, setTabTotalPages] = useState(1)

  // Recent sales
  const [sales, setSales] = useState([])
  const [salesLoading, setSalesLoading] = useState(true)
  const [salesError, setSalesError] = useState('')

  // Dropdown data
  const [clients, setClients] = useState([])
  const [paymentTypes, setPaymentTypes] = useState([])

  // Payment/Sell modal
  const [payModal, setPayModal] = useState(false)
  const [payItem, setPayItem] = useState(null)
  const [payForm, setPayForm] = useState({ clientId: '', paymentTypeId: '', amount: '' })
  const [payFormError, setPayFormError] = useState('')
  const [paying, setPaying] = useState(false)
  const [paySuccess, setPaySuccess] = useState(false)

  const currentTab = SALE_TABS.find(t => t.key === activeTab)

  const fetchTabItems = useCallback(async (tab, p = 0) => {
    const cfg = SALE_TABS.find(t => t.key === tab)
    if (!cfg) return
    setTabLoading(true); setTabError('')
    try {
      const res = await apiClient.get(`${cfg.endpoint}?page=${p}&size=20`)
      const d = res.data
      if (Array.isArray(d)) { setTabItems(d); setTabTotalPages(1) }
      else { setTabItems(d.content || d.data || []); setTabTotalPages(d.totalPages || 1) }
    } catch { setTabError('Failed to load items') }
    finally { setTabLoading(false) }
  }, [])

  const fetchSales = useCallback(async () => {
    setSalesLoading(true); setSalesError('')
    try {
      const res = await apiClient.get('/sales?page=0&size=10')
      const d = res.data
      setSales(Array.isArray(d) ? d : d?.content || d?.data || [])
    } catch { setSalesError('Failed to load sales') }
    finally { setSalesLoading(false) }
  }, [])

  useEffect(() => { setTabPage(0); fetchTabItems(activeTab, 0) }, [activeTab, fetchTabItems])
  useEffect(() => { fetchTabItems(activeTab, tabPage) }, [tabPage])  // eslint-disable-line
  useEffect(() => { fetchSales() }, [fetchSales])
  useEffect(() => {
    Promise.all([
      apiClient.get('/clients').catch(() => ({ data: [] })),
      apiClient.get('/payment-types').catch(() => ({ data: [] })),
    ]).then(([c, pt]) => {
      setClients(Array.isArray(c.data) ? c.data : c.data?.content || c.data?.data || [])
      setPaymentTypes(Array.isArray(pt.data) ? pt.data : pt.data?.content || pt.data?.data || [])
    })
  }, [])

  const openPay = (item) => {
    setPayItem(item)
    setPayForm({ clientId: '', paymentTypeId: '', amount: item.totalAmount || item.price || '' })
    setPayFormError('')
    setPaySuccess(false)
    setPayModal(true)
  }

  const handlePay = async () => {
    if (!payForm.clientId) { setPayFormError('Client is required'); return }
    if (!payForm.amount) { setPayFormError('Amount is required'); return }
    setPayFormError(''); setPaying(true)
    try {
      const isInstallment = activeTab === 'installments'
      const endpoint = isInstallment ? `/installments/${payItem.id}/pay` : '/sales'
      const body = isInstallment
        ? { amountPaid: payForm.amount }
        : {
            clientId: payForm.clientId,
            paymentTypeId: payForm.paymentTypeId,
            amount: payForm.amount,
            itemId: payItem.id,
            itemType: activeTab.toUpperCase().slice(0, -1), // PRODUCT, TREATMENT, PACKAGE
          }
      await apiClient.post(endpoint, body)
      setPaySuccess(true)
      fetchSales()
      fetchTabItems(activeTab, tabPage)
      setTimeout(() => setPayModal(false), 1200)
    } catch (err) {
      setPayFormError(err.response?.data?.message || 'Transaction failed')
    } finally { setPaying(false) }
  }

  const setPayField = (k, v) => setPayForm(f => ({ ...f, [k]: v }))

  const isInstallmentTab = activeTab === 'installments'

  return (
    <div>
      <div className="mb-5">
        <h1 className="text-xl font-bold text-gray-800">Sales</h1>
        <p className="text-sm text-gray-500">Process transactions and record sales</p>
      </div>

      {/* Filter tabs */}
      <div className="flex border-b border-gray-200 mb-0">
        {SALE_TABS.map(t => (
          <SaleTabButton key={t.key} active={activeTab === t.key} onClick={() => setActiveTab(t.key)}>
            {t.label}
          </SaleTabButton>
        ))}
      </div>

      {/* Items table */}
      <div className="bg-white rounded-b-xl rounded-tr-xl shadow-sm overflow-hidden mb-6">
        {tabError && <div className="text-red-500 text-sm p-4">{tabError}</div>}
        <table className="w-full">
          <thead>
            <tr>
              <th className="table-header">Name</th>
              {isInstallmentTab ? (
                <>
                  <th className="table-header">Client</th>
                  <th className="table-header">Due Date</th>
                  <th className="table-header">Total</th>
                  <th className="table-header">Remaining</th>
                </>
              ) : (
                <>
                  <th className="table-header">Price / Amount</th>
                  {activeTab === 'products' && <th className="table-header">Stock</th>}
                </>
              )}
              <th className="table-header">Action</th>
            </tr>
          </thead>
          <tbody>
            {tabLoading ? (
              <tr><td colSpan={6} className="table-cell text-center text-gray-400 py-8">Loading...</td></tr>
            ) : tabItems.length === 0 ? (
              <tr><td colSpan={6} className="table-cell text-center text-gray-400 py-8">No data found</td></tr>
            ) : tabItems.map(item => (
              <tr key={item.id} className="table-row">
                <td className="table-cell font-medium">{item.name || '-'}</td>
                {isInstallmentTab ? (
                  <>
                    <td className="table-cell">{item.clientName || item.client?.name || '-'}</td>
                    <td className="table-cell">{formatDate(item.dueDate)}</td>
                    <td className="table-cell">{item.totalAmount != null ? formatRupiah(item.totalAmount) : '-'}</td>
                    <td className="table-cell">
                      {item.remaining != null
                        ? formatRupiah(item.remaining)
                        : item.totalAmount != null && item.amountPaid != null
                          ? formatRupiah(Number(item.totalAmount) - Number(item.amountPaid))
                          : '-'}
                    </td>
                  </>
                ) : (
                  <>
                    <td className="table-cell">{item.price != null ? formatRupiah(item.price) : '-'}</td>
                    {activeTab === 'products' && <td className="table-cell">{item.stock ?? '-'}</td>}
                  </>
                )}
                <td className="table-cell">
                  <button
                    onClick={() => openPay(item)}
                    className="btn-primary text-sm py-1"
                  >
                    {isInstallmentTab ? 'Pay' : 'Sell'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {tabTotalPages > 1 && (
          <div className="flex justify-center gap-2 py-4">
            <button onClick={() => setTabPage(p => Math.max(0, p-1))} disabled={tabPage===0} className="btn-secondary text-sm disabled:opacity-40">← Prev</button>
            <span className="text-sm text-gray-600 py-2">Page {tabPage+1} of {tabTotalPages}</span>
            <button onClick={() => setTabPage(p => Math.min(tabTotalPages-1, p+1))} disabled={tabPage>=tabTotalPages-1} className="btn-secondary text-sm disabled:opacity-40">Next →</button>
          </div>
        )}
      </div>

      {/* Recent sales */}
      <div>
        <h2 className="text-base font-semibold text-gray-700 mb-3">Recent Transactions</h2>
        {salesError && <div className="text-red-500 text-sm mb-2">{salesError}</div>}
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
          <table className="w-full">
            <thead>
              <tr>
                <th className="table-header">Date</th>
                <th className="table-header">Client</th>
                <th className="table-header">Item</th>
                <th className="table-header">Amount</th>
              </tr>
            </thead>
            <tbody>
              {salesLoading ? (
                <tr><td colSpan={4} className="table-cell text-center text-gray-400 py-6">Loading...</td></tr>
              ) : sales.length === 0 ? (
                <tr><td colSpan={4} className="table-cell text-center text-gray-400 py-6">No transactions yet</td></tr>
              ) : sales.map((s, i) => (
                <tr key={s.id || i} className="table-row">
                  <td className="table-cell">{formatDate(s.date || s.createdAt || s.saleDate)}</td>
                  <td className="table-cell">{s.clientName || s.client?.name || '-'}</td>
                  <td className="table-cell">{s.itemName || s.productName || s.treatmentName || s.packageName || '-'}</td>
                  <td className="table-cell font-medium">{s.amount != null ? formatRupiah(s.amount) : '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Payment Modal */}
      <Modal
        isOpen={payModal}
        onClose={() => setPayModal(false)}
        title={isInstallmentTab ? 'Pay Installment' : `Sell: ${payItem?.name || ''}`}
        footer={
          paySuccess ? null : (
            <>
              <button onClick={() => setPayModal(false)} className="btn-secondary">Cancel</button>
              <button onClick={handlePay} disabled={paying} className="btn-primary">
                {paying ? 'Processing...' : isInstallmentTab ? 'Pay' : 'Confirm Sale'}
              </button>
            </>
          )
        }
      >
        {paySuccess ? (
          <div className="text-center py-6">
            <div className="text-5xl mb-3">✅</div>
            <p className="text-green-700 font-semibold">Transaction successful!</p>
          </div>
        ) : (
          <>
            {payFormError && <div className="mb-3 bg-red-50 border border-red-200 text-red-700 rounded px-3 py-2 text-sm">{payFormError}</div>}

            {payItem && (
              <div className="mb-4 bg-gray-50 rounded-lg px-4 py-3 text-sm">
                <div className="font-medium text-gray-800">{payItem.name}</div>
                {payItem.price != null && <div className="text-gray-500 mt-0.5">Price: {formatRupiah(payItem.price)}</div>}
              </div>
            )}

            <div className="form-group">
              <label className="form-label">Client *</label>
              <select value={payForm.clientId} onChange={e => setPayField('clientId', e.target.value)} className="form-input">
                <option value="">— Select Client —</option>
                {clients.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
              </select>
            </div>

            {!isInstallmentTab && (
              <div className="form-group">
                <label className="form-label">Payment Type</label>
                <select value={payForm.paymentTypeId} onChange={e => setPayField('paymentTypeId', e.target.value)} className="form-input">
                  <option value="">— Select Type —</option>
                  {paymentTypes.map(pt => <option key={pt.id} value={pt.id}>{pt.name}</option>)}
                  {paymentTypes.length === 0 && (
                    <>
                      <option value="CASH">Cash</option>
                      <option value="TRANSFER">Transfer</option>
                      <option value="CARD">Card</option>
                    </>
                  )}
                </select>
              </div>
            )}

            <div className="form-group">
              <label className="form-label">Amount *</label>
              <input
                type="number"
                value={payForm.amount}
                onChange={e => setPayField('amount', e.target.value)}
                className="form-input"
                placeholder="0"
              />
            </div>
          </>
        )}
      </Modal>
    </div>
  )
}
