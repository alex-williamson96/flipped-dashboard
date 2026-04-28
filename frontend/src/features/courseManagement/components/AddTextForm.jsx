import { useState } from 'react'
import { useCreateLessonItem } from '../hooks/useLessonItemMutations'

export default function AddTextForm({ lessonId, onSuccess }) {
  const [title, setTitle] = useState('')
  const [body, setBody] = useState('')
  const createItem = useCreateLessonItem(lessonId)

  function handleSubmit(e) {
    e.preventDefault()
    createItem.mutate({ type: 'TEXT', body, title }, {
      onSuccess: () => {
        setTitle('')
        setBody('')
        onSuccess?.()
      },
    })
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3 p-4 bg-base-200 rounded">
      <h4 className="font-semibold text-sm">Add text block</h4>
      <input
        className="input input-bordered input-sm w-full"
        placeholder="Title"
        value={title}
        onChange={e => setTitle(e.target.value)}
        required
      />
      <textarea
        className="textarea textarea-bordered w-full"
        placeholder="Body content"
        value={body}
        onChange={e => setBody(e.target.value)}
        rows={5}
        required
      />
      {createItem.isError && (
        <div className="alert alert-error py-2 text-sm">{createItem.error.message}</div>
      )}
      <div className="flex gap-2">
        <button className="btn btn-sm btn-primary" type="submit" disabled={createItem.isPending}>
          {createItem.isPending ? <span className="loading loading-spinner loading-xs" /> : 'Add text'}
        </button>
        <button className="btn btn-sm btn-ghost" type="button" onClick={() => onSuccess?.()}>
          Cancel
        </button>
      </div>
    </form>
  )
}
