import { useState } from 'react'
import { useSurveyResponse } from '../hooks/useLessonEvents'
import StarInput from '../../../components/StarInput'

export default function InlineSurvey({ item, readOnly }) {
  const questions = item.questions ?? []
  const [answers, setAnswers] = useState(() =>
    Object.fromEntries(questions.map(q => [q.surveyQuestionId, q.questionType === 'FREE_TEXT' ? '' : null]))
  )
  const [statusById, setStatusById] = useState({})
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState(null)
  const { mutateAsync: submitSurveyResponse } = useSurveyResponse()

  if (readOnly) {
    return <p className="my-4 font-medium">Survey disabled (course archived).</p>
  }

  const allDone =
    questions.length > 0 &&
    questions.every(q => statusById[q.surveyQuestionId] === 'success')

  if (allDone || item.completed) {
    return <p className="my-4 font-medium">Survey submitted</p>
  }

  const handleSubmit = async () => {
    setSubmitError(null)
    setSubmitting(true)
    let sawError = false
    for (const q of questions) {
      if (statusById[q.surveyQuestionId] === 'success') continue
      setStatusById(prev => ({ ...prev, [q.surveyQuestionId]: 'submitting' }))
      try {
        await submitSurveyResponse({
          surveyQuestionId: q.surveyQuestionId,
          likertValue: q.questionType === 'LIKERT' ? answers[q.surveyQuestionId] : null,
          freeText: q.questionType === 'FREE_TEXT' ? answers[q.surveyQuestionId] : null,
        })
        setStatusById(prev => ({ ...prev, [q.surveyQuestionId]: 'success' }))
      } catch (error) {
        sawError = true
        setStatusById(prev => ({ ...prev, [q.surveyQuestionId]: 'error' }))
        if (error.response?.status === 400 && error.response?.data?.error) {
          setSubmitError(error.response.data.error)
        } else {
          setSubmitError('Some responses failed to submit. Please try again.')
        }
      }
    }
    setSubmitting(false)
    if (sawError) return
  }

  return (
    <div className="card card-bordered my-4 p-4">
      <div className="flex flex-col gap-6">
        {questions.map(q => {
          const status = statusById[q.surveyQuestionId]
          return (
            <div key={q.surveyQuestionId}>
              <p className="font-medium mb-2">{q.prompt}</p>
              {q.questionType === 'LIKERT' ? (
                <StarInput
                  value={answers[q.surveyQuestionId] ?? 0}
                  onChange={v => setAnswers(prev => ({ ...prev, [q.surveyQuestionId]: v }))}
                  disabled={status === 'success'}
                />
              ) : (
                <textarea
                  className="textarea textarea-bordered w-full"
                  value={answers[q.surveyQuestionId]}
                  onChange={e => setAnswers(prev => ({ ...prev, [q.surveyQuestionId]: e.target.value }))}
                  disabled={status === 'success'}
                />
              )}
              {status === 'success' && <p className="text-xs text-success mt-1">Saved</p>}
              {status === 'error' && <p className="text-xs text-error mt-1">Retry needed</p>}
            </div>
          )
        })}
      </div>
      {submitError && <p className="text-error text-sm mt-3">{submitError}</p>}
      <button
        className="btn btn-sm btn-primary mt-4"
        onClick={handleSubmit}
        disabled={submitting}
      >
        {submitting ? 'Submitting...' : 'Submit survey'}
      </button>
    </div>
  )
}
