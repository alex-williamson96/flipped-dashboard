import { useQuery } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import apiClient from '../../lib/apiClient'
import { useIdentity } from './IdentityContext'

export default function RoleSwitcher() {
  const { role, studentId, setTeacher, setStudent } = useIdentity()
  const navigate = useNavigate()
  const [pickingStudent, setPickingStudent] = useState(false)

  const { data: students, isLoading, isError } = useQuery({
    queryKey: ['students'],
    queryFn: () => apiClient.get('/students').then((r) => r.data.data),
    retry: 1,
  })

  useEffect(() => {
    if (role === 'teacher' && !window.location.pathname.startsWith('/teacher')) {
      navigate('/teacher/dashboard')
    } else if (role === 'student' && !window.location.pathname.startsWith('/student')) {
      navigate('/student')
    }
  }, [role, studentId])

  function handleStudentChange(e) {
    const selected = students?.find((s) => s.id === Number(e.target.value))
    if (selected) {
      setStudent(selected.id, selected.name)
      setPickingStudent(false)
    }
  }

  if (role === 'teacher' && !pickingStudent) {
    return (
      <div className="flex items-center gap-2">
        <span className="text-sm font-medium">Teacher</span>
        <button className="btn btn-xs btn-ghost" onClick={() => setPickingStudent(true)}>
          Switch to student
        </button>
      </div>
    )
  }

  if (role === 'teacher' && pickingStudent) {
    return (
      <div className="flex items-center gap-2">
        {isLoading && (
          <select className="select select-xs select-bordered" disabled>
            <option>Loading...</option>
          </select>
        )}
        {isError && <span className="text-xs text-error">Error loading students</span>}
        {students && (
          <select className="select select-xs select-bordered" defaultValue="" onChange={handleStudentChange}>
            <option value="" disabled>Select student</option>
            {students.map((s) => (
              <option key={s.id} value={s.id}>{s.name}</option>
            ))}
          </select>
        )}
        <button className="btn btn-xs btn-ghost" onClick={() => setPickingStudent(false)}>
          Cancel
        </button>
      </div>
    )
  }

  return (
    <div className="flex items-center gap-2">
      {isLoading && (
        <select className="select select-xs select-bordered" disabled>
          <option>Loading...</option>
        </select>
      )}
      {isError && <span className="text-xs text-error">Error loading students</span>}
      {students && (
        <select
          className="select select-xs select-bordered"
          value={studentId ?? ''}
          onChange={handleStudentChange}
        >
          {students.map((s) => (
            <option key={s.id} value={s.id}>{s.name}</option>
          ))}
        </select>
      )}
      <button className="btn btn-xs btn-ghost" onClick={setTeacher}>
        Switch to teacher
      </button>
    </div>
  )
}
