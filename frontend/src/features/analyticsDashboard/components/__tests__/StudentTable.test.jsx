import { render, screen, fireEvent } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import StudentTable from '../StudentTable'

const students = [
  { studentId: 1, name: 'Zelda', sectionName: 'A', completed: true, quizScore: 90, totalGrade: 85, confidence: 4 },
  { studentId: 2, name: 'Alice', sectionName: 'B', completed: false, quizScore: null, totalGrade: null, confidence: null },
]

describe('StudentTable', () => {
  it('renders skeleton rows while loading', () => {
    const { container } = render(<StudentTable students={[]} loading={true} onRowClick={() => {}} />)
    expect(container.querySelectorAll('.skeleton').length).toBeGreaterThan(0)
  })

  it('renders empty state message when no students', () => {
    render(<StudentTable students={[]} loading={false} onRowClick={() => {}} />)
    expect(screen.getByText(/no students match/i)).toBeInTheDocument()
  })

  it('renders students sorted by name', () => {
    render(<StudentTable students={students} loading={false} onRowClick={() => {}} />)
    const rows = screen.getAllByRole('row')
    // rows[0] is header, rows[1] = Alice, rows[2] = Zelda
    expect(rows[1]).toHaveTextContent('Alice')
    expect(rows[2]).toHaveTextContent('Zelda')
  })

  it('calls onRowClick with the student when a row is clicked', () => {
    const onRowClick = vi.fn()
    render(<StudentTable students={students} loading={false} onRowClick={onRowClick} />)
    fireEvent.click(screen.getByText('Alice').closest('tr'))
    expect(onRowClick).toHaveBeenCalledWith(students[1])
  })
})
