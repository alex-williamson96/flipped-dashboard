import { render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { describe, it, expect } from 'vitest'
import FilterBar from '../FilterBar'

function wrapper({ children }) {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } })
  return <QueryClientProvider client={client}>{children}</QueryClientProvider>
}

const defaultFilters = {
  courseId: null,
  sectionId: null,
  lessonId: null,
  completionStatus: 'all',
  minQuizScore: null,
  maxQuizScore: null,
  confidenceLevel: null,
}

describe('FilterBar', () => {
  it('renders all filter controls', () => {
    render(<FilterBar filters={defaultFilters} onChange={() => {}} />, { wrapper })
    // Completion status select should always render
    expect(screen.getByDisplayValue('All')).toBeInTheDocument()
  })

  it('section dropdown is disabled when no courseId', () => {
    render(<FilterBar filters={defaultFilters} onChange={() => {}} />, { wrapper })
    // The section select should be disabled
    const selects = screen.getAllByRole('combobox')
    // Find the one that has "All sections" option
    const sectionSelect = selects.find(s => s.textContent.includes('All sections'))
    expect(sectionSelect).toBeDisabled()
  })
})
