import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useUnarchiveCourse } from '../hooks/useCourseMutations'
import ArchiveModal from './ArchiveModal'

export default function CourseCard({ course }) {
  const navigate = useNavigate()
  const [showArchiveModal, setShowArchiveModal] = useState(false)
  const unarchiveCourse = useUnarchiveCourse()

  function handleArchiveClick(e) {
    e.stopPropagation()
    setShowArchiveModal(true)
  }

  function handleUnarchiveClick(e) {
    e.stopPropagation()
    unarchiveCourse.mutate({ courseId: course.id })
  }

  function handleCardKeyDown(e) {
    if (e.target !== e.currentTarget) return
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault()
      navigate(`/teacher/courses/${course.id}`)
    }
  }

  return (
    <>
      <div
        role="button"
        tabIndex={0}
        aria-label={`Open ${course.title}`}
        className="card bg-base-100 border border-base-200 cursor-pointer hover:border-primary transition-colors"
        onClick={() => navigate(`/teacher/courses/${course.id}`)}
        onKeyDown={handleCardKeyDown}
      >
        <div className="card-body py-4">
          <div className="flex items-start justify-between gap-2">
            <div className="flex flex-col gap-1 min-w-0">
              <h2 className="card-title text-base">{course.title}</h2>
              <p className="text-sm text-base-content/60">
                {course.sections?.length ?? 0} section{(course.sections?.length ?? 0) !== 1 ? 's' : ''}
              </p>
              {course.term && (
                <p className="text-xs text-base-content/40">{course.term}</p>
              )}
            </div>
            <div className="flex items-center gap-2 shrink-0">
              {course.isArchived && (
                <span className="badge badge-outline badge-sm">Archived</span>
              )}
              {course.isArchived ? (
                <button
                  className="btn btn-xs btn-outline"
                  type="button"
                  onClick={handleUnarchiveClick}
                  disabled={unarchiveCourse.isPending}
                >
                  Unarchive
                </button>
              ) : (
                <button
                  className="btn btn-xs btn-outline"
                  type="button"
                  onClick={handleArchiveClick}
                >
                  Archive
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
      {showArchiveModal && (
        <ArchiveModal course={course} onClose={() => setShowArchiveModal(false)} />
      )}
    </>
  )
}
