export default function EngagementFunnel({ summary, loading }) {
  if (loading) return <div className="skeleton h-24 w-full" />
  if (!summary) return null

  const steps = [
    { label: 'Enrolled',  count: summary.enrolledCount },
    { label: 'Started',   count: summary.startedCount },
    { label: 'Completed', count: summary.completedCount },
    { label: 'Surveyed',  count: summary.surveyedCount },
  ]

  return (
    <div className="card bg-base-100 shadow p-4 pb-6">
      <h2 className="text-sm font-semibold mb-3">Student engagement funnel</h2>
      <ul className="steps steps-horizontal w-full pt-2 overflow-visible">
        {steps.map(step => (
          <li
            key={step.label}
            className="step step-primary [&::after]:!w-12 [&::after]:!h-12 [&::after]:!text-base [&::after]:!font-bold"
            data-content={step.count ?? '–'}
          >
            <span className="text-xs text-base-content/70 mt-1">{step.label}</span>
          </li>
        ))}
      </ul>
    </div>
  )
}
