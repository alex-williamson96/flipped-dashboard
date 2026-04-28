export default function CompletionFilter({ value, onChange }) {
  return (
    <select
      className="select select-sm pl-0"
      value={value ?? 'all'}
      onChange={e => onChange(e.target.value)}
    >
      <option value="all">All</option>
      <option value="completed">Completed</option>
      <option value="not_completed">Not completed</option>
    </select>
  )
}
