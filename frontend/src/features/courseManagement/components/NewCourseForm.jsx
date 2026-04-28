import { useState } from 'react'
import { useCreateCourse } from '../hooks/useCourseMutations'

export default function NewCourseForm({ onSuccess }) {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const { mutate, isPending, isError, error } = useCreateCourse()

  function handleSubmit(e) {
    e.preventDefault()
    mutate({ title, description }, {
      onSuccess: () => {
        setTitle('')
        setDescription('')
        onSuccess?.()
      },
    })
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-2">
      <input
        className="input input-bordered w-full"
        placeholder="Course title"
        value={title}
        onChange={e => setTitle(e.target.value)}
        required
      />
      <textarea
        className="textarea textarea-bordered w-full"
        placeholder="Description"
        value={description}
        onChange={e => setDescription(e.target.value)}
        rows={3}
      />
      {isError && <div className="alert alert-error py-2 text-sm">{error.message}</div>}
      <button className="btn btn-primary" type="submit" disabled={isPending}>
        {isPending ? <span className="loading loading-spinner loading-sm" /> : 'Create course'}
      </button>
    </form>
  )
}
