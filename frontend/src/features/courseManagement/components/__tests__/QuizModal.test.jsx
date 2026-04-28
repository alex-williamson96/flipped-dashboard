import { render, screen, fireEvent } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { describe, it, expect } from 'vitest'
import QuizModal from '../QuizModal'

function wrapper({ children }) {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } })
  return <QueryClientProvider client={client}>{children}</QueryClientProvider>
}

const getSaveBtn = () => screen.getByRole('button', { name: 'Save', hidden: true })

describe('QuizModal', () => {
  it('disables Save when no questions are present', () => {
    render(<QuizModal lessonId="1" />, { wrapper })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('disables Save when question has fewer than 2 non-empty choices', () => {
    render(<QuizModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getAllByPlaceholderText(/Question 1/i)[0], { target: { value: 'What is 2+2?' } })
    fireEvent.change(screen.getAllByPlaceholderText(/Choice/i)[0], { target: { value: 'Four' } })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('disables Save when no correct choice is marked', () => {
    render(<QuizModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getAllByPlaceholderText(/Question 1/i)[0], { target: { value: 'What is 2+2?' } })
    const choiceInputs = screen.getAllByPlaceholderText(/Choice/i)
    fireEvent.change(choiceInputs[0], { target: { value: 'Four' } })
    fireEvent.change(choiceInputs[1], { target: { value: 'Five' } })
    expect(getSaveBtn()).toBeDisabled()
  })

  it('enables Save when each question has ≥2 choices and exactly 1 correct marked', () => {
    render(<QuizModal lessonId="1" />, { wrapper })
    fireEvent.change(screen.getAllByPlaceholderText(/Question 1/i)[0], { target: { value: 'What is 2+2?' } })
    const choiceInputs = screen.getAllByPlaceholderText(/Choice/i)
    fireEvent.change(choiceInputs[0], { target: { value: 'Four' } })
    fireEvent.change(choiceInputs[1], { target: { value: 'Five' } })
    fireEvent.click(screen.getAllByRole('radio', { hidden: true })[0])
    expect(getSaveBtn()).not.toBeDisabled()
  })
})
