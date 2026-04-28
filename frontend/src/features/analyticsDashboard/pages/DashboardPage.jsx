import { useState, useEffect, useRef } from 'react'
import useCourses from '../../../hooks/useCourses'
import useLessons from '../../../hooks/useLessons'
import useDashboardSummary from '../hooks/useDashboardSummary'
import useLessonTimeline from '../hooks/useLessonTimeline'
import FilterBar from '../components/FilterBar'
import AlertBanner from '../components/AlertBanner'
import StatsBar from '../components/StatsBar'
import EngagementFunnel from '../components/EngagementFunnel'
import StudentTable from '../components/StudentTable'
import LessonTimeline from '../components/LessonTimeline'
import IndividualStudentView from '../components/IndividualStudentView'
import WeakestObjectivesPanel from '../components/WeakestObjectivesPanel'

export default function DashboardPage() {
  const [filters, setFilters] = useState({
    courseId: null,
    sectionId: null,
    lessonId: null,
    completionStatus: 'all',
    minQuizScore: null,
    maxQuizScore: null,
    confidenceLevel: null,
  })
  const [selectedStudentId, setSelectedStudentId] = useState(null)
  const autoSelectCourseRef = useRef(null)

  const { data: courses } = useCourses()
  const { data: lessons } = useLessons(filters.courseId)
  const { data: overviewData, isLoading: overviewLoading } = useDashboardSummary(filters)
  const { data: timelineData, isLoading: timelineLoading } = useLessonTimeline({
    courseId: filters.courseId,
    sectionId: filters.sectionId,
  })

  useEffect(() => {
    if (courses && courses.length > 0 && !filters.courseId) {
      setFilters(f => ({ ...f, courseId: courses[0].id }))
    }
  }, [courses])

  useEffect(() => {
    if (filters.courseId !== autoSelectCourseRef.current && lessons && lessons.length > 0) {
      autoSelectCourseRef.current = filters.courseId
      const active = lessons.find(l => l.isActive) ?? lessons[0]
      setFilters(f => ({ ...f, lessonId: active.id }))
    }
  }, [filters.courseId, lessons])

  function handleFilterChange(partial) {
    setFilters(f => ({ ...f, ...partial }))
    if ('courseId' in partial) {
      setSelectedStudentId(null)
    }
  }

  return (
    <div className="p-4 max-w-7xl mx-auto space-y-4">
      <FilterBar filters={filters} onChange={handleFilterChange} />
      <AlertBanner summary={overviewData} />
      <StatsBar summary={overviewData} loading={overviewLoading} />
      <EngagementFunnel summary={overviewData} loading={overviewLoading} />
      {selectedStudentId ? (
        <IndividualStudentView
          studentId={selectedStudentId}
          courseId={filters.courseId}
          initialLessonId={filters.lessonId}
          filteredStudents={overviewData?.students ?? []}
          onBack={() => setSelectedStudentId(null)}
          onStudentChange={setSelectedStudentId}
        />
      ) : (
        <>
          <WeakestObjectivesPanel loAccuracy={overviewData?.loAccuracy} />
          <StudentTable
            students={overviewData?.students}
            loading={overviewLoading}
            onRowClick={student => setSelectedStudentId(student.studentId)}
          />
          <LessonTimeline lessons={timelineData} loading={timelineLoading} />
        </>
      )}
    </div>
  )
}
