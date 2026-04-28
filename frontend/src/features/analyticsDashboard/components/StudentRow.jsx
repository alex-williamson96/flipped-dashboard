import StarDisplay from '../../../components/StarDisplay'

export default function StudentRow({ student, onClick }) {
  return (
    <tr onClick={onClick} className="hover cursor-pointer">
      <td>{student.name}</td>
      <td>{student.sectionName}</td>
      <td>
        {student.completed
          ? <span className="text-success" aria-label="Completed" title="Completed">✓</span>
          : <span className="text-error" aria-label="Not completed" title="Not completed">✗</span>}
      </td>
      <td>{student.quizScore != null ? `${student.quizScore}%` : '–'}</td>
      <td>{student.totalGrade != null ? `${Math.round(student.totalGrade)}%` : '–'}</td>
      <td>{student.confidence != null ? <StarDisplay value={student.confidence} /> : '–'}</td>
    </tr>
  )
}
