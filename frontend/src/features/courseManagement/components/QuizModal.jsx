import { useState } from 'react'
import { useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

const MODAL_ID = 'quiz-modal'

function emptyQuestion() {
  return { text: '', learningObjective: '', choices: [{ text: '', isCorrect: false }, { text: '', isCorrect: false }] }
}

function isValid(questions) {
  if (questions.length === 0) return false
  return questions.every(q =>
    q.text.trim() !== '' &&
    q.choices.length >= 2 &&
    q.choices.filter(c => c.isCorrect).length === 1 &&
    q.choices.every(c => c.text.trim() !== '')
  )
}

export default function QuizModal({ lessonId }) {
  const [questions, setQuestions] = useState([emptyQuestion()])
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState(null)
  const queryClient = useQueryClient()

  function setQuestion(qi, update) {
    setQuestions(qs => qs.map((q, i) => i === qi ? { ...q, ...update } : q))
  }

  function setChoice(qi, ci, update) {
    setQuestions(qs => qs.map((q, i) => {
      if (i !== qi) return q
      return { ...q, choices: q.choices.map((c, j) => j === ci ? { ...c, ...update } : c) }
    }))
  }

  function markCorrect(qi, ci) {
    setQuestions(qs => qs.map((q, i) => {
      if (i !== qi) return q
      return { ...q, choices: q.choices.map((c, j) => ({ ...c, isCorrect: j === ci })) }
    }))
  }

  function addChoice(qi) {
    setQuestions(qs => qs.map((q, i) => {
      if (i !== qi) return q
      return { ...q, choices: [...q.choices, { text: '', isCorrect: false }] }
    }))
  }

  function addQuestion() {
    setQuestions(qs => [...qs, emptyQuestion()])
  }

  async function handleSave() {
    setSaving(true)
    setSaveError(null)
    try {
      const item = await apiClient.post(`/lessons/${lessonId}/items`, { type: 'QUIZ' }).then(r => r.data.data)
      for (const q of questions) {
        const question = await apiClient.post(`/lesson-items/${item.id}/quiz-questions`, {
          text: q.text,
          ...(q.learningObjective ? { learningObjective: q.learningObjective } : {}),
        }).then(r => r.data.data)
        for (const c of q.choices) {
          await apiClient.post(`/quiz-questions/${question.id}/choices`, {
            text: c.text,
            isCorrect: c.isCorrect,
          })
        }
      }
      queryClient.invalidateQueries({ queryKey: ['lesson', lessonId] })
      setQuestions([emptyQuestion()])
      document.getElementById(MODAL_ID).close()
    } catch (err) {
      setSaveError(err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <dialog id={MODAL_ID} className="modal">
      <div className="modal-box max-w-2xl">
        <h3 className="font-bold text-lg mb-4">Add quiz</h3>
        <div className="flex flex-col gap-4 max-h-[60vh] overflow-y-auto pr-1">
          {questions.map((q, qi) => (
            <div key={qi} className="border border-base-300 rounded p-3 flex flex-col gap-2">
              <input
                className="input input-bordered input-sm w-full"
                placeholder={`Question ${qi + 1}`}
                value={q.text}
                onChange={e => setQuestion(qi, { text: e.target.value })}
              />
              <input
                className="input input-bordered input-xs w-full"
                placeholder="Learning objective (optional)"
                value={q.learningObjective}
                onChange={e => setQuestion(qi, { learningObjective: e.target.value })}
              />
              {q.choices.map((c, ci) => (
                <div key={ci} className="flex items-center gap-2">
                  <input
                    type="radio"
                    className="radio radio-sm radio-primary"
                    name={`correct-${qi}`}
                    checked={c.isCorrect}
                    onChange={() => markCorrect(qi, ci)}
                  />
                  <input
                    className="input input-bordered input-xs flex-1"
                    placeholder={`Choice ${ci + 1}`}
                    value={c.text}
                    onChange={e => setChoice(qi, ci, { text: e.target.value })}
                  />
                </div>
              ))}
              {q.choices.length < 4 && (
                <button
                  className="btn btn-xs btn-ghost self-start"
                  type="button"
                  onClick={() => addChoice(qi)}
                >
                  + Add choice
                </button>
              )}
            </div>
          ))}
        </div>
        <button className="btn btn-sm btn-outline mt-3" type="button" onClick={addQuestion}>
          + Add question
        </button>
        {saveError && <div className="alert alert-error py-2 text-sm mt-2">{saveError}</div>}
        <div className="modal-action">
          <button
            className="btn btn-ghost"
            type="button"
            onClick={() => document.getElementById(MODAL_ID).close()}
          >
            Cancel
          </button>
          <button
            className="btn btn-primary"
            type="button"
            onClick={handleSave}
            disabled={!isValid(questions) || saving}
          >
            {saving ? <span className="loading loading-spinner loading-sm" /> : 'Save'}
          </button>
        </div>
      </div>
    </dialog>
  )
}

QuizModal.open = () => document.getElementById(MODAL_ID).showModal()
