import { useState } from 'react'
import { useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

const MODAL_ID = 'survey-modal'

function isValid(likertQuestions, freeText) {
  return likertQuestions.length >= 1 && freeText.trim() !== ''
}

export default function SurveyModal({ lessonId }) {
  const [likertQuestions, setLikertQuestions] = useState([''])
  const [freeText, setFreeText] = useState('')
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState(null)
  const queryClient = useQueryClient()

  function updateLikert(index, value) {
    setLikertQuestions(qs => qs.map((q, i) => i === index ? value : q))
  }

  function addLikert() {
    setLikertQuestions(qs => [...qs, ''])
  }

  async function handleSave() {
    setSaving(true)
    setSaveError(null)
    try {
      const item = await apiClient.post(`/lessons/${lessonId}/items`, { type: 'SURVEY' }).then(r => r.data.data)
      for (const text of likertQuestions) {
        await apiClient.post(`/lesson-items/${item.id}/survey-questions`, {
          text: text,
          questionType: 'LIKERT',
        })
      }
      await apiClient.post(`/lesson-items/${item.id}/survey-questions`, {
        text: freeText,
        questionType: 'FREE_TEXT',
      })
      queryClient.invalidateQueries({ queryKey: ['lesson', lessonId] })
      setLikertQuestions([''])
      setFreeText('')
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
        <h3 className="font-bold text-lg mb-4">Add survey</h3>
        <div className="flex flex-col gap-3 max-h-[60vh] overflow-y-auto pr-1">
          <p className="text-sm font-semibold">Likert questions</p>
          {likertQuestions.map((q, i) => (
            <input
              key={i}
              className="input input-bordered input-sm w-full"
              placeholder={`Likert question ${i + 1}`}
              value={q}
              onChange={e => updateLikert(i, e.target.value)}
            />
          ))}
          <button className="btn btn-xs btn-ghost self-start" type="button" onClick={addLikert}>
            + Add Likert question
          </button>
          <p className="text-sm font-semibold mt-2">Free-text question</p>
          <input
            className="input input-bordered input-sm w-full"
            placeholder="Free-text question"
            value={freeText}
            onChange={e => setFreeText(e.target.value)}
          />
        </div>
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
            disabled={!isValid(likertQuestions, freeText) || saving}
          >
            {saving ? <span className="loading loading-spinner loading-sm" /> : 'Save'}
          </button>
        </div>
      </div>
    </dialog>
  )
}

SurveyModal.open = () => document.getElementById(MODAL_ID).showModal()
