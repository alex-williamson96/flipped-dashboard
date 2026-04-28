import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import ProtectedRoute from '../ProtectedRoute'

describe('ProtectedRoute', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  afterEach(() => {
    vi.unstubAllEnvs()
    localStorage.clear()
  })

  it('renders children directly when no passcode is configured', () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', '')
    render(
      <ProtectedRoute>
        <div>child content</div>
      </ProtectedRoute>,
    )
    expect(screen.getByText('child content')).toBeInTheDocument()
  })

  it('renders LoginPage when passcode is set and not authed', () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', 'secret')
    render(
      <ProtectedRoute>
        <div>child content</div>
      </ProtectedRoute>,
    )
    expect(screen.queryByText('child content')).not.toBeInTheDocument()
    expect(screen.getByLabelText('Demo passcode')).toBeInTheDocument()
  })

  it('renders children when passcode is set and demo_auth is present', () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', 'secret')
    localStorage.setItem('demo_auth', '1')
    render(
      <ProtectedRoute>
        <div>child content</div>
      </ProtectedRoute>,
    )
    expect(screen.getByText('child content')).toBeInTheDocument()
  })

  it('transitions from LoginPage to children after a correct passcode', async () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', 'secret')
    render(
      <ProtectedRoute>
        <div>child content</div>
      </ProtectedRoute>,
    )
    await userEvent.type(screen.getByLabelText('Demo passcode'), 'secret')
    await userEvent.click(screen.getByRole('button', { name: /continue/i }))
    expect(screen.getByText('child content')).toBeInTheDocument()
  })
})
