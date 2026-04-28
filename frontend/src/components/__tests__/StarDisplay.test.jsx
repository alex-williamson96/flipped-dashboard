import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import StarDisplay from '../StarDisplay'

describe('StarDisplay', () => {
  it('renders 5 stars', () => {
    render(<StarDisplay value={3} />)
    expect(screen.getAllByRole('img')).toHaveLength(5)
  })

  it('with value=4.0, star 4 is fully filled and star 5 is empty', () => {
    render(<StarDisplay value={4.0} />)
    expect(screen.getByLabelText('star 4 filled')).toBeInTheDocument()
    expect(screen.getByLabelText('star 5 empty')).toBeInTheDocument()
  })

  it('with value=3.4, star 4 is partially filled and star 5 is empty', () => {
    const { container } = render(<StarDisplay value={3.4} />)
    expect(screen.getByLabelText('star 4 partial')).toBeInTheDocument()
    expect(screen.getByLabelText('star 5 empty')).toBeInTheDocument()
    expect(container.querySelector('linearGradient')).toBeTruthy()
    expect(container.querySelector('stop').getAttribute('offset')).toBe('40.0%')
  })
})
