import QuizResponseList from './QuizResponseList'
import SurveyResponseList from './SurveyResponseList'

function CompletionBadge({ completed, watchPercent }) {
  if (completed) return <span className="badge badge-success">Completed</span>
  if (watchPercent > 0) return <span className="badge badge-warning">Started</span>
  return <span className="badge">Not started</span>
}

export default function LessonDetailPanel({ lessonDetail, loading }) {
  if (loading) return <div className="skeleton h-48 w-full" />
  if (!lessonDetail) return null

  return (
    <div>
      <CompletionBadge completed={lessonDetail.completed} watchPercent={lessonDetail.watchPercent} />
      {lessonDetail.watchPercent != null && (
        <p className="text-sm">{lessonDetail.watchPercent}% watched</p>
      )}
      <h4 className="font-semibold mt-3 mb-1">Quiz</h4>
      <QuizResponseList responses={lessonDetail.quizResponses} />
      <h4 className="font-semibold mt-3 mb-1">Survey</h4>
      <SurveyResponseList responses={lessonDetail.surveyResponses} />
    </div>
  )
}
