import StatCard from './StatCard'
import StarDisplay from '../../../components/StarDisplay'

export default function StatsBar({ summary, loading }) {
  const completionRate = summary != null
    ? `${Math.round((summary.completionRate ?? 0) * 100)}%`
    : null

  const avgQuizScore = summary?.avgQuizScore != null
    ? `${Math.round(summary.avgQuizScore)}%`
    : null

  return (
    <div className="stats shadow w-full flex-wrap">
      <StatCard label="Total students" value={summary?.totalStudents ?? null} loading={loading} />
      <StatCard label="Completion rate" value={completionRate} loading={loading} />
      <StatCard label="Avg quiz score" value={avgQuizScore} loading={loading} />
      <StatCard label="Avg confidence" value={summary?.avgConfidence != null ? <StarDisplay value={summary.avgConfidence} /> : null} loading={loading} />
    </div>
  )
}
