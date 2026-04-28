export default function LessonHistoryTable({ allLessons, selectedLessonId }) {
  if (!allLessons) return null

  return (
    <div className="overflow-x-auto">
      <table className="table table-sm w-full">
        <thead>
          <tr>
            <th>Lesson</th>
            <th>Completed</th>
            <th>Quiz score</th>
          </tr>
        </thead>
        <tbody>
          {allLessons.map(lesson => (
            <tr key={lesson.lessonId} className={lesson.lessonId === selectedLessonId ? 'bg-base-200' : ''}>
              <td>{lesson.title}</td>
              <td>{lesson.completed ? '✓' : '–'}</td>
              <td>{lesson.quizScore != null ? `${Math.round(lesson.quizScore)}%` : '–'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
