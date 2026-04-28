export default function WeakestObjectivesPanel({ loAccuracy }) {
  if (!loAccuracy || loAccuracy.length === 0) return null

  return (
    <div className="card bg-base-100 shadow">
      <div className="card-body">
        <h3 className="card-title text-base">Weakest learning objectives</h3>
        <ul className="space-y-2">
          {loAccuracy.map(lo => {
            const pct = Math.round(lo.accuracy * 100)
            return (
              <li key={lo.objective} className="flex items-center gap-3">
                <span className="w-48 text-sm truncate">{lo.objective}</span>
                <progress
                  className="progress progress-primary flex-1"
                  value={pct}
                  max={100}
                />
                <span className={`w-10 text-sm text-right${lo.accuracy < 0.6 ? ' text-error' : ''}`}>{pct}%</span>
              </li>
            )
          })}
        </ul>
      </div>
    </div>
  )
}
