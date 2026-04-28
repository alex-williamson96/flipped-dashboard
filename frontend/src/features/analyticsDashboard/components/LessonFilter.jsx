import useLessons from '../../../hooks/useLessons'

export default function LessonFilter({ courseId, value, onChange }) {
  const { data: lessons, isLoading } = useLessons(courseId)

  return (
    <select
      className="select select-sm pl-0"
      value={value ?? ''}
      onChange={e => onChange(Number(e.target.value) || null)}
      disabled={!courseId || isLoading}
    >
      <option value="">All lessons</option>
      {lessons?.map(l => (
        <option key={l.id} value={l.id}>{l.title}</option>
      ))}
    </select>
  )
}
