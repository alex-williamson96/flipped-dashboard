import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import LoginPage from '../LoginPage'

describe('LoginPage', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  afterEach(() => {
    vi.unstubAllEnvs()
    localStorage.clear()
  })

  it('shows an error when the passcode is wrong', async () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', 'secret')
    const onAuthed = vi.fn()
    render(<LoginPage onAuthed={onAuthed} />)

    await userEvent.type(screen.getByLabelText('Demo passcode'), 'nope')
    await userEvent.click(screen.getByRole('button', { name: /continue/i }))

    expect(screen.getByText(/incorrect passcode/i)).toBeInTheDocument()
    expect(onAuthed).not.toHaveBeenCalled()
    expect(localStorage.getItem('demo_auth')).toBeNull()
  })

  it('calls onAuthed and persists on a correct passcode', async () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', 'secret')
    const onAuthed = vi.fn()
    render(<LoginPage onAuthed={onAuthed} />)

    await userEvent.type(screen.getByLabelText('Demo passcode'), 'secret')
    await userEvent.click(screen.getByRole('button', { name: /continue/i }))

    expect(onAuthed).toHaveBeenCalledOnce()
    expect(localStorage.getItem('demo_auth')).toBe('1')
  })

  it('reports a configuration error when the env var is unset', async () => {
    vi.stubEnv('VITE_DEMO_PASSCODE', '')
    const onAuthed = vi.fn()
    render(<LoginPage onAuthed={onAuthed} />)

    await userEvent.click(screen.getByRole('button', { name: /continue/i }))

    expect(screen.getByText(/not configured/i)).toBeInTheDocument()
    expect(onAuthed).not.toHaveBeenCalled()
  })
})
