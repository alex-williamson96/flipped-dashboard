export default function QuizResponseList({ responses }) {
  if (!responses?.length) {
    return <p className="text-sm text-base-content/60">No quiz responses.</p>
  }

  return responses.map((response, i) => (
    <div key={i} className="py-1">
      <p className="text-sm font-medium">{response.questionText}</p>
      <p className="text-sm">
        {response.chosenAnswer} —{' '}
        <span className={response.correct ? 'text-success' : 'text-error'}>
          {response.correct ? 'Correct' : 'Incorrect'}
        </span>
      </p>
    </div>
  ))
}
