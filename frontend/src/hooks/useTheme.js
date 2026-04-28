import { useEffect, useState } from 'react'

const STORAGE_KEY = 'theme'

function readInitial() {
  if (typeof window === 'undefined') return 'light'
  return localStorage.getItem(STORAGE_KEY) || 'light'
}

export default function useTheme() {
  const [theme, setTheme] = useState(readInitial)

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme)
    localStorage.setItem(STORAGE_KEY, theme)
  }, [theme])

  useEffect(() => {
    function onStorage(e) {
      if (e.key === STORAGE_KEY && e.newValue) setTheme(e.newValue)
    }
    window.addEventListener('storage', onStorage)
    return () => window.removeEventListener('storage', onStorage)
  }, [])

  function toggle() {
    setTheme((t) => (t === 'dark' ? 'light' : 'dark'))
  }

  return [theme, toggle]
}
