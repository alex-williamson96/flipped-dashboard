import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, LabelList, ResponsiveContainer,
} from 'recharts'

function truncate(str, max = 15) {
  return str && str.length > max ? str.slice(0, max - 1) + '\u2026' : str
}

export default function LessonTimeline({ lessons, loading }) {
  if (loading || !lessons) {
    return <div className="skeleton h-64 w-full" />
  }

  if (lessons.length === 0) {
    return <p className="text-sm text-base-content/60">No lesson data available.</p>
  }

  const chartData = lessons.map(lesson => ({
    name: lesson.title,
    completionRate: lesson.completionRate != null ? Math.round(lesson.completionRate * 100) : null,
    avgQuizScore: lesson.avgQuizScore != null ? Math.round(lesson.avgQuizScore) : null,
    patternFlag: lesson.patternFlag,
  }))

  return (
    <div>
      <h2 className="text-base font-semibold mb-2">Lesson timeline</h2>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData} margin={{ top: 30, right: 20, left: 0, bottom: 20 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="name"
            tick={({ x, y, payload }) => (
              <g transform={`translate(${x},${y})`}>
                <text x={0} y={0} dy={14} textAnchor="middle" fill="currentColor" fontSize={12}>
                  {truncate(payload.value)}
                </text>
              </g>
            )}
          />
          <YAxis domain={[0, 100]} tickFormatter={v => `${v}%`} />
          <Tooltip formatter={(value, name) => value != null ? [`${value}%`, name] : ['–', name]} />
          <Legend />
          <Bar dataKey="completionRate" name="Completion rate" fill="#4f46e5">
            <LabelList
              dataKey="patternFlag"
              position="top"
              content={({ x, y, width, value }) => {
                if (!value) return null
                const text = value === 'LOW_COMPLETION_HIGH_SCORE' ? 'Review material?' : 'Address before test'
                return (
                  <foreignObject x={x - 20} y={y - 28} width={120} height={24}>
                    <div className="badge badge-warning badge-sm text-xs whitespace-nowrap">{text}</div>
                  </foreignObject>
                )
              }}
            />
          </Bar>
          <Bar dataKey="avgQuizScore" name="Avg quiz score" fill="#06b6d4" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
