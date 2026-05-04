import { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react'
import apiClient from '../../lib/apiClient'

const IdentityContext = createContext(null)

function readStorage() {
  const role = localStorage.getItem('identity.role')
  if (role !== 'teacher' && role !== 'student') return { role: 'teacher', studentId: null, studentName: null }
  const studentId = role === 'student' ? Number(localStorage.getItem('identity.studentId')) || null : null
  const studentName = role === 'student' ? localStorage.getItem('identity.studentName') : null
  return { role, studentId, studentName }
}

export function IdentityProvider({ children }) {
  const [identity, setIdentity] = useState(readStorage)
  const studentIdRef = useRef(identity.studentId)
  const roleRef = useRef(identity.role)

  useEffect(() => {
    studentIdRef.current = identity.studentId
    roleRef.current = identity.role
  }, [identity.studentId, identity.role])

  useEffect(() => {
    const id = apiClient.interceptors.request.use((config) => {
      if (roleRef.current === 'student' && studentIdRef.current != null) {
        config.headers['X-Student-Id'] = studentIdRef.current
      }
      return config
    })
    return () => apiClient.interceptors.request.eject(id)
  }, [])

  const setTeacher = useCallback(() => {
    localStorage.setItem('identity.role', 'teacher')
    localStorage.removeItem('identity.studentId')
    localStorage.removeItem('identity.studentName')
    setIdentity({ role: 'teacher', studentId: null, studentName: null })
  }, [])

  const setStudent = useCallback((studentId, name) => {
    localStorage.setItem('identity.role', 'student')
    localStorage.setItem('identity.studentId', String(studentId))
    localStorage.setItem('identity.studentName', name)
    setIdentity({ role: 'student', studentId, studentName: name })
  }, [])

  return (
    <IdentityContext.Provider value={{ ...identity, setTeacher, setStudent }}>
      {children}
    </IdentityContext.Provider>
  )
}

export function useIdentity() {
  return useContext(IdentityContext)
}
