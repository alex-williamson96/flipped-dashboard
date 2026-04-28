function computeAlerts(summary) {
  if (!summary) return []
  const alerts = []

  const completionPct = Math.round((summary.completionRate ?? 0) * 100)
  if (completionPct < 60) {
    alerts.push({
      severity: 3,
      cls: 'alert-error',
      message: `Only ${completionPct}% of students completed the lesson - consider following up before class.`,
    })
  }

  const quizScore = summary.avgQuizScore != null ? Math.round(summary.avgQuizScore) : null
  if (quizScore != null && quizScore < 60) {
    alerts.push({
      severity: 2,
      cls: 'alert-warning',
      message: `Quiz average is ${quizScore}% - this content may need to be revisited in class.`,
    })
  }

  const conf = summary.avgConfidence
  if (conf != null && conf < 3.0) {
    alerts.push({
      severity: 1,
      cls: 'alert-info',
      message: `Student confidence is low (avg ${conf.toFixed(1)}/5) - plan extra time for questions.`,
    })
  }

  return alerts.sort((a, b) => b.severity - a.severity).slice(0, 3)
}

export default function AlertBanner({ summary }) {
  const alerts = computeAlerts(summary)
  if (alerts.length === 0) return null

  return (
    <div className="space-y-2">
      {alerts.map((alert, i) => (
        <div key={i} className={`alert ${alert.cls} py-2`}>
          <span className="text-sm">{alert.message}</span>
        </div>
      ))}
    </div>
  )
}
