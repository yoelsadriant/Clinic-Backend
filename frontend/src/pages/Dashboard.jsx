import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import apiClient from '../api/client.js'
import { formatRupiah } from '../utils/format.js'

const MODULE_CARDS = [
  {
    icon: '📅',
    label: 'Schedule',
    desc: 'Manage appointments and bookings',
    to: '/schedule',
    bg: 'from-blue-500 to-blue-600',
  },
  {
    icon: '👥',
    label: 'Clients',
    desc: 'View and manage client records',
    to: '/clients',
    bg: 'from-purple-500 to-purple-600',
  },
  {
    icon: '👨‍⚕️',
    label: 'Staff',
    desc: 'Manage staff members',
    to: '/staff',
    bg: 'from-green-500 to-green-600',
  },
  {
    icon: '💆',
    label: 'Treatments',
    desc: 'Treatment types and packages',
    to: '/treatments',
    bg: 'from-orange-400 to-orange-500',
  },
  {
    icon: '📦',
    label: 'Products',
    desc: 'Product inventory and pricing',
    to: '/products',
    bg: 'from-yellow-500 to-yellow-600',
  },
  {
    icon: '💰',
    label: 'Sales',
    desc: 'Record and track transactions',
    to: '/sales',
    bg: 'from-crimson to-crimson-dark',
    style: { background: 'linear-gradient(135deg, #B0003A, #8B002E)' },
  },
  {
    icon: '📊',
    label: 'Reports',
    desc: 'Revenue and analytics reports',
    to: '/reports',
    bg: 'from-gray-700 to-gray-900',
    adminOnly: true,
  },
]

function StatCard({ icon, value, label, color }) {
  return (
    <div className="stat-card">
      <div
        className="w-12 h-12 rounded-full flex items-center justify-center text-2xl shrink-0"
        style={{ backgroundColor: color + '20' }}
      >
        {icon}
      </div>
      <div>
        <div className="text-2xl font-bold text-gray-800">{value ?? '—'}</div>
        <div className="text-sm text-gray-500 mt-0.5">{label}</div>
      </div>
    </div>
  )
}

export default function Dashboard() {
  const { user, isAdmin } = useAuth()
  const navigate = useNavigate()
  const [stats, setStats] = useState(null)
  const [statsError, setStatsError] = useState('')

  useEffect(() => {
    apiClient.get('/dashboard/stats')
      .then(r => setStats(r.data))
      .catch(() => setStatsError('Could not load stats'))
  }, [])

  const modules = MODULE_CARDS.filter(m => !m.adminOnly || isAdmin())

  return (
    <div>
      {/* Page title */}
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800">
          Welcome, {user?.username || user?.name || 'User'}!
        </h1>
        <p className="text-gray-500 text-sm mt-1">
          {new Date().toLocaleDateString('id-ID', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
        </p>
      </div>

      {/* Stats row */}
      {statsError ? (
        <div className="text-sm text-gray-400 mb-6">{statsError}</div>
      ) : (
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          <StatCard
            icon="👥"
            value={stats?.totalClients}
            label="Total Clients"
            color="#9C27B0"
          />
          <StatCard
            icon="👨‍⚕️"
            value={stats?.totalStaff}
            label="Total Staff"
            color="#4CAF50"
          />
          <StatCard
            icon="📅"
            value={stats?.todaySchedules}
            label="Today's Schedules"
            color="#6B6BFF"
          />
          <StatCard
            icon="💰"
            value={stats?.monthlyRevenue != null ? formatRupiah(stats.monthlyRevenue) : null}
            label="Monthly Revenue"
            color="#B0003A"
          />
        </div>
      )}

      {/* Module cards grid */}
      <h2 className="text-lg font-semibold text-gray-700 mb-4">Modules</h2>
      <div className="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
        {modules.map(mod => (
          <button
            key={mod.to}
            onClick={() => navigate(mod.to)}
            className={`bg-gradient-to-br ${mod.bg} text-white rounded-2xl p-5 text-left shadow-md hover:shadow-lg hover:-translate-y-0.5 transition-all duration-200 cursor-pointer`}
            style={mod.style}
          >
            <div className="text-4xl mb-3">{mod.icon}</div>
            <div className="font-semibold text-base">{mod.label}</div>
            <div className="text-white text-opacity-80 text-xs mt-1 leading-snug opacity-90">
              {mod.desc}
            </div>
          </button>
        ))}
      </div>
    </div>
  )
}
