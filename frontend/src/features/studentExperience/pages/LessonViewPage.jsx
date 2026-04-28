import { useEffect, useRef } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useParams, useNavigate, useLocation } from 'react-router-dom'
import apiClient from '../../../lib/apiClient'
import { useLessonStart, useLessonComplete } from '../hooks/useLessonEvents'
import VideoItem from '../components/VideoItem'
import TextItem from '../components/TextItem'
import InlineQuiz from '../components/InlineQuiz'
import InlineSurvey from '../components/InlineSurvey'

function LessonItem({ item, readOnly }) {
  switch (item.type) {
    case 'VIDEO': return <VideoItem item={item} />
    case 'TEXT': return <TextItem item={item} />
    case 'QUIZ': return <InlineQuiz item={item} readOnly={readOnly} />
    case 'SURVEY': return <InlineSurvey item={item} readOnly={readOnly} />
    default: return null
  }
}

export default function LessonViewPage() {
  const { lessonId } = useParams()
  const navigate = useNavigate()
  const { state } = useLocation()
  const startFired = useRef(false)

  const courseId = state?.courseId
  const lessonIds = state?.lessonIds ?? []
  const currentIndex = lessonIds.indexOf(Number(lessonId))
  const nextLessonId = currentIndex >= 0 && currentIndex < lessonIds.length - 1
    ? lessonIds[currentIndex + 1]
    : null

  const { data: course } = useQuery({
    queryKey: ['course', courseId],
    queryFn: () => apiClient.get(`/courses/${courseId}`).then(r => r.data.data),
    enabled: !!courseId,
  })

  const isArchived = course?.isArchived ?? false

  const { data: lesson, isLoading, isError } = useQuery({
    queryKey: ['lessonContent', lessonId],
    queryFn: () => apiClient.get(`/lessons/${lessonId}/content`).then(r => r.data.data),
  })

  const { mutate: lessonStart } = useLessonStart()
  const { mutate: lessonComplete } = useLessonComplete()

  useEffect(() => {
    const courseReady = !courseId || course !== undefined
    if (lesson && courseReady && !startFired.current && !isArchived) {
      startFired.current = true
      lessonStart({ lessonId: Number(lessonId) })
    }
  }, [lesson, course, courseId, lessonId, lessonStart, isArchived])

  const handleComplete = () => {
    lessonComplete(
      { lessonId: Number(lessonId) },
      { onSuccess: () => navigate(`/student/lessons/${lessonId}/completion`) }
    )
  }

  const handleCompleteAndNext = () => {
    lessonComplete(
      { lessonId: Number(lessonId) },
      {
        onSuccess: () => navigate(`/student/lessons/${nextLessonId}`, { state: { courseId, lessonIds } }),
      }
    )
  }

  if (isLoading) {
    return (
      <div className="flex justify-center p-8">
        <span className="loading loading-spinner" />
      </div>
    )
  }

  if (isError) {
    return <div className="alert alert-error m-4">Failed to load lesson.</div>
  }

  const sortedItems = [...(lesson?.items ?? [])].sort((a, b) => a.position - b.position)

  return (
    <div className="p-4 max-w-2xl mx-auto">
      {isArchived && (
        <div className="alert alert-warning mb-4">This course is archived. Quizzes and surveys are disabled.</div>
      )}
      <h1 className="text-xl font-semibold mb-4">{lesson?.title}</h1>
      <div className="flex flex-col gap-4">
        {sortedItems.map(item => (
          <LessonItem key={item.lessonItemId} item={item} readOnly={isArchived} />
        ))}
      </div>
      <div className="flex gap-2 mt-6">
        <button className="btn btn-ghost" onClick={() => navigate(courseId ? `/student/courses/${courseId}/lessons` : -1)}>
          Back
        </button>
        {!isArchived && (
          <>
            <button className="btn btn-primary" onClick={handleComplete}>
              Complete lesson
            </button>
            {nextLessonId && (
              <button className="btn btn-secondary" onClick={handleCompleteAndNext}>
                Complete &amp; next
              </button>
            )}
          </>
        )}
      </div>
    </div>
  )
}
