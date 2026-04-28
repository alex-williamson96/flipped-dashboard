import { useState } from 'react'
import useStudentDetail from '../hooks/useStudentDetail'
import useLessons from '../../../hooks/useLessons'
import StudentViewHeader from './StudentViewHeader'
import LessonDetailPanel from './LessonDetailPanel'
import LessonHistoryTable from './LessonHistoryTable'

export default function IndividualStudentView({
  studentId,
  courseId,
  initialLessonId,
  filteredStudents,
  onBack,
  onStudentChange,
}) {
  const [selectedLessonId, setSelectedLessonId] = useState(initialLessonId)

  const { data: detail, isLoading } = useStudentDetail({ studentId, courseId, lessonId: selectedLessonId })
  const { data: lessons = [] } = useLessons(courseId)

  return (
    <div className="flex flex-col gap-4">
      <StudentViewHeader
        student={isLoading && !detail ? null : detail}
        filteredStudents={filteredStudents}
        onBack={onBack}
        onStudentChange={onStudentChange}
      />

      <div className="flex items-center gap-2">
        <span className="text-sm">Lesson:</span>
        <select
          className="select select-sm"
          value={selectedLessonId ?? ''}
          onChange={e => setSelectedLessonId(Number(e.target.value))}
        >
          {lessons.map(lesson => (
            <option key={lesson.id} value={lesson.id}>{lesson.title}</option>
          ))}
        </select>
      </div>

      <LessonDetailPanel lessonDetail={detail?.lesson} loading={isLoading} />

      <h3 className="font-semibold mt-4 mb-2">Lesson history</h3>
      <LessonHistoryTable allLessons={detail?.allLessons} selectedLessonId={selectedLessonId} />
    </div>
  )
}
