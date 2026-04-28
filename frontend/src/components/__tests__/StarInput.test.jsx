import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi } from 'vitest'
import StarInput from '../StarInput'

describe('StarInput', () => {
  it('renders 5 stars', () => {
    render(<StarInput value={0} onChange={() => {}} />)
    expect(screen.getAllByRole('button')).toHaveLength(5)
  })

  it('clicking the 3rd star calls onChange(3)', async () => {
    const onChange = vi.fn()
    render(<StarInput value={0} onChange={onChange} />)
    await userEvent.click(screen.getAllByRole('button')[2])
    expect(onChange).toHaveBeenCalledWith(3)
  })

  it('hover over star 4 previews stars 1-4 filled', async () => {
    render(<StarInput value={1} onChange={() => {}} />)
    await userEvent.hover(screen.getAllByRole('button')[3])
    expect(screen.getByRole('button', { name: 'star 4 filled' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'star 5 empty' })).toBeInTheDocument()
  })

  it('stars 1-3 are labelled filled when value=3', () => {
    render(<StarInput value={3} onChange={() => {}} />)
    expect(screen.getByRole('button', { name: 'star 1 filled' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'star 2 filled' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'star 3 filled' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'star 4 empty' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'star 5 empty' })).toBeInTheDocument()
  })
})
