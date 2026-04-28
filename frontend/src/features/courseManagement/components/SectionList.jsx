import { useState } from 'react'
import { useCreateSection, useUpdateSection, useDeleteSection } from '../hooks/useSectionMutations'

export default function SectionList({ courseId, sections = [] }) {
  const [newTitle, setNewTitle] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [editTitle, setEditTitle] = useState('')

  const createSection = useCreateSection(courseId)
  const updateSection = useUpdateSection(courseId)
  const deleteSection = useDeleteSection(courseId)

  function handleAdd(e) {
    e.preventDefault()
    if (!newTitle.trim()) return
    createSection.mutate({ title: newTitle }, {
      onSuccess: () => setNewTitle(''),
    })
  }

  function startEdit(section) {
    setEditingId(section.id)
    setEditTitle(section.title)
  }

  function handleUpdate(e) {
    e.preventDefault()
    updateSection.mutate({ id: editingId, title: editTitle }, {
      onSuccess: () => setEditingId(null),
    })
  }

  return (
    <div className="flex flex-col gap-2">
      <h3 className="font-semibold text-sm uppercase text-base-content/60 tracking-wide">Sections</h3>
      {sections.map(section => (
        <div key={section.id} className="flex items-center gap-2 bg-base-200 rounded px-3 py-2">
          {editingId === section.id ? (
            <form onSubmit={handleUpdate} className="flex gap-2 flex-1">
              <input
                className="input input-bordered input-sm flex-1"
                value={editTitle}
                onChange={e => setEditTitle(e.target.value)}
                autoFocus
              />
              <button className="btn btn-sm btn-primary" type="submit" disabled={updateSection.isPending}>
                Save
              </button>
              <button className="btn btn-sm btn-ghost" type="button" onClick={() => setEditingId(null)}>
                Cancel
              </button>
            </form>
          ) : (
            <>
              <span className="flex-1 text-sm">{section.title}</span>
              <button className="btn btn-xs btn-ghost" onClick={() => startEdit(section)}>Edit</button>
              <button
                className="btn btn-xs btn-ghost text-error"
                onClick={() => deleteSection.mutate(section.id)}
                disabled={deleteSection.isPending}
              >
                Delete
              </button>
            </>
          )}
        </div>
      ))}
      {updateSection.isError && (
        <div className="alert alert-error py-2 text-sm">{updateSection.error.message}</div>
      )}
      <form onSubmit={handleAdd} className="flex gap-2 mt-1">
        <input
          className="input input-bordered input-sm flex-1"
          placeholder="New section title"
          value={newTitle}
          onChange={e => setNewTitle(e.target.value)}
        />
        <button className="btn btn-sm btn-outline" type="submit" disabled={createSection.isPending}>
          Add
        </button>
      </form>
      {createSection.isError && (
        <div className="alert alert-error py-2 text-sm">{createSection.error.message}</div>
      )}
    </div>
  )
}
