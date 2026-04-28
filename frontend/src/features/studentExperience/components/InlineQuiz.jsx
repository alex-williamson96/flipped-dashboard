import { useState } from 'react'
import { useQuizResponse } from '../hooks/useLessonEvents'

export default function InlineQuiz({ item, readOnly }) {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0)
  const [selectedChoiceId, setSelectedChoiceId] = useState(null)
  const [done, setDone] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState(null)
  const { mutateAsync: submitQuizResponse } = useQuizResponse()

  const questions = item.questions ?? []

  if (readOnly) {
    return <p className="my-4 font-medium">Quiz disabled (course archived).</p>
  }

  if (done || item.completed || questions.length === 0) {
    return <p className="my-4 font-medium">Quiz complete</p>
  }

  const question = questions[currentQuestionIndex]

  const handleSubmit = async () => {
    setSubmitError(null)
    setSubmitting(true)
    try {
      await submitQuizResponse({ quizQuestionId: question.quizQuestionId, quizChoiceId: selectedChoiceId })
      if (currentQuestionIndex + 1 >= questions.length) {
        setDone(true)
      } else {
        setCurrentQuestionIndex(i => i + 1)
        setSelectedChoiceId(null)
      }
    } catch (error) {
      if (error.response?.status === 400 && error.response?.data?.error) {
        setSubmitError(error.response.data.error)
      } else {
        setSubmitError('Failed to submit answer. Please try again.')
      }
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="card card-bordered my-4 p-4">
      <p className="font-medium mb-3">{question.prompt}</p>
      <div className="flex flex-col gap-2 mb-4">
        {question.choices.map(choice => (
          <label key={choice.quizChoiceId} className="flex items-center gap-2 cursor-pointer">
            <input
              type="radio"
              name={`quiz-${item.lessonItemId}-q${question.quizQuestionId}`}
              value={choice.quizChoiceId}
              checked={selectedChoiceId === choice.quizChoiceId}
              onChange={() => setSelectedChoiceId(choice.quizChoiceId)}
            />
            {choice.text}
          </label>
        ))}
      </div>
      {submitError && <p className="text-error text-sm mb-2">{submitError}</p>}
      <button
        className="btn btn-sm btn-primary"
        onClick={handleSubmit}
        disabled={selectedChoiceId === null || submitting}
      >
        {submitting ? 'Submitting...' : 'Submit answer'}
      </button>
    </div>
  )
}
