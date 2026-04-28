import useCourses from '../../../hooks/useCourses'

export default function CourseFilter({ value, onChange }) {
  const { data: courses, isLoading } = useCourses()

  return (
    <select
      className="select select-sm pl-0"
      value={value ?? ''}
      onChange={e => onChange(Number(e.target.value))}
      disabled={isLoading}
    >
      {isLoading ? (
        <option>Loading...</option>
      ) : (
        courses?.map(c => (
          <option key={c.id} value={c.id}>{c.title}</option>
        ))
      )}
    </select>
  )
}
