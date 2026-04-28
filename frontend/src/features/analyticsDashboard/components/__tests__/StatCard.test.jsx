import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import StatCard from '../StatCard'

describe('StatCard', () => {
  it('renders label and value', () => {
    render(<StatCard label="Total students" value={24} loading={false} />)
    expect(screen.getByText('Total students')).toBeInTheDocument()
    expect(screen.getByText('24')).toBeInTheDocument()
  })

  it('renders skeleton when loading', () => {
    const { container } = render(<StatCard label="Total students" value={24} loading={true} />)
    expect(container.querySelector('.skeleton')).toBeTruthy()
  })

  it('renders dash when value is null', () => {
    render(<StatCard label="Avg quiz score" value={null} loading={false} />)
    expect(screen.getByText('–')).toBeInTheDocument()
  })
})
