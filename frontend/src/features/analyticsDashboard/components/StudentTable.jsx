import { useMemo, useState } from 'react'
import StudentRow from './StudentRow'

const COLUMNS = [
  { key: 'name',        label: 'Name',        sortable: true },
  { key: 'sectionName', label: 'Section',     sortable: false },
  { key: 'completed',   label: 'Completed',   sortable: true },
  { key: 'quizScore',   label: 'Quiz score',  sortable: true },
  { key: 'totalGrade',  label: 'Total grade', sortable: true },
  { key: 'confidence',  label: 'Confidence',  sortable: true },
]

function sortStudents(students, { column, direction }) {
  return [...students].sort((a, b) => {
    const aVal = a[column], bVal = b[column]
    if (aVal == null && bVal == null) return 0
    if (aVal == null) return 1
    if (bVal == null) return -1
    if (typeof aVal === 'boolean') return direction === 'asc' ? (bVal - aVal) : (aVal - bVal)
    if (typeof aVal === 'string') {
      const cmp = aVal.localeCompare(bVal)
      return direction === 'asc' ? cmp : -cmp
    }
    return direction === 'asc' ? aVal - bVal : bVal - aVal
  })
}

export default function StudentTable({ students, loading, onRowClick }) {
  const [sort, setSort] = useState({ column: 'name', direction: 'asc' })

  function handleSort(colKey) {
    setSort(prev =>
      prev.column === colKey
        ? { column: colKey, direction: prev.direction === 'asc' ? 'desc' : 'asc' }
        : { column: colKey, direction: 'asc' }
    )
  }

  const sortedStudents = useMemo(
    () => (students ? sortStudents(students, sort) : []),
    [students, sort],
  )

  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra w-full">
        <thead>
          <tr>
            {COLUMNS.map(col => (
              <th
                key={col.key}
                onClick={col.sortable ? () => handleSort(col.key) : undefined}
                className={col.sortable ? 'cursor-pointer select-none hover:bg-base-200' : ''}
              >
                {col.label}
                {col.sortable && sort.column === col.key && (
                  <span className="ml-1 text-xs">{sort.direction === 'asc' ? '▲' : '▼'}</span>
                )}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {loading ? (
            Array.from({ length: 5 }, (_, i) => (
              <tr key={i}>
                {Array.from({ length: 6 }, (_, j) => (
                  <td key={j}><div className="skeleton h-4 w-full" /></td>
                ))}
              </tr>
            ))
          ) : !students?.length ? (
            <tr>
              <td colSpan={6}>No students match the current filters.</td>
            </tr>
          ) : (
            sortedStudents.map(student => (
              <StudentRow
                key={student.studentId}
                student={student}
                onClick={() => onRowClick(student)}
              />
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}
