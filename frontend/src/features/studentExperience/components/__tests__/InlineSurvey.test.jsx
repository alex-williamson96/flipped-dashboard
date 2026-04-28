import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, vi } from 'vitest'
import InlineSurvey from '../InlineSurvey'

const mockMutateAsync = vi.fn()

vi.mock('../../hooks/useLessonEvents', () => ({
  useSurveyResponse: () => ({ mutateAsync: mockMutateAsync }),
}))

const makeItem = () => ({
  lessonItemId: 2,
  questions: [
    { surveyQuestionId: 20, prompt: 'How was the lesson?', questionType: 'LIKERT' },
    { surveyQuestionId: 21, prompt: 'Any other feedback?', questionType: 'FREE_TEXT' },
  ],
})

describe('InlineSurvey', () => {
  it('renders all questions', () => {
    render(<InlineSurvey item={makeItem()} />)
    expect(screen.getByText('How was the lesson?')).toBeInTheDocument()
    expect(screen.getByText('Any other feedback?')).toBeInTheDocument()
  })

  it('submit button is enabled', () => {
    render(<InlineSurvey item={makeItem()} />)
    expect(screen.getByRole('button', { name: /submit survey/i })).not.toBeDisabled()
  })

  it('shows API error message on 400 response', async () => {
    mockMutateAsync.mockRejectedValue({
      response: { status: 400, data: { error: 'Response contains content that is not allowed' } },
    })
    render(<InlineSurvey item={makeItem()} />)
    await userEvent.click(screen.getByRole('button', { name: /submit survey/i }))
    await waitFor(() =>
      expect(screen.getByText('Response contains content that is not allowed')).toBeInTheDocument()
    )
  })
})
