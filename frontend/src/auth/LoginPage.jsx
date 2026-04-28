import { useState } from 'react'

export default function LoginPage({ onAuthed }) {
  const [passcode, setPasscode] = useState('')
  const [error, setError] = useState(null)

  function handleSubmit(e) {
    e.preventDefault()
    const expected = import.meta.env.VITE_DEMO_PASSCODE
    if (!expected) {
      setError('Demo passcode is not configured.')
      return
    }
    if (passcode === expected) {
      localStorage.setItem('demo_auth', '1')
      onAuthed()
      return
    }
    setError('Incorrect passcode.')
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <form
        className="card card-bordered bg-base-100 p-6 w-full max-w-sm"
        onSubmit={handleSubmit}
      >
        <h1 className="text-xl font-semibold mb-2">Flipped dashboard demo</h1>
        <p className="text-sm text-base-content/60 mb-4">
          Enter the demo passcode to continue.
        </p>
        <input
          type="password"
          autoFocus
          className="input input-bordered w-full mb-3"
          value={passcode}
          onChange={e => setPasscode(e.target.value)}
          aria-label="Demo passcode"
        />
        {error && <p className="text-error text-sm mb-3">{error}</p>}
        <button type="submit" className="btn btn-primary w-full">
          Continue
        </button>
      </form>
    </div>
  )
}
