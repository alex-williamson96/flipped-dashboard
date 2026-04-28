import { Link } from 'react-router-dom'

export default function LessonCompletionPage() {
  return (
    <div className="p-8 max-w-md mx-auto text-center">
      <h1 className="text-2xl font-semibold mb-4">Lesson complete!</h1>
      <Link to="/student" className="btn btn-primary">Back to lessons</Link>
    </div>
  )
}
