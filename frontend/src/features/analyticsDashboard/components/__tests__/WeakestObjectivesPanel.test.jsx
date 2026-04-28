import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import WeakestObjectivesPanel from '../WeakestObjectivesPanel'

describe('WeakestObjectivesPanel', () => {
  it('returns null when loAccuracy is undefined', () => {
    const { container } = render(<WeakestObjectivesPanel />)
    expect(container.firstChild).toBeNull()
  })

  it('returns null when loAccuracy is an empty array', () => {
    const { container } = render(<WeakestObjectivesPanel loAccuracy={[]} />)
    expect(container.firstChild).toBeNull()
  })

  it('renders each objective name and accuracy percentage', () => {
    const data = [
      { objective: 'Understand loops', accuracy: 0.75 },
      { objective: 'Apply recursion', accuracy: 0.5 },
    ]
    render(<WeakestObjectivesPanel loAccuracy={data} />)
    expect(screen.getByText('Understand loops')).toBeInTheDocument()
    expect(screen.getByText('75%')).toBeInTheDocument()
    expect(screen.getByText('Apply recursion')).toBeInTheDocument()
    expect(screen.getByText('50%')).toBeInTheDocument()
  })

  it('applies text-error class to accuracy below 60%', () => {
    const data = [{ objective: 'Low scorer', accuracy: 0.45 }]
    render(<WeakestObjectivesPanel loAccuracy={data} />)
    const pctSpan = screen.getByText('45%')
    expect(pctSpan.className).toContain('text-error')
  })

  it('does not apply text-error class to accuracy at or above 60%', () => {
    const data = [
      { objective: 'At threshold', accuracy: 0.6 },
      { objective: 'Above threshold', accuracy: 0.85 },
    ]
    render(<WeakestObjectivesPanel loAccuracy={data} />)
    expect(screen.getByText('60%').className).not.toContain('text-error')
    expect(screen.getByText('85%').className).not.toContain('text-error')
  })
})
