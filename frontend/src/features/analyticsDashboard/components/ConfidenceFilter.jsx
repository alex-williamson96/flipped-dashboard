export default function ConfidenceFilter({ value, onChange }) {
  return (
    <select
      className="select select-sm pl-0"
      value={value ?? ''}
      onChange={e => onChange(e.target.value || null)}
    >
      <option value="">Any</option>
      {[1, 2, 3, 4, 5].map(n => (
        <option key={n} value={n}>{n}</option>
      ))}
    </select>
  )
}
