import StarDisplay from '../../../components/StarDisplay'

export default function SurveyResponseList({ responses }) {
  if (!responses?.length) {
    return <p className="text-sm text-base-content/60">No survey responses.</p>
  }

  return responses.map((response, i) => (
    <div key={i} className="py-1">
      <p className="text-sm font-medium">{response.questionText}</p>
      {response.type === 'LIKERT' ? (
        <StarDisplay value={response.likertValue} />
      ) : (
        <p className="text-sm">{response.freeText ?? '–'}</p>
      )}
    </div>
  ))
}
