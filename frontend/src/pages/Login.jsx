import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!username.trim()) { setError('Username is required'); return }
    if (!password.trim()) { setError('Password is required'); return }
    setError('')
    setLoading(true)
    try {
      await login(username, password)
      navigate('/dashboard')
    } catch (err) {
      setError(
        err.response?.data?.message ||
        err.response?.data?.error ||
        'Login failed. Please check your credentials.'
      )
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    setUsername('')
    setPassword('')
    setError('')
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-pink-50 to-blue-50">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-md mx-4 overflow-hidden">

        {/* Crimson header bar */}
        <div
          className="flex items-center justify-between px-6 py-5"
          style={{ backgroundColor: '#B0003A' }}
        >
          <div>
            <h1 className="text-white text-2xl font-bold">IB Clinic</h1>
            <p className="text-pink-200 text-sm mt-0.5">Management System</p>
          </div>
          <div className="text-white text-4xl opacity-80">🔒</div>
        </div>

        {/* Login sub-header */}
        <div className="px-6 pt-5 pb-1">
          <h2 className="text-gray-800 text-lg font-semibold border-b border-gray-200 pb-2">Login</h2>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="px-6 py-4">
          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 rounded-lg px-4 py-2 text-sm">
              {error}
            </div>
          )}

          <div className="mb-4">
            <label className="block text-sm text-gray-700 mb-1 font-medium">
              Username :
            </label>
            <input
              type="text"
              value={username}
              onChange={e => setUsername(e.target.value)}
              className="w-full bg-gray-100 border border-gray-200 rounded-lg px-3 py-2.5 text-sm text-gray-800 focus:outline-none focus:ring-2 focus:border-transparent"
              style={{ '--tw-ring-color': '#B0003A' }}
              onFocus={e => e.target.style.boxShadow = '0 0 0 2px #B0003A40'}
              onBlur={e => e.target.style.boxShadow = 'none'}
              placeholder="Enter your username"
              autoComplete="username"
            />
          </div>

          <div className="mb-6">
            <label className="block text-sm text-gray-700 mb-1 font-medium">
              Password :
            </label>
            <input
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              className="w-full bg-gray-100 border border-gray-200 rounded-lg px-3 py-2.5 text-sm text-gray-800 focus:outline-none"
              onFocus={e => e.target.style.boxShadow = '0 0 0 2px #B0003A40'}
              onBlur={e => e.target.style.boxShadow = 'none'}
              placeholder="Enter your password"
              autoComplete="current-password"
            />
          </div>

          {/* Buttons row */}
          <div className="flex gap-3 justify-center pb-2">
            <button
              type="submit"
              disabled={loading}
              className="flex items-center gap-2 text-white px-8 py-2.5 rounded-full font-medium text-sm transition-colors shadow-sm disabled:opacity-60"
              style={{ backgroundColor: loading ? '#888' : '#B0003A' }}
              onMouseOver={e => { if (!loading) e.currentTarget.style.backgroundColor = '#D4004A' }}
              onMouseOut={e => { if (!loading) e.currentTarget.style.backgroundColor = '#B0003A' }}
            >
              <span>•</span>
              <span>{loading ? 'Logging in...' : 'Login'}</span>
            </button>
            <button
              type="button"
              onClick={handleCancel}
              className="bg-gray-200 text-gray-700 px-8 py-2.5 rounded-full font-medium text-sm hover:bg-gray-300 transition-colors"
            >
              Cancel
            </button>
          </div>
        </form>

        {/* Footer */}
        <div className="text-center text-xs text-gray-400 py-3 border-t border-gray-100">
          IB Clinic &copy; {new Date().getFullYear()}
        </div>
      </div>
    </div>
  )
}
