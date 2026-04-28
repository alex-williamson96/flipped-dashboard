import { useQuery } from '@tanstack/react-query'
import { useParams } from 'react-router-dom'
import apiClient from '../../../lib/apiClient'
import { useIdentity } from '../../roleSwitcher/IdentityContext'
import LessonListItem from '../components/LessonListItem'

export default function LessonListPage() {
  const { courseId } = useParams()
  const { studentId } = useIdentity()

  const { data: lessons, isLoading, isError } = useQuery({
    queryKey: ['studentLessons', studentId, courseId],
    queryFn: () => apiClient.get(`/students/${studentId}/courses/${courseId}/lessons`).then(r => r.data.data),
    enabled: !!studentId && !!courseId,
  })

  const { data: course } = useQuery({
    queryKey: ['course', courseId],
    queryFn: () => apiClient.get(`/courses/${courseId}`).then(r => r.data.data),
    enabled: !!courseId,
  })

  if (isLoading) {
    return (
      <div className="flex justify-center p-8">
        <span className="loading loading-spinner" />
      </div>
    )
  }

  if (isError) {
    return (
      <div className="alert alert-error m-4">Failed to load lessons.</div>
    )
  }

  return (
    <div className="p-4 max-w-2xl mx-auto">
      {course?.isArchived && (
        <div className="alert alert-warning mb-4">This course is archived. Content is read-only.</div>
      )}
      <h1 className="text-xl font-semibold mb-4">Lessons</h1>
      <div className="flex flex-col gap-2">
        {lessons?.map(lesson => (
          <LessonListItem
            key={lesson.lessonId}
            lesson={lesson}
            courseId={courseId}
            lessonIds={lessons.map(l => l.lessonId)}
          />
        ))}
      </div>
    </div>
  )
}
