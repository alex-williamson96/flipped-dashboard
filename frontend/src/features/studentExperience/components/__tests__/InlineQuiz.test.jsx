import { render, screen, fireEvent } from '@testing-library/react'
import { describe, it, vi } from 'vitest'
import InlineQuiz from '../InlineQuiz'

vi.mock('../../hooks/useLessonEvents', () => ({
  useQuizResponse: () => ({ mutateAsync: vi.fn().mockResolvedValue({}) }),
}))

const makeItem = () => ({
  lessonItemId: 1,
  questions: [
    {
      quizQuestionId: 10,
      prompt: 'What is 2+2?',
      choices: [
        { quizChoiceId: 1, text: '3' },
        { quizChoiceId: 2, text: '4' },
      ],
    },
    {
      quizQuestionId: 11,
      prompt: 'What color is the sky?',
      choices: [
        { quizChoiceId: 3, text: 'Blue' },
        { quizChoiceId: 4, text: 'Green' },
      ],
    },
  ],
})

describe('InlineQuiz', () => {
  it('submit button is disabled when no choice selected', () => {
    render(<InlineQuiz item={makeItem()} />)
    expect(screen.getByRole('button', { name: /submit answer/i })).toBeDisabled()
  })

  it('shows next question after submit', async () => {
    render(<InlineQuiz item={makeItem()} />)
    fireEvent.click(screen.getByLabelText('4'))
    fireEvent.click(screen.getByRole('button', { name: /submit answer/i }))
    expect(await screen.findByText('What color is the sky?')).toBeInTheDocument()
  })

  it('shows "Quiz complete" after all questions answered', async () => {
    render(<InlineQuiz item={makeItem()} />)
    fireEvent.click(screen.getByLabelText('4'))
    fireEvent.click(screen.getByRole('button', { name: /submit answer/i }))
    fireEvent.click(await screen.findByLabelText('Blue'))
    fireEvent.click(screen.getByRole('button', { name: /submit answer/i }))
    expect(await screen.findByText('Quiz complete')).toBeInTheDocument()
  })
})
