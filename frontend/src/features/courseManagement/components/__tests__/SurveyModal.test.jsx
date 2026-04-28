import { render, screen, fireEvent } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { describe, it, expect } from 'vitest'
import SurveyModal from '../SurveyModal'

function wrapper({ children }) {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } })
  return <QueryClientProvider client={client}>{children}</QueryClientProvider>
}

const getSaveBtn = () => screen.getByRole('button', { name: 'Save', hidden: true })

describe('SurveyModal', () => {
  it('disables Save when Likert question is empty and free-text is empty', () => {
    render(<SurveyModal lessonId="1" />, { wrapper })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('disables Save when Likert question is filled but free-text is empty', () => {
    render(<SurveyModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getByPlaceholderText(/Likert question 1/i), { target: { value: 'How useful was this?' } })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('disables Save when Likert is filled but free-text is cleared after being set', () => {
    render(<SurveyModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getByPlaceholderText(/Likert question 1/i), { target: { value: 'Rate this lesson' } })
    const freeTextInput = screen.getByPlaceholderText(/Free-text question/i)
    fireEvent.change(freeTextInput, { target: { value: 'Any comments?' } })
    fireEvent.change(freeTextInput, { target: { value: '' } })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('enables Save when ≥1 Likert question and non-empty free-text', () => {
    render(<SurveyModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getByPlaceholderText(/Likert question 1/i), { target: { value: 'How useful was this?' } })
    fireEvent.change(screen.getByPlaceholderText(/Free-text question/i), { target: { value: 'Any other comments?' } })
    expect(getSaveBtn()).not.toBeDisabled()
  })
})
