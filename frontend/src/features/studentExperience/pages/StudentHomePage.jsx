import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'
import { useIdentity } from '../../roleSwitcher'

export default function StudentHomePage() {
  const { studentId } = useIdentity()
  const navigate = useNavigate()
  const [filter, setFilter] = useState('false')

  const { data: courses, isLoading, isError } = useQuery({
    queryKey: ['studentCourses', studentId, filter],
    queryFn: () => apiClient.get(`/students/${studentId}/courses?archived=${filter}`).then((r) => r.data.data),
    enabled: !!studentId,
  })

  if (isLoading) return <div className="p-8 flex justify-center"><span className="loading loading-spinner" /></div>
  if (isError) return <div className="p-8"><div className="alert alert-error">Could not load your courses.</div></div>

  const filtered = courses ?? []

  return (
    <div className="p-4 max-w-2xl mx-auto">
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-xl font-semibold">My courses</h1>
        <select
          className="select select-bordered select-sm"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        >
          <option value="false">Active</option>
          <option value="true">Archived</option>
          <option value="all">All</option>
        </select>
      </div>
      <div className="flex flex-col gap-2">
        {filtered.map(course => (
          <button
            key={course.courseId}
            type="button"
            className="card card-bordered cursor-pointer hover:bg-base-200 p-4 text-left w-full"
            onClick={() => navigate(`/student/courses/${course.courseId}/lessons`)}
          >
            <div className="flex items-center gap-2">
              <span className="font-medium">{course.title}</span>
              {course.isArchived && <span className="badge badge-warning badge-sm">Archived</span>}
            </div>
          </button>
        ))}
        {filtered.length === 0 && <p className="text-sm text-base-content/60">No courses found.</p>}
      </div>
    </div>
  )
}
