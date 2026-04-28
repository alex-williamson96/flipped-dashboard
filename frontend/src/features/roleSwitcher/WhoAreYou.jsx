import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import apiClient from '../../lib/apiClient'
import { useIdentity } from './IdentityContext'

export default function WhoAreYou() {
  const { setTeacher, setStudent } = useIdentity()
  const [showStudents, setShowStudents] = useState(false)

  const { data: students, isLoading, isError } = useQuery({
    queryKey: ['students'],
    queryFn: () => apiClient.get('/students').then((r) => r.data.data),
    enabled: showStudents,
    retry: 1,
  })

  function handleTeacher() {
    setTeacher()
  }

  function handleStudentSelect(e) {
    const selected = students.find((s) => s.id === Number(e.target.value))
    if (selected) setStudent(selected.id, selected.name)
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-base-200">
      <div className="card bg-base-100 shadow-xl w-full max-w-sm p-8 flex flex-col gap-6">
        <h1 className="text-2xl font-bold text-center">Who are you?</h1>
        {!showStudents ? (
          <div className="flex flex-col gap-3">
            <button className="btn btn-primary" onClick={handleTeacher}>
              I am the teacher
            </button>
            <button className="btn btn-secondary" onClick={() => setShowStudents(true)}>
              I am a student
            </button>
          </div>
        ) : (
          <div className="flex flex-col gap-3">
            {isLoading && <span className="text-sm text-base-content/60">Loading students...</span>}
            {isError && <span className="text-sm text-error">Could not load students. Please refresh.</span>}
            {students && (
              <select className="select select-bordered w-full" defaultValue="" onChange={handleStudentSelect}>
                <option value="" disabled>Select a student</option>
                {students.map((s) => (
                  <option key={s.id} value={s.id}>{s.name}</option>
                ))}
              </select>
            )}
            <button className="btn btn-ghost btn-sm" onClick={() => setShowStudents(false)}>
              Back
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
