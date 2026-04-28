import { useState } from 'react'
import LoginPage from './LoginPage'

export default function ProtectedRoute({ children }) {
  const [authed, setAuthed] = useState(
    () => localStorage.getItem('demo_auth') === '1'
  )

  if (!import.meta.env.VITE_DEMO_PASSCODE) return children
  if (!authed) return <LoginPage onAuthed={() => setAuthed(true)} />
  return children
}
