import { useNavigate } from 'react-router-dom'

export default function LessonListItem({ lesson, courseId, lessonIds }) {
  const navigate = useNavigate()
  return (
    <div
      className="card card-bordered cursor-pointer hover:bg-base-200 px-4 py-3"
      onClick={() => navigate(`/student/lessons/${lesson.lessonId}`, { state: { courseId, lessonIds } })}
    >
      <div className="flex items-center justify-between">
        <span>{lesson.title}</span>
        {lesson.completed
          ? <span className="badge badge-success">Completed</span>
          : <span className="badge badge-neutral">Not started</span>
        }
      </div>
    </div>
  )
}
