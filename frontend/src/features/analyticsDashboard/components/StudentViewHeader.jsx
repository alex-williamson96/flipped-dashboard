export default function StudentViewHeader({ student, filteredStudents, onBack, onStudentChange }) {
  if (!student) return <div className="skeleton h-10 w-full" />

  return (
    <div className="flex items-center gap-3 flex-wrap">
      <div>
        <span className="font-bold">{student.name}</span>
        <span className="text-sm text-base-content/60 ml-2">{student.sectionName}</span>
      </div>
      <select
        className="select select-sm"
        value={student.studentId}
        onChange={e => onStudentChange(Number(e.target.value))}
      >
        {filteredStudents.map(s => (
          <option key={s.studentId} value={s.studentId}>{s.name}</option>
        ))}
      </select>
      <button className="btn btn-sm btn-ghost" onClick={onBack}>← Back to class overview</button>
    </div>
  )
}
