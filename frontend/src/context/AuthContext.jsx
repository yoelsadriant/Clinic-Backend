import React, { createContext, useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const navigate = useNavigate()

  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('clinic_user')
      return stored ? JSON.parse(stored) : null
    } catch {
      return null
    }
  })

  const [token, setToken] = useState(() => localStorage.getItem('clinic_token') || null)

  const login = async (username, password) => {
    const response = await axios.post('/api/v1/auth/login', { username, password })
    const { token: newToken, ...userData } = response.data
    localStorage.setItem('clinic_token', newToken)
    localStorage.setItem('clinic_user', JSON.stringify(userData))
    setToken(newToken)
    setUser(userData)
    return userData
  }

  const logout = () => {
    localStorage.removeItem('clinic_token')
    localStorage.removeItem('clinic_user')
    setToken(null)
    setUser(null)
    navigate('/login')
  }

  const isAdmin = () => user?.role === 'ADMIN'
  const isDoctor = () => user?.role === 'DOCTOR'

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAdmin, isDoctor }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
