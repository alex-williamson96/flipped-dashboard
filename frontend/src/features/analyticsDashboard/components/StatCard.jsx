export default function StatCard({ label, value, loading }) {
  return (
    <div className="stat">
      <div className="stat-title">{label}</div>
      <div className="stat-value text-2xl">
        {loading ? (
          <div className="skeleton h-8 w-16" />
        ) : value == null ? (
          '–'
        ) : (
          value
        )}
      </div>
    </div>
  )
}
