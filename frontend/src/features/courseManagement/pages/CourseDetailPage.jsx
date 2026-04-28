import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import useCourse from '../../../hooks/useCourse'
import useLessons from '../../../hooks/useLessons'
import { useCreateLesson } from '../hooks/useLessonMutations'
import SectionList from '../components/SectionList'
import LessonList from '../components/LessonList'
import CloneModal from '../components/CloneModal'

export default function CourseDetailPage() {
  const { courseId } = useParams()
  const navigate = useNavigate()
  const { data: course, isLoading: courseLoading, isError: courseError, error: courseErr } = useCourse(courseId)
  const { data: lessons, isLoading: lessonsLoading, isError: lessonsError, error: lessonsErr } = useLessons(courseId)
  const createLesson = useCreateLesson(courseId)
  const [newLessonTitle, setNewLessonTitle] = useState('')
  const [showCloneModal, setShowCloneModal] = useState(false)

  function handleAddLesson(e) {
    e.preventDefault()
    if (!newLessonTitle.trim()) return
    createLesson.mutate({ title: newLessonTitle }, {
      onSuccess: () => setNewLessonTitle(''),
    })
  }

  return (
    <div className="p-6 max-w-3xl mx-auto">
      {courseLoading && <span className="loading loading-spinner" />}
      {courseError && <div className="alert alert-error">{courseErr.message}</div>}
      {course && (
        <>
          <div className="flex items-start justify-between gap-2 mb-1">
            <h1 className="text-2xl font-bold">{course.title}</h1>
            <button
              className="btn btn-sm btn-outline shrink-0"
              type="button"
              onClick={() => setShowCloneModal(true)}
            >
              Clone course
            </button>
          </div>
          {course.description && (
            <p className="text-base-content/70 mb-6">{course.description}</p>
          )}
          <div className="flex flex-col gap-8">
            <SectionList courseId={courseId} sections={course.sections ?? []} />
            <div className="flex flex-col gap-3">
              {lessonsLoading && <span className="loading loading-spinner" />}
              {lessonsError && <div className="alert alert-error">{lessonsErr.message}</div>}
              {lessons && (
                <LessonList courseId={courseId} lessons={lessons} />
              )}
              <form onSubmit={handleAddLesson} className="flex gap-2 mt-1">
                <input
                  className="input input-bordered input-sm flex-1"
                  placeholder="New lesson title"
                  value={newLessonTitle}
                  onChange={e => setNewLessonTitle(e.target.value)}
                />
                <button className="btn btn-sm btn-outline" type="submit" disabled={createLesson.isPending}>
                  Add lesson
                </button>
              </form>
              {createLesson.isError && (
                <div className="alert alert-error py-2 text-sm">{createLesson.error.message}</div>
              )}
            </div>
          </div>
          {showCloneModal && (
            <CloneModal
              courseId={Number(courseId)}
              onClose={() => setShowCloneModal(false)}
              onSuccess={(newCourse) => navigate(`/teacher/courses/${newCourse.id}`)}
            />
          )}
        </>
      )}
    </div>
  )
}
