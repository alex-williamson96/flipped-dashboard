import { useEffect, useState } from 'react'
import apiClient from '../lib/apiClient'

export default function ColdStartBanner() {
  const [status, setStatus] = useState('probing')

  useEffect(() => {
    const slowTimer = setTimeout(() => {
      setStatus(prev => (prev === 'probing' ? 'slow' : prev))
    }, 2_000)

    apiClient
      .get('/health')
      .then(() => setStatus('ready'))
      .catch(() => setStatus('error'))
      .finally(() => clearTimeout(slowTimer))

    return () => clearTimeout(slowTimer)
  }, [])

  if (status !== 'slow') return null

  return (
    <div className="bg-warning/20 border-b border-warning px-4 py-2 text-sm flex items-center gap-2">
      <span className="loading loading-spinner loading-xs" />
      <span>Waking the server - the demo backend may take up to 30 seconds to cold-start...</span>
    </div>
  )
}
