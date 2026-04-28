import useSections from '../../../hooks/useSections'

export default function SectionFilter({ courseId, value, onChange }) {
  const { data: sections, isLoading } = useSections(courseId)

  return (
    <select
      className="select select-sm pl-0"
      value={value ?? ''}
      onChange={e => onChange(Number(e.target.value) || null)}
      disabled={!courseId || isLoading}
    >
      <option value="">All sections</option>
      {sections?.map(s => (
        <option key={s.id} value={s.id}>{s.title}</option>
      ))}
    </select>
  )
}
