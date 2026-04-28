import { useNavigate } from 'react-router-dom'
import { useDeleteLesson, useReorderLessons } from '../hooks/useLessonMutations'

export default function LessonList({ courseId, lessons = [] }) {
  const navigate = useNavigate()
  const deleteLesson = useDeleteLesson(courseId)
  const reorderLessons = useReorderLessons(courseId)

  function moveUp(index) {
    const ids = lessons.map(l => l.id)
    const [item] = ids.splice(index, 1)
    ids.splice(index - 1, 0, item)
    reorderLessons.mutate(ids)
  }

  function moveDown(index) {
    const ids = lessons.map(l => l.id)
    const [item] = ids.splice(index, 1)
    ids.splice(index + 1, 0, item)
    reorderLessons.mutate(ids)
  }

  return (
    <div className="flex flex-col gap-2">
      <h3 className="font-semibold text-sm uppercase text-base-content/60 tracking-wide">Lessons</h3>
      {lessons.map((lesson, index) => (
        <div
          key={lesson.id}
          className="flex items-center gap-2 bg-base-200 rounded px-3 py-2"
        >
          <span
            className="flex-1 text-sm cursor-pointer hover:text-primary"
            onClick={() => navigate(`/teacher/courses/${courseId}/lessons/${lesson.id}`)}
          >
            {lesson.title}
          </span>
          <span className={`badge badge-xs ${lesson.isActive ? 'badge-success' : 'badge-ghost'}`}>
            {lesson.isActive ? 'Active' : 'Inactive'}
          </span>
          <button
            className="btn btn-xs btn-ghost"
            disabled={index === 0 || reorderLessons.isPending}
            onClick={() => moveUp(index)}
          >
            ↑
          </button>
          <button
            className="btn btn-xs btn-ghost"
            disabled={index === lessons.length - 1 || reorderLessons.isPending}
            onClick={() => moveDown(index)}
          >
            ↓
          </button>
          <button
            className="btn btn-xs btn-ghost text-error"
            onClick={() => deleteLesson.mutate(lesson.id)}
            disabled={deleteLesson.isPending}
          >
            Delete
          </button>
        </div>
      ))}
      {reorderLessons.isError && (
        <div className="alert alert-error py-2 text-sm">{reorderLessons.error.message}</div>
      )}
      {deleteLesson.isError && (
        <div className="alert alert-error py-2 text-sm">{deleteLesson.error.message}</div>
      )}
    </div>
  )
}
