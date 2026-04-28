import { useState } from 'react'
import useCourses from '../../../hooks/useCourses'
import CourseCard from '../components/CourseCard'
import NewCourseForm from '../components/NewCourseForm'

const EMPTY_MESSAGES = {
  false: 'No active courses.',
  true: 'No archived courses.',
  all: 'No courses.',
}

export default function CourseListPage() {
  const [archived, setArchived] = useState('false')
  const { data: courses, isLoading, isError, error } = useCourses(archived)
  const [showForm, setShowForm] = useState(false)

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-2xl font-bold">Courses</h1>
        <div className="flex items-center gap-2">
          <select
            className="select select-bordered select-sm"
            value={archived}
            onChange={e => setArchived(e.target.value)}
          >
            <option value="false">Active</option>
            <option value="true">Archived</option>
            <option value="all">All</option>
          </select>
          <button className="btn btn-primary btn-sm" onClick={() => setShowForm(v => !v)}>
            {showForm ? 'Cancel' : 'New course'}
          </button>
        </div>
      </div>
      {showForm && (
        <div className="mb-6">
          <NewCourseForm onSuccess={() => setShowForm(false)} />
        </div>
      )}
      {isLoading && <span className="loading loading-spinner" />}
      {isError && <div className="alert alert-error">{error.message}</div>}
      {courses && (
        <div className="flex flex-col gap-3">
          {courses.map(course => (
            <CourseCard key={course.id} course={course} />
          ))}
          {courses.length === 0 && (
            <p className="text-base-content/60 text-sm">{EMPTY_MESSAGES[archived]}</p>
          )}
        </div>
      )}
    </div>
  )
}
