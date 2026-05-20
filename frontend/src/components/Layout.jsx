import React, { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

const NAV_ITEMS = [
  { icon: '🏠', label: 'Dashboard', to: '/dashboard' },
  { icon: '📅', label: 'Schedule',  to: '/schedule'  },
  { icon: '👥', label: 'Clients',   to: '/clients'   },
  { icon: '👨‍⚕️', label: 'Staff',     to: '/staff'     },
  { icon: '💆', label: 'Treatments',to: '/treatments' },
  { icon: '📦', label: 'Products',  to: '/products'  },
  { icon: '💰', label: 'Sales',     to: '/sales'     },
  { icon: '📊', label: 'Reports',   to: '/reports', adminOnly: true },
]

export default function Layout() {
  const { user, logout, isAdmin } = useAuth()
  const [sidebarOpen, setSidebarOpen] = useState(true)

  const navItems = NAV_ITEMS.filter(item => !item.adminOnly || isAdmin())

  return (
    <div className="flex flex-col min-h-screen">
      {/* Top Header */}
      <header
        className="fixed top-0 left-0 right-0 z-40 flex items-center justify-between px-6 py-3 shadow-md"
        style={{ backgroundColor: '#B0003A' }}
      >
        <div className="flex items-center gap-3">
          <button
            className="text-white text-xl hover:text-pink-200 transition-colors mr-1"
            onClick={() => setSidebarOpen(o => !o)}
            title="Toggle sidebar"
          >
            ☰
          </button>
          <span className="text-white text-xl font-bold tracking-wide">IB Clinic</span>
          <span className="text-pink-200 text-sm hidden sm:inline">Management System</span>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-white text-sm">
            👤 <span className="font-medium">{user?.username || user?.name || 'User'}</span>
            {user?.role && (
              <span className="ml-1 text-pink-200 text-xs">({user.role})</span>
            )}
          </span>
          <button
            onClick={logout}
            className="bg-white text-crimson px-4 py-1.5 rounded-full text-sm font-medium hover:bg-pink-100 transition-colors"
            style={{ color: '#B0003A' }}
          >
            Logout
          </button>
        </div>
      </header>

      <div className="flex flex-1 pt-14">
        {/* Sidebar */}
        <aside
          className={`fixed left-0 top-14 bottom-0 z-30 bg-white shadow-lg transition-all duration-300 overflow-y-auto ${
            sidebarOpen ? 'w-56' : 'w-0 overflow-hidden'
          }`}
        >
          <nav className="py-4">
            {navItems.map(item => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  `flex items-center gap-3 px-5 py-3 text-sm font-medium transition-colors ${
                    isActive
                      ? 'bg-pink-50 text-crimson border-r-4 border-crimson font-semibold'
                      : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                  }`
                }
                style={({ isActive }) => isActive ? { color: '#B0003A' } : {}}
              >
                <span className="text-base">{item.icon}</span>
                <span>{item.label}</span>
              </NavLink>
            ))}
          </nav>
        </aside>

        {/* Main content */}
        <main
          className={`flex-1 transition-all duration-300 ${sidebarOpen ? 'ml-56' : 'ml-0'} p-6 min-h-screen`}
        >
          <Outlet />
        </main>
      </div>
    </div>
  )
}
